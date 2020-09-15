package com.adiops.apigateway.account.web.bo;

import org.springframework.stereotype.Component;

@Component
public class LearningPathBORepository {

	public LearningPathBO getLearningPathBO() {		
		
		return new LearningPathBO();
	}
	
	
}
