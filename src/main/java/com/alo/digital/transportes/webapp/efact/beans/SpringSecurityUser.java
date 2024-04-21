package com.alo.digital.transportes.webapp.efact.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SpringSecurityUser implements UserDetails {

	private static final long serialVersionUID = 5773249960714820392L;

	private Integer id;
	private String username;
	private String password;
	private String nombreCompleto;
	private String nombres;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String nivel;
	private String ruc;
	private String razonSocial;
	private String razonPrueba;
	private String correo;
	private String telefono;
	private String estado;
	private double limiteCredito;
	private double montoVentaActual;
	private double montoVentaConfirmada;
	private String codigoAgencia;
	private String descripcionAgencia;
	private String direccion;
	private String representante;
	private String telefono2;
	private String localidad;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	private String ocultarDetalleEstadoCuenta;
	private String centroCosto;
	private String realizaVenta;
	private double porcentajeComision;
	private int medio_Pago;
	private String ciudad;
	private String ciudadd;
	private String longitud, latitud;
	private String agencia;
	private String agenciaD;
	private String empresa;
	private String empresaD;

	private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

	public void addAuthority(GrantedAuthority authority) {
		authorities.add(authority);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRazonPrueba() {
		return razonPrueba;
	}

	public void setRazonPrueba(String razonPrueba) {
		this.razonPrueba = razonPrueba;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getLimiteCredito() {
		return limiteCredito;
	}

	public void setLimiteCredito(double limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public double getMontoVentaActual() {
		return montoVentaActual;
	}

	public void setMontoVentaActual(double montoVentaActual) {
		this.montoVentaActual = montoVentaActual;
	}

	public double getMontoVentaConfirmada() {
		return montoVentaConfirmada;
	}

	public void setMontoVentaConfirmada(double montoVentaConfirmada) {
		this.montoVentaConfirmada = montoVentaConfirmada;
	}

	public String getCodigoAgencia() {
		return codigoAgencia;
	}

	public void setCodigoAgencia(String codigoAgencia) {
		this.codigoAgencia = codigoAgencia;
	}

	public String getDescripcionAgencia() {
		return descripcionAgencia;
	}

	public void setDescripcionAgencia(String descripcionAgencia) {
		this.descripcionAgencia = descripcionAgencia;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getRepresentante() {
		return representante;
	}

	public void setRepresentante(String representante) {
		this.representante = representante;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getOcultarDetalleEstadoCuenta() {
		return ocultarDetalleEstadoCuenta;
	}

	public void setOcultarDetalleEstadoCuenta(String ocultarDetalleEstadoCuenta) {
		this.ocultarDetalleEstadoCuenta = ocultarDetalleEstadoCuenta;
	}

	public String getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	public String getRealizaVenta() {
		return realizaVenta;
	}

	public void setRealizaVenta(String realizaVenta) {
		this.realizaVenta = realizaVenta;
	}

	public double getPorcentajeComision() {
		return porcentajeComision;
	}

	public void setPorcentajeComision(double porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
	}

	public int getMedio_Pago() {
		return medio_Pago;
	}

	public void setMedio_Pago(int medio_Pago) {
		this.medio_Pago = medio_Pago;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getCiudadd() {
		return ciudadd;
	}

	public void setCiudadd(String ciudadd) {
		this.ciudadd = ciudadd;
	}

	public String getLongitud() {
		return longitud;
	}

	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}

	public String getLatitud() {
		return latitud;
	}

	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getAgenciaD() {
		return agenciaD;
	}

	public void setAgenciaD(String agenciaD) {
		this.agenciaD = agenciaD;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getEmpresaD() {
		return empresaD;
	}

	public void setEmpresaD(String empresaD) {
		this.empresaD = empresaD;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
}