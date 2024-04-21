package com.alo.digital.transportes.webapp.efact.dao;

import com.alo.digital.transportes.webapp.efact.beans.V_Varios_FacturacionBean;
import com.alo.digital.transportes.webapp.efact.mapper.Varios_FacturacionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Varios_FacturacionIDAO implements Varios_FacturacionDAO{

	@Autowired
	Varios_FacturacionMapper variosmapper;
	
	@Override
	public V_Varios_FacturacionBean SQL_SELECT_LISTA_PARAMETROS_FACTURADOR(String empresa) {
		// TODO Auto-generated method stub
		return variosmapper.SQL_SELECT_LISTA_PARAMETROS_FACTURADOR(empresa);
	}

	@Override
	public V_Varios_FacturacionBean SQL_SELECT_LISTA_PARAMETROS_DESCARGA_FACTURADOR(String empresa) {
		// TODO Auto-generated method stub
		return variosmapper.SQL_SELECT_LISTA_PARAMETROS_DESCARGA_FACTURADOR(empresa);
	}

}
