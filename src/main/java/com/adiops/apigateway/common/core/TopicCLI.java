package com.adiops.apigateway.common.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.adiops.apigateway.app.role.resourceobject.AppRoleRO;
import com.adiops.apigateway.app.role.service.AppRoleService;
import com.adiops.apigateway.app.user.resourceobject.AppUserRO;
import com.adiops.apigateway.app.user.service.AppUserService;
import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.course.service.CourseService;
import com.adiops.apigateway.module.resourceobject.ModuleRO;
import com.adiops.apigateway.module.service.ModuleService;
import com.adiops.apigateway.question.resourceobject.QuestionRO;
import com.adiops.apigateway.question.service.QuestionService;
import com.adiops.apigateway.topic.resourceobject.TopicRO;
import com.adiops.apigateway.topic.service.TopicService;

@Component
public class TopicCLI {

	@Autowired
	CourseService mCourseService;

	@Autowired
	ModuleService mModuleService;

	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	TopicService mTopicService;

	@Autowired
	QuestionService mQuestionService;
	
	@Autowired
	AppUserService mAppUserService;
	
	@Autowired
	AppRoleService mAppRoleService;

	
	@Transactional
	public void run() throws IOException  {
		try {
			importFile();
			importCourse();			
			addRoles();		
		} catch (RestException e) {
			e.printStackTrace();
		}

	}

	@Transactional
	public void importCourse() throws IOException {
		List<CourseRO> tCourseROs = mCourseService.getCourseROs();
		List<ModuleRO> tModuleROs = mModuleService.getModuleROs();
		for (CourseRO tCourseRO : tCourseROs)
			for (ModuleRO tModuleRO : tModuleROs) {
				mModuleService.addModuleCourse(tModuleRO.getId(), tCourseRO.getId());

				for (int j = 1; j <= 5; j++) {
					TopicRO tTopicRO = new TopicRO();
					tTopicRO.setKeyid(tModuleRO.getKeyid() + "0" + j);
					tTopicRO.setName("Level " + j);
					tTopicRO.setTitle(tModuleRO.getName());
					tTopicRO.setAuthorId(tCourseRO.getAuthorId());
					try {
						tTopicRO = mTopicService.createOrUpdateTopic(tTopicRO);
						mTopicService.addTopicModule(tTopicRO.getId(), tModuleRO.getId());
						addQuestionsToTopic(tTopicRO);
					} catch (RestException e) {
						e.printStackTrace();
					}
				}

			}
	}

	@Transactional
	private void importFile() throws IOException, RestException {
		Resource resource = resourceLoader.getResource("classpath:db/questions.csv");
		InputStream inputStream = resource.getInputStream();
		mQuestionService.importCSV(inputStream);
	}

	@Transactional
	private void addQuestionsToTopic(TopicRO topicRO) throws IOException, RestException {
		for (QuestionRO tQuestionRO : mQuestionService.getQuestionROs()) {
			mTopicService.addTopicQuestion(topicRO.getId(), tQuestionRO.getId());
		}
	}
	
	@Transactional
	private void addRoles() throws RestException {
		AppRoleRO tAppRole=	mAppRoleService.getAppRoleByKeyId("SYS_ADMIN");
		AppUserRO tAppUserRO = mAppUserService.getAppUserByKeyId("AD001");
		
		mAppUserService.addAppUserAppRole(tAppUserRO.getId(), tAppRole.getId());
		tAppRole=	mAppRoleService.getAppRoleByKeyId("APP_LEARNER");
		mAppUserService.addAppUserAppRole(tAppUserRO.getId(), tAppRole.getId());
		
		tAppUserRO = mAppUserService.getAppUserByKeyId("USER001");
		mAppUserService.addAppUserAppRole(tAppUserRO.getId(), tAppRole.getId());
	}
}
