package com.adiops.apigateway.account.web.bo.view;

import java.util.ArrayList;
import java.util.List;

public class LearningTrackCourseView extends AbstractView{

	private List<LearningTrackChapterView> chapterViews = new ArrayList<>();

	public List<LearningTrackChapterView> getChapterViews() {
		return chapterViews;
	}

	public void setChapterViews(List<LearningTrackChapterView> chapterViews) {
		this.chapterViews = chapterViews;
	}
}
