package com.adiops.apigateway.app.user.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.adiops.apigateway.app.role.repository.AppRoleRepository;
import com.adiops.apigateway.app.user.entity.AppUserEntity;
import com.adiops.apigateway.app.user.repository.AppUserRepository;
import com.adiops.apigateway.app.user.resourceobject.AppUserRO;

@Service(value = "userService")
public class UserService implements UserDetailsService {

	@Autowired
	AppUserRepository mAppUserRepository;

	@Autowired
	AppRoleRepository mAppRoleRepository;
	
	@Autowired
	private ModelMapper mModelMapper;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUserEntity tUserEntity = mAppUserRepository.findByUserName(username);
		if (tUserEntity == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(tUserEntity.getUserName(),
				tUserEntity.getEncryptedPassword(), getAuthority(tUserEntity));
	}

	@Transactional
	private Collection<? extends GrantedAuthority> getAuthority(AppUserEntity tUserEntity) {
		if(tUserEntity.getAppRoles().isEmpty())
			return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
		List<SimpleGrantedAuthority> roles = tUserEntity.getAppRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getPermission())).collect(Collectors.toList());
		return roles;
	}

	public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    
    
    public AppUserRO getCurrentUserRO()
	{
		Authentication authentication = getAuthentication();
		AppUserEntity tUserEntity = mAppUserRepository.findByUserName(authentication.getName());
		if (tUserEntity == null) {
			new AppUserRO();
		}
		  
	    return mModelMapper.map(tUserEntity, AppUserRO.class);
	        
	}
}
