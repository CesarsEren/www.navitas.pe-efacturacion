package com.alo.digital.transportes.webapp.efact.mapper;

import java.util.List;

import com.alo.digital.transportes.webapp.efact.beans.V_Varios_FacturacionBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface Varios_FacturacionMapper {

	public V_Varios_FacturacionBean SQL_SELECT_LISTA_PARAMETROS_FACTURADOR(@Param("empresa") String empresa);
	public V_Varios_FacturacionBean SQL_SELECT_LISTA_PARAMETROS_DESCARGA_FACTURADOR(@Param("empresa") String empresa);
	//public List<V_Varios_FacturacionBean> SQL_SELECT_TODOS_PARAMETROS_FACTURADOR();
	//public V_Varios_FacturacionBean SQL_SELECT_LISTA_PARAMETROS_DESCARGA_FACTURADOR(@Param("empresa") String empresa);
}
