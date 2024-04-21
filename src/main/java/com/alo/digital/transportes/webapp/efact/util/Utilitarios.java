package com.alo.digital.transportes.webapp.efact.util;

import com.alo.digital.transportes.webapp.efact.beans.B_Identidad;
import com.alo.digital.transportes.webapp.efact.beans.V_Varios_FacturacionBean;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodePDF417;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Utilitarios {


    //private static final Log log = LogFactory.getLog(Utilitarios.class);


    public static List<B_Identidad> getListaComboIdentidad() {

        List<B_Identidad> listaComboIdentidad = new ArrayList<B_Identidad>();

        B_Identidad cb1 = new B_Identidad();
        cb1.setId(1);
        cb1.setTipoDocumento("D");
        cb1.setDescripcionDocumento("D.N.I.");

        B_Identidad cb2 = new B_Identidad();
        cb2.setId(2);
        cb2.setTipoDocumento("P");
        cb2.setDescripcionDocumento("Pasaporte");

        B_Identidad cb3 = new B_Identidad();
        cb3.setId(3);
        cb3.setTipoDocumento("M");
        cb3.setDescripcionDocumento("Menor Edad");

        listaComboIdentidad.add(cb1);
        listaComboIdentidad.add(cb2);
        listaComboIdentidad.add(cb3);
        return listaComboIdentidad;
    }

    public static String[] F_Separador_Reporte(String cadena, String separador) {

        try {
            String[] resultado = null;

            if (cadena != null && !cadena.trim().equals("")) {

                resultado = cadena.split(separador);
            }
            return resultado;
        } catch (Exception ex) {

            throw ex;
        }

    }

    public static String FormatoFechaVoucher(String strFecha, String reimprimir) throws ParseException {

        SimpleDateFormat formatoresultado = new SimpleDateFormat("d MMMM',' yyyy", new Locale("ES"));
        SimpleDateFormat formatoInicial = new SimpleDateFormat("yyyy-MM-dd");
        String separadorespacio = " ";
        String[] valorfecha;
        String dia = "";
        String mes = "";
        String anio = "";
        String Resultado = "";
        if ((strFecha != null) && (!(strFecha.trim().equals("")))) {
            if (strFecha.length() == 10) {
                if (reimprimir == "Y") {
                    dia = strFecha.substring(0, 2);
                    mes = strFecha.substring(3, 5);
                    anio = strFecha.substring(6, 10);
                    strFecha = anio + "-" + mes + "-" + dia;
                    dia = "";
                    mes = "";
                    anio = "";
                }
                Date fechaProgramacion = formatoInicial.parse(strFecha);
                String fechasaliente = formatoresultado.format(fechaProgramacion);
                valorfecha = fechasaliente.split(separadorespacio);
                dia = valorfecha[0];
                mes = valorfecha[1];
                anio = valorfecha[2];
                Resultado = dia + " " + mes.toUpperCase() + anio;
            }
        }

        return Resultado;
    }

    public static ByteArrayInputStream CodigoBarras(V_Varios_FacturacionBean facturacion, Map<String, Object> ventas, Map<String, Object> respuestaXML) {


        String datos = facturacion.getRuc() + "|" + ventas.get("TipoDocumento") + "|" + ventas.get("SerieElectronica") + "|" + ventas.get("Numero") + "|" + ventas.get("IGV") + "|"
                + ventas.get("Total") + "|" + ventas.get("FechaEmision").toString().replace("-", "") + "|" + ventas.get("TipoDocumentoReceptor") + "|"
                + (ventas.get("TipoDocumentoReceptor").toString().trim().equals("6") ? ventas.get("Ruc") : ventas.get("DNI")) + "|"
                + respuestaXML.get("codehash") + "|" + respuestaXML.get("signaturevalue");


        BarcodePDF417 barcode = new BarcodePDF417();
        ByteArrayInputStream inputStream = null;

        barcode.setText(datos);
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

        try {


            java.awt.Image img = barcode.createAwtImage(Color.BLACK, Color.WHITE);

            BufferedImage outImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

            barcode.setErrorLevel(5);

            barcode.setLenCodewords(BarcodePDF417.PDF417_FORCE_BINARY);
            outImage.getGraphics().drawImage(img, 0, 0, null);

            ImageIO.write(outImage, "png", bytesOut);
            bytesOut.flush();
            bytesOut.close();

            byte[] pngImageData = bytesOut.toByteArray();

            inputStream = new ByteArrayInputStream(pngImageData);


        } catch (Exception e) {
            //log.info("ERROR AL GENERAR CODIGOBARRAS --> NRO: "+ventas.get("Nro")+" Servicio: "+ ventas.get("Servicio"));
            //log.info(ErrorLog.printStackTraceToString(e));
        }

        return inputStream;


    }
}
