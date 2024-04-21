package com.alo.digital.transportes.webapp.efact.controllers;

import javax.servlet.http.HttpServletRequest;  
import org.springframework.boot.autoconfigure.web.ErrorController;
//import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NotFoundException implements ErrorController {
	
	@RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
       return "/error/404";
    }

	@Override
	public String getErrorPath() {
		 return "/error";
	}

}
