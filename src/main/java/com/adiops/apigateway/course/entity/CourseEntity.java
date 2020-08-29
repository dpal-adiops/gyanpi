package com.adiops.apigateway.course.entity;

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
import com.adiops.apigateway.image.entity.ImageEntity;
import com.adiops.apigateway.video.entity.VideoEntity;
import com.adiops.apigateway.page.entity.PageEntity;
/**
 * The Main data entity class for Course Data
 * @author Deepak Pal
 *
 */
@Entity(name = "course")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"key"})})
public class CourseEntity {

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
	@Column(name="author_id")
	@CsvBindByPosition(position = 3)
	private String authorId;
	
	 @ManyToMany 
@JoinTable( 
 name = "course_module",
 joinColumns = @JoinColumn(name = "course_id"),
 inverseJoinColumns = @JoinColumn(name = "module_id"))
	 private Set<ModuleEntity> modules = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "course_image",
 joinColumns = @JoinColumn(name = "course_id"),
 inverseJoinColumns = @JoinColumn(name = "image_id"))
	 private Set<ImageEntity> images = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "course_video",
 joinColumns = @JoinColumn(name = "course_id"),
 inverseJoinColumns = @JoinColumn(name = "video_id"))
	 private Set<VideoEntity> videos = new HashSet<>();
	 @ManyToMany 
@JoinTable( 
 name = "course_page",
 joinColumns = @JoinColumn(name = "course_id"),
 inverseJoinColumns = @JoinColumn(name = "page_id"))
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
	
	
	
	public void setAuthorId(String authorId){
		this.authorId=authorId;
	}
	
	public String getAuthorId(){
		return this.authorId;
	}
	
	
	
	public void setModules(Set<ModuleEntity> modules){
		this.modules=modules;
	}
	
	public Set<ModuleEntity> getModules(){
		return this.modules;
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
