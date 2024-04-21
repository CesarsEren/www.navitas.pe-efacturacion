package com.alo.digital.transportes.webapp.efact.beans;


public class V_EncomTrackingBean {
	
	
	private Integer Id;
	private Integer NroEncomienda;
	private String Evento;
	private String FechaHora;
	private String Unidad;
	private String Ruta;
	private String Observacion;
	private String Usuario;
	private byte[] Imagen1;
	private byte[] Imagen2;
	private byte[] Imagen3;
	private String EstadoTarea;
	private String EntregaTarea;
	private String ObservacionTarea;
	private String FechaTarea;
	private byte[] Imagen4;
	private byte[] Imagen5;
	private byte[] Imagen6;
	private byte[] Imagen7;
	private byte[] Imagen8;
	private byte[] Imagen9;
	private byte[] Imagen10;
	
	////////////////////////
	// VARIABLES AUXILIARES PARA EL DETALLE 
	private String documento;
	private String serie;
	private String numero;
	private String destinoD;
	private String ruc;
	private String razon;
	private String remitente;
	private String remitenteD;
	private String consignado;
	private String consignadoD;
	private String domiciliado;
	private double base;
	private double flete;
	private String domicilio;
	private double total;
	private String fechaEmision;
	private String empresaD;
	private String documentoD;
	private String agenciaD;
	private String cantidad1;
	private String cantidad2;
	private String cantidad3;
	private String descripcion1;
	private String descripcion2;
	private String descripcion3;
	private double montoGiro;
	private String pago;
	private String fechaViaje;
	private String horaViaje;
	private String bus;
	private String fechaCancelacion;
	private String usuario1;
	private String agenciaD1;
	private String DNI;
	private String nombreApellido;
	private String manifiesto;
	private String manifiestoReenvio;
	private String reenvioBus;
	private String reenvioFecha;
	private String reenvioHora;
	private String manifiestoTrans;
	private String encoEstadoD;
	private String usuarioTracking;
	private String observacionTracking;
	
	private String estado;
	
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getEstado() {
		return estado;
	}
	
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public Integer getNroEncomienda() {
		return NroEncomienda;
	}
	public void setNroEncomienda(Integer nroEncomienda) {
		NroEncomienda = nroEncomienda;
	}
	public String getEvento() {
		return Evento;
	}
	public void setEvento(String evento) {
		Evento = evento;
	}
	public String getFechaHora() {
		return FechaHora;
	}
	public void setFechaHora(String fechaHora) {
		FechaHora = fechaHora;
	}
	public String getUnidad() {
		return Unidad;
	}
	public void setUnidad(String unidad) {
		Unidad = unidad;
	}
	public String getRuta() {
		return Ruta;
	}
	public void setRuta(String ruta) {
		Ruta = ruta;
	}
	public String getObservacion() {
		return Observacion;
	}
	public void setObservacion(String observacion) {
		Observacion = observacion;
	}
	public String getUsuario() {
		return Usuario;
	}
	public void setUsuario(String usuario) {
		Usuario = usuario;
	}
	public byte[] getImagen1() {
		return Imagen1;
	}
	public void setImagen1(byte[] imagen1) {
		Imagen1 = imagen1;
	}
	public byte[] getImagen2() {
		return Imagen2;
	}
	public void setImagen2(byte[] imagen2) {
		Imagen2 = imagen2;
	}
	public byte[] getImagen3() {
		return Imagen3;
	}
	public void setImagen3(byte[] imagen3) {
		Imagen3 = imagen3;
	}
	public String getEstadoTarea() {
		return EstadoTarea;
	}
	public void setEstadoTarea(String estadoTarea) {
		EstadoTarea = estadoTarea;
	}
	public String getEntregaTarea() {
		return EntregaTarea;
	}
	public void setEntregaTarea(String entregaTarea) {
		EntregaTarea = entregaTarea;
	}
	public String getObservacionTarea() {
		return ObservacionTarea;
	}
	public void setObservacionTarea(String observacionTarea) {
		ObservacionTarea = observacionTarea;
	}
	public String getFechaTarea() {
		return FechaTarea;
	}
	public void setFechaTarea(String fechaTarea) {
		FechaTarea = fechaTarea;
	}
	public byte[] getImagen4() {
		return Imagen4;
	}
	public void setImagen4(byte[] imagen4) {
		Imagen4 = imagen4;
	}
	public byte[] getImagen5() {
		return Imagen5;
	}
	public void setImagen5(byte[] imagen5) {
		Imagen5 = imagen5;
	}
	public byte[] getImagen6() {
		return Imagen6;
	}
	public void setImagen6(byte[] imagen6) {
		Imagen6 = imagen6;
	}
	public byte[] getImagen7() {
		return Imagen7;
	}
	public void setImagen7(byte[] imagen7) {
		Imagen7 = imagen7;
	}
	public byte[] getImagen8() {
		return Imagen8;
	}
	public void setImagen8(byte[] imagen8) {
		Imagen8 = imagen8;
	}
	public byte[] getImagen9() {
		return Imagen9;
	}
	public void setImagen9(byte[] imagen9) {
		Imagen9 = imagen9;
	}
	public byte[] getImagen10() {
		return Imagen10;
	}
	public void setImagen10(byte[] imagen10) {
		Imagen10 = imagen10;
	}
	
	
	
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getDestinoD() {
		return destinoD;
	}
	public void setDestinoD(String destinoD) {
		this.destinoD = destinoD;
	}
	public String getRuc() {
		return ruc;
	}
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}
	public String getRazon() {
		return razon;
	}
	public void setRazon(String razon) {
		this.razon = razon;
	}
	public String getRemitente() {
		return remitente;
	}
	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}
	public String getConsignado() {
		return consignado;
	}
	public void setConsignado(String consignado) {
		this.consignado = consignado;
	}
	public String getConsignadoD() {
		return consignadoD;
	}
	public void setConsignadoD(String consignadoD) {
		this.consignadoD = consignadoD;
	}
	public String getDomiciliado() {
		return domiciliado;
	}
	public void setDomiciliado(String domiciliado) {
		this.domiciliado = domiciliado;
	}
	public double getBase() {
		return base;
	}
	public void setBase(double base) {
		this.base = base;
	}
	public double getFlete() {
		return flete;
	}
	public void setFlete(double flete) {
		this.flete = flete;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	
	public String getDocumentoD() {
		return documentoD;
	}
	public void setDocumentoD(String documentoD) {
		this.documentoD = documentoD;
	}
	public void setAgenciaD(String agenciaD) {
		this.agenciaD = agenciaD;
	}
	public String getAgenciaD() {
		return agenciaD;
	}
	public String getCantidad1() {
		return cantidad1;
	}
	public void setCantidad1(String cantidad1) {
		this.cantidad1 = cantidad1;
	}
	public String getCantidad2() {
		return cantidad2;
	}
	public void setCantidad2(String cantidad2) {
		this.cantidad2 = cantidad2;
	}
	public String getCantidad3() {
		return cantidad3;
	}
	public void setCantidad3(String cantidad3) {
		this.cantidad3 = cantidad3;
	}
	public String getDescripcion1() {
		return descripcion1;
	}
	public void setDescripcion1(String descripcion1) {
		this.descripcion1 = descripcion1;
	}
	public String getDescripcion2() {
		return descripcion2;
	}
	public void setDescripcion2(String descripcion2) {
		this.descripcion2 = descripcion2;
	}
	public String getDescripcion3() {
		return descripcion3;
	}
	public void setDescripcion3(String descripcion3) {
		this.descripcion3 = descripcion3;
	}
	public double getMontoGiro() {
		return montoGiro;
	}
	public void setMontoGiro(double montoGiro) {
		this.montoGiro = montoGiro;
	}
	public String getPago() {
		return pago;
	}
	public void setPago(String pago) {
		this.pago = pago;
	}
	public String getFechaViaje() {
		return fechaViaje;
	}
	public void setFechaViaje(String fechaViaje) {
		this.fechaViaje = fechaViaje;
	}
	public String getBus() {
		return bus;
	}
	public void setBus(String bus) {
		this.bus = bus;
	}
	public String getFechaCancelacion() {
		return fechaCancelacion;
	}
	public void setFechaCancelacion(String fechaCancelacion) {
		this.fechaCancelacion = fechaCancelacion;
	}
	public String getUsuario1() {
		return usuario1;
	}
	public void setUsuario1(String usuario1) {
		this.usuario1 = usuario1;
	}
	public String getAgenciaD1() {
		return agenciaD1;
	}
	public void setAgenciaD1(String agenciaD1) {
		this.agenciaD1 = agenciaD1;
	}
	public void setDNI(String dNI) {
		DNI = dNI;
	}
	public String getDNI() {
		return DNI;
	}
	
	public String getNombreApellido() {
		return nombreApellido;
	}
	public void setNombreApellido(String nombreApellido) {
		this.nombreApellido = nombreApellido;
	}
	public String getManifiesto() {
		return manifiesto;
	}
	public void setManifiesto(String manifiesto) {
		this.manifiesto = manifiesto;
	}
	public String getManifiestoReenvio() {
		return manifiestoReenvio;
	}
	public void setManifiestoReenvio(String manifiestoReenvio) {
		this.manifiestoReenvio = manifiestoReenvio;
	}
	public String getReenvioBus() {
		return reenvioBus;
	}
	public void setReenvioBus(String reenvioBus) {
		this.reenvioBus = reenvioBus;
	}
	public String getReenvioFecha() {
		return reenvioFecha;
	}
	public void setReenvioFecha(String reenvioFecha) {
		this.reenvioFecha = reenvioFecha;
	}
	public String getReenvioHora() {
		return reenvioHora;
	}
	public void setReenvioHora(String reenvioHora) {
		this.reenvioHora = reenvioHora;
	}
	public String getManifiestoTrans() {
		return manifiestoTrans;
	}
	public void setManifiestoTrans(String manifiestoTrans) {
		this.manifiestoTrans = manifiestoTrans;
	}
	public String getEncoEstadoD() {
		return encoEstadoD;
	}
	public void setEncoEstadoD(String encoEstadoD) {
		this.encoEstadoD = encoEstadoD;
	}
	public String getUsuarioTracking() {
		return usuarioTracking;
	}
	public void setUsuarioTracking(String usuarioTracking) {
		this.usuarioTracking = usuarioTracking;
	}
	public String getObservacionTracking() {
		return observacionTracking;
	}
	public void setObservacionTracking(String observacionTracking) {
		this.observacionTracking = observacionTracking;
	}
	public void setEmpresaD(String empresaD) {
		this.empresaD = empresaD;
	}
	public String getEmpresaD() {
		return empresaD;
	}

	public void setHoraViaje(String horaViaje) {
		this.horaViaje = horaViaje;
	}
	public String getHoraViaje() {
		return horaViaje;
	}
	
	public void setRemitenteD(String remitenteD) {
		this.remitenteD = remitenteD;
	}
	public String getRemitenteD() {
		return remitenteD;
	}

}
