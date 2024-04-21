package com.alo.digital.transportes.webapp.efact.controllers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alo.digital.transportes.webapp.efact.beans.V_Varios_FacturacionBean;
import com.alo.digital.transportes.webapp.efact.dao.FacturacionDAO;
import com.alo.digital.transportes.webapp.efact.dao.Varios_FacturacionDAO;
import com.alo.digital.transportes.webapp.efact.util.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.microsoft.windowsazure.exception.ServiceException;

//import pe.com.envialo.consultas.beta.service.BillService;
//import pe.com.envialo.consultas.beta.service.BillService_Service;
//import pe.com.grupopalomino.sistema.boletaje.service.FacturacionService;
//import pe.com.grupopalomino.sistema.boletaje.service.FacturacionServiceI;
//import pe.com.grupopalomino.sistema.boletaje.bean.B_FacturacionBean;
//import pe.com.grupopalomino.sistema.boletaje.bean.V_Varios_FacturacionBean; 

//import pe.com.grupopalomino.sistema.boletaje.util.Utils;

@Controller
@PreAuthorize("hasRole('ROLE_1') or hasRole('ROLE_T')")
@RequestMapping("consultaenvio")
public class ConsultaElectronicaController {

    private static final Log log = LogFactory.getLog(ConsultaElectronicaController.class);
    @Autowired
    FacturacionDAO facturacionservice;

    @Autowired
    Varios_FacturacionDAO variosservice;

    @GetMapping("/consultadocumentos")
    public String linkConsultaElectronica() {
        return "/fragments/facturacion/consulta";
    }

    @GetMapping("estadodocumentossunat")
    @ResponseBody
    public Map<String, Object> estadodocumentossunat(@RequestParam("empresa") String empresa,
                                                     @RequestParam("fechaemision") String fechaemision, @RequestParam("documentos") int documentos) {

        Map<String, Object> rpta = new HashMap<>();
        Map<String, Object> mapaJSONResultado = new HashMap<>();

        List<Map<String, Object>> lstResultado = new ArrayList<>();
        Map<String, Object> mapaEnvio = new HashMap<>();
        Map<String, Object> mapaFacturacion = new HashMap<>();

        try {

            V_Varios_FacturacionBean facturacion = variosservice.SQL_SELECT_LISTA_PARAMETROS_FACTURADOR(empresa);

            List<Map<String, Object>> ventas = facturacionservice.SQL_SELECT_VENTA_ENVIO_FACTURADOR(facturacion.getEmpresa(), Utils.FormatoFecha(fechaemision), documentos, 0, 0);

            for (Map<String, Object> map : ventas) {
                mapaFacturacion = facturacionservice.SQL_SELECT_VENTA_FACTURADOR(Integer.parseInt(map.get("Nro").toString()), map.get("Servicio").toString());
                //Cambio de estado a por Enviar
                facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(Integer.parseInt(mapaFacturacion.get("Nro").toString()), mapaFacturacion.get("Servicio").toString(), 0, null);
				
				/*try {

					mapaFacturacion = facturacionservice.SQL_SELECT_VENTA_FACTURADOR(Integer.parseInt(map.get("Nro").toString()), map.get("Servicio").toString());

					BillConsultService serviceport = new BillConsultService();
					HeaderHandlerResolver headerResolver = new HeaderHandlerResolver();

					Utils.USERNAME = facturacion.getUsernameSunat();
					Utils.PASSWORD = facturacion.getPasswordSunat();

					serviceport.setHandlerResolver(headerResolver);
					BillService billService = serviceport.getBillConsultServicePort();

					StatusResponse response = billService.getStatus(facturacion.getRuc(),
							mapaFacturacion.get("TipoDocumento").toString(),
							mapaFacturacion.get("SerieElectronica").toString(),
							Integer.parseInt(mapaFacturacion.get("Numero").toString()));

					// pe.com.grupopalomino.sunat.consultas.produccion.service.StatusResponse
					// response = billService.getStatus("20417931393", "03",
					// "B647",Integer.parseInt("0004024"));

					if (response.getStatusCode().trim().equals("0001")) {

						StatusResponse responseCDR = billService.getStatusCdr(facturacion.getRuc(),
								mapaFacturacion.get("TipoDocumento").toString(),
								mapaFacturacion.get("SerieElectronica").toString(),
								Integer.parseInt(mapaFacturacion.get("Numero").toString()));

						FileOutputStream fos = new FileOutputStream(facturacion.getRutaRespuestaSunat() + "R-"
								+ mapaFacturacion.get("DocumentoZip").toString());
						fos.write(responseCDR.getContent());
						fos.close();

						facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(
								Integer.parseInt(mapaFacturacion.get("Nro").toString()),
								mapaFacturacion.get("Servicio").toString(), 1);

					} else {

						log.info("DOCUMENTO RECHAZADO --> DOCUMENTO : "
								+ mapaFacturacion.get("DocumentoElectronico").toString() + " SERVICIO :"
								+ mapaFacturacion.get("ServicioD").toString() + " ESTADO : " + response.getStatusCode()
								+ " MENSAJE : " + response.getStatusMessage());

						facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(
								Integer.parseInt(mapaFacturacion.get("Nro").toString()),
								mapaFacturacion.get("Servicio").toString(),
								(response.getStatusCode().equals("0011") ? 0 : 2));

						// facturacionerrorervice.SQL_UPDATE_VENTAS_FACTURACION_ERROR(Integer.parseInt(mapaFacturacion.get("Nro").toString()),
						// mapaFacturacion.get("Servicio").toString(), response.getStatusCode());

					}

				} catch (Exception e) {
					log.info("ERROR EN EL SERVICIO DE CONSULTAS SUNAT --> "
							+ mapaFacturacion.get("DocumentoElectronico").toString() + "-"
							+ mapaFacturacion.get("ServicioD").toString() + " " + e.getMessage());
					mapaEnvio.put("respuesta", false);
					mapaEnvio.put("mensaje",
							"Ocurrio un problema al verificar los documentos, por favor vuelva a consultar los documentos.");
					lstResultado.add(mapaEnvio);
					mapaJSONResultado.put("rows", lstResultado);

					facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(
							Integer.parseInt(mapaFacturacion.get("Nro").toString()),
							mapaFacturacion.get("Servicio").toString(), 2);

				}*/
            }

            mapaEnvio.put("respuesta", true);
            mapaEnvio.put("mensaje", "Los documentos fueron enviados correctamente");
            lstResultado.add(mapaEnvio);
            mapaJSONResultado.put("rows", lstResultado);

        } catch (Exception e) {
            log.info("ERROR EN EL SERVICIO DE CONSULTAS SUNAT Exception --> "
                    + mapaFacturacion.get("DocumentoElectronico").toString() + "-"
                    + mapaFacturacion.get("ServicioD").toString() + " " + e.getMessage());
            mapaEnvio.put("respuesta", false);
            mapaEnvio.put("mensaje",
                    "Ocurrio un problema al verificar los documentos, por favor vuelva a consultar los documentos.");
            lstResultado.add(mapaEnvio);
            mapaJSONResultado.put("rows", lstResultado);

            facturacionservice.SQL_UPDATE_VENTA_FACTURADOR(Integer.parseInt(mapaFacturacion.get("Nro").toString()),
                    mapaFacturacion.get("Servicio").toString(), 2, "");
        }

        rpta.put("mapaJSONResultado", mapaJSONResultado);
        return rpta;
    }
}
