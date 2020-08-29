package com.adiops.apigateway.common.core;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.course.service.CourseService;
import com.adiops.apigateway.module.resourceobject.ModuleRO;
import com.adiops.apigateway.module.service.ModuleService;
import com.adiops.apigateway.topic.resourceobject.TopicRO;
import com.adiops.apigateway.topic.service.TopicService;

@Component
public class TopicCLI implements CommandLineRunner{
	
	@Autowired
	CourseService mCourseService;
	
	@Autowired
	ModuleService mModuleService;
	
	@Autowired
	TopicService mTopicService;
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		List<CourseRO> tCourseROs= mCourseService.getCourseROs();
		List<ModuleRO>	tModuleROs= mModuleService.getModuleROs();
		for(CourseRO tCourseRO:tCourseROs)
		for(ModuleRO tModuleRO: tModuleROs)
		{
			mModuleService.addModuleCourse(tModuleRO.getId(), tCourseRO.getId());
			
			for (int j = 1; j <= 5; j++) {
				TopicRO tTopicRO= new TopicRO();
				tTopicRO.setKey(tModuleRO.getKey()+"0"+j);
				tTopicRO.setName("Level "+j);
				tTopicRO.setTitle(tModuleRO.getName());
				try {
					tTopicRO=mTopicService.createOrUpdateTopic(tTopicRO);
					mTopicService.addTopicModule(tTopicRO.getId(), tModuleRO.getId());
					
				} catch (RestException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
