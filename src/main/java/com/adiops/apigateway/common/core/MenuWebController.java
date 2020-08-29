package com.adiops.apigateway.common.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.adiops.apigateway.common.response.RestException;
import com.adiops.apigateway.module.resourceobject.ModuleRO;
import com.adiops.apigateway.module.service.ModuleService;

/**
 * The web controller class for Module 
 * @author Deepak Pal
 *
 */
@Controller
public class MenuWebController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@GetMapping(value = { "/web/coursemgmt" })
	public String showSideNav(Model model) {
		
	    return "layout/layout";
	}

	

}
