package com.adiops.apigateway.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.adiops.apigateway.account.web.AccountDetailService;
import com.adiops.apigateway.account.web.bo.LearningPathBORepository;
import com.adiops.apigateway.account.web.bo.view.LearningTrackView;
import com.adiops.apigateway.app.user.resourceobject.AppUserRO;
import com.adiops.apigateway.app.user.service.AppUserService;
import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.common.utils.UserUtils;
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

	@Autowired
	AppUserService mAppUserService;
	
	@Autowired
	AccountDetailService mAccountDetailService;
	
	@Autowired
	LearningPathBORepository mAccountLearningTrackBO;
	
	@GetMapping(value = "/login")
	public String getLogin(Model model) {
		return "login/login";
	}
	
	@GetMapping(value = "/signup")
	public String showRegistration(Model model) {
		model.addAttribute("userRO", new AppUserRO());
		return "login/registration";
	}
	
	@PostMapping(value = "/web/users/add")
	public String addAppUser(@ModelAttribute AppUserRO tAppUserRO,Model model)  {
		try {
			tAppUserRO=mAccountDetailService.encodePassword(tAppUserRO);
			tAppUserRO.setKeyid(UserUtils.getKeyId(mAppUserService.count()));
			tAppUserRO=mAppUserService.createOrUpdateAppUser(tAppUserRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
			return "login/registration";
		}
		
		return "redirect:/login";
	}
	
	@GetMapping(value = "/account")
	public String showAccount(Model model) {		
		model.addAttribute("courseBO", mAccountLearningTrackBO.getLearningPathBO().getCourse());
		return "web/learning-track";
	}

	@GetMapping("/")
    public String index() {
		if(AuthUtils.hasRole("ROLE_USER"))
			return "redirect:/account";
		
		return "redirect:/admin/web/questions";
    }

}
