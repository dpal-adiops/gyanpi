package com.adiops.apigateway.topic.entity;

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
import com.adiops.apigateway.module.entity.ModuleEntity;
import com.adiops.apigateway.question.entity.QuestionEntity;
import com.adiops.apigateway.image.entity.ImageEntity;
import com.adiops.apigateway.video.entity.VideoEntity;
import com.adiops.apigateway.page.entity.PageEntity;
/**
 * The Main data entity class for Topic Data
 * @author Deepak Pal
 *
 */
@Entity(name = "topic")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"key"})})
public class TopicEntity {

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
	@Column(name="title")
	@CsvBindByPosition(position = 3)
	private String title;
	
	 	@ManyToMany(mappedBy = "topics")
	 private Set<ModuleEntity> modules = new HashSet<>();
	 	@ManyToMany(mappedBy = "topics")
	 private Set<QuestionEntity> questions = new HashSet<>();
	 	@ManyToMany(mappedBy = "topics")
	 private Set<ImageEntity> images = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "topic_video",
 joinColumns = @JoinColumn(name = "topic_id"),
 inverseJoinColumns = @JoinColumn(name = "video_id"))
	 private Set<VideoEntity> videos = new HashSet<>();
	 	@ManyToMany(mappedBy = "topics")
	 private Set<PageEntity> pages = new HashSet<>();
	
	
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
	
	
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	
	
	public void setModules(Set<ModuleEntity> modules){
		this.modules=modules;
	}
	
	public Set<ModuleEntity> getModules(){
		return this.modules;
	}
	public void setQuestions(Set<QuestionEntity> questions){
		this.questions=questions;
	}
	
	public Set<QuestionEntity> getQuestions(){
		return this.questions;
	}
	public void setImages(Set<ImageEntity> images){
		this.images=images;
	}
	
	public Set<ImageEntity> getImages(){
		return this.images;
	}
	public void setVideos(Set<VideoEntity> videos){
		this.videos=videos;
	}
	
	public Set<VideoEntity> getVideos(){
		return this.videos;
	}
	public void setPages(Set<PageEntity> pages){
		this.pages=pages;
	}
	
	public Set<PageEntity> getPages(){
		return this.pages;
	}
	
	
}
