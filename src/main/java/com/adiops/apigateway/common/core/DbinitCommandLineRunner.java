package com.adiops.apigateway.common.core;

import java.io.InputStream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.adiops.apigateway.account.web.bo.view.CacheMgr;
import com.adiops.apigateway.app.role.service.AppRoleService;
import com.adiops.apigateway.app.user.service.AppUserService;
import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.course.service.CourseService;
import com.adiops.apigateway.module.service.ModuleService;

@Component
public class DbinitCommandLineRunner implements CommandLineRunner {

	@Autowired
	ResourceLoader resourceLoader;
	
	@Autowired
	CourseService mCourseService;
	
	@Autowired
	ModuleService mModuleService;
	
	@Autowired
	AppRoleService mAppRoleService;
	
	@Autowired
	AppUserService mAppUserService;
	
	@Autowired
	TopicCLI mTopicCLI;
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {		
		CourseRO tCourseRO= new CourseRO();
		tCourseRO.setKeyid("Math10");
		tCourseRO.setName("NCERT based Class 10 Math chapter-wise practice exercise");
		tCourseRO.setDescription("This fundamental practice set journey enable you to choose the topic areas you're interested in most. Begin your journey to the Master in Math with Gyanpi.");
		tCourseRO.setAuthorId("Sourabh Singh");
		
		try {
			tCourseRO=mCourseService.createOrUpdateCourse(tCourseRO);
			Resource resource = resourceLoader.getResource("classpath:db/modules.csv");
				InputStream inputStream = resource.getInputStream();
			mModuleService.importCSV(inputStream);
			Resource resource2 = resourceLoader.getResource("classpath:db/roles.csv");
			mAppRoleService.importCSV(resource2.getInputStream());
			resource2 = resourceLoader.getResource("classpath:db/users.csv");
			mAppUserService.importCSV(resource2.getInputStream());
			
			mTopicCLI.run();
			
		} catch (RestException e) {
			e.printStackTrace();
		}
		
		
	}
}
