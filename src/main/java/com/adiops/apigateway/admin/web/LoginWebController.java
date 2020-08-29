package com.adiops.apigateway.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginWebController {

	
	@GetMapping(value = "/admin/login")
	public String getLogin(Model model) {
		return "login/login";
	}

	@GetMapping("/")
    public String index() {
        return "redirect:/web/courses";
    }

}
