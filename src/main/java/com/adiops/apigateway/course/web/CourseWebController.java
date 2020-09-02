package com.adiops.apigateway.course.web;

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
import com.adiops.apigateway.course.resourceobject.CourseRO;
import com.adiops.apigateway.course.service.CourseService;

/**
 * The web controller class for Course 
 * @author Deepak Pal
 *
 */
@Controller
public class CourseWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	CourseService mCourseService;

	@GetMapping(value = "/admin/web/courses")
	public String getCourses(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<CourseRO> courses = mCourseService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mCourseService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("courses", courses);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "course");
	    return "admin/course/course-list";
	}

	@GetMapping(value = { "/admin/web/courses/add" })
	public String showAddCourse(Model model) {
		CourseRO tCourseRO = new CourseRO();
	    model.addAttribute("selectedMenu", "course");
	    model.addAttribute("courseRO", tCourseRO);
	    return "admin/course/course-add";
	}

	@PostMapping(value = "/admin/web/courses/add")
	public String addCourse(@ModelAttribute CourseRO tCourseRO,Model model)  {
		try {
		 tCourseRO=mCourseService.createOrUpdateCourse(tCourseRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("courseRO", tCourseRO);
		model.addAttribute("selectedMenu", "course");
		return "admin/course/course-edit";
	}

	@GetMapping(value = { "/admin/web/courses/{courseId}" })
	public String showEditCourse(Model model, @PathVariable Long courseId) throws RestException {
		CourseRO tCourseRO = mCourseService.getCourseById(courseId);
	    model.addAttribute("selectedMenu", "course");
	    model.addAttribute("courseRO", tCourseRO);
	    return "admin/course/course-edit";
	}

	

	@GetMapping(value = { "/admin/web/courses/{courseId}/delete" })
	public String deleteCourseById(Model model, @PathVariable Long courseId) throws RestException {
		mCourseService.deleteCourseById(courseId);
		return "redirect:/admin/web/courses";
	}
	
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/modules" })
	public String getCourseModules(Model model, @PathVariable Long courseId) throws RestException {
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("modules", mCourseService.findCourseModules(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/module/module-list";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/modules/assign" })
	public String assignModules(Model model, @PathVariable Long courseId) throws RestException {
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));		
		model.addAttribute("modules", mCourseService.findUnassignCourseModules(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/module/module-assign";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/modules/{moduleId}/assign" })
	public String getModules(Model model, @PathVariable Long courseId, @PathVariable Long moduleId) throws RestException {
		mCourseService.addCourseModule(courseId, moduleId);
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("modules", mCourseService.findCourseModules(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/module/module-list";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/modules/{moduleId}/unassign" })
	public String unassignModules(Model model, @PathVariable Long courseId, @PathVariable Long moduleId) throws RestException {
		mCourseService.unassignCourseModule(courseId, moduleId);
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("modules", mCourseService.findCourseModules(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/module/module-list";
	}
	
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/images" })
	public String getCourseImages(Model model, @PathVariable Long courseId) throws RestException {
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("images", mCourseService.findCourseImages(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/image/image-list";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/images/assign" })
	public String assignImages(Model model, @PathVariable Long courseId) throws RestException {
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));		
		model.addAttribute("images", mCourseService.findUnassignCourseImages(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/image/image-assign";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/images/{imageId}/assign" })
	public String getImages(Model model, @PathVariable Long courseId, @PathVariable Long imageId) throws RestException {
		mCourseService.addCourseImage(courseId, imageId);
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("images", mCourseService.findCourseImages(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/image/image-list";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/images/{imageId}/unassign" })
	public String unassignImages(Model model, @PathVariable Long courseId, @PathVariable Long imageId) throws RestException {
		mCourseService.unassignCourseImage(courseId, imageId);
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("images", mCourseService.findCourseImages(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/image/image-list";
	}
	
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/videos" })
	public String getCourseVideos(Model model, @PathVariable Long courseId) throws RestException {
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("videos", mCourseService.findCourseVideos(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/video/video-list";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/videos/assign" })
	public String assignVideos(Model model, @PathVariable Long courseId) throws RestException {
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));		
		model.addAttribute("videos", mCourseService.findUnassignCourseVideos(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/video/video-assign";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/videos/{videoId}/assign" })
	public String getVideos(Model model, @PathVariable Long courseId, @PathVariable Long videoId) throws RestException {
		mCourseService.addCourseVideo(courseId, videoId);
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("videos", mCourseService.findCourseVideos(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/video/video-list";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/videos/{videoId}/unassign" })
	public String unassignVideos(Model model, @PathVariable Long courseId, @PathVariable Long videoId) throws RestException {
		mCourseService.unassignCourseVideo(courseId, videoId);
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("videos", mCourseService.findCourseVideos(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/video/video-list";
	}
	
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/pages" })
	public String getCoursePages(Model model, @PathVariable Long courseId) throws RestException {
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("pages", mCourseService.findCoursePages(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/page/page-list";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/pages/assign" })
	public String assignPages(Model model, @PathVariable Long courseId) throws RestException {
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));		
		model.addAttribute("pages", mCourseService.findUnassignCoursePages(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/page/page-assign";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/pages/{pageId}/assign" })
	public String getPages(Model model, @PathVariable Long courseId, @PathVariable Long pageId) throws RestException {
		mCourseService.addCoursePage(courseId, pageId);
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("pages", mCourseService.findCoursePages(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/page/page-list";
	}
	
	@GetMapping(value = { "/admin/web/courses/{courseId}/pages/{pageId}/unassign" })
	public String unassignPages(Model model, @PathVariable Long courseId, @PathVariable Long pageId) throws RestException {
		mCourseService.unassignCoursePage(courseId, pageId);
		model.addAttribute("courseRO", mCourseService.getCourseById(courseId));
		model.addAttribute("pages", mCourseService.findCoursePages(courseId));
		model.addAttribute("selectedMenu", "course");
		return "admin/course/page/page-list";
	}
	

}
