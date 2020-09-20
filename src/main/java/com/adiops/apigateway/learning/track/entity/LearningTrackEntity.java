package com.adiops.apigateway.learning.track.entity;

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
import com.adiops.apigateway.learning.track.path.entity.LearningTrackPathEntity;
import com.adiops.apigateway.app.user.entity.AppUserEntity;
import com.adiops.apigateway.learning.track.entity.LearningTrackEntity;
/**
 * The Main data entity class for LearningTrack Data
 * @author Deepak Pal
 *
 */
@Entity(name = "learning_track")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"keyid"})})
public class LearningTrackEntity {

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
	@Column(name="name")
	@CsvBindByPosition(position = 1)
	private String name;
	@Column(name="credit")
	@CsvBindByPosition(position = 2)
	private Integer credit;
	@Column(name="score")
	@CsvBindByPosition(position = 3)
	private Integer score;
	@Column(name="max_score")
	@CsvBindByPosition(position = 4)
	private Integer maxScore;
	@Column(name="total_step")
	@CsvBindByPosition(position = 5)
	private Integer totalStep;
	@Column(name="status")
	@CsvBindByPosition(position = 6)
	private String status;
	@Column(name="refid")
	@CsvBindByPosition(position = 7)
	private String refid;
	
	
	
	 @OneToMany(mappedBy="learning_track")	 
	 private Set<LearningTrackPathEntity> learning_track_paths = new HashSet<>();
	
	 @ManyToOne
	 @JoinColumn(name="app_user_id", nullable=true)	 
	 private AppUserEntity app_user;
	
	 @OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name="learning_track_id", referencedColumnName = "id")	 
	 private LearningTrackEntity learning_track;
	
	
	
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
	
	
	
	
	
	
	public void setLearningTrackPaths(Set<LearningTrackPathEntity> learning_track_paths){
		this.learning_track_paths=learning_track_paths;
	}
	
	public Set<LearningTrackPathEntity> getLearningTrackPaths(){
		return this.learning_track_paths;
	}
	
	public void setAppUser(AppUserEntity app_user){
		this.app_user=app_user;
	}
	
	public AppUserEntity getAppUser(){
		return this.app_user;
	}
	
	public void setLearningTrack(LearningTrackEntity learning_track){
		this.learning_track=learning_track;
	}
	
	public LearningTrackEntity getLearningTrack(){
		return this.learning_track;
	}
	
	
}
