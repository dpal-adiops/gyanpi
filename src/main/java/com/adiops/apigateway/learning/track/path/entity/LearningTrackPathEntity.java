package com.adiops.apigateway.learning.track.path.entity;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.hibernate.annotations.GenericGenerator;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.adiops.apigateway.learning.track.entity.LearningTrackEntity;
import com.adiops.apigateway.learning.track.question.entity.LearningTrackQuestionEntity;
import com.adiops.apigateway.learning.track.path.entity.LearningTrackPathEntity;
/**
 * The Main data entity class for LearningTrackPath Data
 * @author Deepak Pal
 *
 */
@Entity(name = "learning_track_path")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"keyid"})})
public class LearningTrackPathEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@CsvDate(value = "dd/MM/yyyy")
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;	
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date createdDate;
	
	public Long getId(){
		return id;
	}
	
	public void setId(Long uuid){
		id=uuid;
		
	}

	@Column(name="keyid")
	@CsvBindByPosition(position = 0)
	private String keyid;
	@Column(name="step")
	@CsvBindByPosition(position = 1)
	private Integer step;
	@Column(name="status")
	@CsvBindByPosition(position = 2)
	private String status;
	@Column(name="refid")
	@CsvBindByPosition(position = 3)
	private String refid;
	
	
	
	
	 @ManyToOne
	 @JoinColumn(name="learning_track_id", nullable=true)	 
	 private LearningTrackEntity learning_track;
	
	 @OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name="learning_track_question_id", referencedColumnName = "id")	 
	 private LearningTrackQuestionEntity learning_track_question;
	 @OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name="learning_track_path_id", referencedColumnName = "id")	 
	 private LearningTrackPathEntity learning_track_path;
	
	
	
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
	
	
	
	
	
	
	
	public void setLearningTrack(LearningTrackEntity learning_track){
		this.learning_track=learning_track;
	}
	
	public LearningTrackEntity getLearningTrack(){
		return this.learning_track;
	}
	
	public void setLearningTrackQuestion(LearningTrackQuestionEntity learning_track_question){
		this.learning_track_question=learning_track_question;
	}
	
	public LearningTrackQuestionEntity getLearningTrackQuestion(){
		return this.learning_track_question;
	}
	public void setLearningTrackPath(LearningTrackPathEntity learning_track_path){
		this.learning_track_path=learning_track_path;
	}
	
	public LearningTrackPathEntity getLearningTrackPath(){
		return this.learning_track_path;
	}
	
	
}
