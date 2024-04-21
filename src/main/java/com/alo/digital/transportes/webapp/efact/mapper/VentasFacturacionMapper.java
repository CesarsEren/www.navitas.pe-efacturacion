package com.alo.digital.transportes.webapp.efact.mapper;

import com.alo.digital.transportes.webapp.efact.beans.V_Ventas_FacturacionBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface VentasFacturacionMapper {
	
	public V_Ventas_FacturacionBean SQL_SELECT_VENTAS_IMAGES_FACTURACION(@Param("nro")int nro, @Param("tipoOperacion") String tipoOperacion);
	public int SQL_UPDATE_VENTAS_FACTURACION_IMAGES(@Param("nro")Integer nro,@Param("tipoOperacion")String tipoOperacion,@Param("codigohash")String codigohash,@Param("imageBarras")byte[] imageBarras,@Param("imageQR")byte[] imageQR);

}
