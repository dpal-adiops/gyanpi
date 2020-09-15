package com.adiops.apigateway.module.line.group.web;

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
import com.adiops.apigateway.module.line.group.resourceobject.ModuleLineGroupRO;
import com.adiops.apigateway.module.line.group.service.ModuleLineGroupService;

/**
 * The web controller class for ModuleLineGroup 
 * @author Deepak Pal
 *
 */
@Controller
public class ModuleLineGroupWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	ModuleLineGroupService mModuleLineGroupService;

	@GetMapping(value = "/admin/web/module_line_groups")
	public String getModuleLineGroups(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<ModuleLineGroupRO> module_line_groups = mModuleLineGroupService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mModuleLineGroupService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("module_line_groups", module_line_groups);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "module_line_group");
	    return "admin/module_line_group/module_line_group-list";
	}

	@GetMapping(value = { "/admin/web/module_line_groups/add" })
	public String showAddModuleLineGroup(Model model) {
		ModuleLineGroupRO tModuleLineGroupRO = new ModuleLineGroupRO();
	    model.addAttribute("selectedMenu", "module_line_group");
	    model.addAttribute("module_line_groupRO", tModuleLineGroupRO);
	    return "admin/module_line_group/module_line_group-add";
	}

	@PostMapping(value = "/admin/web/module_line_groups/add")
	public String addModuleLineGroup(@ModelAttribute ModuleLineGroupRO tModuleLineGroupRO,Model model)  {
		try {
		 tModuleLineGroupRO=mModuleLineGroupService.createOrUpdateModuleLineGroup(tModuleLineGroupRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("module_line_groupRO", tModuleLineGroupRO);
		model.addAttribute("selectedMenu", "module_line_group");
		return "admin/module_line_group/module_line_group-edit";
	}

	@GetMapping(value = { "/admin/web/module_line_groups/{module_line_groupId}" })
	public String showEditModuleLineGroup(Model model, @PathVariable Long module_line_groupId) throws RestException {
		ModuleLineGroupRO tModuleLineGroupRO = mModuleLineGroupService.getModuleLineGroupById(module_line_groupId);
	    model.addAttribute("selectedMenu", "module_line_group");
	    model.addAttribute("module_line_groupRO", tModuleLineGroupRO);
	    return "admin/module_line_group/module_line_group-edit";
	}

	

	@GetMapping(value = { "/admin/web/module_line_groups/{module_line_groupId}/delete" })
	public String deleteModuleLineGroupById(Model model, @PathVariable Long module_line_groupId) throws RestException {
		mModuleLineGroupService.deleteModuleLineGroupById(module_line_groupId);
		return "redirect:/admin/web/module_line_groups";
	}
	

}
