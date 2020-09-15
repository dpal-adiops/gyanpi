package com.adiops.apigateway.account.web.bo.view;

import org.apache.commons.lang3.math.NumberUtils;

public class LearningTrackProgressStat {

	protected Integer totalCredit;
	protected Integer completedCredit;
	
	public Integer getTotalCredit() {
		return totalCredit;
	}
	public void setTotalCredit(Integer totalCredit) {
		this.totalCredit = totalCredit;
	}
	public Integer getCompletedCredit() {
		return completedCredit;
	}
	public void setCompletedCredit(Integer completedCredit) {
		this.completedCredit = completedCredit;
	}
	

	public void add(LearningTrackProgressStat progressStat) {
		totalCredit=totalCredit+progressStat.getTotalCredit();
		completedCredit=completedCredit+progressStat.getCompletedCredit();
	}
	
}
