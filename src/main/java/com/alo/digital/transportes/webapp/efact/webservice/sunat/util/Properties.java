package com.alo.digital.transportes.webapp.efact.webservice.sunat.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

    public static Boolean SEND_TO_SUNAT_PRODUCCION;

    @Value("${app.envio.sunat.prd}")
    public void setSendToSunatProduccion(String sendToSunatProduccion) {
        Properties.SEND_TO_SUNAT_PRODUCCION = sendToSunatProduccion.equals("1");
        //= sendToSunatProduccion == null && !sendToSunatProduccion.isEmpty() && sendToSunatProduccion.equals("1");
    }
}
