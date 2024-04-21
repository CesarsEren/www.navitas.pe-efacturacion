package com.alo.digital.transportes.webapp.efact.dao;

import com.alo.digital.transportes.webapp.efact.mapper.VentasFacturacionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alo.digital.transportes.webapp.efact.beans.V_Ventas_FacturacionBean;

@Service
public class VentasFacturacionIDAO implements VentasFacturacionDAO {
	
	
	@Autowired
	VentasFacturacionMapper ventas;

	@Override
	public V_Ventas_FacturacionBean SQL_SELECT_VENTAS_IMAGES_FACTURACION(int nro, String tipoOperacion) {
		// TODO Auto-generated method stub
		return ventas.SQL_SELECT_VENTAS_IMAGES_FACTURACION(nro, tipoOperacion);
	}

	@Override
	public int SQL_UPDATE_VENTAS_FACTURACION_IMAGES(Integer nro, String tipoOperacion, String codigohash,byte[] imageBarras, byte[] imageQR) throws Exception {
		// TODO Auto-generated method stub
		return ventas.SQL_UPDATE_VENTAS_FACTURACION_IMAGES(nro, tipoOperacion, codigohash, imageBarras, imageQR);
	}
 

}
