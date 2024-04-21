package com.alo.digital.transportes.webapp.efact.dao;

import com.alo.digital.transportes.webapp.efact.beans.V_Ventas_FacturacionBean;

public interface VentasFacturacionDAO {

	public V_Ventas_FacturacionBean SQL_SELECT_VENTAS_IMAGES_FACTURACION(int nro, String tipoOperacion);
	public int SQL_UPDATE_VENTAS_FACTURACION_IMAGES(Integer nro,String tipoOperacion,String codigohash,byte[] imageBarras,byte[] imageQR)throws Exception;

}
