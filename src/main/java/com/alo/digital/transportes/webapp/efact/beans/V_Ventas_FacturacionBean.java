package com.alo.digital.transportes.webapp.efact.beans;

import java.util.Arrays;

public class V_Ventas_FacturacionBean {

	private Integer nro;
	private String tipoOperacion;
	private String Codigohash;
	private byte[] image;
	private byte[] imageQR;
	private Integer id;
	private String codigoError;
	private String fechaEmision;
	private String imageQRActualizada;
	private String fechaQRActualizada;

	public Integer getNro() {
		return nro;
	}

	public void setNro(Integer nro) {
		this.nro = nro;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public String getCodigohash() {
		return Codigohash;
	}

	public void setCodigohash(String codigohash) {
		Codigohash = codigohash;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public byte[] getImageQR() {
		return imageQR;
	}

	public void setImageQR(byte[] imageQR) {
		this.imageQR = imageQR;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	public String getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getImageQRActualizada() {
		return imageQRActualizada;
	}

	public void setImageQRActualizada(String imageQRActualizada) {
		this.imageQRActualizada = imageQRActualizada;
	}

	public String getFechaQRActualizada() {
		return fechaQRActualizada;
	}

	public void setFechaQRActualizada(String fechaQRActualizada) {
		this.fechaQRActualizada = fechaQRActualizada;
	}

	@Override
	public String toString() {
		return "V_Ventas_FacturacionBean [nro=" + nro + ", tipoOperacion=" + tipoOperacion + ", Codigohash="
				+ Codigohash + ", image=" + Arrays.toString(image) + ", imageQR=" + Arrays.toString(imageQR) + ", id="
				+ id + ", codigoError=" + codigoError + ", fechaEmision=" + fechaEmision + ", imageQRActualizada="
				+ imageQRActualizada + ", fechaQRActualizada=" + fechaQRActualizada + "]";
	}
}
