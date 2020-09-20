package com.adiops.apigateway.learning.track.resourceobject;

import java.util.Date;

import com.adiops.apigateway.learning.track.resourceobject.LearningTrackRO;

import com.adiops.apigateway.app.user.resourceobject.AppUserRO;

/**
 * This class is responsible for transfer the resource LearningTrack Data
 * @author Deepak Pal
 *
 */
public class LearningTrackRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String keyid;
	private String name;
	private Integer credit;
	private Integer score;
	private Integer maxScore;
	private Integer totalStep;
	private String status;
	private String refid;
	
	
	
	 private AppUserRO app_user;
	
	 private LearningTrackRO learningTrack;
	
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
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setCredit(Integer credit){
		this.credit=credit;
	}
	
	public Integer getCredit(){
		return this.credit;
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
	
	public void setTotalStep(Integer totalStep){
		this.totalStep=totalStep;
	}
	
	public Integer getTotalStep(){
		return this.totalStep;
	}
	
	public void setStatus(String status){
		this.status=status;
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public void setRefid(String refid){
		this.refid=refid;
	}
	
	public String getRefid(){
		return this.refid;
	}
	
	 private LearningTrackRO learningTrackRO;
	
	public void setLearningTrackRO(LearningTrackRO learning_trackRO){
		this.learningTrackRO=learning_trackRO;
	}
	
	public LearningTrackRO getLearningTrackRO(){
		return this.learningTrackRO;
	}
	
	 private AppUserRO appUserRO;
	
	public void setAppUserRO(AppUserRO app_userRO){
		this.appUserRO=app_userRO;
	}
	
	public AppUserRO getAppUserRO(){
		return this.appUserRO;
	}
}
