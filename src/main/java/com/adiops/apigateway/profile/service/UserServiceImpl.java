package com.adiops.apigateway.profile.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.adiops.apigateway.profile.entity.User;
import com.adiops.apigateway.profile.repository.UserRepository;
import com.adiops.apigateway.profile.resourceobject.UserRO;

/**
 * The implementation class for user service. 
 * @author Deepak Pal
 *
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository mUserRepository;

	@Autowired
	private ModelMapper mModelMapper;
	
	
	/**
	 * this method return the user by find by username
	 */
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		User user = mUserRepository.findByUserName(userId);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				getAuthority());
	}

	/**
	 * add ROLE_ADMIN grant to user
	 * @return
	 */
	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	
	
	/**
	 * return all the user list
	 */
	@Override
	public List<UserRO> findAll() {
		List<UserRO> tUserROs = mUserRepository.findAll().stream()
				.map(userEntity -> mModelMapper.map(userEntity, UserRO.class)).collect(Collectors.toList());
		tUserROs.forEach(userRO->userRO.setPassword(null));
		return tUserROs;
	}

	@Override
	public User registerUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserRO getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}


	
}
