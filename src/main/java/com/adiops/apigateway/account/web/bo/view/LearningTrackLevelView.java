package com.adiops.apigateway.account.web.bo.view;

import java.util.ArrayList;
import java.util.List;

public class LearningTrackLevelView extends AbstractView{

	private List<LearningTrackQuestionView> questionViews = new ArrayList<>();

	public List<LearningTrackQuestionView> getQuestionViews() {
		return questionViews;
	}

	public void setQuestionViews(List<LearningTrackQuestionView> questionViews) {
		this.questionViews = questionViews;
	}
	
}
