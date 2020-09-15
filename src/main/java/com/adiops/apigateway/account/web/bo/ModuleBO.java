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
import com.adiops.apigateway.module.line.group.resourceobject.ModuleLineGroupRO;
import com.adiops.apigateway.module.line.group.service.ModuleLineGroupService;
import com.adiops.apigateway.module.resourceobject.ModuleRO;
import com.adiops.apigateway.module.service.ModuleService;
import com.adiops.apigateway.topic.line.group.resourceobject.TopicLineGroupRO;
import com.adiops.apigateway.topic.line.group.service.TopicLineGroupService;
import com.adiops.apigateway.topic.resourceobject.TopicRO;
import com.adiops.apigateway.topic.service.TopicService;

@Configurable
public class ModuleBO {

	@Autowired
	private TopicService mTopicService;

	@Autowired
	private ModuleService mModuleService;

	@Autowired
	private TopicLineGroupService mTopicLineGroupService;

	@Autowired
	private ModuleLineGroupService mModuleLineGroupService;

	private ModuleRO moduleRO;
	private ModuleLineGroupRO moduleLineGroupRO;

	private CourseBO courseBO;

	private List<TopicBO> topicBOs = new ArrayList<TopicBO>();
	private Map<String,TopicBO> mapTopicBOs = new HashMap<String,TopicBO>();

	public ModuleBO() {
		NamingMgr.injectMembers(this);

	}

	public CourseBO getCourseBO() {
		return courseBO;
	}

	public void setCourseBO(CourseBO courseBO) {
		this.courseBO = courseBO;
	}

	public ModuleRO getModuleRO() {
		return moduleRO;
	}

	public void setModuleRO(ModuleRO moduleRO) {
		this.moduleRO = moduleRO;
	}

	public ModuleLineGroupRO getModuleLineGroupRO() {
		return moduleLineGroupRO;
	}

	public void setModuleLineGroupRO(ModuleLineGroupRO moduleLineGroupRO) {
		this.moduleLineGroupRO = moduleLineGroupRO;
	}

	public void fetchTopicBOs() {
		List<TopicRO> tTopicROs = mModuleService.findModuleTopics(moduleRO.getId());
		topicBOs = tTopicROs.stream().map(ro ->{
			TopicBO tTopicBO= getTopicBO(ro.getKeyid());
			mapTopicBOs.put(ro.getKeyid(), tTopicBO);
			return tTopicBO;	
		}).collect(Collectors.toList());
	}

	public void unlock() {
		for (TopicBO topicBO : topicBOs) {
			int step = topicBO.getTopicLineGroupRO().getStep();
			int totalStep = topicBO.getTopicLineGroupRO().getTotalStep();
			if (step == 0) {
				topicBO.getTopicLineGroupRO().setStatus(2);
				break;
			} else if (totalStep == step) {
				topicBO.getTopicLineGroupRO().setStatus(3);
			} else if (step>0) {
				topicBO.getTopicLineGroupRO().setStatus(2);
				break;
			}
		}
	}

	public List<TopicBO> getTopicBOs() {
		return topicBOs;
	}

	public void setTopicBOs(List<TopicBO> topicBOs) {
		this.topicBOs = topicBOs;
	}

	public TopicBO getTopicBO(String id) {
		if(mapTopicBOs.containsKey(id)) {return mapTopicBOs.get(id);}
		TopicBO tTopicBO = new TopicBO();
		ModuleRO tModuleRO = getModuleRO();
		if (tModuleRO != null) {
			TopicLineGroupRO tTopicLineGroup = mTopicLineGroupService
					.getTopicLineGroupByKeyId(moduleLineGroupRO.getKeyid() + "_" + id);
			TopicRO tTopicRO = mTopicService.getTopicByKeyId(id);
			if (tTopicRO == null)
				return tTopicBO;

			if (tTopicLineGroup == null) {
				tTopicLineGroup = new TopicLineGroupRO();
				tTopicLineGroup.setKeyid(moduleLineGroupRO.getKeyid() + "_" + id);
				tTopicLineGroup.setMaxScore(0);
				tTopicLineGroup.setScore(0);
				tTopicLineGroup.setStatus(1);
				tTopicLineGroup.setTitle(tTopicRO.getName());
				tTopicLineGroup.setDescription(tTopicRO.getDescription());
				tTopicLineGroup.setCourseKey(moduleLineGroupRO.getCourseKey());
				tTopicLineGroup.setTopicKey(tTopicRO.getKeyid());
				tTopicLineGroup.setUserKey(moduleLineGroupRO.getUserKey());
				tTopicLineGroup.setModuleKey(moduleLineGroupRO.getModuleKey());
				tTopicLineGroup.setModuleLineGroupRO(moduleLineGroupRO);
				tTopicLineGroup.setTotalStep(0);
				tTopicLineGroup.setStep(0);

			}
			tTopicBO.setTopicRO(tTopicRO);
			tTopicBO.setTopicLineGroupRO(tTopicLineGroup);
		}
		tTopicBO.setModuleBO(this);
		tTopicBO.fetchQuestionBOs();
		return tTopicBO;
	}

	public void save() {
		try {
			moduleLineGroupRO = mModuleLineGroupService.createOrUpdateModuleLineGroup(moduleLineGroupRO);
			if (courseBO.getCourseLineGroupRO().getId() == null) {
				courseBO.save();

			}
			mModuleLineGroupService.addModuleLineGroupCourseLineGroup(moduleLineGroupRO.getId(),
					courseBO.getCourseLineGroupRO().getId());
			
		} catch (RestException e) {
		}
	}

	public void calculateBackwardSteps() {
		int steps = 0;
		int totalsteps = 0;
		for (TopicBO tTopicBO : getTopicBOs()) {
			tTopicBO.calculateBackwardSteps();
			Integer step = tTopicBO.getTopicLineGroupRO().getStep();
			Integer totalstep = tTopicBO.getTopicLineGroupRO().getTotalStep();
			steps = steps + step;
			totalsteps = totalsteps + totalstep;
		}
		moduleLineGroupRO.setStep(steps);
		moduleLineGroupRO.setTotalStep(totalsteps);
	}
	
	public void saveSteps() {
		int steps = 0;
		int totalsteps = 0;
		int score=0;
		int max_score=0;
		for (TopicBO tTopicBO : getTopicBOs()) {
			Integer step = tTopicBO.getTopicLineGroupRO().getStep();
			Integer totalstep = tTopicBO.getTopicLineGroupRO().getTotalStep();
			Integer tScore=tTopicBO.getTopicLineGroupRO().getScore();
			Integer tmaxScore=tTopicBO.getTopicLineGroupRO().getMaxScore();
			steps = (step==null)?0:step + steps ;
			totalsteps = (totalstep==null)?0:totalstep+ totalsteps  ;
			score= (tScore==null)?0:tScore+ score;
			max_score=(tmaxScore==null)?0:tmaxScore+  max_score;
		}
		moduleLineGroupRO.setStep(steps);
		moduleLineGroupRO.setTotalStep(totalsteps);
		moduleLineGroupRO.setScore(score);
		moduleLineGroupRO.setMaxScore(max_score);
		try {
			moduleLineGroupRO = mModuleLineGroupService.createOrUpdateModuleLineGroup(moduleLineGroupRO);
			courseBO.saveSteps();
		} catch (RestException e) {
		}	
		
	}
	
	public int getProgress() {
		int step=moduleLineGroupRO.getStep();
		int total=moduleLineGroupRO.getTotalStep();
		return (step*100)/total;
	}
}
