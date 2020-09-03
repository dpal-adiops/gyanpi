package com.adiops.apigateway.image.entity;

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
 * The Main data entity class for Image Data
 * @author Deepak Pal
 *
 */
@Entity(name = "image")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"keyid"})})
public class ImageEntity {

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

	@Column(name="keyid")
	@CsvBindByPosition(position = 0)
	private String keyid;
	@Column(name="name")
	@CsvBindByPosition(position = 1)
	private String name;
	@Column(name="url")
	@CsvBindByPosition(position = 2)
	private String url;
	@Column(name="author_id")
	@CsvBindByPosition(position = 3)
	private String authorId;
	@Column(name="domain_id")
	@CsvBindByPosition(position = 4)
	private String domainId;
	
	 	@ManyToMany(mappedBy = "images")
	 private Set<CourseEntity> courses = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "image_module",
 joinColumns = @JoinColumn(name = "image_id"),
 inverseJoinColumns = @JoinColumn(name = "module_id"))
	 private Set<ModuleEntity> modules = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "image_topic",
 joinColumns = @JoinColumn(name = "image_id"),
 inverseJoinColumns = @JoinColumn(name = "topic_id"))
	 private Set<TopicEntity> topics = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "image_question",
 joinColumns = @JoinColumn(name = "image_id"),
 inverseJoinColumns = @JoinColumn(name = "question_id"))
	 private Set<QuestionEntity> questions = new HashSet<>();
	
	
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
	
	
	
	public void setUrl(String url){
		this.url=url;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	
	
	public void setAuthorId(String authorId){
		this.authorId=authorId;
	}
	
	public String getAuthorId(){
		return this.authorId;
	}
	
	
	
	public void setDomainId(String domainId){
		this.domainId=domainId;
	}
	
	public String getDomainId(){
		return this.domainId;
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
