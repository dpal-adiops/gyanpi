package com.adiops.apigateway.common.core;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.course.service.CourseService;
import com.adiops.apigateway.module.resourceobject.ModuleRO;
import com.adiops.apigateway.module.service.ModuleService;
import com.adiops.apigateway.topic.resourceobject.TopicRO;
import com.adiops.apigateway.topic.service.TopicService;

@Component
public class DbinitCommandLineRunner implements CommandLineRunner {

	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	CourseService mCourseService;
	
	@Autowired
	ModuleService mModuleService;
	
	@Autowired
	TopicService mTopicService;
	
	@Override
	public void run(String... args) throws Exception {
		CourseRO tCourseRO= new CourseRO();
		tCourseRO.setKeyid("Math10");
		tCourseRO.setName("NCERT Class 10 Maths chapter-wise Quiz");
		tCourseRO.setDescription("This test qiz brings to level wise questions to exam preparation. ");
		tCourseRO.setAuthorId("Sourabh Singh");
		
		
		try {
			tCourseRO=mCourseService.createOrUpdateCourse(tCourseRO);
			Resource resource = resourceLoader.getResource("classpath:db/modules.csv");
			InputStream inputStream = resource.getInputStream();
			mModuleService.importCSV(inputStream);
			
			
			
		
			
//			List<TopicRO> topicROs= mTopicService.getTopicROs();
//			List<ModuleRO>	tModuleROs= mModuleService.getModuleROs();
//			for(ModuleRO tModuleRO: tModuleROs)
//			{
//				mModuleService.addModuleCourse(tModuleRO.getId(), tCourseRO.getId());
//				for(TopicRO topicRO:topicROs)
//				{
//					mTopicService.addTopicModule(topicRO.getId(), tModuleRO.getId());
//				}
//			}
//				
			
		} catch (RestException e) {
			e.printStackTrace();
		}
		
	}
}
