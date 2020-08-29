package com.adiops.apigateway.module.resourceobject;

import java.util.Date;


/**
 * This class is responsible for transfer the resource Module Data
 * @author Deepak Pal
 *
 */
public class ModuleRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String key;
	private String name;
	private String desc;
	
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
	
	public void setDesc(String desc){
		this.desc=desc;
	}
	
	public String getDesc(){
		return this.desc;
	}
	
	
}
