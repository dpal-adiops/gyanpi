package com.adiops.apigateway.learning.track.question.resourceobject;

import java.util.Date;

import com.adiops.apigateway.learning.track.path.resourceobject.LearningTrackPathRO;


/**
 * This class is responsible for transfer the resource LearningTrackQuestion Data
 * @author Deepak Pal
 *
 */
public class LearningTrackQuestionRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String keyid;
	private String title;
	private String question;
	private String answer;
	private String correctAnswer;
	private Integer score;
	private Integer maxScore;
	private String courseKey;
	private String moduleKey;
	private String topicKey;
	private String questionKey;
	private String userKey;
	private Integer status;
	
	
	
	 private LearningTrackPathRO learningTrackPath;
	
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
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setQuestion(String question){
		this.question=question;
	}
	
	public String getQuestion(){
		return this.question;
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
	
	public void setUserKey(String userKey){
		this.userKey=userKey;
	}
	
	public String getUserKey(){
		return this.userKey;
	}
	
	public void setStatus(Integer status){
		this.status=status;
	}
	
	public Integer getStatus(){
		return this.status;
	}
	
	 private LearningTrackPathRO learningTrackPathRO;
	
	public void setLearningTrackPathRO(LearningTrackPathRO learning_track_pathRO){
		this.learningTrackPathRO=learning_track_pathRO;
	}
	
	public LearningTrackPathRO getLearningTrackPathRO(){
		return this.learningTrackPathRO;
	}
	
	
}
