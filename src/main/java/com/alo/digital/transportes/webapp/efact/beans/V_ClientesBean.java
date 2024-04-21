package com.alo.digital.transportes.webapp.efact.beans;

public class V_ClientesBean {
	
	
	private String Ruc,Razon,Direccion,Credito,
	   Responsable,Correo,Telefono,Tipo;



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
		
		public String getDireccion() {
		return Direccion;
		}
		
		public void setDireccion(String direccion) {
		Direccion = direccion;
		}
		
		public String getCredito() {
		return Credito;
		}
		
		public void setCredito(String credito) {
		Credito = credito;
		}
		
		public String getResponsable() {
		return Responsable;
		}
		
		public void setResponsable(String responsable) {
		Responsable = responsable;
		}
		
		public String getCorreo() {
		return Correo;
		}
		
		public void setCorreo(String correo) {
		Correo = correo;
		}
		
		public String getTelefono() {
		return Telefono;
		}
		
		public void setTelefono(String telefono) {
		Telefono = telefono;
		}
		
		public String getTipo() {
		return Tipo;
		}
		
		public void setTipo(String tipo) {
		Tipo = tipo;
		}

		@Override
		public String toString() {
			return "V_ClientesBean [Ruc=" + Ruc + ", Razon=" + Razon + ", Direccion=" + Direccion + ", Credito="
					+ Credito + ", Responsable=" + Responsable + ", Correo=" + Correo + ", Telefono=" + Telefono
					+ ", Tipo=" + Tipo + "]";
		}
		
		

}
