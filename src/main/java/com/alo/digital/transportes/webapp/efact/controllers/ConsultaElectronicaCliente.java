package com.alo.digital.transportes.webapp.efact.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.text.ParseException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//import pe.com.grupopalomino.sistema.boletaje.bean.V_Varios_FacturacionBean;
//import pe.com.grupopalomino.sistema.boletaje.bean.V_Ventas_FacturacionBean;
//import pe.com.grupopalomino.sistema.boletaje.util.FuncionesFacturacionPDF;
//import pe.com.grupopalomino.sistema.boletaje.util.GeneraDocumentoFe;

import com.alo.digital.transportes.webapp.efact.beans.V_ClientesBean;
import com.alo.digital.transportes.webapp.efact.beans.V_Varios_FacturacionBean;
import com.alo.digital.transportes.webapp.efact.beans.V_Ventas_FacturacionBean;
import com.alo.digital.transportes.webapp.efact.dao.*;
import com.alo.digital.transportes.webapp.efact.util.Encryptor;
import com.alo.digital.transportes.webapp.efact.util.FuncionesFacturacionPDF;
import com.alo.digital.transportes.webapp.efact.util.GeneraDocumentoFe;
import com.alo.digital.transportes.webapp.efact.util.Utils;
import com.alo.digital.transportes.webapp.efact.webservice.sunat.util.Properties;
import com.itextpdf.text.Document;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.test.context.support.WithAnonymousUser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

//import pe.com.grupopalomino.sistema.boletaje.util.Encryptor;
//import pe.com.grupopalomino.sistema.boletaje.util.Utils;

@Controller
@RequestMapping("consultacliente")
@WithAnonymousUser
public class ConsultaElectronicaCliente {

    @Autowired
    FacturacionDAO facturacionservice;
    @Autowired
    ClienteDAO servicecliente;
    @Autowired
    VentasFacturacionDAO ventasfacturacionservice;
    @Autowired
    Varios_FacturacionDAO variosservice;

    @GetMapping("/")
    public String linkconsultacliente() {
        return "/fragments/_clientes/consultas";
    }

    @GetMapping("ClientesRUC")
    @ResponseBody
    public Map<String, Object> ClientesRUC(@RequestParam("RUC") String RUC) {
        Map<String, Object> rpta = new HashMap<String, Object>();
        boolean respuestaAjaxCliente = false;
        V_ClientesBean beancliente = servicecliente.listClientesRUC(RUC);
        if (beancliente != null) {

            respuestaAjaxCliente = true;
        }
        rpta.put("respuestaAjaxCliente", respuestaAjaxCliente);
        rpta.put("beancliente", beancliente);

        return rpta;
    }

    @Autowired
    VariosDao variosDao;
    private static final Log log = LogFactory.getLog(ConsultaElectronicaController.class);
    //ZDNzYzRyZzRkM2QwY3VtM3QwczNsM2N0cjBuMWMwc24wcm00bGNsMTNudDNz

    /*@Action(value = "ZDNzYzRyZzRkM2QwY3VtM3QwczNsM2N0cjBuMWMwc24wcm00bGNsMTNudDNz", results = {
            @Result(name = SUCCESS, type="stream" ,
                    params = { "contentType", "application/pdf", "inputName", "inputStream", "contentDisposition", "filename=\"normal.pdf\"", "bufferSize", "1024" })})*/
    @GetMapping("ZDNzYzRyZzRkM2QwY3VtM3QwczNsM2N0cjBuMWMwc24wcm00bGNsMTNudDNz")
    @ResponseBody
    public void generaPDFNormal(HttpServletResponse response, HttpServletRequest request, @RequestParam("em") String em, @RequestParam("id") String id, @RequestParam("ope") String ope) {
        String nombreDocumento = "";
        String number = UUID.randomUUID().toString();

        /*response.setHeader("Content-Disposition", "attachment;filename=" + number.toString() + ".pdf");
        response.setHeader("Content-Type", "application/pdf");*/

        Document documentoPDF = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(documentoPDF, baos);
            documentoPDF.open();

            V_Varios_FacturacionBean facturacionEmpresa = variosservice.SQL_SELECT_LISTA_PARAMETROS_DESCARGA_FACTURADOR(Encryptor.DescifrarBase64(em));

            Image logo = null;

            if (facturacionEmpresa.getEmpresa().trim().equals("001")) {

                InputStream rutalogo = null;
                if (Properties.SEND_TO_SUNAT_PRODUCCION) {
                    rutalogo = new URL("https://encarga.pe/encargaefact/images/headerefact.png").openStream();
                } else {
                    rutalogo = new URL("http://localhost:8081/encargaefact/images/headerefact.png").openStream();
                }

                //InputStream rutaLogoPalomino = request.getServletContext().getResourceAsStream("images" + File.separator + "logo.png");
                logo = Image.getInstance(IOUtils.toByteArray(rutalogo));
                logo.scalePercent(33f);
                logo.setAbsolutePosition(35f, 810f);
                logo.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);

            }

            Map<String, Object> mapaVentas = new HashMap<>();

            V_Ventas_FacturacionBean ventaImages = new V_Ventas_FacturacionBean();

            //mapaVentas = facturacionservice.USP_SELECT_VENTA_APROBADA_X_NRO_FACTURADOR_SQL(Integer.parseInt(Encryptor.DescifrarBase64(id)),Encryptor.DescifrarBase64(ope));

            mapaVentas = facturacionservice.SQL_SELECT_VENTA_DESCARGA_X_NRO_FACTURADOR(Integer.parseInt(Encryptor.DescifrarBase64(id)), Encryptor.DescifrarBase64(ope));
            nombreDocumento = mapaVentas.get("DocumentoZip").toString().replace(".zip","");
            response.setHeader("Content-Disposition", "attachment;filename=" + nombreDocumento + ".pdf");
            response.setHeader("Content-Type", "application/pdf");
            ventaImages = ventasfacturacionservice.SQL_SELECT_VENTAS_IMAGES_FACTURACION(Integer.parseInt(mapaVentas.get("Nro").toString()), Encryptor.DescifrarBase64(ope));


            if (mapaVentas.get("EnvioSunat").toString().trim().equals("1")) {

                if (ventaImages.getImageQRActualizada().trim().equals("N")) {


                    ZipInputStream zisEnvio = new ZipInputStream(new FileInputStream(facturacionEmpresa.getRutaEnvioSunat() + mapaVentas.get("DocumentoZip").toString()));
                    ZipEntry entrada;

                    while (null != (entrada = zisEnvio.getNextEntry())) {

                        byte[] buf = new byte[1024];
                        int len;
                        StringBuffer contentXml = new StringBuffer();

                        if (!entrada.isDirectory()) {

                            while ((len = zisEnvio.read(buf)) > 0) {
                                contentXml.append(new String(buf, 0, len, "UTF-8"));
                            }

                            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                            InputSource inputSource = new InputSource();
                            inputSource.setCharacterStream(new StringReader(contentXml.toString()));

                            org.w3c.dom.Document doc = documentBuilder.parse(inputSource);
                            NodeList nodeListhash = doc.getElementsByTagName("ds:DigestValue");
                            Node nodohash = nodeListhash.item(0);

                            if (nodohash instanceof org.w3c.dom.Element) {

                                Map<String, Object> respuestaXML = new HashMap<>();
                                respuestaXML.put("codehash", nodohash.getTextContent().trim());

                                ventasfacturacionservice.SQL_UPDATE_VENTAS_FACTURACION_IMAGES(Integer.parseInt(mapaVentas.get("Nro").toString()), Encryptor.DescifrarBase64(ope).toString(), respuestaXML.get("codehash").toString(), null, GeneraDocumentoFe.CodigoQR(facturacionEmpresa, mapaVentas, respuestaXML));

                            }
                        }
                    }

                    zisEnvio.close();

                    ventaImages = ventasfacturacionservice.SQL_SELECT_VENTAS_IMAGES_FACTURACION(Integer.parseInt(mapaVentas.get("Nro").toString()), Encryptor.DescifrarBase64(ope).toString());

                }

            }


            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(110f);
            Map<String, Object> mapvarios = variosDao.getVarios();
            String ValorDetraccion = mapaVentas.get("Detraccion").toString();

            if (Double.parseDouble(ValorDetraccion) != 0) {
                table = FuncionesFacturacionPDF.F_Facturacion_Electronica_FormatoGrande_Detraccion(table, facturacionEmpresa, mapaVentas, ventaImages, logo, mapvarios);
            } else {
                table = FuncionesFacturacionPDF.F_Facturacion_Electronica_FormatoGrande(table, facturacionEmpresa, mapaVentas, ventaImages, logo, mapvarios);
            }

            documentoPDF.add(table);

            documentoPDF.close();
            ServletOutputStream outputStream = response.getOutputStream();
            baos.writeTo(outputStream);

            outputStream.flush();
            outputStream.close();
            baos.flush();
            baos.close();


        } catch (Exception e) {
            log.info(e.getLocalizedMessage() + "" + e.getStackTrace() + " | " + e.getCause() + " | " + e.getMessage() + " | " + e);
        }

    }

    @GetMapping("ZDNzYzRyZzRkM2QwY3VtM3QwczNsM2N0cjBuMWMwc3QxY2szdGNsMTNudDNz")
    @ResponseBody
    public void generaPDFTicket(HttpServletResponse response, HttpServletRequest request, @RequestParam("em") String em, @RequestParam("id") String id, @RequestParam("ope") String ope) {

        String number = UUID.randomUUID().toString();
        response.setHeader("Content-Disposition", "attachment;filename=" + number.toString() + ".pdf");
        response.setHeader("Content-Type", "application/pdf");
        //Rectangle pageSize = new Rectangle(300,800);
        com.itextpdf.text.Document documentoPDF = new com.itextpdf.text.Document(PageSize.A4, (float) 2.5, 0, 80, 0);
		/*Rectangle pageSize = new Rectangle(400,300);
		documentoPDF.setPageSize(pageSize);*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(documentoPDF, baos);
            documentoPDF.open();

            V_Varios_FacturacionBean facturacionEmpresa = variosservice.SQL_SELECT_LISTA_PARAMETROS_DESCARGA_FACTURADOR(Encryptor.DescifrarBase64(em));

            // if(facturacionEmpresa.getEmpresa().trim().equals("001")){

            InputStream rutalogo = null;

            if (Properties.SEND_TO_SUNAT_PRODUCCION) {
                rutalogo = new URL("https://encarga.pe/encargaefact/images/headerefact.png").openStream();
            } else {
                rutalogo = new URL("http://localhost:8081/encargaefact/images/headerefact.png").openStream();
            }

            //request.getServletContext().getResourceAsStream("images\\logo.png");
            //System.out.println("rutalogo1"+request.getServletContext());
            //System.out.println("rutalogo2"+rutalogo.toString());
            Image logo = Image.getInstance(IOUtils.toByteArray(rutalogo));
            logo.scalePercent(50f);
            logo.setAbsolutePosition(35f, 770f);
            logo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            documentoPDF.add(logo);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(33f);
            table.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);

            Map<String, Object> mapaVentas = new HashMap<>();

            V_Ventas_FacturacionBean ventaImages = new V_Ventas_FacturacionBean();

            mapaVentas = facturacionservice.SQL_SELECT_VENTA_DESCARGA_X_NRO_FACTURADOR(Integer.parseInt(Encryptor.DescifrarBase64(id)), Encryptor.DescifrarBase64(ope));

            ventaImages = ventasfacturacionservice.SQL_SELECT_VENTAS_IMAGES_FACTURACION(Integer.parseInt(mapaVentas.get("Nro").toString()), Encryptor.DescifrarBase64(ope));

            if (mapaVentas.get("EnvioSunat").toString().trim().equals("1")) {

                if (ventaImages.getImageQRActualizada().trim().equals("N")) {

                    ZipInputStream zisEnvio = new ZipInputStream(new FileInputStream(
                            facturacionEmpresa.getRutaEnvioSunat() + mapaVentas.get("DocumentoZip").toString()));
                    ZipEntry entrada;

                    while (null != (entrada = zisEnvio.getNextEntry())) {

                        byte[] buf = new byte[1024];
                        int len;
                        StringBuffer contentXml = new StringBuffer();

                        if (!entrada.isDirectory()) {

                            while ((len = zisEnvio.read(buf)) > 0) {
                                contentXml.append(new String(buf, 0, len, "UTF-8"));
                            }

                            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                            InputSource inputSource = new InputSource();
                            inputSource.setCharacterStream(new StringReader(contentXml.toString()));

                            org.w3c.dom.Document doc = documentBuilder.parse(inputSource);
                            NodeList nodeListhash = doc.getElementsByTagName("ds:DigestValue");
                            Node nodohash = nodeListhash.item(0);
                            if (nodohash instanceof org.w3c.dom.Element) {
                                Map<String, Object> respuestaXML = new HashMap<>();
                                respuestaXML.put("codehash", nodohash.getTextContent().trim());
                                ventasfacturacionservice.SQL_UPDATE_VENTAS_FACTURACION_IMAGES(Integer.parseInt(mapaVentas.get("Nro").toString()), Encryptor.DescifrarBase64(ope).toString(), respuestaXML.get("codehash").toString(), null, GeneraDocumentoFe.CodigoQR(facturacionEmpresa, mapaVentas, respuestaXML));
                            }
                        }
                    }

                    zisEnvio.close();

                    ventaImages = ventasfacturacionservice.SQL_SELECT_VENTAS_IMAGES_FACTURACION(Integer.parseInt(mapaVentas.get("Nro").toString()), Encryptor.DescifrarBase64(ope).toString());

                }

            }

            if (Encryptor.DescifrarBase64(ope).trim().equals("E")) {

                table = FuncionesFacturacionPDF.F_Facturacion_Electronica_Encomiendas_Ticket(table, facturacionEmpresa,
                        mapaVentas, ventaImages);

            } else {

                table = FuncionesFacturacionPDF.F_Facturacion_Electronica_Pasajes_Ticket(table, facturacionEmpresa,
                        mapaVentas, ventaImages);

            }

            documentoPDF.add(table);

            documentoPDF.close();
            ServletOutputStream outputStream = response.getOutputStream();
            baos.writeTo(outputStream);

            outputStream.flush();
            outputStream.close();
            baos.flush();
            baos.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            // log.info(Utils.printStackTraceToString(e));
        }
    }

    @GetMapping("59f788970fNTlmNzg4OTcwZm")
    @ResponseBody
    public Map<String, Object> cosultadocumentos1(@RequestParam("em") String em,
                                                  @RequestParam("fechaemision") String fechaemision, @RequestParam("serie") String serie,
                                                  @RequestParam("numero") String numero, @RequestParam("tipodocumento") String tipodocumento,
                                                  @RequestParam("ruc") String ruc, @RequestParam("monto") String monto) {

        Map<String, Object> rpta = new HashMap<>();
        List<Map<String, Object>> lstDatos = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> ventasMap = new ArrayList<Map<String, Object>>();
        Map<String, Object> rows = new HashMap<>();
        String mensajeServer;
        boolean respuestaServer;
        try {

            if (fechaemision == null || fechaemision.trim().isEmpty()) {
                respuestaServer = false;
                mensajeServer = "Debe ingresar una fecha de emision.";
                rpta.put("respuestaServer", respuestaServer);
                rpta.put("mensajeServer", mensajeServer);
                return rpta;
            }
            if (tipodocumento == null) {
                respuestaServer = false;
                mensajeServer = "Debe seleccionar un tipo de documento.";
                rpta.put("respuestaServer", respuestaServer);
                rpta.put("mensajeServer", mensajeServer);
                return rpta;
            }
            if (serie == null || serie.trim().isEmpty()) {
                respuestaServer = false;
                mensajeServer = "Debe ingresar la serie del documento.";
                rpta.put("respuestaServer", respuestaServer);
                rpta.put("mensajeServer", mensajeServer);
                return rpta;
            }
            if (numero == null || numero.trim().isEmpty()) {
                respuestaServer = false;
                mensajeServer = "Debe ingresar el numero del documento.";
                rpta.put("respuestaServer", respuestaServer);
                rpta.put("mensajeServer", mensajeServer);
                return rpta;
            }

            if (!(serie.substring(0, 1).trim().toUpperCase().equals("F"))) {
                if (!(serie.substring(0, 1).trim().toUpperCase().equals("B"))) {
                    respuestaServer = false;
                    mensajeServer = "Debe de anteponer la letra F ó B.";
                    rpta.put("respuestaServer", respuestaServer);
                    rpta.put("mensajeServer", mensajeServer);
                    return rpta;
                }
            }

            if (serie.substring(0, 1).trim().toUpperCase().equals("F")) {
                if (!tipodocumento.trim().equals("01") && !tipodocumento.trim().equals("03")
                        && !tipodocumento.trim().equals("05") && !tipodocumento.trim().equals("06")) {
                    respuestaServer = false;
                    mensajeServer = "Tipo de documento no permitido para una factura.";
                    rpta.put("respuestaServer", respuestaServer);
                    rpta.put("mensajeServer", mensajeServer);
                    return rpta;
                }

            }
            if (serie.substring(0, 1).trim().toUpperCase().equals("B")) {
                if (!tipodocumento.trim().equals("02") && !tipodocumento.trim().equals("04")) {
                    respuestaServer = false;
                    mensajeServer = "Tipo de documento no permitido para una boleta.";
                    rpta.put("respuestaServer", respuestaServer);
                    rpta.put("mensajeServer", mensajeServer);
                    return rpta;
                }
            }

            if (tipodocumento.trim().equals("01") || tipodocumento.trim().equals("03")
                    || tipodocumento.trim().equals("05") || tipodocumento.trim().equals("06")) {

                if (ruc == null || ruc.trim().isEmpty()) {
                    respuestaServer = false;
                    mensajeServer = "Debe ingresar un número de ruc.";
                    rpta.put("respuestaServer", respuestaServer);
                    rpta.put("mensajeServer", mensajeServer);
                    return rpta;
                }
            }

            ventasMap = facturacionservice.SQL_SELECT_VENTA_DESCARGA_FACTURADOR_SQL(em,
                    Utils.FormatoFecha(fechaemision.toString()), serie.substring(1, 4), numero, tipodocumento, ruc,
                    monto);

            if (ventasMap.size() == 0) {
                respuestaServer = false;
                mensajeServer = "No se encontraron resultados.";
                rpta.put("respuestaServer", respuestaServer);
                rpta.put("mensajeServer", mensajeServer);
                return rpta;
            }

            for (Map<String, Object> map : ventasMap) {

                Map<String, Object> mapa = new HashMap<>();
                mapa.put("id", Encryptor.CifrarBase64(map.get("Nro").toString()));
                mapa.put("fechaEmision", map.get("FechaEmisionD"));
                mapa.put("em", Encryptor.CifrarBase64(map.get("Empresa").toString()));
                mapa.put("documentoElectronico", map.get("DocumentoElectronico").toString());
                mapa.put("agencia", map.get("AgenciaD"));
                mapa.put("tipo", map.get("Servicio").toString());
                mapa.put("servicio", Encryptor.CifrarBase64(map.get("Servicio").toString()));
                mapa.put("data1", Encryptor.CifrarBase64(Utils.URL_PDF_TICKET_CLIENTES));
                mapa.put("data2", Encryptor.CifrarBase64(Utils.URL_XML_CLIENTES));
                mapa.put("data3", Encryptor.CifrarBase64(Utils.URL_PDF_NORMAL_CLIENTES));
                mapa.put("data4", Encryptor.CifrarBase64(Utils.URL_CDR_CLIENTES));
                mapa.put("enviado", map.get("Enviado"));
                mapa.put("estado", "S");
                lstDatos.add(mapa);
            }

            rows.put("rows", lstDatos);
            respuestaServer = true;
            rpta.put("respuestaServer", respuestaServer);
            rpta.put("rows", rows);
            return rpta;
        } catch (Exception e) {
            respuestaServer = false;
            mensajeServer = "Ocurrio un error al generar la consulta.";
            rpta.put("respuestaServer", respuestaServer);
            rpta.put("mensajeServer", mensajeServer);
            System.out.println(e.getMessage());
            return rpta;
            // log.info(Utils.printStackTraceToString(e));
        }
    }

    @GetMapping("ZDNzYzRyZzRkM2QwY3VtM3QwczNsM2N0cjBuMWMwc3htbGNsMTNudDNz")
    public void generaXML(HttpServletResponse response, @RequestParam("em") String em, @RequestParam("id") String id, @RequestParam("ope") String ope)
            throws ParseException {
        //String number = UUID.randomUUID().toString();

        try {
            V_Varios_FacturacionBean facturacionEmpresa = variosservice.SQL_SELECT_LISTA_PARAMETROS_DESCARGA_FACTURADOR(Encryptor.DescifrarBase64(em));
            Map<String, Object> mapaVentas = new HashMap<>();
            //mapaVentas = facturacionservice.USP_SELECT_VENTA_APROBADA_X_NRO_FACTURADOR_SQL(Integer.parseInt(Encryptor.DescifrarBase64(id)) ,Encryptor.DescifrarBase64(ope));
            mapaVentas = facturacionservice.SQL_SELECT_VENTA_DESCARGA_X_NRO_FACTURADOR(Integer.parseInt(Encryptor.DescifrarBase64(id)), Encryptor.DescifrarBase64(ope));
            File file = new File(facturacionEmpresa.getRutaEnvioSunat() + mapaVentas.get("DocumentoZip").toString());
            if (!file.exists()) {
                file = new File(facturacionEmpresa.getRutaEnvioSunatBackup() + mapaVentas.get("DocumentoZip").toString());
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + mapaVentas.get("DocumentoZip").toString());
            response.setHeader("Content-Type", "application/zip");
            FileInputStream inStream = new FileInputStream(file);
            ServletOutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            inStream.close();
            outStream.close();
        } catch (Exception e) {
            //og.info(Utils.printStackTraceToString(e));
            System.out.println(e.getMessage());
        }
    }


    @GetMapping("ZDNzYzRyZzRkM2QwY3VtM3QwczNsM2N0cjBuMWMwc2NkcmNsMTNudDNz")
    public void generaCDR(HttpServletResponse response, @RequestParam("em") String em, @RequestParam("id") String id, @RequestParam("ope") String ope)
            throws ParseException {
        /*String number = UUID.randomUUID().toString();
        response.setHeader("Content-Disposition", "attachment;filename=" + number.toString() + ".zip");
        response.setHeader("Content-Type", "application/zip");*/
        try {
            V_Varios_FacturacionBean facturacionEmpresa = variosservice.SQL_SELECT_LISTA_PARAMETROS_DESCARGA_FACTURADOR(Encryptor.DescifrarBase64(em));
            Map<String, Object> mapaVentas = new HashMap<>();
            //mapaVentas = facturacionservice.USP_SELECT_VENTA_APROBADA_X_NRO_FACTURADOR_SQL(Integer.parseInt(Encryptor.DescifrarBase64(id)) ,Encryptor.DescifrarBase64(ope));
            mapaVentas = facturacionservice.SQL_SELECT_VENTA_DESCARGA_X_NRO_FACTURADOR(Integer.parseInt(Encryptor.DescifrarBase64(id)), Encryptor.DescifrarBase64(ope));
            response.setHeader("Content-Disposition", "attachment;filename=R-" + mapaVentas.get("DocumentoZip").toString());
            response.setHeader("Content-Type", "application/zip");
            File file = new File(facturacionEmpresa.getRutaRespuestaSunat() + "R-" + mapaVentas.get("DocumentoZip").toString());
            /*if (!file.exists()) {
                file = new File(facturacionEmpresa.getRutaEnvioSunatBackup() + mapaVentas.get("DocumentoZip").toString());
            }*/
            FileInputStream inStream = new FileInputStream(file);
            ServletOutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            inStream.close();
            outStream.close();
        } catch (Exception e) {
            //og.info(Utils.printStackTraceToString(e));
            System.out.println(e.getMessage());
        }
    }
}
