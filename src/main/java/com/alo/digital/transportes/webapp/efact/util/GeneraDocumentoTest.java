package com.alo.digital.transportes.webapp.efact.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.alo.digital.transportes.webapp.efact.beans.V_Varios_FacturacionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class GeneraDocumentoTest {



    private static final String KEYSTORE_TYPE = "JKS";

    /**CODIGO DE MONTO EN LETRAS (CATALOGO 15 - sac:AdditionalProperty/cbc:ID)**/
    public static final String CODIGO_MONTO_LETRAS = "1000";
    /**CODIGO TIPO DE TRIBUTO (CATALOGO 5 - cac:TaxScheme/cbc:ID)**/
    public static final String CODIGO_TIPO_TRIBUTO = "1000";
    /**CODIGO TIPO DE TRIBUTO (CATALOGO 5 - cac:TaxScheme/cbc:Name)**/
    public static final String NOMBRE_TRIBUTO = "IGV";
    /**CODIGO TIPO DE TRIBUTO (CATALOGO 5 - cac:TaxScheme/cbc:TaxTypeCode)**/
    public static final String CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
    //public static final  Charset UTF8_CHARSET = Charset.forName("UTF-8");

    private static final Log log = LogFactory.getLog(GeneraDocumentoFe.class);

    public static Map<String, Object> DocumentoFacturaXML(Map<String, Object> map, V_Varios_FacturacionBean empresa) {


        Map<String, Object> respuesta = new HashMap<>();

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();

            Element rootElementInvoice = doc.createElement("Invoice");
            doc.appendChild(rootElementInvoice);

            // GENERANDO ATRIBUTOS INVOICE
            Attr attrInvoice = doc.createAttribute("xmlns");
            attrInvoice.setValue("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2");
            rootElementInvoice.setAttributeNode(attrInvoice);

            Attr attrInvoiceCac = doc.createAttribute("xmlns:cac");
            attrInvoiceCac.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");
            rootElementInvoice.setAttributeNode(attrInvoiceCac);

            Attr attrInvoiceCbc = doc.createAttribute("xmlns:cbc");
            attrInvoiceCbc.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
            rootElementInvoice.setAttributeNode(attrInvoiceCbc);

            Attr attrInvoiceCcts = doc.createAttribute("xmlns:ccts");
            attrInvoiceCcts.setValue("urn:un:unece:uncefact:documentation:2");
            rootElementInvoice.setAttributeNode(attrInvoiceCcts);

            Attr attrInvoiceDs = doc.createAttribute("xmlns:ds");
            attrInvoiceDs.setValue("http://www.w3.org/2000/09/xmldsig#");
            rootElementInvoice.setAttributeNode(attrInvoiceDs);

            Attr attrInvoiceExt = doc.createAttribute("xmlns:ext");
            attrInvoiceExt.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2");
            rootElementInvoice.setAttributeNode(attrInvoiceExt);

            Attr attrInvoiceQdt = doc.createAttribute("xmlns:qdt");
            attrInvoiceQdt.setValue("urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2");
            rootElementInvoice.setAttributeNode(attrInvoiceQdt);

            Attr attrInvoiceSac = doc.createAttribute("xmlns:sac");
            attrInvoiceSac.setValue("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1");
            rootElementInvoice.setAttributeNode(attrInvoiceSac);

            Attr attrInvoiceUdt = doc.createAttribute("xmlns:udt");
            attrInvoiceUdt.setValue("urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2");
            rootElementInvoice.setAttributeNode(attrInvoiceUdt);

            Attr attrInvoiceXsi = doc.createAttribute("xmlns:xsi");
            attrInvoiceXsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
            rootElementInvoice.setAttributeNode(attrInvoiceXsi);

            //Extensions XML
            Element ublExtensions = doc.createElement("ext:UBLExtensions");
            rootElementInvoice.appendChild(ublExtensions);

            Element ublExtension = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtension);

            Element extExtensionContent = doc.createElement("ext:ExtensionContent");
            ublExtension.appendChild(extExtensionContent);

            Element sacAdditionalInformation = doc.createElement("sac:AdditionalInformation");
            extExtensionContent.appendChild(sacAdditionalInformation);


            // ******************************  VERIFICAR CON LA SUNAT SI SON MANDATORIOS ESTOS CAMPOS DE TOTALES *******************************************************************

            // TOTAL DE VENTA OPERACIONES GRAVADAS (MANDATORIO)
            Element sacAdditionalMonetaryTotalGravadas = doc.createElement("sac:AdditionalMonetaryTotal");
            sacAdditionalInformation.appendChild(sacAdditionalMonetaryTotalGravadas);

            Element cbcIDGravadas = doc.createElement("cbc:ID");
            cbcIDGravadas.appendChild(doc.createTextNode(map.get("CodigoTotalVenta").toString() /*venta.getCodigoTotalVenta()*/));// CODIGO 1003 ES PARA PASAJES - CODIGO PARA ENCOMIENDAS ES 1001
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcIDGravadas);

            Element cbcPayableAmountGravadas = doc.createElement("cbc:PayableAmount");
            cbcPayableAmountGravadas.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV")/* venta.getTotalSinIGV()*/))); // "0.00" /// VERIFICAR ES TOTAL  SIN IGV
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcPayableAmountGravadas);

            Attr attrGravadas = doc.createAttribute("currencyID");
            attrGravadas.setValue(map.get("Moneda").toString());
            cbcPayableAmountGravadas.setAttributeNode(attrGravadas);

            // TOTAL DE VENTA MONTO POR VENTA (EN LETRAS)
            Element sacAdditionalProperty = doc.createElement("sac:AdditionalProperty");
            sacAdditionalInformation.appendChild(sacAdditionalProperty);

            Element cbcIDProperty = doc.createElement("cbc:ID");
            cbcIDProperty.appendChild(doc.createTextNode(CODIGO_MONTO_LETRAS));
            sacAdditionalProperty.appendChild(cbcIDProperty);

            Element cbcValue = doc.createElement("cbc:Value");
            cbcValue.appendChild(doc.createTextNode(map.get("MontoLetras").toString()/* venta.getMontoLetras()*/));// "CUATROCIENTOS VEINTITRES MIL DOSCIENTOS VEINTICINCO Y 00/100"
            sacAdditionalProperty.appendChild(cbcValue);

            // EXTENSION PARA LA FIRMA DIGITAL

            Element ublExtensionSignature = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtensionSignature);

            Text texto = doc.createTextNode(" ");
            Element ublExtensionContentSignature = doc.createElement("ext:ExtensionContent");
            ublExtensionContentSignature.appendChild(texto);
            ublExtensionSignature.appendChild(ublExtensionContentSignature);

            //********************************************************************************

            // VERSION DE UBL (2.0)
            Element ublVersionID = doc.createElement("cbc:UBLVersionID");
            ublVersionID.appendChild(doc.createTextNode("2.0"));
            rootElementInvoice.appendChild(ublVersionID);

            // VERSION DE LA ESTRUCTURA DEL DOCUMENTO (VER PAG. 57)
            Element cbcCustomizationID = doc.createElement("cbc:CustomizationID");
            cbcCustomizationID.appendChild(doc.createTextNode("1.0"));
            rootElementInvoice.appendChild(cbcCustomizationID);

            // NUMERO DEL DOCUMENTO (SERIE Y NUMERO)
            Element cbcID = doc.createElement("cbc:ID");
            cbcID.appendChild(doc.createTextNode(map.get("DocumentoElectronico").toString() /*venta.getDocumentoElectronico()*/)); // "F703-0000689"
            rootElementInvoice.appendChild(cbcID);

            // FECHA DE EMISION
            Element cbcIssueDate = doc.createElement("cbc:IssueDate");
            cbcIssueDate.appendChild(doc.createTextNode(map.get("FechaEmision").toString()/*venta.getFechaEmision()*/));// "2017-01-01"
            rootElementInvoice.appendChild(cbcIssueDate);

            // TIPO DE DOCUMENTO (01 FACTURA ...)
            Element cbcInvoiceTypeCode = doc.createElement("cbc:InvoiceTypeCode");
            cbcInvoiceTypeCode.appendChild(doc.createTextNode(map.get("TipoDocumento").toString()/*venta.getTipoDocumento()*/));  // 01
            rootElementInvoice.appendChild(cbcInvoiceTypeCode);

            // CODIGO DE MONEDA (CATALOGO NÂ°. 2)
            Element cbcDocumentCurrencyCode = doc.createElement("cbc:DocumentCurrencyCode");
            cbcDocumentCurrencyCode.appendChild(doc.createTextNode(map.get("Moneda").toString()));
            rootElementInvoice.appendChild(cbcDocumentCurrencyCode);

            // CAC SIGNATURE
            Element cacSignature = doc.createElement("cac:Signature");
            rootElementInvoice.appendChild(cacSignature);

            Element cacSignatureID = doc.createElement("cbc:ID");
            cacSignatureID.appendChild(doc.createTextNode("IDSignKG"));
            cacSignature.appendChild(cacSignatureID);

            Element cacSignatoryParty = doc.createElement("cac:SignatoryParty");
            cacSignature.appendChild(cacSignatoryParty);

            Element cacPartyIdentification = doc.createElement("cac:PartyIdentification");
            cacSignatoryParty.appendChild(cacPartyIdentification);

            // RUC DE LA EMPRESA EMISORA
            Element cacPartyIdentificationID = doc.createElement("cbc:ID");
            cacPartyIdentificationID.appendChild(doc.createTextNode(empresa.getRuc())); // "20273841700"
            cacPartyIdentification.appendChild(cacPartyIdentificationID);

            Element cacPartySignatureName = doc.createElement("cac:PartyName");
            cacSignatoryParty.appendChild(cacPartySignatureName);

            // RAZON SOCIAL DE LA EMPRESA EMISORA
            Element cbcSignatureName = doc.createElement("cbc:Name");
            cbcSignatureName.appendChild(doc.createCDATASection(empresa.getRazon())); // "ROMELIZA S.A.C."
            cacPartySignatureName.appendChild(cbcSignatureName);

            Element cacDigitalSignatureAttachment = doc.createElement("cac:DigitalSignatureAttachment");
            cacSignature.appendChild(cacDigitalSignatureAttachment);

            Element cacExternalReference = doc.createElement("cac:ExternalReference");
            cacDigitalSignatureAttachment.appendChild(cacExternalReference);

            Element cbcURI = doc.createElement("cbc:URI");
            cbcURI.appendChild(doc.createTextNode("#signatureKG"));
            cacExternalReference.appendChild(cbcURI);


            // *************************************************************
            // DATOS DEL EMISOR DEL DOCUMENTO
            Element cacAccountingSupplierParty = doc.createElement("cac:AccountingSupplierParty");
            rootElementInvoice.appendChild(cacAccountingSupplierParty);

            // NUMERO DE DOCUMENTO DE IDENTIDAD (RUC) -- EMISOR
            Element cbcCustomerAssignedAccountID = doc.createElement("cbc:CustomerAssignedAccountID");
            cbcCustomerAssignedAccountID.appendChild(doc.createTextNode(empresa.getRuc())); // RUC EMISORA (EJEMPLO)
            cacAccountingSupplierParty.appendChild(cbcCustomerAssignedAccountID);

            // TIPO DE DOCUMENTO DE IDENTIFICACION -- EMISOR
            Element cbcAdditionalAccountID = doc.createElement("cbc:AdditionalAccountID");
            cbcAdditionalAccountID.appendChild(doc.createTextNode("6"));
            cacAccountingSupplierParty.appendChild(cbcAdditionalAccountID);

            // TIPO DE DOCUMENTO DE IDENTIFICACION
            Element cacParty = doc.createElement("cac:Party");
            cacAccountingSupplierParty.appendChild(cacParty);

            Element cacPartyName = doc.createElement("cac:PartyName");
            cacParty.appendChild(cacPartyName);

            Element cbcName = doc.createElement("cbc:Name");
            cbcName.appendChild(doc.createCDATASection(empresa.getRazon())); // "ROMELIZA S.A.C."
            cacPartyName.appendChild(cbcName);

            // CODIGO POSTAL (UBIGEO)
            Element cacPostalAddress = doc.createElement("cac:PostalAddress");
            cacParty.appendChild(cacPostalAddress);

            Element cbcPostalAddressID = doc.createElement("cbc:ID");
            cbcPostalAddressID.appendChild(doc.createTextNode(empresa.getUbigeo()));
            cacPostalAddress.appendChild(cbcPostalAddressID);

            // DIRECCION POSTAL
            Element cbcStreetName = doc.createElement("cbc:StreetName");
            cbcStreetName.appendChild(doc.createTextNode(empresa.getDireccion())); // "AV. LUNA PIZARRO NRO. 343 - 353 - LA VICTORIA - LIMA"
            cacPostalAddress.appendChild(cbcStreetName);

            // CODIGO DE PAIS
            Element cbcCountry = doc.createElement("cac:Country");
            cacPostalAddress.appendChild(cbcCountry);

            Element cbcIdentificationCode = doc.createElement("cbc:IdentificationCode");
            cbcIdentificationCode.appendChild(doc.createTextNode(empresa.getCodigoPais())); // PE
            cbcCountry.appendChild(cbcIdentificationCode);

            // DATOS DEL EMISOR
            Element cacPartyLegalEntity = doc.createElement("cac:PartyLegalEntity");
            cacParty.appendChild(cacPartyLegalEntity);


            // DATOS DEL EMISOR (DESCRIPCION)
            Element cbcRegistrationName = doc.createElement("cbc:RegistrationName");
            cbcRegistrationName.appendChild(doc.createCDATASection(empresa.getRazon())); // "ROMELIZA S.A.C."
            cacPartyLegalEntity.appendChild(cbcRegistrationName);

            // *************************************************************
            // DATOS DEL RECEPTOR
            Element cacAccountingCustomerParty = doc.createElement("cac:AccountingCustomerParty");
            rootElementInvoice.appendChild(cacAccountingCustomerParty);

            Element cbcCustomerAssignedAccountIDReceptor = doc.createElement("cbc:CustomerAssignedAccountID");
            cbcCustomerAssignedAccountIDReceptor.appendChild(doc.createTextNode((map.get("TipoDocumento").toString().trim().equals("01")? map.get("Ruc").toString() :map.get("DNI").toString()))); //"20341198217"
            //cbcCustomerAssignedAccountIDReceptor.appendChild(doc.createTextNode((venta.getTipoDocumento().trim().equals("01")? venta.getRuc():venta.getDNI()))); //"20341198217"
            cacAccountingCustomerParty.appendChild(cbcCustomerAssignedAccountIDReceptor);

            Element cbcAdditionalAccountIDReceptor = doc.createElement("cbc:AdditionalAccountID");
            cbcAdditionalAccountIDReceptor.appendChild(doc.createTextNode(map.get("TipoDocumentoReceptor").toString()/*venta.getTipoDocumentoReceptor()*/)); //"6"
            cacAccountingCustomerParty.appendChild(cbcAdditionalAccountIDReceptor);

            Element cacPartyReceptor = doc.createElement("cac:Party");
            cacAccountingCustomerParty.appendChild(cacPartyReceptor);

            Element cacPartyLegalEntityReceptor = doc.createElement("cac:PartyLegalEntity");
            cacPartyReceptor.appendChild(cacPartyLegalEntityReceptor);

            Element cbcRegistrationNameReceptor = doc.createElement("cbc:RegistrationName");
            cbcRegistrationNameReceptor.appendChild(doc.createCDATASection(( map.get("TipoDocumento").toString().trim().equals("01")? map.get("Razon").toString():map.get("Nombre").toString())));//"VISANET - CIA PERUANA DE MEDIOS DE PAGO SAC"
            //cbcRegistrationNameReceptor.appendChild(doc.createCDATASection((venta.getTipoDocumento().trim().equals("01")? venta.getRazon():venta.getNombre())));//"VISANET - CIA PERUANA DE MEDIOS DE PAGO SAC"
            cacPartyLegalEntityReceptor.appendChild(cbcRegistrationNameReceptor);

            // *******************************************************************************************
            // INFORMACION DE SUMATORIA IVG GLOBAL

            Element cacTaxTotalGlobal = doc.createElement("cac:TaxTotal");
            rootElementInvoice.appendChild(cacTaxTotalGlobal);

            Element cbcTaxAmountGlobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA  DEL IGV TOTAL (PARA PASAJES 0)
            cacTaxTotalGlobal.appendChild(cbcTaxAmountGlobal);

            Attr attrTaxAmountGlobal = doc.createAttribute("currencyID");
            attrTaxAmountGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountGlobal.setAttributeNode(attrTaxAmountGlobal);

            Element cacTaxSubtotalGlobal = doc.createElement("cac:TaxSubtotal");
            cacTaxTotalGlobal.appendChild(cacTaxSubtotalGlobal);

            Element cbcTaxAmountSubtotalGblobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotalGblobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotalGlobal.appendChild(cbcTaxAmountSubtotalGblobal);

            Attr attrTaxAmountSubtotalGlobal = doc.createAttribute("currencyID");
            attrTaxAmountSubtotalGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotalGblobal.setAttributeNode(attrTaxAmountSubtotalGlobal);

            Element cacTaxtCategoryGlobal = doc.createElement("cac:TaxCategory");
            cacTaxSubtotalGlobal.appendChild(cacTaxtCategoryGlobal);

            Element cbcTaxExemptionReasonCodeGlobal = doc.createElement("cbc:TaxExemptionReasonCode");
            cbcTaxExemptionReasonCodeGlobal.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR CATALOGO Nï¿½ 7 ( CODIGO 20 PARA PASAJES - CODIGO 10 PARA ENCOMIENDAS)
            cacTaxtCategoryGlobal.appendChild(cbcTaxExemptionReasonCodeGlobal);

            Element cacTaxSchemeGlobal = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobal.appendChild(cacTaxSchemeGlobal);

            Element cacTaxSchemeIDGlobal = doc.createElement("cbc:ID");
            cacTaxSchemeIDGlobal.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cacTaxSchemeIDGlobal);

            Element cbcTaxSchemeNameGlobal = doc.createElement("cbc:Name");
            cbcTaxSchemeNameGlobal.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxSchemeNameGlobal);

            Element cbcTaxTypeCodeGlobal = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCodeGlobal.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxTypeCodeGlobal);

            // *************************************************************
            // IMPORTE TOTAL DE LA VENTA CESION EN USO O DEL SERVICIO PRESTADO

            Element cacLegalMonetaryTotal = doc.createElement("cac:LegalMonetaryTotal");
            rootElementInvoice.appendChild(cacLegalMonetaryTotal);

            Element cbcPayableAmount = doc.createElement("cbc:PayableAmount");
            cbcPayableAmount.appendChild(doc.createTextNode(String.valueOf(map.get("Total")))); // "150.00"
            cacLegalMonetaryTotal.appendChild(cbcPayableAmount);

            Attr attrMonetaryTotal = doc.createAttribute("currencyID");
            attrMonetaryTotal.setValue(map.get("Moneda").toString());
            cbcPayableAmount.setAttributeNode(attrMonetaryTotal);


            // *************************************************************
            // INFORMACIï¿½N DEL ARTICULO
            Element cacInvoiceLine = doc.createElement("cac:InvoiceLine");
            rootElementInvoice.appendChild(cacInvoiceLine);

            Element cbcIDInvoiceLine = doc.createElement("cbc:ID");
            cbcIDInvoiceLine.appendChild(doc.createTextNode("1"));// SIMPRE VA SER 1 (YA QUE ES UN SOLO ITEM)
            cacInvoiceLine.appendChild(cbcIDInvoiceLine);

            // CANTIDA DE ARTICULOS
            Element cbcInvoicedQuantity = doc.createElement("cbc:InvoicedQuantity");
            cbcInvoicedQuantity.appendChild(doc.createTextNode("1"));// SIMPRE VA SER 1 (YA QUE ES UN SOLO ITEM)
            cacInvoiceLine.appendChild(cbcInvoicedQuantity);

            Attr attrInvoicedQuantity = doc.createAttribute("unitCode");
            attrInvoicedQuantity.setValue(empresa.getCodigoUnidadMedida()); // CODIGO DE UNIDAD DE MEDIDA
            cbcInvoicedQuantity.setAttributeNode(attrInvoicedQuantity);

            // VALOR DE VENTA DEL ITEM
            Element cbcLineExtensionAmount = doc.createElement("cbc:LineExtensionAmount");
            cbcLineExtensionAmount.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV")/*venta.getTotalSinIGV()*/)));
            cacInvoiceLine.appendChild(cbcLineExtensionAmount);

            Attr attrLineExtensionAmount = doc.createAttribute("currencyID");
            attrLineExtensionAmount.setValue(map.get("Moneda").toString());
            cbcLineExtensionAmount.setAttributeNode(attrLineExtensionAmount);

            // PRECIO UNITARIO O VALOR REFERENCIAL UNITARIO EN OPERACIONES NO ONEROSAS
            Element cacPricingReference = doc.createElement("cac:PricingReference");
            //cacPricingReference.appendChild(doc.createTextNode("100"));
            cacInvoiceLine.appendChild(cacPricingReference);

            Element cacAlternativeConditionPrice = doc.createElement("cac:AlternativeConditionPrice");
            //cacPricingReference.appendChild(doc.createTextNode("100"));
            cacPricingReference.appendChild(cacAlternativeConditionPrice);

            Element cbcPriceAmount = doc.createElement("cbc:PriceAmount");
            cbcPriceAmount.appendChild(doc.createTextNode(String.valueOf(map.get("Total")/*venta.getTotal()*/)));
            cacAlternativeConditionPrice.appendChild(cbcPriceAmount);

            Attr attrPriceAmount = doc.createAttribute("currencyID");
            attrPriceAmount.setValue(map.get("Moneda").toString());
            cbcPriceAmount.setAttributeNode(attrPriceAmount);

            ////REGUNTAR QUE CODIGO VA IR  !!!!!!   2017-11-26
            Element cbcPriceTypeCode = doc.createElement("cbc:PriceTypeCode");
            cbcPriceTypeCode.appendChild(doc.createTextNode("01"));// CATALOGO Nï¿½ 16  (01- Precio unitario (incluye el IGV)) (02 - Valor referencial unitario en operaciones no onerosas)
            cacAlternativeConditionPrice.appendChild(cbcPriceTypeCode);


            // AFECTACION AL IGV POR ITEM
            Element cacTaxTotal = doc.createElement("cac:TaxTotal");
            cacInvoiceLine.appendChild(cacTaxTotal);

            Element cbcTaxAmount = doc.createElement("cbc:TaxAmount");
            cbcTaxAmount.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")/*venta.getIGV()*/)));/// MONTO DEL IGV (PARA PASAJES 0)
            cacTaxTotal.appendChild(cbcTaxAmount);

            Attr attrTaxAmount = doc.createAttribute("currencyID");
            attrTaxAmount.setValue(map.get("Moneda").toString());
            cbcTaxAmount.setAttributeNode(attrTaxAmount);

            Element cacTaxSubtotal = doc.createElement("cac:TaxSubtotal");
            cacTaxTotal.appendChild(cacTaxSubtotal);

            Element cbcTaxAmountSubtotal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")/*venta.getIGV()*/))); // "100"
            cacTaxSubtotal.appendChild(cbcTaxAmountSubtotal);

            Attr attrTaxAmountSubtotal = doc.createAttribute("currencyID");
            attrTaxAmountSubtotal.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotal.setAttributeNode(attrTaxAmountSubtotal);

            Element cacTaxtCategory = doc.createElement("cac:TaxCategory");
            //cacTaxtCategory.appendChild(doc.createTextNode("100"));
            cacTaxSubtotal.appendChild(cacTaxtCategory);


            Element cbcTaxExemptionReasonCode = doc.createElement("cbc:TaxExemptionReasonCode");
            cbcTaxExemptionReasonCode.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()/* venta.getCodigoAfectacionIGV()*/));// VERIFICAR CATALOGO Nï¿½ 7 ( CODIGO 20 PARA PASAJES - CODIGO 10 PARA ENCOMIENDAS)
            cacTaxtCategory.appendChild(cbcTaxExemptionReasonCode);

            Element cacTaxScheme = doc.createElement("cac:TaxScheme");
            cacTaxtCategory.appendChild(cacTaxScheme);

            Element cacTaxSchemeID = doc.createElement("cbc:ID");
            cacTaxSchemeID.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxScheme.appendChild(cacTaxSchemeID);

            Element cbcTaxSchemeName = doc.createElement("cbc:Name");
            cbcTaxSchemeName.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxScheme.appendChild(cbcTaxSchemeName);

            Element cbcTaxTypeCode = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCode.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxScheme.appendChild(cbcTaxTypeCode);


            //DESCRIPCION DEL ITEM
            Element cacItem = doc.createElement("cac:Item");
            cacInvoiceLine.appendChild(cacItem);

            Element cbcDescription = doc.createElement("cbc:Description");



            if (map.get("Servicio").toString().trim().equals("B")){
                cbcDescription.appendChild(doc.createCDATASection("SERVICIO DE TRANSPORTE EN LA RUTA: "+ map.get("DestinoD") /*venta.getDestinoD()*/));
                cacItem.appendChild(cbcDescription);
            }else {

                String Descripcion = "";
                if(!map.get("Cantidad1").toString().trim().equals("") && !map.get("Descripcion1").toString().trim().equals("")) {

                    Descripcion =  map.get("Cantidad1").toString().trim() +" " + map.get("Descripcion1").toString().trim();
                }
                if(!map.get("Cantidad2").toString().trim().equals("") && !map.get("Descripcion2").toString().trim().equals("")) {

                    Descripcion = Descripcion +"/"+ map.get("Cantidad2").toString().trim() +" "+ map.get("Descripcion2").toString().trim();
                }
                if(!map.get("Cantidad3").toString().trim().equals("") && !map.get("Descripcion3").toString().trim().equals("")) {

                    Descripcion = Descripcion+"/" + map.get("Cantidad3").toString().trim() +" "+ map.get("Descripcion3").toString().trim();
                }

                System.out.println("VALORES DE DESCRIPCION FINALL!!!!!!!---- "+ Descripcion);

                cbcDescription.appendChild(doc.createCDATASection("SERVICIO DE ENVIO DE ENCOMIENDA: "+ Descripcion));
                cacItem.appendChild(cbcDescription);
            }

            //VALOR UNITARIO POR ITEM
            Element cacPrice = doc.createElement("cac:Price");
            cacInvoiceLine.appendChild(cacPrice);

            Element cbcPriceAmountItem = doc.createElement("cbc:PriceAmount");
            cbcPriceAmountItem.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV")/*venta.getTotalSinIGV()*/)));
            cacPrice.appendChild(cbcPriceAmountItem);

            Attr attrPriceAmountItem = doc.createAttribute("currencyID");
            attrPriceAmountItem.setValue(map.get("Moneda").toString());
            cbcPriceAmountItem.setAttributeNode(attrPriceAmountItem);


            // ******* GENERANDO LA FIRMA DIGITAL

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(empresa.getRutaEnvioTemporal()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronico")+".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioTemporal()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronico");

            Init.init();

            DocumentBuilderFactory dof=DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            //Si el Documento XML no tiene la estructura Correcta entra al  catch SAXException
            Document doc1= dof.newDocumentBuilder().parse(new FileInputStream(RutaXML+".xml"));

            Constants.setSignatureSpecNSprefix("ds");	// Sino, pone por defecto como prefijo: "ns"

            // Cargamos el almacen de claves
            KeyStore ks  = KeyStore.getInstance(KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()),empresa.getKeystorePassword().toCharArray());

            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),empresa.getKeystorePassword().toCharArray());
            File	signatureFile = new File(RutaXML+".xml");
            //@SuppressWarnings("deprecation")
            String	baseURI       = signatureFile.toString();	// BaseURI para las URL Relativas.

            // Instanciamos un objeto XMLSignature desde el Document. El algoritmo de firma serï¿½ RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);

            // Aï¿½adimos el nodo de la firma a la raiz antes de firmar.
            // Observe que ambos elementos pueden ser mezclados en una forma con referencias separadas
            //doc.getDocumentElement().appendChild(xmlSignature.getElement());

            /////   VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA  2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(1).appendChild(xmlSignature.getElement());


            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Aï¿½adimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

            // Aï¿½adimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias()/*PRIVATE_KEY_ALIAS.trim()*/);
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os= new  FileOutputStream(RutaXML+".xml");
            TransformerFactory tf= TransformerFactory.newInstance();
            Transformer  trans=tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();


            NodeList nodeListhash = doc1.getElementsByTagName("ds:DigestValue");
            Node nodohash = nodeListhash.item(0);

            NodeList nodeListSignatureValue = doc1.getElementsByTagName("ds:SignatureValue");
            Node nodoSignatureValue = nodeListSignatureValue.item(0);


            if (nodohash instanceof Element){
                respuesta.put("codehash", nodohash.getTextContent().trim());
                respuesta.put("signaturevalue", nodoSignatureValue.getTextContent().trim());
            }


        } catch (Exception e) {
            log.info(ErrorLog.printStackTraceToString(e));
        }

        return respuesta;


    }
    public static Map<String, Object> DocumentoNotaCreditoXML(Map<String, Object> map, V_Varios_FacturacionBean empresa) {

        Map<String, Object> respuesta = new HashMap<>();



        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element rootElementCreditNote = doc.createElement("CreditNote");
            doc.appendChild(rootElementCreditNote);

            // GENERANDO ATRIBUTOS INVOICE
            Attr attrInvoice = doc.createAttribute("xmlns");
            attrInvoice.setValue("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2");
            rootElementCreditNote.setAttributeNode(attrInvoice);

            Attr attrInvoiceCac = doc.createAttribute("xmlns:cac");
            attrInvoiceCac.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");
            rootElementCreditNote.setAttributeNode(attrInvoiceCac);

            Attr attrInvoiceCbc = doc.createAttribute("xmlns:cbc");
            attrInvoiceCbc.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
            rootElementCreditNote.setAttributeNode(attrInvoiceCbc);

            Attr attrInvoiceCcts = doc.createAttribute("xmlns:ccts");
            attrInvoiceCcts.setValue("urn:un:unece:uncefact:documentation:2");
            rootElementCreditNote.setAttributeNode(attrInvoiceCcts);

            Attr attrInvoiceDs = doc.createAttribute("xmlns:ds");
            attrInvoiceDs.setValue("http://www.w3.org/2000/09/xmldsig#");
            rootElementCreditNote.setAttributeNode(attrInvoiceDs);

            Attr attrInvoiceExt = doc.createAttribute("xmlns:ext");
            attrInvoiceExt.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2");
            rootElementCreditNote.setAttributeNode(attrInvoiceExt);

            Attr attrInvoiceQdt = doc.createAttribute("xmlns:qdt");
            attrInvoiceQdt.setValue("urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2");
            rootElementCreditNote.setAttributeNode(attrInvoiceQdt);

            Attr attrInvoiceSac = doc.createAttribute("xmlns:sac");
            attrInvoiceSac.setValue("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1");
            rootElementCreditNote.setAttributeNode(attrInvoiceSac);

            Attr attrInvoiceUdt = doc.createAttribute("xmlns:udt");
            attrInvoiceUdt.setValue("urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2");
            rootElementCreditNote.setAttributeNode(attrInvoiceUdt);

            Attr attrInvoiceXsi = doc.createAttribute("xmlns:xsi");
            attrInvoiceXsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
            rootElementCreditNote.setAttributeNode(attrInvoiceXsi);


            Element ublExtensions = doc.createElement("ext:UBLExtensions");
            rootElementCreditNote.appendChild(ublExtensions);

            Element ublExtension = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtension);

            Element extExtensionContent = doc.createElement("ext:ExtensionContent");
            ublExtension.appendChild(extExtensionContent);

            Element sacAdditionalInformation = doc.createElement("sac:AdditionalInformation");
            extExtensionContent.appendChild(sacAdditionalInformation);


            // ******************************  VERIFICAR CON LA SUNAT SI SON MANDATORIOS ESTOS CAMPOS DE TOTALES *******************************************************************

            // TOTAL DE VENTA OPERACIONES GRAVADAS (MANDATORIO)
            Element sacAdditionalMonetaryTotalGravadas = doc.createElement("sac:AdditionalMonetaryTotal");
            sacAdditionalInformation.appendChild(sacAdditionalMonetaryTotalGravadas);

            Element cbcIDGravadas = doc.createElement("cbc:ID");
            cbcIDGravadas.appendChild(doc.createTextNode(map.get("CodigoTotalVenta").toString()));// CODIGO 1003 ES PARA PASAJES - CODIGO PARA ENCOMIENDAS ES 1001
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcIDGravadas);


            Element cbcPayableAmountGravadas = doc.createElement("cbc:PayableAmount");
            cbcPayableAmountGravadas.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcPayableAmountGravadas);

            Attr attrGravadas = doc.createAttribute("currencyID");
            attrGravadas.setValue(map.get("Moneda").toString());
            cbcPayableAmountGravadas.setAttributeNode(attrGravadas);

            // TOTAL DE VENTA MONTO POR VENTA (EN LETRAS)
            Element sacAdditionalProperty = doc.createElement("sac:AdditionalProperty");
            sacAdditionalInformation.appendChild(sacAdditionalProperty);


            Element cbcIDProperty = doc.createElement("cbc:ID");
            cbcIDProperty.appendChild(doc.createTextNode(CODIGO_MONTO_LETRAS));
            sacAdditionalProperty.appendChild(cbcIDProperty);


            Element cbcValue = doc.createElement("cbc:Value");
            cbcValue.appendChild(doc.createTextNode(map.get("MontoLetras").toString()));
            sacAdditionalProperty.appendChild(cbcValue);

            Element ublExtensionSignature = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtensionSignature);

            Text texto = doc.createTextNode(" ");
            Element ublExtensionContentSignature = doc.createElement("ext:ExtensionContent");
            ublExtensionContentSignature.appendChild(texto);
            ublExtensionSignature.appendChild(ublExtensionContentSignature);


            //*****************************************************************

            // VERSION DE UBL (2.0)
            Element ublVersionID = doc.createElement("cbc:UBLVersionID");
            ublVersionID.appendChild(doc.createTextNode("2.0"));
            rootElementCreditNote.appendChild(ublVersionID);

            // VERSION DE LA ESTRUCTURA DEL DOCUMENTO (VER PAG. 57)
            Element cbcCustomizationID = doc.createElement("cbc:CustomizationID");
            cbcCustomizationID.appendChild(doc.createTextNode("1.0"));
            rootElementCreditNote.appendChild(cbcCustomizationID);

            // NUMERO DEL DOCUMENTO DE LA NOTA DE CREDITO (SERIE Y NUMERO -- CARACTER INICIAL --> "F" CUANDO AFECTA A UNA FACTURA Y "B" CUANDO AFECTA A UNA BOLETA)
            Element cbcID = doc.createElement("cbc:ID");
            cbcID.appendChild(doc.createTextNode(map.get("DocumentoElectronico").toString()));
            rootElementCreditNote.appendChild(cbcID);

            // FECHA DE EMISION
            Element cbcIssueDate = doc.createElement("cbc:IssueDate");
            cbcIssueDate.appendChild(doc.createTextNode(map.get("FechaEmision").toString()));
            rootElementCreditNote.appendChild(cbcIssueDate);

            // CODIGO DE MONEDA (CATALOGO N. 2)
            Element cbcDocumentCurrencyCode = doc.createElement("cbc:DocumentCurrencyCode");
            cbcDocumentCurrencyCode.appendChild(doc.createTextNode(map.get("Moneda").toString()));
            rootElementCreditNote.appendChild(cbcDocumentCurrencyCode);

            //*************************************************************
            // DATOS DEL DOCUMENTO A APLICAR
            Element cacDiscrepancyResponse = doc.createElement("cac:DiscrepancyResponse");
            rootElementCreditNote.appendChild(cacDiscrepancyResponse);

            // DOCUMENTO APLICABLE (FACTURA O BOLETA)
            Element cbcReferenceID = doc.createElement("cbc:ReferenceID");
            cbcReferenceID.appendChild(doc.createTextNode(map.get("DocumentoElectronicoAplicar").toString()));
            cacDiscrepancyResponse.appendChild(cbcReferenceID);

            // CODIGO DE TIPO DE NOTA DE CREDITO (CATALOGO No 9)
            Element cbcRespondeCode = doc.createElement("cbc:ResponseCode");
            cbcRespondeCode.appendChild(doc.createTextNode(map.get("CodigoMotivoNota").toString()));
            cacDiscrepancyResponse.appendChild(cbcRespondeCode);

            // CODIGO DE TIPO DE NOTA DE CREDITO (CATALOGO No 9)
            Element cbcDescription = doc.createElement("cbc:Description");
            cbcDescription.appendChild(doc.createCDATASection(map.get("Descripcion1").toString()));
            cacDiscrepancyResponse.appendChild(cbcDescription);

            // *************************************************************

            Element cacBillingReference = doc.createElement("cac:BillingReference");
            rootElementCreditNote.appendChild(cacBillingReference);

            Element cacInvoiceDocumentReference = doc.createElement("cac:InvoiceDocumentReference");
            cacBillingReference.appendChild(cacInvoiceDocumentReference);

            // VALOR DEL DOCUEMTO A QUE SE APLICA
            Element cbcInvoiceID = doc.createElement("cbc:ID");
            cbcInvoiceID.appendChild(doc.createTextNode(map.get("DocumentoElectronicoAplicar").toString()));
            cacInvoiceDocumentReference.appendChild(cbcInvoiceID);

            // VALOR DEL TIPO DE  DOCUEMTO A QUE SE APLICA
            Element cbcDocumentTypeCode = doc.createElement("cbc:DocumentTypeCode");
            cbcDocumentTypeCode.appendChild(doc.createTextNode(map.get("TipoDocumentoAplicar").toString()));
            cacInvoiceDocumentReference.appendChild(cbcDocumentTypeCode);

            // *************************************************************

            // CAC SIGNATURE
            Element cacSignature = doc.createElement("cac:Signature");
            rootElementCreditNote.appendChild(cacSignature);


            Element cacSignatureID = doc.createElement("cbc:ID");
            cacSignatureID.appendChild(doc.createTextNode("IDSignKG"));
            cacSignature.appendChild(cacSignatureID);

            Element cacSignatoryParty = doc.createElement("cac:SignatoryParty");
            cacSignature.appendChild(cacSignatoryParty);

            Element cacPartyIdentification = doc.createElement("cac:PartyIdentification");
            cacSignatoryParty.appendChild(cacPartyIdentification);

            Element cacPartyIdentificationID = doc.createElement("cbc:ID");
            cacPartyIdentificationID.appendChild(doc.createTextNode(empresa.getRuc()));
            cacPartyIdentification.appendChild(cacPartyIdentificationID);

            Element cacPartySignatureName = doc.createElement("cac:PartyName");
            cacSignatoryParty.appendChild(cacPartySignatureName);

            Element cbcSignatureName = doc.createElement("cbc:Name");
            cbcSignatureName.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartySignatureName.appendChild(cbcSignatureName);


            Element cacDigitalSignatureAttachment = doc.createElement("cac:DigitalSignatureAttachment");
            cacSignature.appendChild(cacDigitalSignatureAttachment);

            Element cacExternalReference = doc.createElement("cac:ExternalReference");
            cacDigitalSignatureAttachment.appendChild(cacExternalReference);

            Element cbcURI = doc.createElement("cbc:URI");
            cbcURI.appendChild(doc.createTextNode("#signatureKG"));
            cacExternalReference.appendChild(cbcURI);

            // *************************************************************
            // DATOS DEL EMISOR DEL DOCUMENTO
            Element cacAccountingSupplierParty = doc.createElement("cac:AccountingSupplierParty");
            rootElementCreditNote.appendChild(cacAccountingSupplierParty);

            // NUMERO DE DOCUMENTO DE IDENTIDAD (RUC) -- EMISOR
            Element cbcCustomerAssignedAccountID = doc.createElement("cbc:CustomerAssignedAccountID");
            cbcCustomerAssignedAccountID.appendChild(doc.createTextNode(empresa.getRuc())); // RUC PALOMINO (EJEMPLO)
            cacAccountingSupplierParty.appendChild(cbcCustomerAssignedAccountID);

            // TIPO DE DOCUMENTO DE IDENTIFICACION -- EMISOR
            Element cbcAdditionalAccountID = doc.createElement("cbc:AdditionalAccountID");
            cbcAdditionalAccountID.appendChild(doc.createTextNode("6"));
            cacAccountingSupplierParty.appendChild(cbcAdditionalAccountID);

            // TIPO DE DOCUMENTO DE IDENTIFICACION
            Element cacParty = doc.createElement("cac:Party");
            cacAccountingSupplierParty.appendChild(cacParty);

            Element cacPartyName = doc.createElement("cac:PartyName");
            cacParty.appendChild(cacPartyName);

            Element cbcName = doc.createElement("cbc:Name");
            cbcName.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartyName.appendChild(cbcName);

            // CODIGO POSTAL
            Element cacPostalAddress = doc.createElement("cac:PostalAddress");
            cacParty.appendChild(cacPostalAddress);

            Element cbcPostalAddressID = doc.createElement("cbc:ID");
            cbcPostalAddressID.appendChild(doc.createTextNode(empresa.getUbigeo()));
            cacPostalAddress.appendChild(cbcPostalAddressID);

            // DIRECCION POSTAL
            Element cbcStreetName = doc.createElement("cbc:StreetName");
            cbcStreetName.appendChild(doc.createTextNode(empresa.getDireccion()));
            cacPostalAddress.appendChild(cbcStreetName);

            // CODIGO DE PAIS
            Element cbcCountry = doc.createElement("cac:Country");
            cacPostalAddress.appendChild(cbcCountry);

            Element cbcIdentificationCode = doc.createElement("cbc:IdentificationCode");
            cbcIdentificationCode.appendChild(doc.createTextNode(empresa.getCodigoPais()));
            cbcCountry.appendChild(cbcIdentificationCode);

            // DATOS DEL EMISOR
            Element cacPartyLegalEntity = doc.createElement("cac:PartyLegalEntity");
            cacParty.appendChild(cacPartyLegalEntity);


            // DATOS DEL EMISOR (DESCRIPCION)
            Element cbcRegistrationName = doc.createElement("cbc:RegistrationName");
            cbcRegistrationName.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartyLegalEntity.appendChild(cbcRegistrationName);

            // *************************************************************
            // DATOS DEL RECEPTOR DE LA NOTA DE CREDITO
            Element cacAccountingCustomerParty = doc.createElement("cac:AccountingCustomerParty");
            rootElementCreditNote.appendChild(cacAccountingCustomerParty);


            // NUMERO DE DOCUMENTO DEL ADQUIRIENTE (CUANDO ES A UNA FACTURA SE LE ASIGNA EL RUC DEL ADQUIRIENTE, SI ES UNA BOLETA SE LE COLOCA UN GUION "-")
            Element cbcCustomerAssignedAccountIDReceptor = doc.createElement("cbc:CustomerAssignedAccountID");
            cbcCustomerAssignedAccountIDReceptor.appendChild(doc.createTextNode(map.get("Ruc").toString()));
            cacAccountingCustomerParty.appendChild(cbcCustomerAssignedAccountIDReceptor);

            Element cbcAdditionalAccountIDReceptor = doc.createElement("cbc:AdditionalAccountID");
            cbcAdditionalAccountIDReceptor.appendChild(doc.createTextNode(map.get("TipoDocumentoReceptor").toString()));
            cacAccountingCustomerParty.appendChild(cbcAdditionalAccountIDReceptor);

            Element cacPartyReceptor = doc.createElement("cac:Party");
            cacAccountingCustomerParty.appendChild(cacPartyReceptor);

            // *******************************************************************************************
            // INFORMACION DE SUMATORIA IVG GLOBAL

            Element cacTaxTotalGlobal = doc.createElement("cac:TaxTotal");
            rootElementCreditNote.appendChild(cacTaxTotalGlobal);

            Element cbcTaxAmountGlobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA  DEL IGV TOTAL (PARA PASAJES 0)
            cacTaxTotalGlobal.appendChild(cbcTaxAmountGlobal);

            Attr attrTaxAmountGlobal = doc.createAttribute("currencyID");
            attrTaxAmountGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountGlobal.setAttributeNode(attrTaxAmountGlobal);

            Element cacTaxSubtotalGlobal = doc.createElement("cac:TaxSubtotal");
            cacTaxTotalGlobal.appendChild(cacTaxSubtotalGlobal);

            Element cbcTaxAmountSubtotalGblobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotalGblobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotalGlobal.appendChild(cbcTaxAmountSubtotalGblobal);

            Attr attrTaxAmountSubtotalGlobal = doc.createAttribute("currencyID");
            attrTaxAmountSubtotalGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotalGblobal.setAttributeNode(attrTaxAmountSubtotalGlobal);

            Element cacTaxtCategoryGlobal = doc.createElement("cac:TaxCategory");
            cacTaxSubtotalGlobal.appendChild(cacTaxtCategoryGlobal);

            Element cbcTaxExemptionReasonCodeGlobal = doc.createElement("cbc:TaxExemptionReasonCode");
            cbcTaxExemptionReasonCodeGlobal.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR CATALOGO Nï¿½ 7 ( CODIGO 20 PARA PASAJES - CODIGO 10 PARA ENCOMIENDAS)
            cacTaxtCategoryGlobal.appendChild(cbcTaxExemptionReasonCodeGlobal);

            Element cacTaxSchemeGlobal = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobal.appendChild(cacTaxSchemeGlobal);

            Element cacTaxSchemeIDGlobal = doc.createElement("cbc:ID");
            cacTaxSchemeIDGlobal.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cacTaxSchemeIDGlobal);

            Element cbcTaxSchemeNameGlobal = doc.createElement("cbc:Name");
            cbcTaxSchemeNameGlobal.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxSchemeNameGlobal);

            Element cbcTaxTypeCodeGlobal = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCodeGlobal.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxTypeCodeGlobal);

            //*******************************************************************************************
            Element cacPartyLegalEntityReceptor = doc.createElement("cac:PartyLegalEntity");
            cacPartyReceptor.appendChild(cacPartyLegalEntityReceptor);

            Element cbcRegistrationNameReceptor = doc.createElement("cbc:RegistrationName");
            cbcRegistrationNameReceptor.appendChild(doc.createCDATASection(map.get("Razon").toString()));
            cacPartyLegalEntityReceptor.appendChild(cbcRegistrationNameReceptor);

            // *******************************************************************************************

            // IMPORTE TOTAL A PAGAR POR EL DOCUMENTO

            Element cacLegalMonetaryTotal = doc.createElement("cac:LegalMonetaryTotal");
            rootElementCreditNote.appendChild(cacLegalMonetaryTotal);

            Element cbcPayableAmount = doc.createElement("cbc:PayableAmount");
            cbcPayableAmount.appendChild(doc.createTextNode(String.valueOf(map.get("Total"))));
            cacLegalMonetaryTotal.appendChild(cbcPayableAmount);

            Attr attrMonetaryTotal = doc.createAttribute("currencyID");
            attrMonetaryTotal.setValue(map.get("Moneda").toString());
            cbcPayableAmount.setAttributeNode(attrMonetaryTotal);


            // *************************************************************
            Element cacCreditNoteLine = doc.createElement("cac:CreditNoteLine");
            rootElementCreditNote.appendChild(cacCreditNoteLine);

            // CALOR DEL ITEM SIEMPRE VA SER 1
            Element cbcCreditNoteLineID = doc.createElement("cbc:ID");
            cbcCreditNoteLineID.appendChild(doc.createTextNode("1"));
            cacCreditNoteLine.appendChild(cbcCreditNoteLineID);

            Element cacTaxTotalGlobalCredit = doc.createElement("cac:TaxTotal");
            cacCreditNoteLine.appendChild(cacTaxTotalGlobalCredit);

            Element cbcTaxAmountGlobalCredit = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountGlobalCredit.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA  DEL IGV TOTAL (PARA PASAJES 0)
            cacTaxTotalGlobalCredit.appendChild(cbcTaxAmountGlobalCredit);

            Attr attrTaxAmountGlobalCredit = doc.createAttribute("currencyID");
            attrTaxAmountGlobalCredit.setValue(map.get("Moneda").toString());
            cbcTaxAmountGlobalCredit.setAttributeNode(attrTaxAmountGlobalCredit);

            Element cacTaxSubtotalGlobalCredit = doc.createElement("cac:TaxSubtotal");
            cacTaxTotalGlobalCredit.appendChild(cacTaxSubtotalGlobalCredit);

            Element cbcTaxAmountSubtotalGblobalCredit = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotalGblobalCredit.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotalGlobalCredit.appendChild(cbcTaxAmountSubtotalGblobalCredit);

            Attr attrTaxAmountSubtotalGlobalCredit = doc.createAttribute("currencyID");
            attrTaxAmountSubtotalGlobalCredit.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotalGblobalCredit.setAttributeNode(attrTaxAmountSubtotalGlobalCredit);

            Element cacTaxtCategoryGlobalCredit = doc.createElement("cac:TaxCategory");
            cacTaxSubtotalGlobalCredit.appendChild(cacTaxtCategoryGlobalCredit);

            Element cbcTaxExemptionReasonCodeGlobalCredit = doc.createElement("cbc:TaxExemptionReasonCode");
            cbcTaxExemptionReasonCodeGlobalCredit.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR CATALOGO  7 ( CODIGO 20 PARA PASAJES - CODIGO 10 PARA ENCOMIENDAS)
            cacTaxtCategoryGlobalCredit.appendChild(cbcTaxExemptionReasonCodeGlobalCredit);

            Element cacTaxSchemeGlobalCredit = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobalCredit.appendChild(cacTaxSchemeGlobalCredit);

            Element cacTaxSchemeIDGlobalCredit = doc.createElement("cbc:ID");
            cacTaxSchemeIDGlobalCredit.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR CATALOGO N 5 (CODIGO 1000)
            cacTaxSchemeGlobalCredit.appendChild(cacTaxSchemeIDGlobalCredit);

            Element cbcTaxSchemeNameGlobalCredit = doc.createElement("cbc:Name");
            cbcTaxSchemeNameGlobalCredit.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// VERIFICAR CATALOGO N 5 (CODIGO 1000)
            cacTaxSchemeGlobalCredit.appendChild(cbcTaxSchemeNameGlobalCredit);

            Element cbcTaxTypeCodeGlobalCredit = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCodeGlobalCredit.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR CATALOGO N  5 (CODIGO 1000)
            cacTaxSchemeGlobalCredit.appendChild(cbcTaxTypeCodeGlobalCredit);


            // ******* GENERANDO LA FIRMA DIGITAL

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(empresa.getRutaEnvioTemporal()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronico")+".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioTemporal()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronico");

            //String NombreFile = empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronico");

            Init.init();

            DocumentBuilderFactory dof=DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            //Si el Documento XML no tiene la estructura Correcta entra al  catch SAXException
            Document doc1= dof.newDocumentBuilder().parse(new FileInputStream(RutaXML+".xml"));

            Constants.setSignatureSpecNSprefix("ds");	// Sino, pone por defecto como prefijo: "ns"

            // Cargamos el almacen de claves
            KeyStore ks  = KeyStore.getInstance(KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()),empresa.getKeystorePassword().toCharArray());

            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),empresa.getKeystorePassword().toCharArray());
            File	signatureFile = new File(RutaXML+".xml");
            //@SuppressWarnings("deprecation")
            String	baseURI       = signatureFile.toString();	// BaseURI para las URL Relativas.

            // Instanciamos un objeto XMLSignature desde el Document. El algoritmo de firma sera RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);


            /////   VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA  2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(1).appendChild(xmlSignature.getElement());


            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Aaadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

            // Aaadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias()/*PRIVATE_KEY_ALIAS.trim()*/);
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os= new  FileOutputStream(RutaXML+".xml");
            TransformerFactory tf= TransformerFactory.newInstance();
            Transformer  trans=tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();


            NodeList nodeListhash = doc1.getElementsByTagName("ds:DigestValue");
            Node nodohash = nodeListhash.item(0);

            NodeList nodeListSignatureValue = doc1.getElementsByTagName("ds:SignatureValue");
            Node nodoSignatureValue = nodeListSignatureValue.item(0);


            if (nodohash instanceof Element){
                respuesta.put("codehash", nodohash.getTextContent().trim());
                respuesta.put("signaturevalue", nodoSignatureValue.getTextContent().trim());
            }


        } catch (Exception e) {
            log.info(ErrorLog.printStackTraceToString(e));
        }

        return respuesta;
    }

    public static Map<String, Object> DocumentoNotaDebitoXML(Map<String, Object> map, V_Varios_FacturacionBean empresa) {


        Map<String, Object> respuesta = new HashMap<>();

        try {


            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element rootElementDebitNote = doc.createElement("DebitNote");
            doc.appendChild(rootElementDebitNote);

            // GENERANDO ATRIBUTOS INVOICE
            Attr attrInvoice = doc.createAttribute("xmlns");
            attrInvoice.setValue("urn:oasis:names:specification:ubl:schema:xsd:DebitNote-2");
            rootElementDebitNote.setAttributeNode(attrInvoice);

            Attr attrInvoiceCac = doc.createAttribute("xmlns:cac");
            attrInvoiceCac.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");
            rootElementDebitNote.setAttributeNode(attrInvoiceCac);

            Attr attrInvoiceCbc = doc.createAttribute("xmlns:cbc");
            attrInvoiceCbc.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
            rootElementDebitNote.setAttributeNode(attrInvoiceCbc);

            Attr attrInvoiceCcts = doc.createAttribute("xmlns:ccts");
            attrInvoiceCcts.setValue("urn:un:unece:uncefact:documentation:2");
            rootElementDebitNote.setAttributeNode(attrInvoiceCcts);

            Attr attrInvoiceDs = doc.createAttribute("xmlns:ds");
            attrInvoiceDs.setValue("http://www.w3.org/2000/09/xmldsig#");
            rootElementDebitNote.setAttributeNode(attrInvoiceDs);

            Attr attrInvoiceExt = doc.createAttribute("xmlns:ext");
            attrInvoiceExt.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2");
            rootElementDebitNote.setAttributeNode(attrInvoiceExt);

            Attr attrInvoiceQdt = doc.createAttribute("xmlns:qdt");
            attrInvoiceQdt.setValue("urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2");
            rootElementDebitNote.setAttributeNode(attrInvoiceQdt);

            Attr attrInvoiceSac = doc.createAttribute("xmlns:sac");
            attrInvoiceSac.setValue("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1");
            rootElementDebitNote.setAttributeNode(attrInvoiceSac);

            Attr attrInvoiceUdt = doc.createAttribute("xmlns:udt");
            attrInvoiceUdt.setValue("urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2");
            rootElementDebitNote.setAttributeNode(attrInvoiceUdt);

            Attr attrInvoiceXsi = doc.createAttribute("xmlns:xsi");
            attrInvoiceXsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
            rootElementDebitNote.setAttributeNode(attrInvoiceXsi);

            Element ublExtensions = doc.createElement("ext:UBLExtensions");
            rootElementDebitNote.appendChild(ublExtensions);

            Element ublExtension = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtension);


            Element extExtensionContent = doc.createElement("ext:ExtensionContent");
            ublExtension.appendChild(extExtensionContent);


            Element sacAdditionalInformation = doc.createElement("sac:AdditionalInformation");
            extExtensionContent.appendChild(sacAdditionalInformation);


            // TOTAL DE VENTA OPERACIONES GRAVADAS (MANDATORIO)
            Element sacAdditionalMonetaryTotalGravadas = doc.createElement("sac:AdditionalMonetaryTotal");
            sacAdditionalInformation.appendChild(sacAdditionalMonetaryTotalGravadas);

            Element cbcIDGravadas = doc.createElement("cbc:ID");
            cbcIDGravadas.appendChild(doc.createTextNode(map.get("CodigoTotalVenta").toString()));// CODIGO 1003 ES PARA PASAJES - CODIGO PARA ENCOMIENDAS ES 1001
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcIDGravadas);


            Element cbcPayableAmountGravadas = doc.createElement("cbc:PayableAmount");
            cbcPayableAmountGravadas.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcPayableAmountGravadas);

            Attr attrGravadas = doc.createAttribute("currencyID");
            attrGravadas.setValue(map.get("Moneda").toString());
            cbcPayableAmountGravadas.setAttributeNode(attrGravadas);

            // TOTAL DE VENTA MONTO POR VENTA (EN LETRAS)
            Element sacAdditionalProperty = doc.createElement("sac:AdditionalProperty");
            sacAdditionalInformation.appendChild(sacAdditionalProperty);


            Element cbcIDProperty = doc.createElement("cbc:ID");
            cbcIDProperty.appendChild(doc.createTextNode(CODIGO_MONTO_LETRAS));
            sacAdditionalProperty.appendChild(cbcIDProperty);


            Element cbcValue = doc.createElement("cbc:Value");
            cbcValue.appendChild(doc.createTextNode(map.get("MontoLetras").toString()));
            sacAdditionalProperty.appendChild(cbcValue);

            Element ublExtensionSignature = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtensionSignature);

            Text texto = doc.createTextNode(" ");
            Element ublExtensionContentSignature = doc.createElement("ext:ExtensionContent");
            ublExtensionContentSignature.appendChild(texto);
            ublExtensionSignature.appendChild(ublExtensionContentSignature);


            //*****************************************************************

            // VERSION DE UBL (2.0)
            Element ublVersionID = doc.createElement("cbc:UBLVersionID");
            ublVersionID.appendChild(doc.createTextNode("2.0"));
            rootElementDebitNote.appendChild(ublVersionID);

            // VERSION DE LA ESTRUCTURA DEL DOCUMENTO (VER PAG. 57)
            Element cbcCustomizationID = doc.createElement("cbc:CustomizationID");
            cbcCustomizationID.appendChild(doc.createTextNode("1.0"));
            rootElementDebitNote.appendChild(cbcCustomizationID);

            // NUMERO DEL DOCUMENTO DE LA NOTA DE CREDITO (SERIE Y NUMERO -- CARACTER INICIAL --> "F" CUANDO AFECTA A UNA FACTURA Y "B" CUANDO AFECTA A UNA BOLETA)
            Element cbcID = doc.createElement("cbc:ID");
            cbcID.appendChild(doc.createTextNode(map.get("DocumentoElectronico").toString()));
            rootElementDebitNote.appendChild(cbcID);

            // FECHA DE EMISION
            Element cbcIssueDate = doc.createElement("cbc:IssueDate");
            cbcIssueDate.appendChild(doc.createTextNode(map.get("FechaEmision").toString()));
            rootElementDebitNote.appendChild(cbcIssueDate);

            // *************************************************************

            // CODIGO DE MONEDA (CATALOGO NO. 2)
            Element cbcDocumentCurrencyCode = doc.createElement("cbc:DocumentCurrencyCode");
            cbcDocumentCurrencyCode.appendChild(doc.createTextNode(map.get("Moneda").toString()));
            rootElementDebitNote.appendChild(cbcDocumentCurrencyCode);

            //*************************************************************
            // DATOS DEL DOCUMENTO A APLICAR
            Element cacDiscrepancyResponse = doc.createElement("cac:DiscrepancyResponse");
            rootElementDebitNote.appendChild(cacDiscrepancyResponse);

            // DOCUMENTO APLICABLE (FACTURA O BOLETA)
            Element cbcReferenceID = doc.createElement("cbc:ReferenceID");
            cbcReferenceID.appendChild(doc.createTextNode(map.get("DocumentoElectronicoAplicar").toString()));
            cacDiscrepancyResponse.appendChild(cbcReferenceID);

            // CODIGO DE TIPO DE NOTA DE CREDITO (CATALOGO No 9)
            Element cbcRespondeCode = doc.createElement("cbc:ResponseCode");
            cbcRespondeCode.appendChild(doc.createTextNode(map.get("CodigoMotivoNota").toString()));
            cacDiscrepancyResponse.appendChild(cbcRespondeCode);

            // CODIGO DE TIPO DE NOTA DE CREDITO (CATALOGO No 9)
            Element cbcDescription = doc.createElement("cbc:Description");
            cbcDescription.appendChild(doc.createCDATASection(map.get("Descripcion1").toString()));
            cacDiscrepancyResponse.appendChild(cbcDescription);

            // *************************************************************

            Element cacBillingReference = doc.createElement("cac:BillingReference");
            rootElementDebitNote.appendChild(cacBillingReference);

            Element cacInvoiceDocumentReference = doc.createElement("cac:InvoiceDocumentReference");
            cacBillingReference.appendChild(cacInvoiceDocumentReference);

            // VALOR DEL DOCUEMTO A QUE SE APLICA
            Element cbcInvoiceID = doc.createElement("cbc:ID");
            cbcInvoiceID.appendChild(doc.createTextNode(map.get("DocumentoElectronicoAplicar").toString()));
            cacInvoiceDocumentReference.appendChild(cbcInvoiceID);

            // TIPO DEL DOCUEMTO A QUE SE APLICA
            Element cbcDocumentTypeCode = doc.createElement("cbc:DocumentTypeCode");
            cbcDocumentTypeCode.appendChild(doc.createTextNode(map.get("TipoDocumentoAplicar").toString()));
            cacInvoiceDocumentReference.appendChild(cbcDocumentTypeCode);

            // *************************************************************


            // CAC SIGNATURE

            Element cacSignature = doc.createElement("cac:Signature");
            rootElementDebitNote.appendChild(cacSignature);


            Element cacSignatureID = doc.createElement("cbc:ID");
            cacSignatureID.appendChild(doc.createTextNode("IDSignKG"));
            cacSignature.appendChild(cacSignatureID);

            Element cacSignatoryParty = doc.createElement("cac:SignatoryParty");
            cacSignature.appendChild(cacSignatoryParty);

            Element cacPartyIdentification = doc.createElement("cac:PartyIdentification");
            cacSignatoryParty.appendChild(cacPartyIdentification);

            Element cacPartyIdentificationID = doc.createElement("cbc:ID");
            cacPartyIdentificationID.appendChild(doc.createTextNode(empresa.getRuc()));
            cacPartyIdentification.appendChild(cacPartyIdentificationID);

            Element cacPartySignatureName = doc.createElement("cac:PartyName");
            cacSignatoryParty.appendChild(cacPartySignatureName);

            Element cbcSignatureName = doc.createElement("cbc:Name");
            cbcSignatureName.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartySignatureName.appendChild(cbcSignatureName);


            Element cacDigitalSignatureAttachment = doc.createElement("cac:DigitalSignatureAttachment");
            cacSignature.appendChild(cacDigitalSignatureAttachment);

            Element cacExternalReference = doc.createElement("cac:ExternalReference");
            cacDigitalSignatureAttachment.appendChild(cacExternalReference);

            Element cbcURI = doc.createElement("cbc:URI");
            cbcURI.appendChild(doc.createTextNode("#signatureKG"));
            cacExternalReference.appendChild(cbcURI);

            // *************************************************************

            // DATOS DEL EMISOR DEL DOCUMENTO
            Element cacAccountingSupplierParty = doc.createElement("cac:AccountingSupplierParty");
            rootElementDebitNote.appendChild(cacAccountingSupplierParty);

            // NUMERO DE DOCUMENTO DE IDENTIDAD (RUC) -- EMISOR
            Element cbcCustomerAssignedAccountID = doc.createElement("cbc:CustomerAssignedAccountID");
            cbcCustomerAssignedAccountID.appendChild(doc.createTextNode(empresa.getRuc()));
            cacAccountingSupplierParty.appendChild(cbcCustomerAssignedAccountID);

            // TIPO DE DOCUMENTO DE IDENTIFICACION -- EMISOR
            Element cbcAdditionalAccountID = doc.createElement("cbc:AdditionalAccountID");
            cbcAdditionalAccountID.appendChild(doc.createTextNode("6"));
            cacAccountingSupplierParty.appendChild(cbcAdditionalAccountID);

            // TIPO DE DOCUMENTO DE IDENTIFICACION
            Element cacParty = doc.createElement("cac:Party");
            cacAccountingSupplierParty.appendChild(cacParty);

            Element cacPartyName = doc.createElement("cac:PartyName");
            cacParty.appendChild(cacPartyName);

            Element cbcName = doc.createElement("cbc:Name");
            cbcName.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartyName.appendChild(cbcName);

            // CODIGO POSTAL
            Element cacPostalAddress = doc.createElement("cac:PostalAddress");
            cacParty.appendChild(cacPostalAddress);

            Element cbcPostalAddressID = doc.createElement("cbc:ID");
            cbcPostalAddressID.appendChild(doc.createTextNode(empresa.getUbigeo()));
            cacPostalAddress.appendChild(cbcPostalAddressID);

            // DIRECCION POSTAL
            Element cbcStreetName = doc.createElement("cbc:StreetName");
            cbcStreetName.appendChild(doc.createTextNode(empresa.getDireccion()));
            cacPostalAddress.appendChild(cbcStreetName);

            // CODIGO DE PAIS
            Element cbcCountry = doc.createElement("cac:Country");
            cacPostalAddress.appendChild(cbcCountry);

            Element cbcIdentificationCode = doc.createElement("cbc:IdentificationCode");
            cbcIdentificationCode.appendChild(doc.createTextNode(empresa.getCodigoPais()));
            cbcCountry.appendChild(cbcIdentificationCode);

            // DATOS DEL EMISOR
            Element cacPartyLegalEntity = doc.createElement("cac:PartyLegalEntity");
            cacParty.appendChild(cacPartyLegalEntity);


            // DATOS DEL EMISOR (DESCRIPCION)
            Element cbcRegistrationName = doc.createElement("cbc:RegistrationName");
            cbcRegistrationName.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartyLegalEntity.appendChild(cbcRegistrationName);

            // *************************************************************
            // DATOS DEL RECEPTOR DE LA NOTA DE DEBITO
            Element cacAccountingCustomerParty = doc.createElement("cac:AccountingCustomerParty");
            rootElementDebitNote.appendChild(cacAccountingCustomerParty);

            // NUMERO DE DOCUMENTO DEL ADQUIRIENTE (CUANDO ES A UNA FACTURA SE LE ASIGNA EL RUC DEL ADQUIRIENTE, SI ES UNA BOLETA SE LE COLOCA UN GUION "-")
            Element cbcCustomerAssignedAccountIDReceptor = doc.createElement("cbc:CustomerAssignedAccountID");
            cbcCustomerAssignedAccountIDReceptor.appendChild(doc.createTextNode(map.get("Ruc").toString()));
            cacAccountingCustomerParty.appendChild(cbcCustomerAssignedAccountIDReceptor);

            Element cbcAdditionalAccountIDReceptor = doc.createElement("cbc:AdditionalAccountID");
            cbcAdditionalAccountIDReceptor.appendChild(doc.createTextNode(map.get("TipoDocumentoReceptor").toString()));
            cacAccountingCustomerParty.appendChild(cbcAdditionalAccountIDReceptor);

            Element cacPartyReceptor = doc.createElement("cac:Party");
            cacAccountingCustomerParty.appendChild(cacPartyReceptor);

            Element cacPartyLegalEntityReceptor = doc.createElement("cac:PartyLegalEntity");
            cacPartyReceptor.appendChild(cacPartyLegalEntityReceptor);

            Element cbcRegistrationNameReceptor = doc.createElement("cbc:RegistrationName");
            cbcRegistrationNameReceptor.appendChild(doc.createCDATASection(map.get("Razon").toString()));
            cacPartyLegalEntityReceptor.appendChild(cbcRegistrationNameReceptor);


            // *******************************************************************************************
            // INFORMACION DE SUMATORIA IVG GLOBAL

            Element cacTaxTotalGlobal = doc.createElement("cac:TaxTotal");
            rootElementDebitNote.appendChild(cacTaxTotalGlobal);

            Element cbcTaxAmountGlobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA  DEL IGV TOTAL (PARA PASAJES 0)
            cacTaxTotalGlobal.appendChild(cbcTaxAmountGlobal);

            Attr attrTaxAmountGlobal = doc.createAttribute("currencyID");
            attrTaxAmountGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountGlobal.setAttributeNode(attrTaxAmountGlobal);

            Element cacTaxSubtotalGlobal = doc.createElement("cac:TaxSubtotal");
            cacTaxTotalGlobal.appendChild(cacTaxSubtotalGlobal);

            Element cbcTaxAmountSubtotalGblobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotalGblobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotalGlobal.appendChild(cbcTaxAmountSubtotalGblobal);

            Attr attrTaxAmountSubtotalGlobal = doc.createAttribute("currencyID");
            attrTaxAmountSubtotalGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotalGblobal.setAttributeNode(attrTaxAmountSubtotalGlobal);

            Element cacTaxtCategoryGlobal = doc.createElement("cac:TaxCategory");
            cacTaxSubtotalGlobal.appendChild(cacTaxtCategoryGlobal);

            Element cbcTaxExemptionReasonCodeGlobal = doc.createElement("cbc:TaxExemptionReasonCode");
            cbcTaxExemptionReasonCodeGlobal.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR CATALOGO Nï¿½ 7 ( CODIGO 20 PARA PASAJES - CODIGO 10 PARA ENCOMIENDAS)
            cacTaxtCategoryGlobal.appendChild(cbcTaxExemptionReasonCodeGlobal);

            Element cacTaxSchemeGlobal = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobal.appendChild(cacTaxSchemeGlobal);

            Element cacTaxSchemeIDGlobal = doc.createElement("cbc:ID");
            cacTaxSchemeIDGlobal.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cacTaxSchemeIDGlobal);

            Element cbcTaxSchemeNameGlobal = doc.createElement("cbc:Name");
            cbcTaxSchemeNameGlobal.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxSchemeNameGlobal);

            Element cbcTaxTypeCodeGlobal = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCodeGlobal.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR CATALOGO Nï¿½ 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxTypeCodeGlobal);

            // *************************************************************

            Element cacRequestedMonetaryTotal = doc.createElement("cac:RequestedMonetaryTotal");
            rootElementDebitNote.appendChild(cacRequestedMonetaryTotal);

            Element cbcPayableAmount = doc.createElement("cbc:PayableAmount");
            cbcPayableAmount.appendChild(doc.createTextNode(String.valueOf(map.get("Total"))));
            cacRequestedMonetaryTotal.appendChild(cbcPayableAmount);

            Attr attrPayableAmount = doc.createAttribute("currencyID");
            attrPayableAmount.setValue(map.get("Moneda").toString());
            cbcPayableAmount.setAttributeNode(attrPayableAmount);

            // *************************************************************

            Element cacCreditNoteLine = doc.createElement("cac:DebitNoteLine");
            rootElementDebitNote.appendChild(cacCreditNoteLine);

            // CALOR DEL ITEM SIEMPRE VA SER 1
            Element cbcCreditNoteLineID = doc.createElement("cbc:ID");
            cbcCreditNoteLineID.appendChild(doc.createTextNode("1"));
            cacCreditNoteLine.appendChild(cbcCreditNoteLineID);

            Element cbcLineExtensionAmount = doc.createElement("cbc:LineExtensionAmount");
            cbcLineExtensionAmount.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));
            cacCreditNoteLine.appendChild(cbcLineExtensionAmount);

            Attr attrLineExtensionAmount = doc.createAttribute("currencyID");
            attrLineExtensionAmount.setValue(map.get("Moneda").toString());
            cbcLineExtensionAmount.setAttributeNode(attrLineExtensionAmount);


            // *************************************************************

            // ******* GENERANDO LA FIRMA DIGITAL

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(empresa.getRutaEnvioTemporal()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronico")+".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioTemporal()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronico");

            //String NombreFile = empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronico");

            Init.init();

            DocumentBuilderFactory dof=DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            //Si el Documento XML no tiene la estructura Correcta entra al  catch SAXException
            Document doc1= dof.newDocumentBuilder().parse(new FileInputStream(RutaXML+".xml"));

            Constants.setSignatureSpecNSprefix("ds");	// Sino, pone por defecto como prefijo: "ns"

            // Cargamos el almacen de claves
            KeyStore ks  = KeyStore.getInstance(KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()),empresa.getKeystorePassword().toCharArray());

            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),empresa.getKeystorePassword().toCharArray());
            File	signatureFile = new File(RutaXML+".xml");
            //@SuppressWarnings("deprecation")
            String	baseURI       = signatureFile.toString();	// BaseURI para las URL Relativas.

            // Instanciamos un objeto XMLSignature desde el Document. El algoritmo de firma sera RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);


            /////   VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA  2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(1).appendChild(xmlSignature.getElement());


            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Aaadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

            // Aaadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias()/*PRIVATE_KEY_ALIAS.trim()*/);
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os= new  FileOutputStream(RutaXML+".xml");
            TransformerFactory tf= TransformerFactory.newInstance();
            Transformer  trans=tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();


            NodeList nodeListhash = doc1.getElementsByTagName("ds:DigestValue");
            Node nodohash = nodeListhash.item(0);

            NodeList nodeListSignatureValue = doc1.getElementsByTagName("ds:SignatureValue");
            Node nodoSignatureValue = nodeListSignatureValue.item(0);


            if (nodohash instanceof Element){
                respuesta.put("codehash", nodohash.getTextContent().trim());
                respuesta.put("signaturevalue", nodoSignatureValue.getTextContent().trim());
            }



        } catch (Exception e) {
            log.info(ErrorLog.printStackTraceToString(e));
        }

        return respuesta;


    }
    public static Map<String, Object> crearZip(String rutaXML, File signatureFile,String nombreFile) {

        byte[] buffer = new byte[1024];

        Map<String, Object> respuesta = new HashMap<>();


        try{


            FileOutputStream fos = new FileOutputStream(rutaXML+".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze= new ZipEntry(nombreFile+".xml");
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(rutaXML+".xml");

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();
            zos.closeEntry();
            zos.close();
            //fos.close();

            /*ELIMINANDO EL ARCHIVO XML (SOLO QUEDARA EL ZIP)*/

            System.out.println("NOMBRE DEL ARCHIVO "+ rutaXML+".xml");

            File file = new File(rutaXML+".xml");

            if(file.exists()){
                file.delete();
            }

            //************* LEEMOS EL DOCUMENTO ZIP GENERADO PARA PODER ENVIAR AL SISTEMA DE BOLETAJE

            ZipInputStream zisEnvio = new ZipInputStream(new FileInputStream(rutaXML+".zip"));

            ZipEntry entrada;


            while (null != (entrada = zisEnvio.getNextEntry())){

                byte[] buf = new byte[1024];
                int lenfile;
                StringBuffer contentXml = new StringBuffer();

                if(!entrada.isDirectory()) {

                    while ((lenfile = zisEnvio.read(buf)) > 0) {
                        contentXml.append(new String(buf, 0, lenfile, "UTF-8"));
                    }

                    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    InputSource inputSource = new InputSource();
                    inputSource.setCharacterStream(new StringReader(contentXml.toString()));

                    Document doc = documentBuilder.parse(inputSource);

                    NodeList nodeListhash = doc.getElementsByTagName("ds:DigestValue");
                    Node nodohash = nodeListhash.item(0);

						    /*NodeList nodeListSignatureValue = doc.getElementsByTagName("ds:SignatureValue");
						    Node nodoSignatureValue = nodeListSignatureValue.item(0);*/


                    if (nodohash instanceof Element){
                        respuesta.put("codehash", nodohash.getTextContent().trim());
                        //respuesta.put("signaturevalue", nodoSignatureValue.getTextContent().trim());
                    }
                }
            }

            zisEnvio.close();


        }catch(Exception e){
            log.info(ErrorLog.printStackTraceToString(e));
        }
        return respuesta;
    }


}
