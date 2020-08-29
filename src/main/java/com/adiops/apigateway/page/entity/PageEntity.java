package com.adiops.apigateway.page.entity;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;


import org.hibernate.annotations.GenericGenerator;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.adiops.apigateway.course.entity.CourseEntity;
import com.adiops.apigateway.module.entity.ModuleEntity;
import com.adiops.apigateway.topic.entity.TopicEntity;
import com.adiops.apigateway.question.entity.QuestionEntity;
/**
 * The Main data entity class for Page Data
 * @author Deepak Pal
 *
 */
@Entity(name = "page")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"key"})})
public class PageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@CsvDate(value = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date lastModified;	
	
	@Column
	private Date createdDate;
	
	public Long getId(){
		return id;
	}
	
	public void setId(Long uuid){
		id=uuid;
		
	}

	@Column(name="key")
	@CsvBindByPosition(position = 0)
	private String key;
	@Column(name="name")
	@CsvBindByPosition(position = 1)
	private String name;
	@Column(name="desc")
	@CsvBindByPosition(position = 2)
	private String desc;
	
	 	@ManyToMany(mappedBy = "pages")
	 private Set<CourseEntity> courses = new HashSet<>();
	 	@ManyToMany(mappedBy = "pages")
	 private Set<ModuleEntity> modules = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "page_topic",
 joinColumns = @JoinColumn(name = "page_id"),
 inverseJoinColumns = @JoinColumn(name = "topic_id"))
	 private Set<TopicEntity> topics = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "page_question",
 joinColumns = @JoinColumn(name = "page_id"),
 inverseJoinColumns = @JoinColumn(name = "question_id"))
	 private Set<QuestionEntity> questions = new HashSet<>();
	
	
	public void setKey(String key){
		this.key=key;
	}
	
	public String getKey(){
		return this.key;
	}
	
	
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	
	
	
	public void setDesc(String desc){
		this.desc=desc;
	}
	
	public String getDesc(){
		return this.desc;
	}
	
	
	
	public void setCourses(Set<CourseEntity> courses){
		this.courses=courses;
	}
	
	public Set<CourseEntity> getCourses(){
		return this.courses;
	}
	public void setModules(Set<ModuleEntity> modules){
		this.modules=modules;
	}
	
	public Set<ModuleEntity> getModules(){
		return this.modules;
	}
	public void setTopics(Set<TopicEntity> topics){
		this.topics=topics;
	}
	
	public Set<TopicEntity> getTopics(){
		return this.topics;
	}
	public void setQuestions(Set<QuestionEntity> questions){
		this.questions=questions;
	}
	
	public Set<QuestionEntity> getQuestions(){
		return this.questions;
	}
	
	
}
