package com.adiops.apigateway.account.web.bo.view;

import java.util.ArrayList;
import java.util.List;

public class LearningTrackChapterView extends AbstractView{
	
	private List<LearningTrackLevelView> levelViews = new ArrayList<>();

	public List<LearningTrackLevelView> getLevelViews() {
		return levelViews;
	}

	public void setLevelViews(List<LearningTrackLevelView> levelViews) {
		this.levelViews = levelViews;
	}

}
