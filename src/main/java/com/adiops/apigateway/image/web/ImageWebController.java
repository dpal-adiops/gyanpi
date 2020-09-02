package com.adiops.apigateway.image.web;

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
import com.adiops.apigateway.image.resourceobject.ImageRO;
import com.adiops.apigateway.image.service.ImageService;

/**
 * The web controller class for Image 
 * @author Deepak Pal
 *
 */
@Controller
public class ImageWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	ImageService mImageService;

	@GetMapping(value = "/admin/web/images")
	public String getImages(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<ImageRO> images = mImageService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mImageService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("images", images);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "image");
	    return "admin/image/image-list";
	}

	@GetMapping(value = { "/admin/web/images/add" })
	public String showAddImage(Model model) {
		ImageRO tImageRO = new ImageRO();
	    model.addAttribute("selectedMenu", "image");
	    model.addAttribute("imageRO", tImageRO);
	    return "admin/image/image-add";
	}

	@PostMapping(value = "/admin/web/images/add")
	public String addImage(@ModelAttribute ImageRO tImageRO,Model model)  {
		try {
		 tImageRO=mImageService.createOrUpdateImage(tImageRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("imageRO", tImageRO);
		model.addAttribute("selectedMenu", "image");
		return "admin/image/image-edit";
	}

	@GetMapping(value = { "/admin/web/images/{imageId}" })
	public String showEditImage(Model model, @PathVariable Long imageId) throws RestException {
		ImageRO tImageRO = mImageService.getImageById(imageId);
	    model.addAttribute("selectedMenu", "image");
	    model.addAttribute("imageRO", tImageRO);
	    return "admin/image/image-edit";
	}

	

	@GetMapping(value = { "/admin/web/images/{imageId}/delete" })
	public String deleteImageById(Model model, @PathVariable Long imageId) throws RestException {
		mImageService.deleteImageById(imageId);
		return "redirect:/admin/web/images";
	}
	
	
	@GetMapping(value = { "/admin/web/images/{imageId}/courses" })
	public String getImageCourses(Model model, @PathVariable Long imageId) throws RestException {
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("courses", mImageService.findImageCourses(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/course/course-list";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/courses/assign" })
	public String assignCourses(Model model, @PathVariable Long imageId) throws RestException {
		model.addAttribute("imageRO", mImageService.getImageById(imageId));		
		model.addAttribute("courses", mImageService.findUnassignImageCourses(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/course/course-assign";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/courses/{courseId}/assign" })
	public String getCourses(Model model, @PathVariable Long imageId, @PathVariable Long courseId) throws RestException {
		mImageService.addImageCourse(imageId, courseId);
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("courses", mImageService.findImageCourses(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/course/course-list";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/courses/{courseId}/unassign" })
	public String unassignCourses(Model model, @PathVariable Long imageId, @PathVariable Long courseId) throws RestException {
		mImageService.unassignImageCourse(imageId, courseId);
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("courses", mImageService.findImageCourses(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/course/course-list";
	}
	
	
	@GetMapping(value = { "/admin/web/images/{imageId}/modules" })
	public String getImageModules(Model model, @PathVariable Long imageId) throws RestException {
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("modules", mImageService.findImageModules(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/module/module-list";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/modules/assign" })
	public String assignModules(Model model, @PathVariable Long imageId) throws RestException {
		model.addAttribute("imageRO", mImageService.getImageById(imageId));		
		model.addAttribute("modules", mImageService.findUnassignImageModules(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/module/module-assign";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/modules/{moduleId}/assign" })
	public String getModules(Model model, @PathVariable Long imageId, @PathVariable Long moduleId) throws RestException {
		mImageService.addImageModule(imageId, moduleId);
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("modules", mImageService.findImageModules(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/module/module-list";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/modules/{moduleId}/unassign" })
	public String unassignModules(Model model, @PathVariable Long imageId, @PathVariable Long moduleId) throws RestException {
		mImageService.unassignImageModule(imageId, moduleId);
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("modules", mImageService.findImageModules(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/module/module-list";
	}
	
	
	@GetMapping(value = { "/admin/web/images/{imageId}/topics" })
	public String getImageTopics(Model model, @PathVariable Long imageId) throws RestException {
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("topics", mImageService.findImageTopics(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/topic/topic-list";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/topics/assign" })
	public String assignTopics(Model model, @PathVariable Long imageId) throws RestException {
		model.addAttribute("imageRO", mImageService.getImageById(imageId));		
		model.addAttribute("topics", mImageService.findUnassignImageTopics(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/topic/topic-assign";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/topics/{topicId}/assign" })
	public String getTopics(Model model, @PathVariable Long imageId, @PathVariable Long topicId) throws RestException {
		mImageService.addImageTopic(imageId, topicId);
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("topics", mImageService.findImageTopics(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/topic/topic-list";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/topics/{topicId}/unassign" })
	public String unassignTopics(Model model, @PathVariable Long imageId, @PathVariable Long topicId) throws RestException {
		mImageService.unassignImageTopic(imageId, topicId);
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("topics", mImageService.findImageTopics(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/topic/topic-list";
	}
	
	
	@GetMapping(value = { "/admin/web/images/{imageId}/questions" })
	public String getImageQuestions(Model model, @PathVariable Long imageId) throws RestException {
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("questions", mImageService.findImageQuestions(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/question/question-list";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/questions/assign" })
	public String assignQuestions(Model model, @PathVariable Long imageId) throws RestException {
		model.addAttribute("imageRO", mImageService.getImageById(imageId));		
		model.addAttribute("questions", mImageService.findUnassignImageQuestions(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/question/question-assign";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/questions/{questionId}/assign" })
	public String getQuestions(Model model, @PathVariable Long imageId, @PathVariable Long questionId) throws RestException {
		mImageService.addImageQuestion(imageId, questionId);
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("questions", mImageService.findImageQuestions(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/question/question-list";
	}
	
	@GetMapping(value = { "/admin/web/images/{imageId}/questions/{questionId}/unassign" })
	public String unassignQuestions(Model model, @PathVariable Long imageId, @PathVariable Long questionId) throws RestException {
		mImageService.unassignImageQuestion(imageId, questionId);
		model.addAttribute("imageRO", mImageService.getImageById(imageId));
		model.addAttribute("questions", mImageService.findImageQuestions(imageId));
		model.addAttribute("selectedMenu", "image");
		return "admin/image/question/question-list";
	}
	

}
