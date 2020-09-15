package com.adiops.apigateway.module.entity;

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
import com.adiops.apigateway.course.entity.CourseEntity;
import com.adiops.apigateway.topic.entity.TopicEntity;
import com.adiops.apigateway.image.entity.ImageEntity;
import com.adiops.apigateway.video.entity.VideoEntity;
import com.adiops.apigateway.page.entity.PageEntity;
/**
 * The Main data entity class for Module Data
 * @author Deepak Pal
 *
 */
@Entity(name = "module")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"keyid"})})
public class ModuleEntity {

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
	@Column(name="description")
	@CsvBindByPosition(position = 2)
	private String description;
	@Column(name="author_id")
	@CsvBindByPosition(position = 3)
	private String authorId;
	@Column(name="domain_id")
	@CsvBindByPosition(position = 4)
	private String domainId;
	
	 	@ManyToMany(mappedBy = "modules")
	 private Set<CourseEntity> courses = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "module_topic",
 joinColumns = @JoinColumn(name = "module_id"),
 inverseJoinColumns = @JoinColumn(name = "topic_id"))
	 private Set<TopicEntity> topics = new HashSet<>();
	 	@ManyToMany(mappedBy = "modules")
	 private Set<ImageEntity> images = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "module_video",
 joinColumns = @JoinColumn(name = "module_id"),
 inverseJoinColumns = @JoinColumn(name = "video_id"))
	 private Set<VideoEntity> videos = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "module_page",
 joinColumns = @JoinColumn(name = "module_id"),
 inverseJoinColumns = @JoinColumn(name = "page_id"))
	 private Set<PageEntity> pages = new HashSet<>();
	
	
	
	
	
	
	
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
	
	
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getDescription(){
		return this.description;
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
	public void setTopics(Set<TopicEntity> topics){
		this.topics=topics;
	}
	
	public Set<TopicEntity> getTopics(){
		return this.topics;
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
