package com.adiops.apigateway.account.web.bo.view;

public class LearningTrackQuestionView  extends AbstractView{

	private String answer;
	private Integer score;
	private String topicKey;
	private String trackKey;
	private String questionKey;
	
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	
	
	public String getTopicKey() {
		return topicKey;
	}
	public void setTopicKey(String topicKey) {
		this.topicKey = topicKey;
	}
	public String getTrackKey() {
		return trackKey;
	}
	public void setTrackKey(String trackKey) {
		this.trackKey = trackKey;
	}
	public String getQuestionKey() {
		return questionKey;
	}
	public void setQuestionKey(String questionKey) {
		this.questionKey = questionKey;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}


	
}
