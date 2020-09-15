package com.adiops.apigateway.account.web.bo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.adiops.apigateway.app.user.service.UserService;
import com.adiops.apigateway.common.inject.NamingMgr;
import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.common.utils.ObjectUtils;
import com.adiops.apigateway.course.line.group.resourceobject.CourseLineGroupRO;
import com.adiops.apigateway.course.line.group.service.CourseLineGroupService;
import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.course.service.CourseService;
import com.adiops.apigateway.learning.path.resourceobject.LearningPathRO;
import com.adiops.apigateway.learning.path.service.LearningPathService;

@Configurable
public class LearningPathBO {

	@Autowired
	private LearningPathService mLearningPathService;

	@Autowired
	private CourseLineGroupService mCourseLineGroupService;

	@Autowired
	private CourseService mCourseService;

	@Autowired
	private UserService mUserService;

	public LearningPathBO() {
		NamingMgr.injectMembers(this);
	}

	private CourseBO courseBO;
	
	
	public CourseBO getCourse() {
		if(courseBO==null)		
		 courseBO=getCourseBO("Math10");
		courseBO.calculateBackwardSteps();
		return courseBO;
	}

	public CourseBO getCourseBO(String id) {
		CourseBO tCourseBO = new CourseBO();
		LearningPathRO tLearningPath = getLearningPath();
		if (tLearningPath != null) {
			CourseLineGroupRO tCourseLineGroup = mCourseLineGroupService
					.getCourseLineGroupByKeyId(tLearningPath.getKeyid() + "_" + id);
			CourseRO tCourseRO = mCourseService.getCourseByKeyId(id);
			if (tCourseRO == null)
				return tCourseBO;

			if (tCourseLineGroup == null) {
				tCourseLineGroup = new CourseLineGroupRO();
				tCourseLineGroup.setKeyid(tLearningPath.getKeyid() + "_" + id);
				tCourseLineGroup.setMaxScore(0);
				tCourseLineGroup.setScore(0);
				tCourseLineGroup.setStatus(1);
				tCourseLineGroup.setTitle(tCourseRO.getName());
				tCourseLineGroup.setDescription(tCourseRO.getDescription());
				tCourseLineGroup.setCourseKey(tCourseRO.getKeyid());
				tCourseLineGroup.setUserKey(tLearningPath.getKeyid());
				tCourseLineGroup.setLearningPathRO(tLearningPath);
				try {
					tCourseLineGroup = mCourseLineGroupService.createOrUpdateCourseLineGroup(tCourseLineGroup);
					mCourseLineGroupService.addCourseLineGroupLearningPath(tCourseLineGroup.getId(), tCourseRO.getId());
				} catch (RestException e) {
				}
			}
			tCourseBO.setCourseRO(tCourseRO);
			tCourseBO.setCourseLineGroupRO(tCourseLineGroup);
		}

		tCourseBO.setLearningPathBO(this);
		tCourseBO.fetchModuleBOs();
		return tCourseBO;
	}

	public LearningPathRO getLearningPath() {
		LearningPathRO tLearningPath = null;
		Optional<String> key = ObjectUtils.resolve(() -> mUserService.getCurrentUserRO().getKeyid());
		if (key.isPresent()) {
			tLearningPath = mLearningPathService.getLearningPathByKeyId(key.get());
			if (tLearningPath == null) {
				tLearningPath = new LearningPathRO();
				tLearningPath.setKeyid(key.get());
				try {
					tLearningPath = mLearningPathService.createOrUpdateLearningPath(tLearningPath);
				} catch (RestException e) {
				}
			}
		}

		return tLearningPath;
	}


}
