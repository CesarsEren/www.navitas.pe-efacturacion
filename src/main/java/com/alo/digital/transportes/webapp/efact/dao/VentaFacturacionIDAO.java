package com.alo.digital.transportes.webapp.efact.dao;

import com.alo.digital.transportes.webapp.efact.mapper.VentaFacturacionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Map;

@Service
public class VentaFacturacionIDAO implements VentaFacturacionDAO {

    @Autowired
    private VentaFacturacionMapper ventafacturacionMapper;

    @Override
    public Map<String, Object> SQL_SELECT_VENTA_FACTURADOR(Integer nro, String tipoOperacion) {

        return ventafacturacionMapper.SQL_SELECT_VENTA_FACTURADOR(nro, tipoOperacion);
    }

    @Override
    public int SQL_INSERT_VENTA_FACTURADOR(Integer nro, String tipoOperacion, String codigohash, byte[] codigobarras, ByteArrayInputStream image) {
        return ventafacturacionMapper.SQL_INSERT_VENTA_FACTURADOR(nro, tipoOperacion, codigohash, codigobarras, image);
    }
}
