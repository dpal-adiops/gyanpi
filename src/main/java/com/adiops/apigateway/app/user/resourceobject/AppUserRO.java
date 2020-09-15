package com.adiops.apigateway.app.user.resourceobject;

import java.util.Date;



/**
 * This class is responsible for transfer the resource AppUser Data
 * @author Deepak Pal
 *
 */
public class AppUserRO {

	private Long id;
	
	
	private Date lastModified;	
	
	
	private Date createdDate;
	
	private String keyid;
	private String userName;
	private String email;
	private String firstName;
	private String lastName;
	private String encryptedPassword;
	private String mobile;
	private Boolean enabled;
	
	
	
	
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
	
	public void setUserName(String userName){
		this.userName=userName;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public void setEmail(String email){
		this.email=email;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public void setFirstName(String firstName){
		this.firstName=firstName;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public void setLastName(String lastName){
		this.lastName=lastName;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public void setEncryptedPassword(String encryptedPassword){
		this.encryptedPassword=encryptedPassword;
	}
	
	public String getEncryptedPassword(){
		return this.encryptedPassword;
	}
	
	public void setMobile(String mobile){
		this.mobile=mobile;
	}
	
	public String getMobile(){
		return this.mobile;
	}
	
	public void setEnabled(Boolean enabled){
		this.enabled=enabled;
	}
	
	public Boolean getEnabled(){
		return this.enabled;
	}
	
	
	
	
}
