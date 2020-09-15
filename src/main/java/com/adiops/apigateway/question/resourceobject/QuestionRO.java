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
	
	private String keyid;
	private String title;
	private String description;
	private String answer;
	private String authorId;
	private String domainId;
	
	
	
	
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
	
	public void setAnswer(String answer){
		this.answer=answer;
	}
	
	public String getAnswer(){
		return this.answer;
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
	
	
	
	
}
