package com.adiops.apigateway.account.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.adiops.apigateway.app.user.resourceobject.AppUserRO;

@Service
public class AccountDetailService {

	@Autowired
	BCryptPasswordEncoder mBCryptPasswordEncoder;
	
	public AppUserRO encodePassword(AppUserRO tAppUserRO)
	{
		
		tAppUserRO.setEncryptedPassword(mBCryptPasswordEncoder.encode(tAppUserRO.getEncryptedPassword()));
		tAppUserRO.setUserName(tAppUserRO.getEmail());
		return	tAppUserRO;
	}
	
	    
}
