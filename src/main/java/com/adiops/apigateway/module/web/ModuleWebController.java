package com.adiops.apigateway.module.web;

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
import com.adiops.apigateway.module.resourceobject.ModuleRO;
import com.adiops.apigateway.module.service.ModuleService;

/**
 * The web controller class for Module 
 * @author Deepak Pal
 *
 */
@Controller
public class ModuleWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	ModuleService mModuleService;

	@GetMapping(value = "/admin/web/modules")
	public String getModules(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<ModuleRO> modules = mModuleService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mModuleService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("modules", modules);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "module");
	    return "admin/module/module-list";
	}

	@GetMapping(value = { "/admin/web/modules/add" })
	public String showAddModule(Model model) {
		ModuleRO tModuleRO = new ModuleRO();
	    model.addAttribute("selectedMenu", "module");
	    model.addAttribute("moduleRO", tModuleRO);
	    return "admin/module/module-add";
	}

	@PostMapping(value = "/admin/web/modules/add")
	public String addModule(@ModelAttribute ModuleRO tModuleRO,Model model)  {
		try {
		 tModuleRO=mModuleService.createOrUpdateModule(tModuleRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("moduleRO", tModuleRO);
		model.addAttribute("selectedMenu", "module");
		return "admin/module/module-edit";
	}

	@GetMapping(value = { "/admin/web/modules/{moduleId}" })
	public String showEditModule(Model model, @PathVariable Long moduleId) throws RestException {
		ModuleRO tModuleRO = mModuleService.getModuleById(moduleId);
	    model.addAttribute("selectedMenu", "module");
	    model.addAttribute("moduleRO", tModuleRO);
	    return "admin/module/module-edit";
	}

	

	@GetMapping(value = { "/admin/web/modules/{moduleId}/delete" })
	public String deleteModuleById(Model model, @PathVariable Long moduleId) throws RestException {
		mModuleService.deleteModuleById(moduleId);
		return "redirect:/admin/web/modules";
	}
	
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/courses" })
	public String getModuleCourses(Model model, @PathVariable Long moduleId) throws RestException {
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("courses", mModuleService.findModuleCourses(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/course/course-list";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/courses/assign" })
	public String assignCourses(Model model, @PathVariable Long moduleId) throws RestException {
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));		
		model.addAttribute("courses", mModuleService.findUnassignModuleCourses(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/course/course-assign";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/courses/{courseId}/assign" })
	public String getCourses(Model model, @PathVariable Long moduleId, @PathVariable Long courseId) throws RestException {
		mModuleService.addModuleCourse(moduleId, courseId);
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("courses", mModuleService.findModuleCourses(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/course/course-list";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/courses/{courseId}/unassign" })
	public String unassignCourses(Model model, @PathVariable Long moduleId, @PathVariable Long courseId) throws RestException {
		mModuleService.unassignModuleCourse(moduleId, courseId);
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("courses", mModuleService.findModuleCourses(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/course/course-list";
	}
	
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/topics" })
	public String getModuleTopics(Model model, @PathVariable Long moduleId) throws RestException {
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("topics", mModuleService.findModuleTopics(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/topic/topic-list";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/topics/assign" })
	public String assignTopics(Model model, @PathVariable Long moduleId) throws RestException {
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));		
		model.addAttribute("topics", mModuleService.findUnassignModuleTopics(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/topic/topic-assign";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/topics/{topicId}/assign" })
	public String getTopics(Model model, @PathVariable Long moduleId, @PathVariable Long topicId) throws RestException {
		mModuleService.addModuleTopic(moduleId, topicId);
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("topics", mModuleService.findModuleTopics(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/topic/topic-list";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/topics/{topicId}/unassign" })
	public String unassignTopics(Model model, @PathVariable Long moduleId, @PathVariable Long topicId) throws RestException {
		mModuleService.unassignModuleTopic(moduleId, topicId);
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("topics", mModuleService.findModuleTopics(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/topic/topic-list";
	}
	
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/images" })
	public String getModuleImages(Model model, @PathVariable Long moduleId) throws RestException {
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("images", mModuleService.findModuleImages(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/image/image-list";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/images/assign" })
	public String assignImages(Model model, @PathVariable Long moduleId) throws RestException {
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));		
		model.addAttribute("images", mModuleService.findUnassignModuleImages(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/image/image-assign";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/images/{imageId}/assign" })
	public String getImages(Model model, @PathVariable Long moduleId, @PathVariable Long imageId) throws RestException {
		mModuleService.addModuleImage(moduleId, imageId);
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("images", mModuleService.findModuleImages(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/image/image-list";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/images/{imageId}/unassign" })
	public String unassignImages(Model model, @PathVariable Long moduleId, @PathVariable Long imageId) throws RestException {
		mModuleService.unassignModuleImage(moduleId, imageId);
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("images", mModuleService.findModuleImages(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/image/image-list";
	}
	
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/videos" })
	public String getModuleVideos(Model model, @PathVariable Long moduleId) throws RestException {
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("videos", mModuleService.findModuleVideos(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/video/video-list";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/videos/assign" })
	public String assignVideos(Model model, @PathVariable Long moduleId) throws RestException {
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));		
		model.addAttribute("videos", mModuleService.findUnassignModuleVideos(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/video/video-assign";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/videos/{videoId}/assign" })
	public String getVideos(Model model, @PathVariable Long moduleId, @PathVariable Long videoId) throws RestException {
		mModuleService.addModuleVideo(moduleId, videoId);
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("videos", mModuleService.findModuleVideos(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/video/video-list";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/videos/{videoId}/unassign" })
	public String unassignVideos(Model model, @PathVariable Long moduleId, @PathVariable Long videoId) throws RestException {
		mModuleService.unassignModuleVideo(moduleId, videoId);
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("videos", mModuleService.findModuleVideos(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/video/video-list";
	}
	
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/pages" })
	public String getModulePages(Model model, @PathVariable Long moduleId) throws RestException {
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("pages", mModuleService.findModulePages(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/page/page-list";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/pages/assign" })
	public String assignPages(Model model, @PathVariable Long moduleId) throws RestException {
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));		
		model.addAttribute("pages", mModuleService.findUnassignModulePages(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/page/page-assign";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/pages/{pageId}/assign" })
	public String getPages(Model model, @PathVariable Long moduleId, @PathVariable Long pageId) throws RestException {
		mModuleService.addModulePage(moduleId, pageId);
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("pages", mModuleService.findModulePages(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/page/page-list";
	}
	
	@GetMapping(value = { "/admin/web/modules/{moduleId}/pages/{pageId}/unassign" })
	public String unassignPages(Model model, @PathVariable Long moduleId, @PathVariable Long pageId) throws RestException {
		mModuleService.unassignModulePage(moduleId, pageId);
		model.addAttribute("moduleRO", mModuleService.getModuleById(moduleId));
		model.addAttribute("pages", mModuleService.findModulePages(moduleId));
		model.addAttribute("selectedMenu", "module");
		return "admin/module/page/page-list";
	}
	

}
