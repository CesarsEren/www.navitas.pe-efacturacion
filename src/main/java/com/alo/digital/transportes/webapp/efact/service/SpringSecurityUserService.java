package com.alo.digital.transportes.webapp.efact.service;
import com.alo.digital.transportes.webapp.efact.beans.SpringSecurityUser;
import com.alo.digital.transportes.webapp.efact.dao.UsuarioDAO;
import com.alo.digital.transportes.webapp.efact.service.SpringSecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class SpringSecurityUserService implements UserDetailsService  {
	
	
	@Autowired
	private UsuarioDAO usuarioDAO;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if(username != null){
			
			SpringSecurityUser usuarioSpringSecurity = usuarioDAO.SQL_LOGIN_USUARIOS(username.toUpperCase());
			
			if(usuarioSpringSecurity != null){
				usuarioSpringSecurity.setCredentialsNonExpired(true);
				usuarioSpringSecurity.setAccountNonExpired(true);
				usuarioSpringSecurity.setAccountNonLocked(true);
				
				usuarioSpringSecurity.setEnabled(usuarioSpringSecurity.getEstado().equals("Y") ? true : false);
				SimpleGrantedAuthority rol = new SimpleGrantedAuthority("ROLE_"+usuarioSpringSecurity.getNivel());
				usuarioSpringSecurity.addAuthority(rol);
				
				return usuarioSpringSecurity;
			}
			
		}
		
		return null;

	}

}
