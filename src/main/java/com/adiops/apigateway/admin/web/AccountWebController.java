package com.adiops.apigateway.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.question.resourceobject.QuestionRO;
import com.adiops.apigateway.question.service.QuestionService;
import com.adiops.apigateway.topic.service.TopicService;

@Controller
public class AccountWebController {


	@Autowired
	TopicService mTopicService;
	
	@Autowired
	QuestionService mQuestionService;
	
	
	@GetMapping(value = "/web/account/topic/{topicId}")
	public String getQuestions(Model model,@PathVariable Long topicId) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicId));
		List<QuestionRO> questions= mTopicService.findTopicQuestions(topicId);
		model.addAttribute("questions", questions);
		if(!questions.isEmpty())
		model.addAttribute("question", questions.get(0));		
		return "web/question-list";
	}
	
	@GetMapping(value = "/web/account/topic/{topicid}/question/{questionid}")
	public String getQuestions(Model model,@PathVariable Long topicid,@PathVariable Long questionid) throws RestException {
		model.addAttribute("topicRO", mTopicService.getTopicById(topicid));
		List<QuestionRO> questions= mTopicService.findTopicQuestions(topicid);		
		model.addAttribute("questions", questions);
		QuestionRO tQuestionRO= mQuestionService.getQuestionById(questionid);		
		model.addAttribute("question", tQuestionRO);		
		return "web/question-list";
	}
	
}
