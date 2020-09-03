package com.adiops.apigateway.image.resourceobject;

import java.util.Date;


/**
 * This class is responsible for transfer the resource Image Data
 * @author Deepak Pal
 *
 */
public class ImageRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String keyid;
	private String name;
	private String url;
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
	
	
}
