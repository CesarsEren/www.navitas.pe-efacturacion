package com.alo.digital.transportes.webapp.efact.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorLog {
	
	public static String printStackTraceToString(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));

		return errors.toString();
	}
}
