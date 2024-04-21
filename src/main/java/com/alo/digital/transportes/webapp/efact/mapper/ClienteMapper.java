package com.alo.digital.transportes.webapp.efact.mapper;

import com.alo.digital.transportes.webapp.efact.beans.V_ClientesBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ClienteMapper {
	
	public V_ClientesBean listClientesRUC(@Param("RUC")String RUC);
}
