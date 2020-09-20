package com.adiops.apigateway.account.web.bo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adiops.apigateway.account.web.bo.view.CacheMgr;
import com.adiops.apigateway.app.user.service.UserService;
import com.adiops.apigateway.common.utils.ObjectUtils;

@Component
public class LearningPathBORepository {

	@Autowired
	private UserService mUserService;
	
	public LearningPathBO getLearningPathBO() {		
		Optional<String> key = ObjectUtils.resolve(() -> mUserService.getCurrentUserRO().getKeyid());
		if (key.isPresent()) {
			LearningPathBO tLearningPathBO=	CacheMgr.getLearningPathBO(key.get());
			if(tLearningPathBO==null)
			{
				tLearningPathBO= new LearningPathBO(key.get());
				CacheMgr.addLearningPathBO(tLearningPathBO);
			}
			return tLearningPathBO;
		}
		
		return new LearningPathBO(key.get());
	}
	
	
	
}
