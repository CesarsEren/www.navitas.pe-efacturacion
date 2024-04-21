package com.alo.digital.transportes.webapp.efact.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alo.digital.transportes.webapp.efact.mapper.FacturacionMapper;

@Service
public class FacturacionIDAO implements FacturacionDAO {

    @Autowired
    private FacturacionMapper facturacionMapper;

    @Override
    public List<Map<String, Object>> SQL_SELECT_VENTA_ENVIO_FACTURADOR(String empresa, String fechaemision,
                                                                       int documentos, int limit, int offset) {
        return facturacionMapper.SQL_SELECT_VENTA_ENVIO_FACTURADOR(empresa, fechaemision, documentos, limit, offset);
    }

    @Override
    public Map<String, Object> SQL_SELECT_VENTA_FACTURADOR(Integer nro, String tipoOperacion) {
        // TODO Auto-generated method stub
        return facturacionMapper.SQL_SELECT_VENTA_FACTURADOR(nro, tipoOperacion);
    }

    @Override
    public int SQL_UPDATE_VENTA_FACTURADOR(Integer nro, String tipoOperacion, Integer envioSunat, String respuestaSunat) {
        // TODO Auto-generated method stub
        return facturacionMapper.SQL_UPDATE_VENTA_FACTURADOR(nro, tipoOperacion, envioSunat, respuestaSunat);
    }

    @Override
    public List<Map<String, Object>> SQL_SELECT_VENTA_DESCARGA_FACTURADOR_SQL(String em, String fechaemision,
                                                                              String serie, String numero, String tipodocumento, String ruc, String monto) {
        // TODO Auto-generated method stub
        return facturacionMapper.SQL_SELECT_VENTA_DESCARGA_FACTURADOR_SQL(em, fechaemision, serie, numero, tipodocumento, ruc, monto);
    }

    @Override
    public Map<String, Object> SQL_SELECT_VENTA_DESCARGA_X_NRO_FACTURADOR(int nro, String tipoOperacion) {
        // TODO Auto-generated method stub
        return facturacionMapper.SQL_SELECT_VENTA_DESCARGA_X_NRO_FACTURADOR(nro, tipoOperacion);
    }
}
