package com.alo.digital.transportes.webapp.efact.util;

import com.alo.digital.transportes.webapp.efact.beans.UsuarioBodyBean;

public class ValidaDatosUsuario {

	private UsuarioBodyBean usuario;
	
	public ValidaDatosUsuario(UsuarioBodyBean usuario){
		this.usuario = usuario;
		
	}
	
	public ErrorValidacion esUsuarioValido(){
		
		
		ErrorValidacion errorValidacion = new ErrorValidacion();
		errorValidacion.setError(false);
		
		if(usuario.getNombres() == null || usuario.getNombres().toString().trim().isEmpty()){
			errorValidacion.addMensaje("El Nombre no puede estar vacio");
			errorValidacion.setError(true);
			return errorValidacion;
	 	}
		if(usuario.getNombres().toString().trim().length()< 4){
			errorValidacion.addMensaje("El Nombre debe ser mayor a 4 caracteres.");
			errorValidacion.setError(true);
			return errorValidacion;
	 	}
		
		if(usuario.getRuc() == null || usuario.getRuc().toString().trim().isEmpty()){
			errorValidacion.addMensaje("El Ruc no puede estar vacio");
			errorValidacion.setError(true);
			return errorValidacion;
	 	}
		if(usuario.getRazonSocial() == null || usuario.getRazonSocial().toString().trim().isEmpty()){
			errorValidacion.addMensaje("La Razon Social no puede estar vacio");
			errorValidacion.setError(true);
			return errorValidacion;
	 	}
		if(usuario.getNivel() == null || usuario.getNivel().toString().trim().isEmpty()){
			errorValidacion.addMensaje("Por favor seleccione un Nivel");
			errorValidacion.setError(true);
			return errorValidacion;
	 	}
		if(usuario.getPassword() == null || usuario.getPassword().toString().trim().isEmpty()){
			errorValidacion.addMensaje("El password no puede estar vacio");
			errorValidacion.setError(true);
			return errorValidacion;
	 	}
		
		if(usuario.getPassword().toString().trim().length()< 8 ){
			errorValidacion.addMensaje("El password debe ser como mínimo 8 caracteres");
			errorValidacion.setError(true);
			return errorValidacion;
	 	}
		
		return errorValidacion;
		
	}
}
