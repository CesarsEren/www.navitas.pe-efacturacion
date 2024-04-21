package com.alo.digital.transportes.webapp.efact.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.util.Map;

@Repository
@Mapper
public interface VentaFacturacionMapper {

    public Map<String, Object> SQL_SELECT_VENTA_FACTURADOR(@Param("nro") Integer nro, @Param("tipoOperacion") String tipoOperacion);

    public int SQL_INSERT_VENTA_FACTURADOR(@Param("nro") Integer nro, @Param("tipoOperacion") String tipoOperacion, @Param("codigohash") String codigohash, @Param("codigobarras") byte[] codigobarras, @Param("image") ByteArrayInputStream image);

}
