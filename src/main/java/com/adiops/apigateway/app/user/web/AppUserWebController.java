package com.adiops.apigateway.app.user.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.app.user.resourceobject.AppUserRO;
import com.adiops.apigateway.app.user.service.AppUserService;

/**
 * The web controller class for AppUser 
 * @author Deepak Pal
 *
 */
@Controller
public class AppUserWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	AppUserService mAppUserService;

	@GetMapping(value = "/admin/web/app_users")
	public String getAppUsers(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<AppUserRO> app_users = mAppUserService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mAppUserService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("app_users", app_users);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "app_user");
	    return "admin/app_user/app_user-list";
	}

	@GetMapping(value = { "/admin/web/app_users/add" })
	public String showAddAppUser(Model model) {
		AppUserRO tAppUserRO = new AppUserRO();
	    model.addAttribute("selectedMenu", "app_user");
	    model.addAttribute("app_userRO", tAppUserRO);
	    return "admin/app_user/app_user-add";
	}

	@PostMapping(value = "/admin/web/app_users/add")
	public String addAppUser(@ModelAttribute AppUserRO tAppUserRO,Model model)  {
		try {
		 tAppUserRO=mAppUserService.createOrUpdateAppUser(tAppUserRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("app_userRO", tAppUserRO);
		model.addAttribute("selectedMenu", "app_user");
		return "admin/app_user/app_user-edit";
	}

	@GetMapping(value = { "/admin/web/app_users/{app_userId}" })
	public String showEditAppUser(Model model, @PathVariable Long app_userId) throws RestException {
		AppUserRO tAppUserRO = mAppUserService.getAppUserById(app_userId);
	    model.addAttribute("selectedMenu", "app_user");
	    model.addAttribute("app_userRO", tAppUserRO);
	    return "admin/app_user/app_user-edit";
	}

	

	@GetMapping(value = { "/admin/web/app_users/{app_userId}/delete" })
	public String deleteAppUserById(Model model, @PathVariable Long app_userId) throws RestException {
		mAppUserService.deleteAppUserById(app_userId);
		return "redirect:/admin/web/app_users";
	}
	
	
	@GetMapping(value = { "/admin/web/app_users/{app_userId}/app_roles" })
	public String getAppUserAppRoles(Model model, @PathVariable Long app_userId) throws RestException {
		model.addAttribute("app_userRO", mAppUserService.getAppUserById(app_userId));
		model.addAttribute("app_roles", mAppUserService.findAppUserAppRoles(app_userId));
		model.addAttribute("selectedMenu", "app_user");
		return "admin/app_user/app_role/app_role-list";
	}
	
	@GetMapping(value = { "/admin/web/app_users/{app_userId}/app_roles/assign" })
	public String assignAppRoles(Model model, @PathVariable Long app_userId) throws RestException {
		model.addAttribute("app_userRO", mAppUserService.getAppUserById(app_userId));		
		model.addAttribute("app_roles", mAppUserService.findUnassignAppUserAppRoles(app_userId));
		model.addAttribute("selectedMenu", "app_user");
		return "admin/app_user/app_role/app_role-assign";
	}
	
	@GetMapping(value = { "/admin/web/app_users/{app_userId}/app_roles/{app_roleId}/assign" })
	public String getAppRoles(Model model, @PathVariable Long app_userId, @PathVariable Long app_roleId) throws RestException {
		mAppUserService.addAppUserAppRole(app_userId, app_roleId);
		model.addAttribute("app_userRO", mAppUserService.getAppUserById(app_userId));
		model.addAttribute("app_roles", mAppUserService.findAppUserAppRoles(app_userId));
		model.addAttribute("selectedMenu", "app_user");
		return "admin/app_user/app_role/app_role-list";
	}
	
	@GetMapping(value = { "/admin/web/app_users/{app_userId}/app_roles/{app_roleId}/unassign" })
	public String unassignAppRoles(Model model, @PathVariable Long app_userId, @PathVariable Long app_roleId) throws RestException {
		mAppUserService.unassignAppUserAppRole(app_userId, app_roleId);
		model.addAttribute("app_userRO", mAppUserService.getAppUserById(app_userId));
		model.addAttribute("app_roles", mAppUserService.findAppUserAppRoles(app_userId));
		model.addAttribute("selectedMenu", "app_user");
		return "admin/app_user/app_role/app_role-list";
	}
	

}
