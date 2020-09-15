package com.adiops.apigateway.admin.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.adiops.apigateway.account.web.bo.CourseBO;
import com.adiops.apigateway.account.web.bo.LearningPathBORepository;
import com.adiops.apigateway.account.web.bo.ModuleBO;
import com.adiops.apigateway.account.web.bo.QuestionBO;
import com.adiops.apigateway.account.web.bo.TopicBO;
import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.question.line.item.resourceobject.QuestionLineItemRO;
import com.adiops.apigateway.question.service.QuestionService;
import com.adiops.apigateway.topic.service.TopicService;

@Controller
public class AccountWebController {

	@Autowired
	TopicService mTopicService;

	@Autowired
	QuestionService mQuestionService;

	@Autowired
	LearningPathBORepository mAccountLearningTrackBO;

	@GetMapping(value = "/web/browse/{levelId}")
	public String getLevelQuestions(Model model, @PathVariable String levelId) throws RestException {
		String courseId = StringUtils.truncate(levelId, 6);
		String chapterId = StringUtils.truncate(levelId, 9);
		model.addAttribute("course", courseId);
		model.addAttribute("chapter", chapterId);
		model.addAttribute("level", levelId);
		model.addAttribute("courseBO", mAccountLearningTrackBO.getLearningPathBO().getCourse());

		CourseBO courseBO = mAccountLearningTrackBO.getLearningPathBO().getCourse();
		ModuleBO moduleBO = courseBO.getModuleBO(chapterId);
		TopicBO topicBO = moduleBO.getTopicBO(levelId);
		model.addAttribute("topicBO", topicBO);
		topicBO.getQuestionBOs().stream().filter(
				bo -> (bo.getQuestionLineItemRO().getStatus() == 1 || bo.getQuestionLineItemRO().getStatus() == 2))
				.findFirst().ifPresent(qBO -> {
					qBO.getQuestionLineItemRO().setStatus(2);
					model.addAttribute("questionBO", qBO);
				});
		if(!model.containsAttribute("questionBO"))
			model.addAttribute("Course_Complete", "This topic has been completed");
		return "web/learning-track-level";
	}

	@GetMapping(value = "/web/browse/{levelId}/{questionid}")
	public String getQuestions(Model model, @PathVariable String levelId, @PathVariable Long questionid)
			throws RestException {
		String courseId = StringUtils.truncate(levelId, 6);
		String chapterId = StringUtils.truncate(levelId, 9);
		model.addAttribute("course", courseId);
		model.addAttribute("chapter", chapterId);
		model.addAttribute("level", levelId);
		CourseBO courseBO = mAccountLearningTrackBO.getLearningPathBO().getCourse();
		ModuleBO moduleBO = courseBO.getModuleBO(chapterId);
		TopicBO topicBO = moduleBO.getTopicBO(levelId);
		model.addAttribute("topicBO", topicBO);
		topicBO.getQuestionBOs().stream().filter(
				bo -> (bo.getQuestionRO().getId() == questionid))
				.findFirst().ifPresent(qBO -> {
					if(qBO.getQuestionLineItemRO().getStatus()==1)
						qBO.getQuestionLineItemRO().setStatus(2);
					
					model.addAttribute("questionBO", qBO);
				});

		topicBO.getQuestionBOs().stream().filter(
				bo -> (bo.getQuestionLineItemRO().getStatus() == 1 || bo.getQuestionLineItemRO().getStatus() == 2))
				.findFirst().ifPresent(qBO -> {
					model.addAttribute("nextQuestionBO", qBO);
				});
		return "web/learning-track-level";
	}

	@PostMapping(value = "/web/browse/question")
	public String submitQuestion(QuestionLineItemRO pQuestionLineItemRO, Model model) {

		ModuleBO moduleBO = mAccountLearningTrackBO.getLearningPathBO().getCourse()
				.getModuleBO(pQuestionLineItemRO.getModuleKey());
		TopicBO topicBO = moduleBO.getTopicBO(pQuestionLineItemRO.getTopicKey());
		QuestionBO tQuestionBO=topicBO.getQuestionBO(pQuestionLineItemRO.getQuestionKey());
		if(tQuestionBO.getQuestionRO().getAnswer().equals(pQuestionLineItemRO.getAnswer()))
		{
			pQuestionLineItemRO.setStatus(3);
			pQuestionLineItemRO.setScore(100);
			pQuestionLineItemRO.setMaxScore(100);
			pQuestionLineItemRO.setCorrectAnswer(tQuestionBO.getQuestionRO().getAnswer());
			tQuestionBO.setQuestionLineItemRO(pQuestionLineItemRO);
			tQuestionBO.save();		
			return "redirect:/web/browse/"+pQuestionLineItemRO.getTopicKey();
		}else
		{
			model.addAttribute("course", pQuestionLineItemRO.getCourseKey());
			model.addAttribute("chapter", pQuestionLineItemRO.getModuleKey());
			model.addAttribute("level", pQuestionLineItemRO.getTopicKey());
			model.addAttribute("topicBO", topicBO);
			model.addAttribute("questionBO", tQuestionBO);
			model.addAttribute("error", "Wrong Answer..");
			return "web/learning-track-level";
		}
		
	}

}
