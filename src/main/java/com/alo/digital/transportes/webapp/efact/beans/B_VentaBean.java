package com.alo.digital.transportes.webapp.efact.beans;

public class B_VentaBean {
	
	private int Nro;
    private int Salida;
    private int Destino;
    private String DestinoD;
    private String HoraViaje;
    private String HoraViajeIni;
    private String Empresa;
    private String EmpresaD;
    private String Serie;
    private String Numero;
    private String Origen;
    private String OrigenD;
    private String Destino1;
    private String Destino1D;
    private String Tipo;
    private String TipoD;
    private String Asiento;
    private String Retorno;
    private String Autorizo;
    private String Identidad;
    private String IdentidadD;
    private String DNI;
    private String Edad;
    private String Telefono;
    private String Ruc;
    private String Razon;
    private String Usuario;
    private String Terminal;
    private String Agencia;
    private String AgenciaD;
    private String Otro;
    private String Intermedio;
    private String Comida;
    private String Voucher;
    private String Nombre;
    private String agenciaembarque;
    private String agenciaembarqued;
    private String FechaViaje;
    private String FechaEmision;
    private double PrecioAct;
    private double Precio;
    private int Codigo;
    private int B_Identity;
    private String Comentario;
    private String EstadoWeb;
    private String FechaCaducidadWeb;
    private String EticketVisa;
    private String UsuarioVisa;
    private String Eticket;
    private String NumeroTarjeta;
    private String TarjetaHabiente;
    
    // VARIABLES AUXILIARES PARA LA FACTURACIÓN ELECTRÓNICA
    /**
     * TEXTO DE MONTO EN LETRAS 
     * */
    private String MontoLetras;
    private String DocumentoElectronico;
    private String TipoDocumento;
    private String TipoDocumentoD;
    private String  TipoDocumentoReceptor;
    private String  TipoDocumentoReceptorD;
    private String  CodigoTotalVenta;
    private String  CodigoAfectacionIGV;
    private double IGV;
    private double TotalSinIGV;
    private double Total;
    
    
	public int getNro() {
		return Nro;
	}
	public void setNro(int nro) {
		Nro = nro;
	}
	public int getSalida() {
		return Salida;
	}
	public void setSalida(int salida) {
		Salida = salida;
	}
	public int getDestino() {
		return Destino;
	}
	public void setDestino(int destino) {
		Destino = destino;
	}
	public String getDestinoD() {
		return DestinoD;
	}
	public void setDestinoD(String destinoD) {
		DestinoD = destinoD;
	}
	public String getHoraViaje() {
		return HoraViaje;
	}
	public void setHoraViaje(String horaViaje) {
		HoraViaje = horaViaje;
	}
	public String getHoraViajeIni() {
		return HoraViajeIni;
	}
	public void setHoraViajeIni(String horaViajeIni) {
		HoraViajeIni = horaViajeIni;
	}
	public String getEmpresa() {
		return Empresa;
	}
	public void setEmpresa(String empresa) {
		Empresa = empresa;
	}
	public String getEmpresaD() {
		return EmpresaD;
	}
	public void setEmpresaD(String empresaD) {
		EmpresaD = empresaD;
	}
	public String getSerie() {
		return Serie;
	}
	public void setSerie(String serie) {
		Serie = serie;
	}
	public String getNumero() {
		return Numero;
	}
	public void setNumero(String numero) {
		Numero = numero;
	}
	public String getOrigen() {
		return Origen;
	}
	public void setOrigen(String origen) {
		Origen = origen;
	}
	public String getOrigenD() {
		return OrigenD;
	}
	public void setOrigenD(String origenD) {
		OrigenD = origenD;
	}
	public String getDestino1() {
		return Destino1;
	}
	public void setDestino1(String destino1) {
		Destino1 = destino1;
	}
	public String getDestino1D() {
		return Destino1D;
	}
	public void setDestino1D(String destino1d) {
		Destino1D = destino1d;
	}
	public String getTipo() {
		return Tipo;
	}
	public void setTipo(String tipo) {
		Tipo = tipo;
	}
	public String getTipoD() {
		return TipoD;
	}
	public void setTipoD(String tipoD) {
		TipoD = tipoD;
	}
	public String getAsiento() {
		return Asiento;
	}
	public void setAsiento(String asiento) {
		Asiento = asiento;
	}
	public String getRetorno() {
		return Retorno;
	}
	public void setRetorno(String retorno) {
		Retorno = retorno;
	}
	public String getAutorizo() {
		return Autorizo;
	}
	public void setAutorizo(String autorizo) {
		Autorizo = autorizo;
	}
	public String getIdentidad() {
		return Identidad;
	}
	public void setIdentidad(String identidad) {
		Identidad = identidad;
	}
	public String getIdentidadD() {
		return IdentidadD;
	}
	public void setIdentidadD(String identidadD) {
		IdentidadD = identidadD;
	}
	public String getDNI() {
		return DNI;
	}
	public void setDNI(String dNI) {
		DNI = dNI;
	}
	public String getEdad() {
		return Edad;
	}
	public void setEdad(String edad) {
		Edad = edad;
	}
	public String getTelefono() {
		return Telefono;
	}
	public void setTelefono(String telefono) {
		Telefono = telefono;
	}
	public String getRuc() {
		return Ruc;
	}
	public void setRuc(String ruc) {
		Ruc = ruc;
	}
	public String getRazon() {
		return Razon;
	}
	public void setRazon(String razon) {
		Razon = razon;
	}
	public String getUsuario() {
		return Usuario;
	}
	public void setUsuario(String usuario) {
		Usuario = usuario;
	}
	public String getTerminal() {
		return Terminal;
	}
	public void setTerminal(String terminal) {
		Terminal = terminal;
	}
	public String getAgencia() {
		return Agencia;
	}
	public void setAgencia(String agencia) {
		Agencia = agencia;
	}
	public String getAgenciaD() {
		return AgenciaD;
	}
	public void setAgenciaD(String agenciaD) {
		AgenciaD = agenciaD;
	}
	public String getOtro() {
		return Otro;
	}
	public void setOtro(String otro) {
		Otro = otro;
	}
	public String getIntermedio() {
		return Intermedio;
	}
	public void setIntermedio(String intermedio) {
		Intermedio = intermedio;
	}
	public String getComida() {
		return Comida;
	}
	public void setComida(String comida) {
		Comida = comida;
	}
	public String getVoucher() {
		return Voucher;
	}
	public void setVoucher(String voucher) {
		Voucher = voucher;
	}
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public String getAgenciaembarque() {
		return agenciaembarque;
	}
	public void setAgenciaembarque(String agenciaembarque) {
		this.agenciaembarque = agenciaembarque;
	}
	public String getAgenciaembarqued() {
		return agenciaembarqued;
	}
	public void setAgenciaembarqued(String agenciaembarqued) {
		this.agenciaembarqued = agenciaembarqued;
	}
	public String getFechaViaje() {
		return FechaViaje;
	}
	public void setFechaViaje(String fechaViaje) {
		FechaViaje = fechaViaje;
	}
	/**
	 * FECHA DE EMISION DEL DOCUMENTO (Formato yyyy-mm-dd)
	 * **/
	public String getFechaEmision() {
		return FechaEmision;
	}
	public void setFechaEmision(String fechaEmision) {
		FechaEmision = fechaEmision;
	}
	public double getPrecioAct() {
		return PrecioAct;
	}
	public void setPrecioAct(double precioAct) {
		PrecioAct = precioAct;
	}
	/**
	 * MONTO DE TOTAL DE VENTA (sac:AdditionalMonetaryTotal/cbc:PayableAmount) 
	 * **/
	public double getPrecio() {
		return Precio;
	}
	public void setPrecio(double precio) {
		Precio = precio;
	}
	public int getCodigo() {
		return Codigo;
	}
	public void setCodigo(int codigo) {
		Codigo = codigo;
	}
	public int getB_Identity() {
		return B_Identity;
	}
	public void setB_Identity(int b_Identity) {
		B_Identity = b_Identity;
	}
	public String getComentario() {
		return Comentario;
	}
	public void setComentario(String comentario) {
		Comentario = comentario;
	}
	public String getEstadoWeb() {
		return EstadoWeb;
	}
	public void setEstadoWeb(String estadoWeb) {
		EstadoWeb = estadoWeb;
	}
	public String getFechaCaducidadWeb() {
		return FechaCaducidadWeb;
	}
	public void setFechaCaducidadWeb(String fechaCaducidadWeb) {
		FechaCaducidadWeb = fechaCaducidadWeb;
	}
	public String getEticketVisa() {
		return EticketVisa;
	}
	public void setEticketVisa(String eticketVisa) {
		EticketVisa = eticketVisa;
	}
	public String getUsuarioVisa() {
		return UsuarioVisa;
	}
	public void setUsuarioVisa(String usuarioVisa) {
		UsuarioVisa = usuarioVisa;
	}
	public String getEticket() {
		return Eticket;
	}
	public void setEticket(String eticket) {
		Eticket = eticket;
	}
	public String getNumeroTarjeta() {
		return NumeroTarjeta;
	}
	public void setNumeroTarjeta(String numeroTarjeta) {
		NumeroTarjeta = numeroTarjeta;
	}
	public String getTarjetaHabiente() {
		return TarjetaHabiente;
	}
	public void setTarjetaHabiente(String tarjetaHabiente) {
		TarjetaHabiente = tarjetaHabiente;
	}
	
	public void setMontoLetras(String montoLetras) {
		MontoLetras = montoLetras;
	}
	/**
	 * MONTO EN LETRAS (CATALOGO 15)
	 * **/
	public String getMontoLetras() {
		return MontoLetras;
	}
	public void setDocumentoElectronico(String documentoElectronico) {
		DocumentoElectronico = documentoElectronico;
	}
	/**
	 * DOCUMENTO ELECTRONICO (Ejem. F001-002345)
	 * **/
	public String getDocumentoElectronico() {
		return DocumentoElectronico;
	}
	public void setTipoDocumento(String tipoDocumento) {
		TipoDocumento = tipoDocumento;
	}
	/**
	 * TIPO DE DOCUMENTO A EMITIR (CATALOGO 1 - Factura = 01 / Boleta = 03 / Nota Credito = 07 / Nota Debito = 08)
	 * **/
	public String getTipoDocumento() {
		return TipoDocumento;
	}
	public void setTipoDocumentoD(String tipoDocumentoD) {
		TipoDocumentoD = tipoDocumentoD;
	}
	public String getTipoDocumentoD() {
		return TipoDocumentoD;
	}
	public void setTipoDocumentoReceptor(String tipoDocumentoReceptor) {
		TipoDocumentoReceptor = tipoDocumentoReceptor;
	}
	public String getTipoDocumentoReceptor() {
		return TipoDocumentoReceptor;
	}
	public void setTipoDocumentoReceptorD(String tipoDocumentoReceptorD) {
		TipoDocumentoReceptorD = tipoDocumentoReceptorD;
	}
	public String getTipoDocumentoReceptorD() {
		return TipoDocumentoReceptorD;
	}
	
	public void setCodigoTotalVenta(String codigoTotalVenta) {
		CodigoTotalVenta = codigoTotalVenta;
	}
	 /**
     * VENTAS EXONERADAS PASAJES 1003 (CATALOGO 14 - sac:AdditionalMonetaryTotal/cbc:ID)
     * */
	public String getCodigoTotalVenta() {
		return CodigoTotalVenta;
	}
	
	public void setIGV(double iGV) {
		IGV = iGV;
	}
	/**MONTO DEL IGV (AFECTACION DEL IGV POR ITEM - PASAJES 0)**/
	public double getIGV() {
		return IGV;
	}
	
	public void setCodigoAfectacionIGV(String codigoAfectacionIGV) {
		CodigoAfectacionIGV = codigoAfectacionIGV;
	}
	/**CODIGO AFECTACION IGV (CATALOGO 07 - Ejem. 10 = Encomiendas (Con IGV) / 20 = Pasajes (Sin IGV))**/
	public String getCodigoAfectacionIGV() {
		return CodigoAfectacionIGV;
	}
	
	public void setTotalSinIGV(double totalSinIGV) {
		TotalSinIGV = totalSinIGV;
	}
	/**TOTAL SIN IGV (VALOR UNITARIO POR ITEM)**/
	public double getTotalSinIGV() {
		return TotalSinIGV;
	}
	
	public void setTotal(double total) {
		Total = total;
	}
	/**TOTAL DE LA VENTA DEL PRODUCTO O SERVICIO**/
	public double getTotal() {
		return Total;
	}
	
	
	@Override
	public String toString() {
		return "B_VentaBean [Nro=" + Nro + ", Salida=" + Salida + ", Destino=" + Destino + ", DestinoD=" + DestinoD
				+ ", HoraViaje=" + HoraViaje + ", HoraViajeIni=" + HoraViajeIni + ", Empresa=" + Empresa + ", EmpresaD="
				+ EmpresaD + ", Serie=" + Serie + ", Numero=" + Numero + ", Origen=" + Origen + ", OrigenD=" + OrigenD
				+ ", Destino1=" + Destino1 + ", Destino1D=" + Destino1D + ", Tipo=" + Tipo + ", TipoD=" + TipoD
				+ ", Asiento=" + Asiento + ", Retorno=" + Retorno + ", Autorizo=" + Autorizo + ", Identidad="
				+ Identidad + ", IdentidadD=" + IdentidadD + ", DNI=" + DNI + ", Edad=" + Edad + ", Telefono="
				+ Telefono + ", Ruc=" + Ruc + ", Razon=" + Razon + ", Usuario=" + Usuario + ", Terminal=" + Terminal
				+ ", Agencia=" + Agencia + ", AgenciaD=" + AgenciaD + ", Otro=" + Otro + ", Intermedio=" + Intermedio
				+ ", Comida=" + Comida + ", Voucher=" + Voucher + ", Nombre=" + Nombre + ", agenciaembarque="
				+ agenciaembarque + ", agenciaembarqued=" + agenciaembarqued + ", FechaViaje=" + FechaViaje
				+ ", FechaEmision=" + FechaEmision + ", PrecioAct=" + PrecioAct + ", Precio=" + Precio + ", Codigo="
				+ Codigo + ", B_Identity=" + B_Identity + ", Comentario=" + Comentario + ", EstadoWeb=" + EstadoWeb
				+ ", FechaCaducidadWeb=" + FechaCaducidadWeb + ", EticketVisa=" + EticketVisa + ", UsuarioVisa="
				+ UsuarioVisa + ", Eticket=" + Eticket + ", NumeroTarjeta=" + NumeroTarjeta + ", TarjetaHabiente="
				+ TarjetaHabiente + "]";
	}
    
    
    

}
