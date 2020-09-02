package com.adiops.apigateway.app.role.web;

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
import com.adiops.apigateway.app.role.resourceobject.AppRoleRO;
import com.adiops.apigateway.app.role.service.AppRoleService;

/**
 * The web controller class for AppRole 
 * @author Deepak Pal
 *
 */
@Controller
public class AppRoleWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	AppRoleService mAppRoleService;

	@GetMapping(value = "/admin/web/app_roles")
	public String getAppRoles(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<AppRoleRO> app_roles = mAppRoleService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mAppRoleService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("app_roles", app_roles);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "app_role");
	    return "admin/app_role/app_role-list";
	}

	@GetMapping(value = { "/admin/web/app_roles/add" })
	public String showAddAppRole(Model model) {
		AppRoleRO tAppRoleRO = new AppRoleRO();
	    model.addAttribute("selectedMenu", "app_role");
	    model.addAttribute("app_roleRO", tAppRoleRO);
	    return "admin/app_role/app_role-add";
	}

	@PostMapping(value = "/admin/web/app_roles/add")
	public String addAppRole(@ModelAttribute AppRoleRO tAppRoleRO,Model model)  {
		try {
		 tAppRoleRO=mAppRoleService.createOrUpdateAppRole(tAppRoleRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("app_roleRO", tAppRoleRO);
		model.addAttribute("selectedMenu", "app_role");
		return "admin/app_role/app_role-edit";
	}

	@GetMapping(value = { "/admin/web/app_roles/{app_roleId}" })
	public String showEditAppRole(Model model, @PathVariable Long app_roleId) throws RestException {
		AppRoleRO tAppRoleRO = mAppRoleService.getAppRoleById(app_roleId);
	    model.addAttribute("selectedMenu", "app_role");
	    model.addAttribute("app_roleRO", tAppRoleRO);
	    return "admin/app_role/app_role-edit";
	}

	

	@GetMapping(value = { "/admin/web/app_roles/{app_roleId}/delete" })
	public String deleteAppRoleById(Model model, @PathVariable Long app_roleId) throws RestException {
		mAppRoleService.deleteAppRoleById(app_roleId);
		return "redirect:/admin/web/app_roles";
	}
	
	
	@GetMapping(value = { "/admin/web/app_roles/{app_roleId}/app_users" })
	public String getAppRoleAppUsers(Model model, @PathVariable Long app_roleId) throws RestException {
		model.addAttribute("app_roleRO", mAppRoleService.getAppRoleById(app_roleId));
		model.addAttribute("app_users", mAppRoleService.findAppRoleAppUsers(app_roleId));
		model.addAttribute("selectedMenu", "app_role");
		return "admin/app_role/app_user/app_user-list";
	}
	
	@GetMapping(value = { "/admin/web/app_roles/{app_roleId}/app_users/assign" })
	public String assignAppUsers(Model model, @PathVariable Long app_roleId) throws RestException {
		model.addAttribute("app_roleRO", mAppRoleService.getAppRoleById(app_roleId));		
		model.addAttribute("app_users", mAppRoleService.findUnassignAppRoleAppUsers(app_roleId));
		model.addAttribute("selectedMenu", "app_role");
		return "admin/app_role/app_user/app_user-assign";
	}
	
	@GetMapping(value = { "/admin/web/app_roles/{app_roleId}/app_users/{app_userId}/assign" })
	public String getAppUsers(Model model, @PathVariable Long app_roleId, @PathVariable Long app_userId) throws RestException {
		mAppRoleService.addAppRoleAppUser(app_roleId, app_userId);
		model.addAttribute("app_roleRO", mAppRoleService.getAppRoleById(app_roleId));
		model.addAttribute("app_users", mAppRoleService.findAppRoleAppUsers(app_roleId));
		model.addAttribute("selectedMenu", "app_role");
		return "admin/app_role/app_user/app_user-list";
	}
	
	@GetMapping(value = { "/admin/web/app_roles/{app_roleId}/app_users/{app_userId}/unassign" })
	public String unassignAppUsers(Model model, @PathVariable Long app_roleId, @PathVariable Long app_userId) throws RestException {
		mAppRoleService.unassignAppRoleAppUser(app_roleId, app_userId);
		model.addAttribute("app_roleRO", mAppRoleService.getAppRoleById(app_roleId));
		model.addAttribute("app_users", mAppRoleService.findAppRoleAppUsers(app_roleId));
		model.addAttribute("selectedMenu", "app_role");
		return "admin/app_role/app_user/app_user-list";
	}
	

}
