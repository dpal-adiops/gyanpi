package com.adiops.apigateway.question.line.item.resourceobject;

import java.util.Date;


import com.adiops.apigateway.topic.line.group.resourceobject.TopicLineGroupRO;

/**
 * This class is responsible for transfer the resource QuestionLineItem Data
 * @author Deepak Pal
 *
 */
public class QuestionLineItemRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String keyid;
	private Integer status;
	private Integer score;
	private Integer maxScore;
	private String title;
	private String description;
	private String userKey;
	private String courseKey;
	private String moduleKey;
	private String topicKey;
	private String questionKey;
	private String answer;
	private String correctAnswer;
	private Integer questionType;
	
	
	
	 private TopicLineGroupRO topic_line_group;
	
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long uuid){
		this.id=uuid;
	}
	
	
	public void setKeyid(String keyid){
		this.keyid=keyid;
	}
	
	public String getKeyid(){
		return this.keyid;
	}
	
	public void setStatus(Integer status){
		this.status=status;
	}
	
	public Integer getStatus(){
		return this.status;
	}
	
	public void setScore(Integer score){
		this.score=score;
	}
	
	public Integer getScore(){
		return this.score;
	}
	
	public void setMaxScore(Integer maxScore){
		this.maxScore=maxScore;
	}
	
	public Integer getMaxScore(){
		return this.maxScore;
	}
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setUserKey(String userKey){
		this.userKey=userKey;
	}
	
	public String getUserKey(){
		return this.userKey;
	}
	
	public void setCourseKey(String courseKey){
		this.courseKey=courseKey;
	}
	
	public String getCourseKey(){
		return this.courseKey;
	}
	
	public void setModuleKey(String moduleKey){
		this.moduleKey=moduleKey;
	}
	
	public String getModuleKey(){
		return this.moduleKey;
	}
	
	public void setTopicKey(String topicKey){
		this.topicKey=topicKey;
	}
	
	public String getTopicKey(){
		return this.topicKey;
	}
	
	public void setQuestionKey(String questionKey){
		this.questionKey=questionKey;
	}
	
	public String getQuestionKey(){
		return this.questionKey;
	}
	
	public void setAnswer(String answer){
		this.answer=answer;
	}
	
	public String getAnswer(){
		return this.answer;
	}
	
	public void setCorrectAnswer(String correctAnswer){
		this.correctAnswer=correctAnswer;
	}
	
	public String getCorrectAnswer(){
		return this.correctAnswer;
	}
	
	public void setQuestionType(Integer questionType){
		this.questionType=questionType;
	}
	
	public Integer getQuestionType(){
		return this.questionType;
	}
	
	
	
	 private TopicLineGroupRO topicLineGroupRO;
	
	public void setTopicLineGroupRO(TopicLineGroupRO topic_line_groupRO){
		this.topicLineGroupRO=topic_line_groupRO;
	}
	
	public TopicLineGroupRO getTopicLineGroupRO(){
		return this.topicLineGroupRO;
	}
}
