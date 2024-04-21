package com.alo.digital.transportes.webapp.efact.controllers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.soap.SOAPFaultException;

import com.alo.digital.transportes.webapp.efact.dao.VariosDao;
import com.alo.digital.transportes.webapp.efact.dao.VariosIDAO;
import com.alo.digital.transportes.webapp.efact.util.GeneraDocumentoFe;
import com.alo.digital.transportes.webapp.efact.util.HeaderHandlerResolver;
import com.alo.digital.transportes.webapp.efact.util.Utils;
import com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.BillService;
import com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.BillService_Service;
import com.alo.digital.transportes.webapp.efact.webservice.sunat.util.Properties;


//import com.sun.xml.internal.ws.fault.ServerSOAPFaultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import com.alo.digital.transportes.webapp.efact.beans.B_FacturacionBean;
import com.alo.digital.transportes.webapp.efact.beans.V_Varios_FacturacionBean;
//import pe.com.envialo.consultas.beta.service.BillService;
//import pe.com.envialo.consultas.beta.service.BillService_Service;
import com.alo.digital.transportes.webapp.efact.dao.FacturacionDAO;
import com.alo.digital.transportes.webapp.efact.dao.Varios_FacturacionDAO;
/*import pe.com.envialo.consultas.sunat.produccion.service.BillService;
import pe.com.envialo.consultas.sunat.produccion.service.BillService_Service;
import pe.com.envialo.consultas.util.GeneraDocumentoFe;
import pe.com.envialo.consultas.util.HeaderHandlerResolver;
import pe.com.envialo.consultas.util.Utils;*/

//pe.com.envialo.consultas.consultas.produccion.service
/*
import pe.com.envialo.consultas.consultas.produccion.service.BillConsultService;
import pe.com.envialo.consultas.consultas.produccion.service.BillService;
import pe.com.envialo.consultas.consultas.produccion.service.StatusResponse;
-*/
@Controller
@PreAuthorize("hasRole('ROLE_1') or hasRole('ROLE_T')")
@RequestMapping("/documentosenvio")
public class FacturacionElectronicaController {

    private static final Log log = LogFactory.getLog(FacturacionElectronicaController.class);

    @GetMapping("/")
    public String linkenviodedocumentos() {
        return "/fragments/facturacion/documentos";
    }

    @Autowired
    FacturacionDAO facturacionservice;

    @Autowired
    Varios_FacturacionDAO variosservice;

    @GetMapping(value = "consultadocumentos", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> consultadocumentos(@RequestParam("order") String order,
                                                  @RequestParam("offset") int offset, @RequestParam("limit") int limit,
                                                  @RequestParam("empresa") String empresa, @RequestParam("fechaemision") String fechaemision,
                                                  @RequestParam("documentos") int documentos) {
        int total;
        Map<String, Object> rpta = new HashMap<String, Object>();
        Map<String, Object> mapaDocumentos = new HashMap<String, Object>();

        List<Map<String, Object>> lstDatos = new ArrayList<Map<String, Object>>();

        try {
            if ((empresa == null || empresa.toString().isEmpty())
                    || (fechaemision == null || fechaemision.toString().trim().isEmpty())) {
                Map<String, Object> mapa = new HashMap<>();
                mapaDocumentos.put("respuesta", false);
                mapaDocumentos.put("mensaje", "por favor seleccione la fecha y/o la empresa.");
                lstDatos.add(mapa);
                // return SUCCESS;
            }
            // System.out.println(empresa);
            // System.out.println(order);
            // System.out.println(offset);
            // System.out.println(limit);
            // System.out.println(fechaemision);
            // System.out.println(documentos);

            List<Map<String, Object>> ventas = facturacionservice.SQL_SELECT_VENTA_ENVIO_FACTURADOR(empresa.trim(),
                    Utils.FormatoFecha(fechaemision.trim().toString()), documentos, limit, offset);

            List<Map<String, Object>> lstAuxiliar = new ArrayList<Map<String, Object>>();

            total = 0;

            lstAuxiliar.addAll(ventas);

            for (Map<String, Object> map : lstAuxiliar) {
                Map<String, Object> mapa = new HashMap<>();
                mapa.put("id", map.get("Nro").toString());
                mapa.put("fechaEmision", map.get("FechaEmisionD"));
                mapa.put("empresa", map.get("Empresa"));
                mapa.put("documentoElectronico", map.get("DocumentoElectronico").toString());
                mapa.put("agencia", map.get("AgenciaD"));
                mapa.put("servicio", map.get("Servicio"));
                mapa.put("servicioD", map.get("ServicioD"));
                mapa.put("estado", "S");
                mapa.put("enviar", "N");
                mapa.put("todos", map.get("variosservice"));
                mapa.put("documentos", documentos);
                mapa.put("observaciones", map.get("RespuestaOse"));//"La consulta se ha generado correctamente");
                lstDatos.add(mapa);
            }

            if (ventas.size() > 0) {

                total = Integer.parseInt(ventas.get(0).get("Cantidad").toString());
            }

            mapaDocumentos.put("respuesta", true);
            mapaDocumentos.put("rows", lstDatos);
            mapaDocumentos.put("total", total);
            mapaDocumentos.put("mensaje", "la operación se realizó con éxito...");

        } catch (Exception e) {
            mapaDocumentos.put("respuesta", false);
            mapaDocumentos.put("mensaje", "Ocurrió un problema al realizar la consulta.");
            System.out.println(e.getMessage());
        }

        rpta.put("mapaDocumentos", mapaDocumentos);
        rpta.put("documentos", documentos);
        return rpta;
    }

    @Autowired
    VariosDao variosDao;

    @GetMapping("enviosunat")
    @ResponseBody
    public Map<String, Object> enviosunat(@RequestParam("dato") List<String> dato) {
        Map<String, Object> rpta = new HashMap<>();
        Map<String, Object> mapaJSONResultado = new HashMap<>();
        Map<String, Object> mapaEnvio = new HashMap<>();
        List<Map<String, Object>> lstResultado = new ArrayList<>();
        Map<String, Object> mapaFacturacion = new HashMap<>();
        V_Varios_FacturacionBean facturacion = null;
        try {
            byte[] respuestaSunat = null;
            List<Map<String, Object>> lstData = new ArrayList<>();
            Map<String, Object> mapaRespuesta = new HashMap<>();
            List<B_FacturacionBean> listaEnviar = new ArrayList<>();
            boolean todos = false;

            String parametro = null;
            parametro = dato.toString();
            parametro = parametro.substring(1, parametro.length() - 1);
            System.out.println("PARAMETRO:" + parametro);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(parametro);
            JSONArray msg = (JSONArray) jsonObject.get("bean");
            for (int i = 0; i < msg.size(); i++) {
                Map<String, Object> mapaData = new HashMap<>();
                JSONParser parser1 = new JSONParser();
                JSONObject json = (JSONObject) parser1.parse(msg.get(i).toString());

                // System.out.println("DATA " + i + " --> " + json.get("fechaEmision"));
                mapaData.put("id", json.get("id"));
                mapaData.put("fechaEmision", json.get("fechaEmision"));
                mapaData.put("empresa", json.get("empresa"));
                mapaData.put("documentoElectronico", json.get("documentoElectronico"));
                mapaData.put("agencia", json.get("agencia"));
                mapaData.put("servicio", json.get("servicio"));
                mapaData.put("enviar", json.get("enviar"));
                mapaData.put("todos", json.get("todos"));
                mapaData.put("documentos", json.get("documentos"));
                mapaData.put("observaciones", json.get("observaciones"));
                // System.out.println("MAPA DATA : "+mapaData.toString());
                lstData.add(mapaData);
            }

            if (lstData.size() == 0) {
                mapaRespuesta.put("respuesta", false);
                mapaRespuesta.put("mensaje", "Por favor debe de seleccionar un elemento.");
                lstResultado.add(mapaRespuesta);
                mapaJSONResultado.put("rows", lstResultado);
                rpta.put("mapaJSONResultado", mapaJSONResultado);
                return rpta;
            }
            todos = Boolean.parseBoolean(lstData.get(0).get("todos").toString());
            facturacion = variosservice
                    .SQL_SELECT_LISTA_PARAMETROS_FACTURADOR(lstData.get(0).get("empresa").toString());
            if (todos) {
                List<Map<String, Object>> ventas = facturacionservice.SQL_SELECT_VENTA_ENVIO_FACTURADOR(
                        facturacion.getEmpresa(), Utils.FormatoFecha(lstData.get(0).get("fechaEmision").toString()),
                        Integer.parseInt(lstData.get(0).get("documentos").toString()), 0, 0);

                for (Map<String, Object> map : ventas) {
                    B_FacturacionBean documentos = new B_FacturacionBean();
                    documentos.setId(Integer.parseInt(map.get("Nro").toString()));
                    documentos.setAgencia(map.get("AgenciaD").toString());
                    documentos.setDocumentoElectronico(map.get("DocumentoElectronico").toString());
                    documentos.setEmpresa(map.get("Empresa").toString());
                    documentos.setEstado("S");
                    documentos.setEnviar("S");
                    documentos.setFechaEmision(map.get("FechaEmisionD").toString());
                    documentos.setObservaciones("");
                    documentos.setServicio(map.get("Servicio").toString());
                    documentos.setTodos(true);
                    listaEnviar.add(documentos);
                }
            } else {

                for (Map<String, Object> data : lstData) {
                    if (data.get("enviar").toString().equals("S")) {
                        B_FacturacionBean bean = new B_FacturacionBean();
                        bean.setId(Integer.parseInt(data.get("id").toString()));
                        bean.setFechaEmision(data.get("fechaEmision").toString());
                        bean.setEmpresa(data.get("empresa").toString());
                        bean.setDocumentoElectronico(data.get("documentoElectronico").toString());
                        bean.setAgencia(data.get("agencia").toString());
                        bean.setServicio(data.get("servicio").toString());
                        bean.setEnviar(data.get("enviar").toString());
                        bean.setTodos(Boolean.parseBoolean(data.get("todos").toString()));
                        bean.setDocumentos(Integer.parseInt(data.get("documentos").toString()));
                        bean.setObservaciones(data.get("observaciones") == null ? "" : data.get("observaciones").toString());
                        listaEnviar.add(bean);
                    }
                }
            }
            for (B_FacturacionBean data : listaEnviar) {

                // System.out.println("ID "+data.getId());
                // System.out.println("SERVICIO "+data.getServicio());

                mapaFacturacion = facturacionservice.SQL_SELECT_VENTA_FACTURADOR(data.getId(), data.getServicio());
                Map<String, Object> mapvarios = variosDao.getVarios();
                if (mapaFacturacion.get("Servicio").toString().equals("N")) {
                    GeneraDocumentoFe.GeneraDocumentoNotaCreditoXMLUBL21(mapaFacturacion, facturacion);
                } else if (mapaFacturacion.get("Servicio").toString().equals("T")) {
                    GeneraDocumentoFe.GeneraDocumentoNotaDebitoXMLUBL21(mapaFacturacion, facturacion);
                } else if (mapaFacturacion.get("Servicio").toString().equals("B")
                        || mapaFacturacion.get("Servicio").toString().equals("E")
                        || mapaFacturacion.get("Servicio").toString().equals("C")) {
                    GeneraDocumentoFe.GeneraDocumentoFacturaXMLPruebaUBL21(mapaFacturacion, facturacion, mapvarios);
                }
                Utils.USERNAME = facturacion.getUsernameSunat();
                Utils.PASSWORD = facturacion.getPasswordSunat();
                log.info("credenciales " + Utils.USERNAME);
                log.info("credenciales " + Utils.PASSWORD);
                try {
                    FileDataSource dataSource = new FileDataSource(
                            facturacion.getRutaEnvioSunat() + mapaFacturacion.get("DocumentoZip").toString());
                    DataHandler dataHandler = new DataHandler(dataSource);
                    if (Properties.SEND_TO_SUNAT_PRODUCCION) {
                        log.info("ENVIANDO A SUNAT");
                        com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.BillService_Service serviceport = new com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.BillService_Service();
                        HeaderHandlerResolver headerResolver = new HeaderHandlerResolver();

                        serviceport.setHandlerResolver(headerResolver);
                        com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.BillService billService = serviceport.getBillServicePort();
                        respuestaSunat = billService.sendBill(mapaFacturacion.get("DocumentoZip").toString(), dataHandler, "");// FACTURA
                    } else {
                        log.info("ENVIANDO A BETA");
                        com.alo.digital.transportes.webapp.efact.webservice.sunat.beta.BillService_Service serviceport = new com.alo.digital.transportes.webapp.efact.webservice.sunat.beta.BillService_Service();
                        HeaderHandlerResolver headerResolver = new HeaderHandlerResolver();

                        serviceport.setHandlerResolver(headerResolver);
                        com.alo.digital.transportes.webapp.efact.webservice.sunat.beta.BillService billService = serviceport.getBillServicePort();
                        respuestaSunat = billService.sendBill(mapaFacturacion.get("DocumentoZip").toString(), dataHandler);// FACTURA
                    }


                    FileOutputStream fos = new FileOutputStream(facturacion.getRutaRespuestaSunat() + "R-"
                            + mapaFacturacion.get("DocumentoZip").toString());// FACTURA
                    fos.write(respuestaSunat);
                    fos.close();

                    ZipInputStream zisEnvio = new ZipInputStream(new FileInputStream(facturacion.getRutaRespuestaSunat() + "R-" + mapaFacturacion.get("DocumentoZip").toString()));

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

                            Document doc = documentBuilder.parse(inputSource);
                            NodeList nodeListhash = doc.getElementsByTagName("cbc:ResponseCode");
                            NodeList nodeListdescription = doc.getElementsByTagName("cbc:Description");
                            Node code = nodeListhash.item(0);
                            Node descripcion = nodeListdescription.item(0);

                            String message = "";
                            if (descripcion.getTextContent().trim().length() > 300) {
                                message = descripcion.getTextContent().trim().substring(0, 300);
                            } else {
                                message = descripcion.getTextContent().trim();
                            }

                            if (code instanceof Element) {
                                if (code.getTextContent().trim().equals("0")) {

                                    facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(
                                            Integer.parseInt(mapaFacturacion.get("Nro").toString()),
                                            mapaFacturacion.get("Servicio").toString(), 1, message);
                                } else {
                                    facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(
                                            Integer.parseInt(mapaFacturacion.get("Nro").toString()),
                                            mapaFacturacion.get("Servicio").toString(), 2, message);
                                }
                            }
                        }
                    }

                    zisEnvio.close();
                    mapaEnvio.put("respuesta", true);
                    mapaEnvio.put("mensaje", "Los documentos fueron enviados correctamente");
                    lstResultado.add(mapaEnvio);
                    mapaJSONResultado.put("rows", lstResultado);

                } catch (IOException io) {
                    System.out.println(
                            "EXCEPTION --- IOException" + mapaFacturacion.get("DocumentoElectronico").toString()
                                    + " Servicio :" + mapaFacturacion.get("ServicioD").toString() + " "
                                    + io.getMessage().substring(0, io.getMessage().length()));
                    System.out.println(
                            "EXCEPTION --- IOException " + mapaFacturacion.get("DocumentoElectronico").toString()
                                    + " Servicio : " + mapaFacturacion.get("ServicioD").toString() + " "
                                    + io.getMessage().substring(0, io.getMessage().length()));

                    mapaEnvio.put("respuesta", false);
                    mapaEnvio.put("documentoElectronico", mapaFacturacion.get("DocumentoElectronico").toString());
                    mapaEnvio.put("mensaje",
                            "Ocurrió un error al enviar los documentos:" + "\n" + "Documentos: "
                                    + mapaFacturacion.get("DocumentoElectronico").toString() + "\n"
                                    + Utils.printStackTraceToString(io));
                    lstResultado.add(mapaEnvio);
                    String message = "";
                    if (io.getMessage().length() > 300) {
                        message = io.getMessage().trim().substring(0, 300);
                    } else {
                        message = io.getMessage().trim();
                    }

                    facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(Integer.parseInt(mapaFacturacion.get("Nro").toString()), mapaFacturacion.get("Servicio").toString(), 2, message);
                    mapaJSONResultado.put("rows", lstResultado);

                } catch (ParserConfigurationException pa) {

                    System.out.println("EXCEPTION --- ParserConfigurationException"
                            + mapaFacturacion.get("DocumentoElectronico").toString() + " Servicio :"
                            + mapaFacturacion.get("ServicioD").toString() + " "
                            + pa.getMessage().substring(0, pa.getMessage().length()));
                    System.out.println("EXCEPTION --- ParserConfigurationException "
                            + mapaFacturacion.get("DocumentoElectronico").toString() + " Servicio : "
                            + mapaFacturacion.get("ServicioD").toString() + " "
                            + pa.getMessage().substring(0, pa.getMessage().length()));

                    mapaEnvio.put("respuesta", false);
                    mapaEnvio.put("documentoElectronico", mapaFacturacion.get("DocumentoElectronico").toString());
                    mapaEnvio.put("mensaje",
                            "Ocurrió un error al enviar los documentos:" + "\n" + "Documentos: "
                                    + mapaFacturacion.get("DocumentoElectronico").toString() + "\n"
                                    + Utils.printStackTraceToString(pa));
                    lstResultado.add(mapaEnvio);
                    String message = "";
                    if (pa.getMessage().length() > 300) {
                        message = pa.getMessage().trim().substring(0, 300);
                    } else {
                        message = pa.getMessage().trim();
                    }
                    facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(
                            Integer.parseInt(mapaFacturacion.get("Nro").toString()),
                            mapaFacturacion.get("Servicio").toString(), 2, message);
                    mapaJSONResultado.put("rows", lstResultado);
                    // return SUCCESS;
                } catch (SAXException sa) {
                    System.out.println(
                            "EXCEPTION --- SAXException" + mapaFacturacion.get("DocumentoElectronico").toString()
                                    + " Servicio :" + mapaFacturacion.get("ServicioD").toString() + " "
                                    + sa.getMessage().substring(0, sa.getMessage().length()));
                    System.out.println(
                            "EXCEPTION --- SAXException" + mapaFacturacion.get("DocumentoElectronico").toString()
                                    + " Servicio : " + mapaFacturacion.get("ServicioD").toString() + " "
                                    + sa.getMessage().substring(0, sa.getMessage().length()));
                    mapaEnvio.put("respuesta", false);
                    mapaEnvio.put("documentoElectronico", mapaFacturacion.get("DocumentoElectronico").toString());
                    mapaEnvio.put("mensaje",
                            "Ocurrió un error al enviar los documentos:" + "\n" + "Documentos: "
                                    + mapaFacturacion.get("DocumentoElectronico").toString() + "\n"
                                    + Utils.printStackTraceToString(sa));
                    lstResultado.add(mapaEnvio);
                    String message = "";
                    if (sa.getMessage().length() > 300) {
                        message = sa.getMessage().trim().substring(0, 300);
                    } else {
                        message = sa.getMessage().trim();
                    }

                    facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(
                            Integer.parseInt(mapaFacturacion.get("Nro").toString()),
                            mapaFacturacion.get("Servicio").toString(), 2, message);
                    mapaJSONResultado.put("rows", lstResultado);
                } /*catch (ServerSOAPFaultException se) {
                    System.out.println("EXCEPTION --- ServerSOAPFaultException "
                            + mapaFacturacion.get("DocumentoElectronico").toString() + " Servicio : "
                            + mapaFacturacion.get("ServicioD").toString() + " "
                            + se.getMessage().substring(0, se.getMessage().length()));
                    mapaEnvio.put("respuesta", false);
                    mapaEnvio.put("documentoElectronico", mapaFacturacion.get("DocumentoElectronico").toString());
                    mapaEnvio.put("mensaje",
                            "Ocurrió un error al enviar los documentos: " + "\n" + "Documentos: "
                                    + mapaFacturacion.get("DocumentoElectronico").toString() + "\n" + "Mensaje : "
                                    + Utils.printStackTraceToString(se));
                    lstResultado.add(mapaEnvio);
                    facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(
                            Integer.parseInt(mapaFacturacion.get("Nro").toString()),
                            mapaFacturacion.get("Servicio").toString(), 2);
                    mapaJSONResultado.put("rows", lstResultado);
                } */ catch (SOAPFaultException so) {
                    System.out.println(
                            "EXCEPTION --- SOAPFaultException " + mapaFacturacion.get("DocumentoElectronico").toString()
                                    + " Servicio : " + mapaFacturacion.get("ServicioD").toString() + " "
                                    + so.getMessage().substring(0, so.getMessage().length()));
                    mapaEnvio.put("respuesta", false);
                    mapaEnvio.put("documentoElectronico", mapaFacturacion.get("DocumentoElectronico").toString());
                    mapaEnvio.put("mensaje",
                            "Ocurrió un error al enviar los documentos : " + "\n" + "Documentos: "
                                    + mapaFacturacion.get("DocumentoElectronico").toString() + "\n" + "Codigo Error: "
                                    + (so.getMessage().substring(so.getMessage().lastIndexOf("INFO:") + 5,
                                    so.getMessage().lastIndexOf("INFO:") + 11)).trim()
                                    + "\n" + "Observaciones: "
                                    + (so.getMessage().substring(so.getMessage().lastIndexOf("server:") + 7,
                                    so.getMessage().lastIndexOf("-"))).trim()
                                    + " \n" + "Mensaje : "
                                    + (so.getMessage().substring(so.getMessage().lastIndexOf("INFO:") + 5,
                                    so.getMessage().lastIndexOf("INFO:") + 11)).trim());
                    lstResultado.add(mapaEnvio);
                    String message = "";
                    if (so.getMessage().length() > 300) {
                        message = so.getMessage().trim().substring(0, 300);
                    } else {
                        message = so.getMessage().trim();
                    }
                    facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(
                            Integer.parseInt(mapaFacturacion.get("Nro").toString()),
                            mapaFacturacion.get("Servicio").toString(), 2, message);
                    mapaJSONResultado.put("rows", lstResultado);
                }
            }

        } catch (Exception ex) {
            System.out.println("PADRE DE LA EXCEPTION ERROR " + mapaFacturacion.get("DocumentoElectronico").toString()
                    + " Servicio : " + mapaFacturacion.get("ServicioD").toString() + " "
                    + ex.getMessage().substring(0, ex.getMessage().length()));

            mapaEnvio.put("respuesta", false);
            mapaEnvio.put("documentoElectronico", mapaFacturacion.get("DocumentoElectronico").toString());
            mapaEnvio.put("mensaje",
                    "Ocurrió un error al enviar los documentos: " + "\n" + "Documentos: "
                            + mapaFacturacion.get("DocumentoElectronico").toString() + "\n" + "Mensaje : "
                            + Utils.printStackTraceToString(ex));

            lstResultado.add(mapaEnvio);
            String message = "";
            if (ex.getMessage().length() > 300) {
                message = ex.getMessage().trim().substring(0, 300);
            } else {
                message = ex.getMessage().trim();
            }
            facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(Integer.parseInt(mapaFacturacion.get("Nro").toString()),
                    mapaFacturacion.get("Servicio").toString(), 2, message);
            mapaJSONResultado.put("rows", lstResultado);
        }
        rpta.put("mapaJSONResultado", mapaJSONResultado);
        return rpta;
    }

}
