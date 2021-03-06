package com.adiops.apigateway.common.inject;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class NamingMgr {
	
	private static AutowireCapableBeanFactory factory;


	@Autowired 
	private AutowireCapableBeanFactory beanFactory;

	@PostConstruct     
	private void setfactory () {
		NamingMgr.factory=beanFactory;
	}
		
	
    public static void injectMembers(Object obj) {
    	factory.autowireBean(obj);
    }
    
    
}
