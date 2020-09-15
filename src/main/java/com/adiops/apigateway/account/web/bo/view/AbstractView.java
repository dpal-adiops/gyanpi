package com.adiops.apigateway.account.web.bo.view;

public class AbstractView {
	protected String name;
	protected String desc1;
	protected Integer status;
	protected String refid;
	protected String userid;
	protected Long id;
	
	protected LearningTrackProgressStat progressStat;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRefid() {
		return refid;
	}
	public void setRefid(String refid) {
		this.refid = refid;
	}
	public LearningTrackProgressStat getProgressStat() {
		return progressStat;
	}
	public void setProgressStat(LearningTrackProgressStat progressStat) {
		this.progressStat = progressStat;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
}
