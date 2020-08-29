package com.adiops.apigateway.question.resourceobject;

import java.util.Date;


/**
 * This class is responsible for transfer the resource Question Data
 * @author Deepak Pal
 *
 */
public class QuestionRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String key;
	private String title;
	private String desc;
	private String answer;
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long uuid){
		this.id=uuid;
	}
	
	
	public void setKey(String key){
		this.key=key;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setDesc(String desc){
		this.desc=desc;
	}
	
	public String getDesc(){
		return this.desc;
	}
	
	public void setAnswer(String answer){
		this.answer=answer;
	}
	
	public String getAnswer(){
		return this.answer;
	}
	
	
}
