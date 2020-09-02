package com.adiops.apigateway.app.role.resourceobject;

import java.util.Date;


/**
 * This class is responsible for transfer the resource AppRole Data
 * @author Deepak Pal
 *
 */
public class AppRoleRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String keyid;
	private String name;
	private String description;
	private String permission;
	
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
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setPermission(String permission){
		this.permission=permission;
	}
	
	public String getPermission(){
		return this.permission;
	}
	
	
}
