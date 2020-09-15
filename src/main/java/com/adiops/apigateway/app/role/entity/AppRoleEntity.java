package com.adiops.apigateway.app.role.entity;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.hibernate.annotations.GenericGenerator;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import com.adiops.apigateway.app.user.entity.AppUserEntity;
/**
 * The Main data entity class for AppRole Data
 * @author Deepak Pal
 *
 */
@Entity(name = "app_role")
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"keyid"})})
public class AppRoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	@CsvDate(value = "dd/MM/yyyy")
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;	
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
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
	@Column(name="name")
	@CsvBindByPosition(position = 1)
	private String name;
	@Column(name="description")
	@CsvBindByPosition(position = 2)
	private String description;
	@Column(name="permission")
	@CsvBindByPosition(position = 3)
	private String permission;
	
	 @ManyToMany 
@JoinTable( 
 name = "app_role_app_user",
 joinColumns = @JoinColumn(name = "app_role_id"),
 inverseJoinColumns = @JoinColumn(name = "app_user_id"))
	 private Set<AppUserEntity> app_users = new HashSet<>();
	
	
	
	
	
	
	
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
	
	
	
	public void setAppUsers(Set<AppUserEntity> app_users){
		this.app_users=app_users;
	}
	
	public Set<AppUserEntity> getAppUsers(){
		return this.app_users;
	}
	
	
	
	
	
	
	
}
