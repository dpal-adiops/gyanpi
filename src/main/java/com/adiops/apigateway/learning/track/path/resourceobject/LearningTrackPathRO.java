package com.adiops.apigateway.learning.track.path.resourceobject;

import java.util.Date;

import com.adiops.apigateway.learning.track.question.resourceobject.LearningTrackQuestionRO;
import com.adiops.apigateway.learning.track.path.resourceobject.LearningTrackPathRO;

import com.adiops.apigateway.learning.track.resourceobject.LearningTrackRO;

/**
 * This class is responsible for transfer the resource LearningTrackPath Data
 * @author Deepak Pal
 *
 */
public class LearningTrackPathRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String keyid;
	private Integer step;
	private String status;
	private String refid;
	
	
	
	 private LearningTrackRO learning_track;
	
	 private LearningTrackQuestionRO learningTrackQuestion;
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
	
	public void setStep(Integer step){
		this.step=step;
	}
	
	public Integer getStep(){
		return this.step;
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
	
	 private LearningTrackQuestionRO learningTrackQuestionRO;
	 private LearningTrackPathRO learningTrackPathRO;
	
	public void setLearningTrackQuestionRO(LearningTrackQuestionRO learning_track_questionRO){
		this.learningTrackQuestionRO=learning_track_questionRO;
	}
	
	public LearningTrackQuestionRO getLearningTrackQuestionRO(){
		return this.learningTrackQuestionRO;
	}
	public void setLearningTrackPathRO(LearningTrackPathRO learning_track_pathRO){
		this.learningTrackPathRO=learning_track_pathRO;
	}
	
	public LearningTrackPathRO getLearningTrackPathRO(){
		return this.learningTrackPathRO;
	}
	
	 private LearningTrackRO learningTrackRO;
	
	public void setLearningTrackRO(LearningTrackRO learning_trackRO){
		this.learningTrackRO=learning_trackRO;
	}
	
	public LearningTrackRO getLearningTrackRO(){
		return this.learningTrackRO;
	}
}
