
package com.alo.digital.transportes.webapp.efact.webservice.sunat.prd;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10
 * Generated source version: 2.2
 * 
 */
@WebService(name = "billService", targetNamespace = "http://service.sunat.gob.pe")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface BillService {


    /**
     * 
     * @param ticket
     * @return
     *     returns pe.gob.sunat.service.StatusResponse
     */
    @WebMethod(action = "urn:getStatus")
    @WebResult(name = "status", targetNamespace = "")
    @RequestWrapper(localName = "getStatus", targetNamespace = "http://service.sunat.gob.pe", className = "com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.GetStatus")
    @ResponseWrapper(localName = "getStatusResponse", targetNamespace = "http://service.sunat.gob.pe", className = "com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.GetStatusResponse")
    public StatusResponse getStatus(
        @WebParam(name = "ticket", targetNamespace = "")
        String ticket);

    /**
     * 
     * @param fileName
     * @param contentFile
     * @param partyType
     * @return
     *     returns byte[]
     */
    @WebMethod(action = "urn:sendBill")
    @WebResult(name = "applicationResponse", targetNamespace = "")
    @RequestWrapper(localName = "sendBill", targetNamespace = "http://service.sunat.gob.pe", className = "com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.SendBill")
    @ResponseWrapper(localName = "sendBillResponse", targetNamespace = "http://service.sunat.gob.pe", className = "com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.SendBillResponse")
    public byte[] sendBill(
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName,
        @WebParam(name = "contentFile", targetNamespace = "")
        DataHandler contentFile,
        @WebParam(name = "partyType", targetNamespace = "")
        String partyType);

    /**
     * 
     * @param fileName
     * @param contentFile
     * @param partyType
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:sendPack")
    @WebResult(name = "ticket", targetNamespace = "")
    @RequestWrapper(localName = "sendPack", targetNamespace = "http://service.sunat.gob.pe", className = "com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.SendPack")
    @ResponseWrapper(localName = "sendPackResponse", targetNamespace = "http://service.sunat.gob.pe", className = "com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.SendPackResponse")
    public String sendPack(
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName,
        @WebParam(name = "contentFile", targetNamespace = "")
        DataHandler contentFile,
        @WebParam(name = "partyType", targetNamespace = "")
        String partyType);

    /**
     * 
     * @param fileName
     * @param contentFile
     * @param partyType
     * @return
     *     returns java.lang.String
     */
    @WebMethod(action = "urn:sendSummary")
    @WebResult(name = "ticket", targetNamespace = "")
    @RequestWrapper(localName = "sendSummary", targetNamespace = "http://service.sunat.gob.pe", className = "com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.SendSummary")
    @ResponseWrapper(localName = "sendSummaryResponse", targetNamespace = "http://service.sunat.gob.pe", className = "com.alo.digital.transportes.webapp.efact.webservice.sunat.prd.SendSummaryResponse")
    public String sendSummary(
        @WebParam(name = "fileName", targetNamespace = "")
        String fileName,
        @WebParam(name = "contentFile", targetNamespace = "")
        DataHandler contentFile,
        @WebParam(name = "partyType", targetNamespace = "")
        String partyType);

}
