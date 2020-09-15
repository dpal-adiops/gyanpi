package com.adiops.apigateway.account.web.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.adiops.apigateway.common.inject.NamingMgr;
import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.course.line.group.resourceobject.CourseLineGroupRO;
import com.adiops.apigateway.course.line.group.service.CourseLineGroupService;
import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.course.service.CourseService;
import com.adiops.apigateway.module.line.group.resourceobject.ModuleLineGroupRO;
import com.adiops.apigateway.module.line.group.service.ModuleLineGroupService;
import com.adiops.apigateway.module.resourceobject.ModuleRO;
import com.adiops.apigateway.module.service.ModuleService;

@Configurable
public class CourseBO {

	@Autowired
	private CourseLineGroupService mCourseLineGroupService;

	@Autowired
	private ModuleLineGroupService mModuleLineGroupService;

	@Autowired
	private ModuleService mModuleService;
	
	@Autowired
	private CourseService mCourseService;
	
	private CourseRO courseRO;

	private CourseLineGroupRO courseLineGroupRO;
	
	private LearningPathBO learningPathBO;

	private List<ModuleBO> moduleBOs = new ArrayList<ModuleBO>();
	private Map<String,ModuleBO> mapModuleBOs = new HashMap<String,ModuleBO>();


	public CourseBO() {
		NamingMgr.injectMembers(this);		
	}

	public LearningPathBO getLearningPathBO() {
		return learningPathBO;
	}

	public void setLearningPathBO(LearningPathBO learningPathBO) {
		this.learningPathBO = learningPathBO;
	}

	public CourseRO getCourseRO() {
		return courseRO;
	}

	public CourseLineGroupRO getCourseLineGroupRO() {
		return courseLineGroupRO;
	}

	public void setCourseRO(CourseRO courseRO) {
		this.courseRO = courseRO;
	}

	public void setCourseLineGroupRO(CourseLineGroupRO courseLineGroupRO) {
		this.courseLineGroupRO = courseLineGroupRO;
	}

	public List<ModuleBO> getModuleBOs() {
		return moduleBOs;
	}

	public void setModuleBOs(List<ModuleBO> moduleBOs) {
		this.moduleBOs = moduleBOs;
	}

	public void fetchModuleBOs() {
		List<ModuleRO> tModuleROs = mCourseService.findCourseModules(courseRO.getId());
		moduleBOs = tModuleROs.stream().map(ro -> {
			ModuleBO tModuleBO= getModuleBO(ro.getKeyid());
			mapModuleBOs.put(ro.getKeyid(), tModuleBO);
			return tModuleBO;
			}).collect(Collectors.toList());
		
	}

	public ModuleBO getModuleBO(String id) {
		if(mapModuleBOs.containsKey(id)) {return mapModuleBOs.get(id);}
		ModuleBO tModuleBO = new ModuleBO();
		CourseRO tCourseRO = getCourseRO();
		if (tCourseRO != null) {
			ModuleLineGroupRO tModuleLineGroup = mModuleLineGroupService
					.getModuleLineGroupByKeyId(courseLineGroupRO.getKeyid() + "_" + id);
			ModuleRO tModuleRO = mModuleService.getModuleByKeyId(id);
			if (tModuleRO == null)
				return tModuleBO;
			
			if (tModuleLineGroup == null) {
				tModuleLineGroup = new ModuleLineGroupRO();
				tModuleLineGroup.setKeyid(courseLineGroupRO.getKeyid() + "_" + id);
				tModuleLineGroup.setMaxScore(0);
				tModuleLineGroup.setScore(0);
				tModuleLineGroup.setStatus(1);
				tModuleLineGroup.setTitle(tModuleRO.getName());
				tModuleLineGroup.setDescription(tModuleRO.getDescription());
				tModuleLineGroup.setCourseKey(courseLineGroupRO.getCourseKey());
				tModuleLineGroup.setModuleKey(tModuleRO.getKeyid());
				tModuleLineGroup.setUserKey(courseLineGroupRO.getUserKey());
				tModuleLineGroup.setCourseLineGroupRO(courseLineGroupRO);
				tModuleLineGroup.setStep(0);
				tModuleLineGroup.setTotalStep(0);
				
			}
			tModuleBO.setModuleRO(tModuleRO);
			tModuleBO.setModuleLineGroupRO(tModuleLineGroup);
		}
		tModuleBO.setCourseBO(this);
		tModuleBO.fetchTopicBOs();
		tModuleBO.unlock();
		return tModuleBO;
	}

	public void save() {
		try {
			courseLineGroupRO = mCourseLineGroupService.createOrUpdateCourseLineGroup(courseLineGroupRO);			
			mCourseLineGroupService.addCourseLineGroupLearningPath(courseLineGroupRO.getId(),
					learningPathBO.getLearningPath().getId());
			
		} catch (RestException e) {
		}
	}
	
	public void calculateBackwardSteps() {
		int steps=0;
		int totalsteps=0;
		for(ModuleBO tModuleBO:getModuleBOs()) {			
			tModuleBO.calculateBackwardSteps();
			Integer step=tModuleBO.getModuleLineGroupRO().getStep();
			Integer totalstep=tModuleBO.getModuleLineGroupRO().getTotalStep();
			steps=steps+step;
			totalsteps=totalsteps+totalstep;
		}
		courseLineGroupRO.setStep(steps);
		courseLineGroupRO.setTotalStep(totalsteps);		
	}
	
	public void saveSteps() {
		int steps=0;
		int totalsteps=0;
		int score=0;
		int max_score=0;
		for(ModuleBO tModuleBO:getModuleBOs()) {			
			Integer step=tModuleBO.getModuleLineGroupRO().getStep();
			Integer totalstep=tModuleBO.getModuleLineGroupRO().getTotalStep();
			Integer tScore=tModuleBO.getModuleLineGroupRO().getScore();
			Integer tmaxScore=tModuleBO.getModuleLineGroupRO().getMaxScore();
			steps = (step==null)?0:step + steps ;
			totalsteps = (totalstep==null)?0:totalstep+ totalsteps  ;
			score= (tScore==null)?0:tScore+ score;
			max_score=(tmaxScore==null)?0:tmaxScore+  max_score;
		}
		courseLineGroupRO.setStep(steps);
		courseLineGroupRO.setTotalStep(totalsteps);	
		courseLineGroupRO.setScore(score);
		courseLineGroupRO.setMaxScore(max_score);
		try {
			courseLineGroupRO = mCourseLineGroupService.createOrUpdateCourseLineGroup(courseLineGroupRO);
		} catch (RestException e) {
		
		}	
	}

	public int getProgress() {
		int step=courseLineGroupRO.getStep();
		int total=courseLineGroupRO.getTotalStep();
		return (step*100)/total;
	}
}
