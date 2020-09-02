package com.adiops.apigateway.app.user.entity;

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
import com.adiops.apigateway.app.role.entity.AppRoleEntity;
/**
 * The Main data entity class for AppUser Data
 * @author Deepak Pal
 *
 */
@Entity(name = "app_user")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"keyid"})})
public class AppUserEntity {

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

	@Column(name="keyid")
	@CsvBindByPosition(position = 0)
	private String keyid;
	@Column(name="user_name")
	@CsvBindByPosition(position = 1)
	private String userName;
	@Column(name="email")
	@CsvBindByPosition(position = 2)
	private String email;
	@Column(name="first_name")
	@CsvBindByPosition(position = 3)
	private String firstName;
	@Column(name="last_name")
	@CsvBindByPosition(position = 4)
	private String lastName;
	@Column(name="encrypted_password")
	@CsvBindByPosition(position = 5)
	private String encryptedPassword;
	@Column(name="enabled")
	@CsvBindByPosition(position = 6)
	private Boolean enabled;
	
	 	@ManyToMany(mappedBy = "app_users")
	 private Set<AppRoleEntity> app_roles = new HashSet<>();
	
	
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
	
	
	
	public void setEnabled(Boolean enabled){
		this.enabled=enabled;
	}
	
	public Boolean getEnabled(){
		return this.enabled;
	}
	
	
	
	public void setAppRoles(Set<AppRoleEntity> app_roles){
		this.app_roles=app_roles;
	}
	
	public Set<AppRoleEntity> getAppRoles(){
		return this.app_roles;
	}
	
	
}
