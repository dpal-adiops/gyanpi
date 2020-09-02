package com.adiops.apigateway.question.web;

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
import com.adiops.apigateway.question.resourceobject.QuestionRO;
import com.adiops.apigateway.question.service.QuestionService;

/**
 * The web controller class for Question 
 * @author Deepak Pal
 *
 */
@Controller
public class QuestionWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	QuestionService mQuestionService;

	@GetMapping(value = "/admin/web/questions")
	public String getQuestions(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<QuestionRO> questions = mQuestionService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mQuestionService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("questions", questions);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "question");
	    return "admin/question/question-list";
	}

	@GetMapping(value = { "/admin/web/questions/add" })
	public String showAddQuestion(Model model) {
		QuestionRO tQuestionRO = new QuestionRO();
	    model.addAttribute("selectedMenu", "question");
	    model.addAttribute("questionRO", tQuestionRO);
	    return "admin/question/question-add";
	}

	@PostMapping(value = "/admin/web/questions/add")
	public String addQuestion(@ModelAttribute QuestionRO tQuestionRO,Model model)  {
		try {
		 tQuestionRO=mQuestionService.createOrUpdateQuestion(tQuestionRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("questionRO", tQuestionRO);
		model.addAttribute("selectedMenu", "question");
		return "admin/question/question-edit";
	}

	@GetMapping(value = { "/admin/web/questions/{questionId}" })
	public String showEditQuestion(Model model, @PathVariable Long questionId) throws RestException {
		QuestionRO tQuestionRO = mQuestionService.getQuestionById(questionId);
	    model.addAttribute("selectedMenu", "question");
	    model.addAttribute("questionRO", tQuestionRO);
	    return "admin/question/question-edit";
	}

	

	@GetMapping(value = { "/admin/web/questions/{questionId}/delete" })
	public String deleteQuestionById(Model model, @PathVariable Long questionId) throws RestException {
		mQuestionService.deleteQuestionById(questionId);
		return "redirect:/admin/web/questions";
	}
	
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/topics" })
	public String getQuestionTopics(Model model, @PathVariable Long questionId) throws RestException {
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("topics", mQuestionService.findQuestionTopics(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/topic/topic-list";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/topics/assign" })
	public String assignTopics(Model model, @PathVariable Long questionId) throws RestException {
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));		
		model.addAttribute("topics", mQuestionService.findUnassignQuestionTopics(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/topic/topic-assign";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/topics/{topicId}/assign" })
	public String getTopics(Model model, @PathVariable Long questionId, @PathVariable Long topicId) throws RestException {
		mQuestionService.addQuestionTopic(questionId, topicId);
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("topics", mQuestionService.findQuestionTopics(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/topic/topic-list";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/topics/{topicId}/unassign" })
	public String unassignTopics(Model model, @PathVariable Long questionId, @PathVariable Long topicId) throws RestException {
		mQuestionService.unassignQuestionTopic(questionId, topicId);
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("topics", mQuestionService.findQuestionTopics(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/topic/topic-list";
	}
	
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/images" })
	public String getQuestionImages(Model model, @PathVariable Long questionId) throws RestException {
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("images", mQuestionService.findQuestionImages(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/image/image-list";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/images/assign" })
	public String assignImages(Model model, @PathVariable Long questionId) throws RestException {
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));		
		model.addAttribute("images", mQuestionService.findUnassignQuestionImages(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/image/image-assign";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/images/{imageId}/assign" })
	public String getImages(Model model, @PathVariable Long questionId, @PathVariable Long imageId) throws RestException {
		mQuestionService.addQuestionImage(questionId, imageId);
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("images", mQuestionService.findQuestionImages(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/image/image-list";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/images/{imageId}/unassign" })
	public String unassignImages(Model model, @PathVariable Long questionId, @PathVariable Long imageId) throws RestException {
		mQuestionService.unassignQuestionImage(questionId, imageId);
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("images", mQuestionService.findQuestionImages(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/image/image-list";
	}
	
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/videos" })
	public String getQuestionVideos(Model model, @PathVariable Long questionId) throws RestException {
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("videos", mQuestionService.findQuestionVideos(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/video/video-list";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/videos/assign" })
	public String assignVideos(Model model, @PathVariable Long questionId) throws RestException {
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));		
		model.addAttribute("videos", mQuestionService.findUnassignQuestionVideos(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/video/video-assign";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/videos/{videoId}/assign" })
	public String getVideos(Model model, @PathVariable Long questionId, @PathVariable Long videoId) throws RestException {
		mQuestionService.addQuestionVideo(questionId, videoId);
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("videos", mQuestionService.findQuestionVideos(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/video/video-list";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/videos/{videoId}/unassign" })
	public String unassignVideos(Model model, @PathVariable Long questionId, @PathVariable Long videoId) throws RestException {
		mQuestionService.unassignQuestionVideo(questionId, videoId);
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("videos", mQuestionService.findQuestionVideos(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/video/video-list";
	}
	
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/pages" })
	public String getQuestionPages(Model model, @PathVariable Long questionId) throws RestException {
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("pages", mQuestionService.findQuestionPages(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/page/page-list";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/pages/assign" })
	public String assignPages(Model model, @PathVariable Long questionId) throws RestException {
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));		
		model.addAttribute("pages", mQuestionService.findUnassignQuestionPages(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/page/page-assign";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/pages/{pageId}/assign" })
	public String getPages(Model model, @PathVariable Long questionId, @PathVariable Long pageId) throws RestException {
		mQuestionService.addQuestionPage(questionId, pageId);
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("pages", mQuestionService.findQuestionPages(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/page/page-list";
	}
	
	@GetMapping(value = { "/admin/web/questions/{questionId}/pages/{pageId}/unassign" })
	public String unassignPages(Model model, @PathVariable Long questionId, @PathVariable Long pageId) throws RestException {
		mQuestionService.unassignQuestionPage(questionId, pageId);
		model.addAttribute("questionRO", mQuestionService.getQuestionById(questionId));
		model.addAttribute("pages", mQuestionService.findQuestionPages(questionId));
		model.addAttribute("selectedMenu", "question");
		return "admin/question/page/page-list";
	}
	

}
