package com.adiops.apigateway.topic.web;

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
import com.adiops.apigateway.topic.resourceobject.TopicRO;
import com.adiops.apigateway.topic.service.TopicService;

/**
 * The web controller class for Topic 
 * @author Deepak Pal
 *
 */
@Controller
public class TopicWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	TopicService mTopicService;

	@GetMapping(value = "/admin/web/topics")
	public String getTopics(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<TopicRO> topics = mTopicService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mTopicService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("topics", topics);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "topic");
	    return "admin/topic/topic-list";
	}

	@GetMapping(value = { "/admin/web/topics/add" })
	public String showAddTopic(Model model) {
		TopicRO tTopicRO = new TopicRO();
	    model.addAttribute("selectedMenu", "topic");
	    model.addAttribute("topicRO", tTopicRO);
	    return "admin/topic/topic-add";
	}

	@PostMapping(value = "/admin/web/topics/add")
	public String addTopic(@ModelAttribute TopicRO tTopicRO,Model model)  {
		try {
		 tTopicRO=mTopicService.createOrUpdateTopic(tTopicRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("topicRO", tTopicRO);
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/topic-edit";
	}

	@GetMapping(value = { "/admin/web/topics/{topicId}" })
	public String showEditTopic(Model model, @PathVariable Long topicId) throws RestException {
		TopicRO tTopicRO = mTopicService.getTopicById(topicId);
	    model.addAttribute("selectedMenu", "topic");
	    model.addAttribute("topicRO", tTopicRO);
	    return "admin/topic/topic-edit";
	}

	

	@GetMapping(value = { "/admin/web/topics/{topicId}/delete" })
	public String deleteTopicById(Model model, @PathVariable Long topicId) throws RestException {
		mTopicService.deleteTopicById(topicId);
		return "redirect:/admin/web/topics";
	}
	
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/modules" })
	public String getTopicModules(Model model, @PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("modules", mTopicService.findTopicModules(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/module/module-list";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/modules/assign" })
	public String assignModules(Model model, @PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));		
		model.addAttribute("modules", mTopicService.findUnassignTopicModules(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/module/module-assign";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/modules/{moduleId}/assign" })
	public String getModules(Model model, @PathVariable Long topicId, @PathVariable Long moduleId) throws RestException {
		mTopicService.addTopicModule(topicId, moduleId);
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("modules", mTopicService.findTopicModules(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/module/module-list";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/modules/{moduleId}/unassign" })
	public String unassignModules(Model model, @PathVariable Long topicId, @PathVariable Long moduleId) throws RestException {
		mTopicService.unassignTopicModule(topicId, moduleId);
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("modules", mTopicService.findTopicModules(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/module/module-list";
	}
	
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/questions" })
	public String getTopicQuestions(Model model, @PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("questions", mTopicService.findTopicQuestions(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/question/question-list";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/questions/assign" })
	public String assignQuestions(Model model, @PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));		
		model.addAttribute("questions", mTopicService.findUnassignTopicQuestions(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/question/question-assign";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/questions/{questionId}/assign" })
	public String getQuestions(Model model, @PathVariable Long topicId, @PathVariable Long questionId) throws RestException {
		mTopicService.addTopicQuestion(topicId, questionId);
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("questions", mTopicService.findTopicQuestions(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/question/question-list";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/questions/{questionId}/unassign" })
	public String unassignQuestions(Model model, @PathVariable Long topicId, @PathVariable Long questionId) throws RestException {
		mTopicService.unassignTopicQuestion(topicId, questionId);
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("questions", mTopicService.findTopicQuestions(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/question/question-list";
	}
	
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/images" })
	public String getTopicImages(Model model, @PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("images", mTopicService.findTopicImages(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/image/image-list";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/images/assign" })
	public String assignImages(Model model, @PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));		
		model.addAttribute("images", mTopicService.findUnassignTopicImages(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/image/image-assign";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/images/{imageId}/assign" })
	public String getImages(Model model, @PathVariable Long topicId, @PathVariable Long imageId) throws RestException {
		mTopicService.addTopicImage(topicId, imageId);
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("images", mTopicService.findTopicImages(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/image/image-list";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/images/{imageId}/unassign" })
	public String unassignImages(Model model, @PathVariable Long topicId, @PathVariable Long imageId) throws RestException {
		mTopicService.unassignTopicImage(topicId, imageId);
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("images", mTopicService.findTopicImages(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/image/image-list";
	}
	
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/videos" })
	public String getTopicVideos(Model model, @PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("videos", mTopicService.findTopicVideos(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/video/video-list";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/videos/assign" })
	public String assignVideos(Model model, @PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));		
		model.addAttribute("videos", mTopicService.findUnassignTopicVideos(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/video/video-assign";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/videos/{videoId}/assign" })
	public String getVideos(Model model, @PathVariable Long topicId, @PathVariable Long videoId) throws RestException {
		mTopicService.addTopicVideo(topicId, videoId);
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("videos", mTopicService.findTopicVideos(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/video/video-list";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/videos/{videoId}/unassign" })
	public String unassignVideos(Model model, @PathVariable Long topicId, @PathVariable Long videoId) throws RestException {
		mTopicService.unassignTopicVideo(topicId, videoId);
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("videos", mTopicService.findTopicVideos(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/video/video-list";
	}
	
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/pages" })
	public String getTopicPages(Model model, @PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("pages", mTopicService.findTopicPages(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/page/page-list";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/pages/assign" })
	public String assignPages(Model model, @PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));		
		model.addAttribute("pages", mTopicService.findUnassignTopicPages(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/page/page-assign";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/pages/{pageId}/assign" })
	public String getPages(Model model, @PathVariable Long topicId, @PathVariable Long pageId) throws RestException {
		mTopicService.addTopicPage(topicId, pageId);
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("pages", mTopicService.findTopicPages(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/page/page-list";
	}
	
	@GetMapping(value = { "/admin/web/topics/{topicId}/pages/{pageId}/unassign" })
	public String unassignPages(Model model, @PathVariable Long topicId, @PathVariable Long pageId) throws RestException {
		mTopicService.unassignTopicPage(topicId, pageId);
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		model.addAttribute("pages", mTopicService.findTopicPages(topicId));
		model.addAttribute("selectedMenu", "topic");
		return "admin/topic/page/page-list";
	}
	

}
