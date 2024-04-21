package com.alo.digital.transportes.webapp.efact.dao;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Map;


public interface VentaFacturacionDAO {

    public Map<String, Object> SQL_SELECT_VENTA_FACTURADOR(Integer nro, String tipoOperacion);

    public int SQL_INSERT_VENTA_FACTURADOR(Integer nro, String tipoOperacion, String codigohash, byte[] codigobarras, ByteArrayInputStream image);
}
