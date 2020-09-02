package com.adiops.apigateway.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.course.service.CourseService;
import com.adiops.apigateway.module.service.ModuleService;
import com.adiops.apigateway.oauth2.security.auth.AuthUtils;

@Controller
public class LoginWebController {

	@Autowired
	CourseService mCourseService;
	
	@Autowired
	ModuleService mModuleService;

	
	@GetMapping(value = "/admin/login")
	public String getLogin(Model model) {
		return "login/login";
	}
	
	@GetMapping(value = "/signup")
	public String showRegistration(Model model) {
		return "login/login";
	}
	
	@GetMapping(value = "/account")
	public String showAccount(Model model) {
		List<CourseRO> courses = mCourseService.findAll(1, 1);
		CourseRO tCourseRO=courses.get(0);
		model.addAttribute("course", tCourseRO);
		model.addAttribute("modules", mCourseService.findCourseModules(tCourseRO.getId()));
		model.addAttribute("moduleService", mModuleService);
		return "web/course-list";
	}

	@GetMapping("/")
    public String index() {
		if(AuthUtils.hasRole("ROLE_USER"))
			return "redirect:/account";
		
		return "redirect:/admin/web/questions";
    }

}
