package com.alo.digital.transportes.webapp.efact.util;

import java.util.ArrayList;
import java.util.List;

public class ErrorValidacion {

	private List<String> mensajeError = new ArrayList<String>();
	private boolean error = false;
	
	public void addMensaje(String mensaje){
		mensajeError.add(mensaje);
	}
	
	public List<String> getMensajeError() {
		return mensajeError;
	}
	
	public void setError(boolean error) {
		this.error = error;
	}
	
	public boolean getError() {
		return error;
	}
}
