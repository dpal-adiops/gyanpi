package com.adiops.apigateway.learning.path.entity;

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
import com.adiops.apigateway.course.line.group.entity.CourseLineGroupEntity;
/**
 * The Main data entity class for LearningPath Data
 * @author Deepak Pal
 *
 */
@Entity(name = "learning_path")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"keyid"})})
public class LearningPathEntity {

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
	@Column(name="status")
	@CsvBindByPosition(position = 1)
	private Integer status;
	@Column(name="score")
	@CsvBindByPosition(position = 2)
	private Integer score;
	@Column(name="max_score")
	@CsvBindByPosition(position = 3)
	private Integer maxScore;
	@Column(name="user_key")
	@CsvBindByPosition(position = 4)
	private String userKey;
	@Column(name="step")
	@CsvBindByPosition(position = 5)
	private Integer step;
	@Column(name="total_step")
	@CsvBindByPosition(position = 6)
	private Integer totalStep;
	
	
	
	 @OneToMany(mappedBy="learning_path")	 
	 private Set<CourseLineGroupEntity> course_line_groups = new HashSet<>();
	
	
	
	
	
	public void setKeyid(String keyid){
		this.keyid=keyid;
	}
	
	public String getKeyid(){
		return this.keyid;
	}
	
	
	
	public void setStatus(Integer status){
		this.status=status;
	}
	
	public Integer getStatus(){
		return this.status;
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
	
	
	
	public void setUserKey(String userKey){
		this.userKey=userKey;
	}
	
	public String getUserKey(){
		return this.userKey;
	}
	
	
	
	public void setStep(Integer step){
		this.step=step;
	}
	
	public Integer getStep(){
		return this.step;
	}
	
	
	
	public void setTotalStep(Integer totalStep){
		this.totalStep=totalStep;
	}
	
	public Integer getTotalStep(){
		return this.totalStep;
	}
	
	
	
	
	
	
	public void setCourseLineGroups(Set<CourseLineGroupEntity> course_line_groups){
		this.course_line_groups=course_line_groups;
	}
	
	public Set<CourseLineGroupEntity> getCourseLineGroups(){
		return this.course_line_groups;
	}
	
	
	
	
}
