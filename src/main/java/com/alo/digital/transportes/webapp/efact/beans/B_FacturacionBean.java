package com.alo.digital.transportes.webapp.efact.beans;

public class B_FacturacionBean {
	
	private Integer id;
	private String fechaEmision;
	private String empresa;
	private String documentoElectronico;
	private String agencia;
	private String servicio;
	private String estado;
	private String observaciones;
	private String enviar;
	private boolean todos;
	private Integer documentos;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getDocumentoElectronico() {
		return documentoElectronico;
	}
	public void setDocumentoElectronico(String documentoElectronico) {
		this.documentoElectronico = documentoElectronico;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public void setEnviar(String enviar) {
		this.enviar = enviar;
	}
	public String getEnviar() {
		return enviar;
	}
	
	public void setTodos(boolean todos) {
		this.todos = todos;
	}
	public boolean isTodos() {
		return todos;
	}
	
	public void setDocumentos(Integer documentos) {
		this.documentos = documentos;
	}
	public Integer getDocumentos() {
		return documentos;
	}
	
	@Override
	public String toString() {
		return "B_VentaFacturacionBean [id=" + id + ", fechaEmision=" + fechaEmision + ", empresa=" + empresa
				+ ", documentoElectronico=" + documentoElectronico + ", agencia=" + agencia + ", servicio=" + servicio
				+ ", estado=" + estado + ", observaciones=" + observaciones + ", enviar=" + enviar + ", todos=" + todos
				+ "]";
	}


}
