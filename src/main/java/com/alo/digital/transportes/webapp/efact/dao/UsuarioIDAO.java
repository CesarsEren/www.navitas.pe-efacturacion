package com.alo.digital.transportes.webapp.efact.dao;

import java.util.List;
import java.util.Map;

import com.alo.digital.transportes.webapp.efact.beans.SpringSecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alo.digital.transportes.webapp.efact.service.SpringSecurityUserService;
import com.alo.digital.transportes.webapp.efact.mapper.UsuarioMapper;

@Service
public class UsuarioIDAO implements UsuarioDAO {
	
	
	@Autowired
	private UsuarioMapper usuarioMapper;

	@Override
	public SpringSecurityUser SQL_LOGIN_USUARIOS(String username) {
		return usuarioMapper.SQL_LOGIN_USUARIOS(username);
	}
/*
	@Override
	public int SQL_INSERT_USUARIOS(SpringSecurityUser user) {
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		
		return usuarioMapper.SQL_INSERT_USUARIOS(user);
	}

	@Override
	public List<Map<String, Object>> SQL_SELECT_USUARIOS_TODOS(Integer offset, Integer limit) {
		return usuarioMapper.SQL_SELECT_USUARIOS_TODOS(offset,limit);
	}

	@Override
	public int SQL_UPDATE_USUARIOS(SpringSecurityUser user) {
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		
		return usuarioMapper.SQL_UPDATE_USUARIOS(user);
	}

	@Override
	public SpringSecurityUser SQL_SELECT_VERIFICA_USUARIO(String username) {
		return usuarioMapper.SQL_SELECT_VERIFICA_USUARIO(username);
	}

	@Override
	public List<SpringSecurityUser> SQL_SELECT_VERIFICA_USUARIO_X_RUC(String ruc) {
		return usuarioMapper.SQL_SELECT_VERIFICA_USUARIO_X_RUC(ruc);
	}
*/
}
