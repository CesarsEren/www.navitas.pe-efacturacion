package com.alo.digital.transportes.webapp.efact.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Encryptor {

	public static final String ISO_8859	= "ISO-8859-1";
	public static final String UTF_8	= "UTF8";

	
	public static String CifrarBase64(String texto){
        Base64.Encoder encoder = Base64.getEncoder();
        
        String textocifrado = encoder.encodeToString(texto.getBytes(StandardCharsets.UTF_8) );        
        return textocifrado;
	}

	public static String DescifrarBase64(String texto){
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedByteArray = decoder.decode(texto);
 
        String textodescifrado = new String(decodedByteArray);        
        return textodescifrado;
    }
	
	public static byte[] Base64ToBytes(String texto){
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedByteArray = decoder.decode(texto);
         
        return decodedByteArray;
	}
	
	public static String BytesToBase64( byte[] bytes) {
		
		String encodeBase64 = Base64.getEncoder().encodeToString(bytes);
		return encodeBase64;
		
	}
	
}
