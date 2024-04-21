package com.alo.digital.transportes.webapp.efact.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		/*
		 Authentication auth
         = SecurityContextHolder.getContext().getAuthentication();
		 
		 if (auth != null) {
		     logger.info("User '" + auth.getName()
		             + "' attempted to access the protected URL: "
		             + httpServletRequest.getRequestURI());
		 }
		 */

		 response.sendRedirect(request.getContextPath() + "/error/403");
		
	}
	
	
	
	

}
