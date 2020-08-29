package com.adiops.apigateway.video.web;

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
import com.adiops.apigateway.video.resourceobject.VideoRO;
import com.adiops.apigateway.video.service.VideoService;

/**
 * The web controller class for Video 
 * @author Deepak Pal
 *
 */
@Controller
public class VideoWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	VideoService mVideoService;

	@GetMapping(value = "/web/videos")
	public String getVideos(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<VideoRO> videos = mVideoService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mVideoService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("videos", videos);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "video");
	    return "video/video-list";
	}

	@GetMapping(value = { "/web/videos/add" })
	public String showAddVideo(Model model) {
		VideoRO tVideoRO = new VideoRO();
	    model.addAttribute("selectedMenu", "video");
	    model.addAttribute("videoRO", tVideoRO);
	    return "video/video-add";
	}

	@PostMapping(value = "/web/videos/add")
	public String addVideo(@ModelAttribute VideoRO tVideoRO,Model model)  {
		try {
		 tVideoRO=mVideoService.createOrUpdateVideo(tVideoRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("videoRO", tVideoRO);
		model.addAttribute("selectedMenu", "video");
		return "video/video-edit";
	}

	@GetMapping(value = { "/web/videos/{videoId}" })
	public String showEditVideo(Model model, @PathVariable Long videoId) throws RestException {
		VideoRO tVideoRO = mVideoService.getVideoById(videoId);
	    model.addAttribute("selectedMenu", "video");
	    model.addAttribute("videoRO", tVideoRO);
	    return "video/video-edit";
	}

	

	@GetMapping(value = { "/web/videos/{videoId}/delete" })
	public String deleteVideoById(Model model, @PathVariable Long videoId) throws RestException {
		mVideoService.deleteVideoById(videoId);
		return "redirect:/web/videos";
	}
	
	
	@GetMapping(value = { "/web/videos/{videoId}/courses" })
	public String getVideoCourses(Model model, @PathVariable Long videoId) throws RestException {
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("courses", mVideoService.findVideoCourses(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/course/course-list";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/courses/assign" })
	public String assignCourses(Model model, @PathVariable Long videoId) throws RestException {
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));		
		model.addAttribute("courses", mVideoService.findUnassignVideoCourses(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/course/course-assign";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/courses/{courseId}/assign" })
	public String getCourses(Model model, @PathVariable Long videoId, @PathVariable Long courseId) throws RestException {
		mVideoService.addVideoCourse(videoId, courseId);
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("courses", mVideoService.findVideoCourses(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/course/course-list";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/courses/{courseId}/unassign" })
	public String unassignCourses(Model model, @PathVariable Long videoId, @PathVariable Long courseId) throws RestException {
		mVideoService.unassignVideoCourse(videoId, courseId);
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("courses", mVideoService.findVideoCourses(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/course/course-list";
	}
	
	
	@GetMapping(value = { "/web/videos/{videoId}/modules" })
	public String getVideoModules(Model model, @PathVariable Long videoId) throws RestException {
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("modules", mVideoService.findVideoModules(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/module/module-list";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/modules/assign" })
	public String assignModules(Model model, @PathVariable Long videoId) throws RestException {
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));		
		model.addAttribute("modules", mVideoService.findUnassignVideoModules(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/module/module-assign";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/modules/{moduleId}/assign" })
	public String getModules(Model model, @PathVariable Long videoId, @PathVariable Long moduleId) throws RestException {
		mVideoService.addVideoModule(videoId, moduleId);
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("modules", mVideoService.findVideoModules(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/module/module-list";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/modules/{moduleId}/unassign" })
	public String unassignModules(Model model, @PathVariable Long videoId, @PathVariable Long moduleId) throws RestException {
		mVideoService.unassignVideoModule(videoId, moduleId);
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("modules", mVideoService.findVideoModules(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/module/module-list";
	}
	
	
	@GetMapping(value = { "/web/videos/{videoId}/topics" })
	public String getVideoTopics(Model model, @PathVariable Long videoId) throws RestException {
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("topics", mVideoService.findVideoTopics(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/topic/topic-list";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/topics/assign" })
	public String assignTopics(Model model, @PathVariable Long videoId) throws RestException {
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));		
		model.addAttribute("topics", mVideoService.findUnassignVideoTopics(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/topic/topic-assign";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/topics/{topicId}/assign" })
	public String getTopics(Model model, @PathVariable Long videoId, @PathVariable Long topicId) throws RestException {
		mVideoService.addVideoTopic(videoId, topicId);
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("topics", mVideoService.findVideoTopics(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/topic/topic-list";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/topics/{topicId}/unassign" })
	public String unassignTopics(Model model, @PathVariable Long videoId, @PathVariable Long topicId) throws RestException {
		mVideoService.unassignVideoTopic(videoId, topicId);
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("topics", mVideoService.findVideoTopics(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/topic/topic-list";
	}
	
	
	@GetMapping(value = { "/web/videos/{videoId}/questions" })
	public String getVideoQuestions(Model model, @PathVariable Long videoId) throws RestException {
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("questions", mVideoService.findVideoQuestions(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/question/question-list";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/questions/assign" })
	public String assignQuestions(Model model, @PathVariable Long videoId) throws RestException {
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));		
		model.addAttribute("questions", mVideoService.findUnassignVideoQuestions(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/question/question-assign";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/questions/{questionId}/assign" })
	public String getQuestions(Model model, @PathVariable Long videoId, @PathVariable Long questionId) throws RestException {
		mVideoService.addVideoQuestion(videoId, questionId);
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("questions", mVideoService.findVideoQuestions(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/question/question-list";
	}
	
	@GetMapping(value = { "/web/videos/{videoId}/questions/{questionId}/unassign" })
	public String unassignQuestions(Model model, @PathVariable Long videoId, @PathVariable Long questionId) throws RestException {
		mVideoService.unassignVideoQuestion(videoId, questionId);
		model.addAttribute("videoRO", mVideoService.getVideoById(videoId));
		model.addAttribute("questions", mVideoService.findVideoQuestions(videoId));
		model.addAttribute("selectedMenu", "video");
		return "video/question/question-list";
	}
	

}
