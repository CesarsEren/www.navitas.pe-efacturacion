package com.alo.digital.transportes.webapp.efact.dao;

import java.util.List;
import java.util.Map;

import com.alo.digital.transportes.webapp.efact.beans.SpringSecurityUser;

public interface UsuarioDAO {
	
	public SpringSecurityUser SQL_LOGIN_USUARIOS(String username);
	/*
	public SpringSecurityUser SQL_SELECT_VERIFICA_USUARIO(String username);
	public List<SpringSecurityUser> SQL_SELECT_VERIFICA_USUARIO_X_RUC(String ruc);
	public int SQL_INSERT_USUARIOS(SpringSecurityUser user);
	public int SQL_UPDATE_USUARIOS(SpringSecurityUser user);
	public List<Map<String, Object>> SQL_SELECT_USUARIOS_TODOS(Integer offset, Integer limit);*/

}
