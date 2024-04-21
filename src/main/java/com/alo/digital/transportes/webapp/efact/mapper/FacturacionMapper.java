package com.alo.digital.transportes.webapp.efact.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface FacturacionMapper {

	public List<Map<String, Object>> SQL_SELECT_VENTA_ENVIO_FACTURADOR(@Param("empresa") String empresa,
			@Param("fechaemision") String fechaemision, @Param("documentos") int documentos, @Param("limit") int limit,
			@Param("offset") int offset);

	public Map<String, Object> SQL_SELECT_VENTA_FACTURADOR(@Param("nro") Integer nro,
			@Param("tipoOperacion") String tipoOperacion);

	public int SQL_UPDATE_VENTA_FACTURADOR(@Param("nro") Integer nro, @Param("tipoOperacion") String tipoOperacion,
			@Param("envioSunat") Integer envioSunat,@Param("respuesta_ose")String respuestaSunat);
	public List<Map<String, Object>> SQL_SELECT_VENTA_DESCARGA_FACTURADOR_SQL(@Param("empresa")String em,@Param("fechaEmision") String fechaemision,
			@Param("serie")String serie,@Param("numero") String numero,@Param("tipodocumento") String tipodocumento,@Param("ruc") String ruc,@Param("monto") String monto);
	
	public Map<String, Object> SQL_SELECT_VENTA_DESCARGA_X_NRO_FACTURADOR(@Param("nro") int nro,@Param("tipoOperacion") String tipoOperacion);	
	
}
