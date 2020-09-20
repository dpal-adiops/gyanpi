package com.adiops.apigateway.learning.track.question.web;

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
import com.adiops.apigateway.learning.track.question.resourceobject.LearningTrackQuestionRO;
import com.adiops.apigateway.learning.track.question.service.LearningTrackQuestionService;

/**
 * The web controller class for LearningTrackQuestion 
 * @author Deepak Pal
 *
 */
@Controller
public class LearningTrackQuestionWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int ROW_PER_PAGE = 30;
	
	@Autowired
	LearningTrackQuestionService mLearningTrackQuestionService;

	@GetMapping(value = "/admin/web/learning_track_questions")
	public String getLearningTrackQuestions(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
		List<LearningTrackQuestionRO> learning_track_questions = mLearningTrackQuestionService.findAll(pageNumber, ROW_PER_PAGE);
		 
	    long count = mLearningTrackQuestionService.count();
	    boolean hasPrev = pageNumber > 1;
	    boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
	    model.addAttribute("learning_track_questions", learning_track_questions);
	    model.addAttribute("hasPrev", hasPrev);
	    model.addAttribute("prev", pageNumber - 1);
	    model.addAttribute("hasNext", hasNext);
	    model.addAttribute("next", pageNumber + 1);
	    model.addAttribute("selectedMenu", "learning_track_question");
	    return "admin/learning_track_question/learning_track_question-list";
	}

	@GetMapping(value = { "/admin/web/learning_track_questions/add" })
	public String showAddLearningTrackQuestion(Model model) {
		LearningTrackQuestionRO tLearningTrackQuestionRO = new LearningTrackQuestionRO();
	    model.addAttribute("selectedMenu", "learning_track_question");
	    model.addAttribute("learning_track_questionRO", tLearningTrackQuestionRO);
	    return "admin/learning_track_question/learning_track_question-add";
	}

	@PostMapping(value = "/admin/web/learning_track_questions/add")
	public String addLearningTrackQuestion(@ModelAttribute LearningTrackQuestionRO tLearningTrackQuestionRO,Model model)  {
		try {
		 tLearningTrackQuestionRO=mLearningTrackQuestionService.createOrUpdateLearningTrackQuestion(tLearningTrackQuestionRO);
		} catch (RestException e) {
			model.addAttribute("error", e.getMessage());
		}
		
		
		model.addAttribute("learning_track_questionRO", tLearningTrackQuestionRO);
		model.addAttribute("selectedMenu", "learning_track_question");
		return "admin/learning_track_question/learning_track_question-edit";
	}

	@GetMapping(value = { "/admin/web/learning_track_questions/{learning_track_questionId}" })
	public String showEditLearningTrackQuestion(Model model, @PathVariable Long learning_track_questionId) throws RestException {
		LearningTrackQuestionRO tLearningTrackQuestionRO = mLearningTrackQuestionService.getLearningTrackQuestionById(learning_track_questionId);
	    model.addAttribute("selectedMenu", "learning_track_question");
	    model.addAttribute("learning_track_questionRO", tLearningTrackQuestionRO);
	    return "admin/learning_track_question/learning_track_question-edit";
	}

	

	@GetMapping(value = { "/admin/web/learning_track_questions/{learning_track_questionId}/delete" })
	public String deleteLearningTrackQuestionById(Model model, @PathVariable Long learning_track_questionId) throws RestException {
		mLearningTrackQuestionService.deleteLearningTrackQuestionById(learning_track_questionId);
		return "redirect:/admin/web/learning_track_questions";
	}
	

}
