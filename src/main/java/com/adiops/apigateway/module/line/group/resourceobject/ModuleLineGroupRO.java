package com.adiops.apigateway.module.line.group.resourceobject;

import java.util.Date;


import com.adiops.apigateway.course.line.group.resourceobject.CourseLineGroupRO;

/**
 * This class is responsible for transfer the resource ModuleLineGroup Data
 * @author Deepak Pal
 *
 */
public class ModuleLineGroupRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String keyid;
	private Integer status;
	private Integer score;
	private Integer maxScore;
	private String title;
	private String description;
	private String userKey;
	private String courseKey;
	private String moduleKey;
	private Integer step;
	private Integer totalStep;
	
	
	
	 private CourseLineGroupRO course_line_group;
	
	
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
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setUserKey(String userKey){
		this.userKey=userKey;
	}
	
	public String getUserKey(){
		return this.userKey;
	}
	
	public void setCourseKey(String courseKey){
		this.courseKey=courseKey;
	}
	
	public String getCourseKey(){
		return this.courseKey;
	}
	
	public void setModuleKey(String moduleKey){
		this.moduleKey=moduleKey;
	}
	
	public String getModuleKey(){
		return this.moduleKey;
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
	
	
	
	 private CourseLineGroupRO courseLineGroupRO;
	
	public void setCourseLineGroupRO(CourseLineGroupRO course_line_groupRO){
		this.courseLineGroupRO=course_line_groupRO;
	}
	
	public CourseLineGroupRO getCourseLineGroupRO(){
		return this.courseLineGroupRO;
	}
}
