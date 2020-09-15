package com.adiops.apigateway.course.line.group.web;

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
import com.adiops.apigateway.course.line.group.resourceobject.CourseLineGroupRO;
import com.adiops.apigateway.course.line.group.service.CourseLineGroupService;

/**
 * The web controller class for CourseLineGroup 
 * @author Deepak Pal
 *
 */
@Controller
public class CourseLineGroupWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	CourseLineGroupService mCourseLineGroupService;

	@GetMapping(value = "/admin/web/course_line_groups")
	public String getCourseLineGroups(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<CourseLineGroupRO> course_line_groups = mCourseLineGroupService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mCourseLineGroupService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("course_line_groups", course_line_groups);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "course_line_group");
	    return "admin/course_line_group/course_line_group-list";
	}

	@GetMapping(value = { "/admin/web/course_line_groups/add" })
	public String showAddCourseLineGroup(Model model) {
		CourseLineGroupRO tCourseLineGroupRO = new CourseLineGroupRO();
	    model.addAttribute("selectedMenu", "course_line_group");
	    model.addAttribute("course_line_groupRO", tCourseLineGroupRO);
	    return "admin/course_line_group/course_line_group-add";
	}

	@PostMapping(value = "/admin/web/course_line_groups/add")
	public String addCourseLineGroup(@ModelAttribute CourseLineGroupRO tCourseLineGroupRO,Model model)  {
		try {
		 tCourseLineGroupRO=mCourseLineGroupService.createOrUpdateCourseLineGroup(tCourseLineGroupRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("course_line_groupRO", tCourseLineGroupRO);
		model.addAttribute("selectedMenu", "course_line_group");
		return "admin/course_line_group/course_line_group-edit";
	}

	@GetMapping(value = { "/admin/web/course_line_groups/{course_line_groupId}" })
	public String showEditCourseLineGroup(Model model, @PathVariable Long course_line_groupId) throws RestException {
		CourseLineGroupRO tCourseLineGroupRO = mCourseLineGroupService.getCourseLineGroupById(course_line_groupId);
	    model.addAttribute("selectedMenu", "course_line_group");
	    model.addAttribute("course_line_groupRO", tCourseLineGroupRO);
	    return "admin/course_line_group/course_line_group-edit";
	}

	

	@GetMapping(value = { "/admin/web/course_line_groups/{course_line_groupId}/delete" })
	public String deleteCourseLineGroupById(Model model, @PathVariable Long course_line_groupId) throws RestException {
		mCourseLineGroupService.deleteCourseLineGroupById(course_line_groupId);
		return "redirect:/admin/web/course_line_groups";
	}
	

}
