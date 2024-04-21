package com.alo.digital.transportes.webapp.efact.controllers;

import com.alo.digital.transportes.webapp.efact.beans.V_Varios_FacturacionBean;
import com.alo.digital.transportes.webapp.efact.dao.Varios_FacturacionDAO;
import com.alo.digital.transportes.webapp.efact.dao.VentaFacturacionDAO;
import com.alo.digital.transportes.webapp.efact.util.GeneraDocumentoFe;
import com.alo.digital.transportes.webapp.efact.util.GeneraDocumentoTest;
import com.alo.digital.transportes.webapp.efact.util.Utilitarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("qr")
@WithAnonymousUser
public class QRController {

    @Autowired
    private VentaFacturacionDAO ventafacturacionDAO;

    @Autowired
    private Varios_FacturacionDAO variosfacturacionDAO;

    @RequestMapping(value = "generadocumento", method = RequestMethod.POST)
    public Map<String, Object> postFacturacionElectronica(@RequestParam("nro") Integer nro,
                                                          @RequestParam("tipoOperacion") String tipoOperacion,
                                                          ServletRequest request) {

        Map<String, Object> respuestaXML = new HashMap<>();

        Calendar calendario = new GregorianCalendar();


        int hora, minutos, segundos;
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);

        System.out.println("ENTRO " + hora + ":" + minutos + ":" + segundos + " PARAMETROS : Nro: " + nro + " - Operación: " + tipoOperacion);


        //log.info("PARAMETROS : Nro: " + nro+ " - Operación: "+ tipoOperacion);

        try {

            int respuestaserver = -1;
            Map<String, Object> ventas = ventafacturacionDAO.SQL_SELECT_VENTA_FACTURADOR(nro, tipoOperacion);

            V_Varios_FacturacionBean facturacion = variosfacturacionDAO.SQL_SELECT_LISTA_PARAMETROS_FACTURADOR(ventas.get("Empresa").toString());

            if (tipoOperacion.trim().equals("N")) {

                respuestaXML = GeneraDocumentoTest.DocumentoNotaCreditoXML(ventas, facturacion);

            } else if (tipoOperacion.trim().equals("T")) {

                respuestaXML = GeneraDocumentoTest.DocumentoNotaDebitoXML(ventas, facturacion);

            } else {

                respuestaXML = GeneraDocumentoTest.DocumentoFacturaXML(ventas, facturacion);

            }

            respuestaserver = ventafacturacionDAO.SQL_INSERT_VENTA_FACTURADOR(nro, tipoOperacion, respuestaXML.get("codehash").toString(), null, Utilitarios.CodigoBarras(facturacion, ventas, respuestaXML));

            if (respuestaserver == -1) {
                respuestaXML.put("respuesta", -1);
            } else {

                Calendar calendario1 = new GregorianCalendar();


                int hora1, minutos1, segundos1;
                hora1 = calendario1.get(Calendar.HOUR_OF_DAY);
                minutos1 = calendario1.get(Calendar.MINUTE);
                segundos1 = calendario1.get(Calendar.SECOND);

                respuestaXML.put("respuesta", 1);
                System.out.println("SALIO " + hora1 + ":" + minutos1 + ":" + segundos1 + " PARAMETROS : Nro: " + nro + " - Operación: " + tipoOperacion);
                System.out.println("*********************************************");
            }

        } catch (Exception e) {
            //log.info("ERROR IP DE ACCESO (generadocumento) : " + request.getRemoteAddr());
            //log.info("ERROR PARAMETROS : Nro: " + nro + " - Operación: " + tipoOperacion);
            respuestaXML.put("respuesta", -1);
            //log.info("respuesta -->" + respuestaXML);
            System.out.println(e.getMessage());
            //e.printStackTraceToString(e);
        }


        return respuestaXML;

    }


}
