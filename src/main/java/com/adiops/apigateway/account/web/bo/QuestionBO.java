package com.adiops.apigateway.account.web.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.adiops.apigateway.common.inject.NamingMgr;
import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.question.line.item.resourceobject.QuestionLineItemRO;
import com.adiops.apigateway.question.line.item.service.QuestionLineItemService;
import com.adiops.apigateway.question.resourceobject.QuestionRO;

@Configurable
public class QuestionBO {

	@Autowired
	private QuestionLineItemService mQuestionLineItemService;

	private QuestionRO questionRO;
	private QuestionLineItemRO questionLineItemRO;
	private TopicBO topicBO;

	
	public QuestionBO() {
		NamingMgr.injectMembers(this);
	}

	public TopicBO getTopicBO() {
		return topicBO;
	}

	public void setTopicBO(TopicBO topicBO) {
		this.topicBO = topicBO;
	}

	public QuestionRO getQuestionRO() {
		return questionRO;
	}

	public void setQuestionRO(QuestionRO questionRO) {
		this.questionRO = questionRO;
	}

	public QuestionLineItemRO getQuestionLineItemRO() {
		return questionLineItemRO;
	}

	public void setQuestionLineItemRO(QuestionLineItemRO questionLineItemRO) {
		this.questionLineItemRO = questionLineItemRO;
	}

	public void save() {
		try {
			questionLineItemRO = mQuestionLineItemService.createOrUpdateQuestionLineItem(questionLineItemRO);
			if (topicBO.getTopicLineGroupRO().getId() == null) {
				topicBO.save();

			}
			mQuestionLineItemService.addQuestionLineItemTopicLineGroup(questionLineItemRO.getId(),
					topicBO.getTopicLineGroupRO().getId());
			
			topicBO.saveSteps();
		} catch (RestException e) {
		}
	}
	

}
