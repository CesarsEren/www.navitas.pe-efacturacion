package com.alo.digital.transportes.webapp.efact.controllers;

import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginControllers {
	
	
	@GetMapping("/login")
    public String root(HttpServletRequest request, HttpServletResponse response) {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    	
        return "login";
    }
	
	
    @GetMapping("/inicio")
    @PreAuthorize("hasRole('ROLE_1') or hasRole('ROLE_2') or hasRole('ROLE_T')")
    public String inicio() {
    	//return "/fragments/facturacion/documentos";
    	return "redirect:/documentosenvio/";
    }
	
	@GetMapping("loginerror")
    public String loginerror(Model model) {
       model.addAttribute("loginError", true);
       return "login";
        
    }
	
}
