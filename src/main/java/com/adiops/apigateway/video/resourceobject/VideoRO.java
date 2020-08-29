package com.adiops.apigateway.video.resourceobject;

import java.util.Date;


/**
 * This class is responsible for transfer the resource Video Data
 * @author Deepak Pal
 *
 */
public class VideoRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String key;
	private String name;
	private String url;
	
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
	
	
}
