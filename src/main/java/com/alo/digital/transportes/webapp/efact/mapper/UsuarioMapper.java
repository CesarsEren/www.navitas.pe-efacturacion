package com.alo.digital.transportes.webapp.efact.mapper;

import java.util.List; 
import java.util.Map;

import com.alo.digital.transportes.webapp.efact.beans.SpringSecurityUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UsuarioMapper {
	
	public SpringSecurityUser SQL_LOGIN_USUARIOS(@Param("username")String username);
	/*public SpringSecurityUser SQL_SELECT_VERIFICA_USUARIO(@Param("username")String username);
	public List<SpringSecurityUser> SQL_SELECT_VERIFICA_USUARIO_X_RUC(@Param("ruc")String ruc);
	public int SQL_INSERT_USUARIOS(SpringSecurityUser user);
	public int SQL_UPDATE_USUARIOS(SpringSecurityUser user);
	public List<Map<String, Object>> SQL_SELECT_USUARIOS_TODOS(@Param("offset")Integer offset, @Param("limit")Integer limit);*/

}
