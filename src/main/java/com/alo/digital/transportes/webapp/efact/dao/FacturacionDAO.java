package com.alo.digital.transportes.webapp.efact.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
 
public interface FacturacionDAO {

	public List<Map<String, Object>> SQL_SELECT_VENTA_ENVIO_FACTURADOR(String empresa,String fechaemision,int documentos,int limit,int offset);
	public Map<String, Object> SQL_SELECT_VENTA_FACTURADOR (Integer nro, String tipoOperacion);
	public int SQL_UPDATE_VENTA_FACTURADOR (Integer nro, String tipoOperacion,Integer envioSunat,String respuestaSunat);
	//SQL_SELECT_VENTA_FACTURADOR();
	public List<Map<String, Object>> SQL_SELECT_VENTA_DESCARGA_FACTURADOR_SQL(String em, String formatoFecha,
			String serie, String numero, String tipodocumento, String ruc, String monto);
	public Map<String, Object> SQL_SELECT_VENTA_DESCARGA_X_NRO_FACTURADOR(int nro, String tipoOperacion);
}
