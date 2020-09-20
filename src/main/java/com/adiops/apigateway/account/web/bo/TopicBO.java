package com.adiops.apigateway.account.web.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.adiops.apigateway.account.web.bo.view.CacheMgr;
import com.adiops.apigateway.common.inject.NamingMgr;
import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.question.line.item.resourceobject.QuestionLineItemRO;
import com.adiops.apigateway.question.line.item.service.QuestionLineItemService;
import com.adiops.apigateway.question.resourceobject.QuestionRO;
import com.adiops.apigateway.question.service.QuestionService;
import com.adiops.apigateway.topic.line.group.resourceobject.TopicLineGroupRO;
import com.adiops.apigateway.topic.line.group.service.TopicLineGroupService;
import com.adiops.apigateway.topic.resourceobject.TopicRO;
import com.adiops.apigateway.topic.service.TopicService;

@Configurable
public class TopicBO {

	@Autowired
	private QuestionService mQuestionService;
	
	@Autowired
	private TopicService mTopicService;

	@Autowired
	private QuestionLineItemService mQuestionLineItemService;

	@Autowired
	private TopicLineGroupService mTopicLineGroupService;

	private TopicRO topicRO;
	private TopicLineGroupRO topicLineGroupRO;
	private ModuleBO moduleBO;
	private List<QuestionBO> questionBOs = new ArrayList<QuestionBO>();
	private Map<String,QuestionBO> mapQuestionBOs = new HashMap<>();
	
	public TopicBO() {
		NamingMgr.injectMembers(this);
	}

	public ModuleBO getModuleBO() {
		return moduleBO;
	}

	public void setModuleBO(ModuleBO moduleBO) {
		this.moduleBO = moduleBO;
	}

	public TopicRO getTopicRO() {
		return topicRO;
	}

	public void setTopicRO(TopicRO topicRO) {
		this.topicRO = topicRO;
	}

	public TopicLineGroupRO getTopicLineGroupRO() {
		return topicLineGroupRO;
	}

	public void setTopicLineGroupRO(TopicLineGroupRO topicLineGroupRO) {
		this.topicLineGroupRO = topicLineGroupRO;
	}

	public void fetchQuestionBOs() {
		Collection<QuestionRO> tQuestionROs =CacheMgr.getTopicQuestionROs(topicRO.getKeyid());
		AtomicBoolean missflag = new AtomicBoolean(false);
		if (tQuestionROs.isEmpty()) {
			tQuestionROs =mTopicService.findTopicQuestions(topicRO.getId());
			missflag.set(true);
		}
		questionBOs = tQuestionROs.stream().map(ro ->{
			QuestionBO tBo=getQuestionBO(ro.getKeyid());
			mapQuestionBOs.put(ro.getKeyid(), tBo);
			if (missflag.get()) {
				CacheMgr.addQuestionRO(ro);
				CacheMgr.addTopicQuestionROs(topicRO.getKeyid(), ro.getKeyid());
			}
			return tBo;			
		}).collect(Collectors.toList());
		
	}

	
	public List<QuestionBO> getQuestionBOs() {
		return questionBOs;
	}

	public void setQuestionBOs(List<QuestionBO> questionBOs) {
		this.questionBOs = questionBOs;
	}

	public QuestionBO getQuestionBO(String id) {
		if(mapQuestionBOs.containsKey(id)) {return mapQuestionBOs.get(id);}
		QuestionBO tQuestionBO = new QuestionBO();
		TopicRO tTopicRO = getTopicRO();
		if (tTopicRO != null) {
			QuestionLineItemRO tQuestionLineItem = mQuestionLineItemService
					.getQuestionLineItemByKeyId(topicLineGroupRO.getKeyid() + "_" + id);
			//QuestionRO tQuestionRO = mQuestionService.getQuestionByKeyId(id);
			QuestionRO tQuestionRO = CacheMgr.getQuestionRO(id);
			if (tQuestionRO == null)
				return tQuestionBO;
			if (tQuestionLineItem == null) {
				tQuestionLineItem = new QuestionLineItemRO();
				tQuestionLineItem.setKeyid(topicLineGroupRO.getKeyid() + "_" + id);
				tQuestionLineItem.setMaxScore(0);
				tQuestionLineItem.setScore(0);
				tQuestionLineItem.setStatus(1);
				tQuestionLineItem.setTitle(tQuestionRO.getTitle());
				tQuestionLineItem.setDescription(tQuestionRO.getDescription());
				tQuestionLineItem.setCourseKey(topicLineGroupRO.getCourseKey());
				tQuestionLineItem.setQuestionKey(tQuestionRO.getKeyid());
				tQuestionLineItem.setUserKey(topicLineGroupRO.getUserKey());
				tQuestionLineItem.setTopicKey(topicLineGroupRO.getTopicKey());
				tQuestionLineItem.setModuleKey(topicLineGroupRO.getModuleKey());
				tQuestionLineItem.setTopicLineGroupRO(topicLineGroupRO);
			}
			tQuestionBO.setQuestionRO(tQuestionRO);
			tQuestionBO.setQuestionLineItemRO(tQuestionLineItem);
		}
		tQuestionBO.setTopicBO(this);		
		return tQuestionBO;
	}

	public void save() {
		try {
			topicLineGroupRO = mTopicLineGroupService.createOrUpdateTopicLineGroup(topicLineGroupRO);
			if (moduleBO.getModuleLineGroupRO().getId() == null) {
				moduleBO.save();

			}
			mTopicLineGroupService.addTopicLineGroupModuleLineGroup(topicLineGroupRO.getId(),
					moduleBO.getModuleLineGroupRO().getId());			
			
		} catch (RestException e) {
		}
	}
	
	public void calculateBackwardSteps() {
		int steps=0;
		int totalsteps=0;
		int score=0;
		int max_score=0;
		for(QuestionBO tQuestionBO:getQuestionBOs()) {
			Integer status=tQuestionBO.getQuestionLineItemRO().getStatus();
			if(status.intValue()==3)
			{
				steps++;
				score=score+tQuestionBO.getQuestionLineItemRO().getScore();
			}
			totalsteps++;
			max_score=max_score+tQuestionBO.getQuestionLineItemRO().getMaxScore();
		}
		topicLineGroupRO.setStep(steps);
		topicLineGroupRO.setTotalStep(totalsteps);	
		topicLineGroupRO.setScore(score);
		topicLineGroupRO.setMaxScore(max_score);
	}
	
	public void saveSteps() {
		calculateBackwardSteps();
		try {
			topicLineGroupRO = mTopicLineGroupService.createOrUpdateTopicLineGroup(topicLineGroupRO);
			moduleBO.saveSteps();
		} catch (RestException e) {			
		}
		
	}
	
	public int getProgress() {
		int step=topicLineGroupRO.getStep();
		int total=topicLineGroupRO.getTotalStep();
		return (step*100)/total;
	}
}
