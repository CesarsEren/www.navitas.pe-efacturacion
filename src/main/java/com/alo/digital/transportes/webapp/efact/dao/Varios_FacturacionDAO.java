package com.alo.digital.transportes.webapp.efact.dao;

import com.alo.digital.transportes.webapp.efact.beans.V_Varios_FacturacionBean;

import java.util.List;


public interface Varios_FacturacionDAO {

	public V_Varios_FacturacionBean SQL_SELECT_LISTA_PARAMETROS_FACTURADOR(String empresa);
	public V_Varios_FacturacionBean SQL_SELECT_LISTA_PARAMETROS_DESCARGA_FACTURADOR(String empresa);
	
	//public List<V_Varios_FacturacionBean> SQL_SELECT_TODOS_PARAMETROS_FACTURADOR();
	//public V_Varios_FacturacionBean SQL_SELECT_LISTA_PARAMETROS_DESCARGA_FACTURADOR(String empresa);
}
