package com.alo.digital.transportes.webapp.efact.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.alo.digital.transportes.webapp.efact.dao.VariosDao;
import com.alo.digital.transportes.webapp.efact.dao.VariosIDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.text.pdf.BarcodePDF417;

import com.alo.digital.transportes.webapp.efact.beans.V_Varios_FacturacionBean;

public class GeneraDocumentoFe {

    private static final Log log = LogFactory.getLog(GeneraDocumentoFe.class);

    public static void/* Map<String, Object> */ GeneraDocumentoFacturaXML(
            Map<String, Object> map/* B_VentaBean venta */, V_Varios_FacturacionBean empresa) {

        // Map<String, Object> respuesta = new HashMap<>();

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

            // Extensions XML
            Element ublExtensions = doc.createElement("ext:UBLExtensions");
            rootElementInvoice.appendChild(ublExtensions);

            Element ublExtension = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtension);

            Element extExtensionContent = doc.createElement("ext:ExtensionContent");
            ublExtension.appendChild(extExtensionContent);

            Element sacAdditionalInformation = doc.createElement("sac:AdditionalInformation");
            extExtensionContent.appendChild(sacAdditionalInformation);

            // ****************************** VERIFICAR CON LA SUNAT SI SON
            // MANDATORIOS ESTOS CAMPOS DE TOTALES
            // *******************************************************************

            // TOTAL DE VENTA OPERACIONES GRAVADAS (MANDATORIO)
            Element sacAdditionalMonetaryTotalGravadas = doc.createElement("sac:AdditionalMonetaryTotal");
            sacAdditionalInformation.appendChild(sacAdditionalMonetaryTotalGravadas);

            Element cbcIDGravadas = doc.createElement("cbc:ID");
            cbcIDGravadas.appendChild(doc.createTextNode(map.get("CodigoTotalVenta").toString()));// CODIGO
            // 1003
            // ES
            // PARA
            // PASAJES
            // -
            // CODIGO
            // PARA
            // ENCOMIENDAS
            // ES
            // 1001
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcIDGravadas);

            Element cbcPayableAmountGravadas = doc.createElement("cbc:PayableAmount");
            cbcPayableAmountGravadas.appendChild(doc.createTextNode(String.valueOf(map.get("Total")))); // "0.00"
            // ///
            // VERIFICAR
            // ES
            // TOTAL
            // SIN
            // IGV
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcPayableAmountGravadas);

            Attr attrGravadas = doc.createAttribute("currencyID");
            attrGravadas.setValue(map.get("Moneda").toString());
            cbcPayableAmountGravadas.setAttributeNode(attrGravadas);

            // TOTAL DE VENTA MONTO POR VENTA (EN LETRAS)
            Element sacAdditionalProperty = doc.createElement("sac:AdditionalProperty");
            sacAdditionalInformation.appendChild(sacAdditionalProperty);

            Element cbcIDProperty = doc.createElement("cbc:ID");
            cbcIDProperty.appendChild(doc.createTextNode(Utils.CODIGO_MONTO_LETRAS));
            sacAdditionalProperty.appendChild(cbcIDProperty);

            Element cbcValue = doc.createElement("cbc:Value");
            cbcValue.appendChild(doc.createTextNode(map.get("MontoLetras").toString()));// "CUATROCIENTOS
            // VEINTITRES
            // MIL
            // DOSCIENTOS
            // VEINTICINCO
            // Y
            // 00/100"
            sacAdditionalProperty.appendChild(cbcValue);

            // EXTENSION PARA LA FIRMA DIGITAL

            Element ublExtensionSignature = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtensionSignature);

            Text texto = doc.createTextNode(" ");
            Element ublExtensionContentSignature = doc.createElement("ext:ExtensionContent");
            ublExtensionContentSignature.appendChild(texto);
            ublExtensionSignature.appendChild(ublExtensionContentSignature);

            // ********************************************************************************

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
            cbcID.appendChild(doc.createTextNode(map.get("DocumentoElectronico").toString())); // "F703-0000689"
            rootElementInvoice.appendChild(cbcID);

            // FECHA DE EMISION
            Element cbcIssueDate = doc.createElement("cbc:IssueDate");
            cbcIssueDate.appendChild(doc.createTextNode(map.get("FechaEmision").toString()));// "2017-01-01"
            rootElementInvoice.appendChild(cbcIssueDate);

            // TIPO DE DOCUMENTO (01 FACTURA ...)
            Element cbcInvoiceTypeCode = doc.createElement("cbc:InvoiceTypeCode");
            cbcInvoiceTypeCode.appendChild(doc.createTextNode(map.get("TipoDocumento").toString())); // 01
            rootElementInvoice.appendChild(cbcInvoiceTypeCode);

            // CODIGO DE MONEDA (CATALOGO N°. 2)
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
            cacPartyIdentificationID.appendChild(doc.createTextNode(empresa.getRuc()));
            cacPartyIdentification.appendChild(cacPartyIdentificationID);

            Element cacPartySignatureName = doc.createElement("cac:PartyName");
            cacSignatoryParty.appendChild(cacPartySignatureName);

            // RAZON SOCIAL DE LA EMPRESA EMISORA
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
            rootElementInvoice.appendChild(cacAccountingSupplierParty);

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

            // CODIGO POSTAL (UBIGEO)
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
            // DATOS DEL RECEPTOR
            Element cacAccountingCustomerParty = doc.createElement("cac:AccountingCustomerParty");
            rootElementInvoice.appendChild(cacAccountingCustomerParty);

            Element cbcCustomerAssignedAccountIDReceptor = doc.createElement("cbc:CustomerAssignedAccountID");
            cbcCustomerAssignedAccountIDReceptor
                    .appendChild(doc.createTextNode((map.get("TipoDocumento").toString().trim().equals("01")
                            ? map.get("Ruc").toString() : map.get("DNI").toString()))); // "20341198217"
            cacAccountingCustomerParty.appendChild(cbcCustomerAssignedAccountIDReceptor);

            Element cbcAdditionalAccountIDReceptor = doc.createElement("cbc:AdditionalAccountID");
            cbcAdditionalAccountIDReceptor.appendChild(doc.createTextNode(map.get("TipoDocumentoReceptor").toString())); // "6"
            cacAccountingCustomerParty.appendChild(cbcAdditionalAccountIDReceptor);

            Element cacPartyReceptor = doc.createElement("cac:Party");
            cacAccountingCustomerParty.appendChild(cacPartyReceptor);

            Element cacPartyLegalEntityReceptor = doc.createElement("cac:PartyLegalEntity");
            cacPartyReceptor.appendChild(cacPartyLegalEntityReceptor);

            Element cbcRegistrationNameReceptor = doc.createElement("cbc:RegistrationName");
            cbcRegistrationNameReceptor
                    .appendChild(doc.createCDATASection((map.get("TipoDocumento").toString().trim().equals("01")
                            ? map.get("Razon").toString() : map.get("Nombre").toString())));// "VISANET
            // -
            // CIA
            // PERUANA
            // DE
            // MEDIOS
            // DE
            // PAGO
            // SAC"
            cacPartyLegalEntityReceptor.appendChild(cbcRegistrationNameReceptor);

            // *******************************************************************************************
            // INFORMACION DE SUMATORIA IVG GLOBAL


            Element cacTaxTotalGlobal = doc.createElement("cac:TaxTotal");
            rootElementInvoice.appendChild(cacTaxTotalGlobal);

            Element cbcTaxAmountGlobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA
            /// DEL
            /// IGV
            /// TOTAL
            /// (PARA
            /// PASAJES
            /// 0)
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
            cbcTaxExemptionReasonCodeGlobal.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR
            // CATALOGO
            // N�
            // 7
            // (
            // CODIGO
            // 20
            // PARA
            // PASAJES
            // -
            // CODIGO
            // 10
            // PARA
            // ENCOMIENDAS)
            cacTaxtCategoryGlobal.appendChild(cbcTaxExemptionReasonCodeGlobal);

            Element cacTaxSchemeGlobal = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobal.appendChild(cacTaxSchemeGlobal);

            Element cacTaxSchemeIDGlobal = doc.createElement("cbc:ID");
            // cacTaxSchemeIDGlobal.appendChild(doc.createTextNode(Utils.CODIGO_TIPO_TRIBUTO));//
            // VERIFICAR CATALOGO N� 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cacTaxSchemeIDGlobal);

            Element cbcTaxSchemeNameGlobal = doc.createElement("cbc:Name");
            // cbcTaxSchemeNameGlobal.appendChild(doc.createTextNode(Utils.NOMBRE_TRIBUTO));//
            // VERIFICAR CATALOGO N� 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxSchemeNameGlobal);

            Element cbcTaxTypeCodeGlobal = doc.createElement("cbc:TaxTypeCode");
            // cbcTaxTypeCodeGlobal.appendChild(doc.createTextNode(Utils.CODIGO_INTERNACIONAL_TRIBUTO));//
            // VERIFICAR CATALOGO N� 5 (CODIGO 1000)
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
            // INFORMACI�N DEL ARTICULO
            Element cacInvoiceLine = doc.createElement("cac:InvoiceLine");
            rootElementInvoice.appendChild(cacInvoiceLine);

            Element cbcIDInvoiceLine = doc.createElement("cbc:ID");
            cbcIDInvoiceLine.appendChild(doc.createTextNode("1"));// SIMPRE VA
            // SER 1 (YA
            // QUE ES UN
            // SOLO
            // ITEM)
            cacInvoiceLine.appendChild(cbcIDInvoiceLine);

            // CANTIDA DE ARTICULOS
            Element cbcInvoicedQuantity = doc.createElement("cbc:InvoicedQuantity");
            cbcInvoicedQuantity.appendChild(doc.createTextNode("1"));// SIMPRE
            // VA
            // SER 1
            // (YA
            // QUE
            // ES UN
            // SOLO
            // ITEM)
            cacInvoiceLine.appendChild(cbcInvoicedQuantity);

            Attr attrInvoicedQuantity = doc.createAttribute("unitCode");
            attrInvoicedQuantity.setValue(empresa.getCodigoUnidadMedida()); // CODIGO
            // DE
            // UNIDAD
            // DE
            // MEDIDA
            cbcInvoicedQuantity.setAttributeNode(attrInvoicedQuantity);

            // VALOR DE VENTA DEL ITEM
            Element cbcLineExtensionAmount = doc.createElement("cbc:LineExtensionAmount");
            cbcLineExtensionAmount.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));
            cacInvoiceLine.appendChild(cbcLineExtensionAmount);

            Attr attrLineExtensionAmount = doc.createAttribute("currencyID");
            attrLineExtensionAmount.setValue(map.get("Moneda").toString());
            cbcLineExtensionAmount.setAttributeNode(attrLineExtensionAmount);

            // PRECIO UNITARIO O VALOR REFERENCIAL UNITARIO EN OPERACIONES NO
            // ONEROSAS
            Element cacPricingReference = doc.createElement("cac:PricingReference");
            cacInvoiceLine.appendChild(cacPricingReference);

            Element cacAlternativeConditionPrice = doc.createElement("cac:AlternativeConditionPrice");
            cacPricingReference.appendChild(cacAlternativeConditionPrice);

            Element cbcPriceAmount = doc.createElement("cbc:PriceAmount");
            cbcPriceAmount.appendChild(doc.createTextNode(String.valueOf(map.get("Total"))));
            cacAlternativeConditionPrice.appendChild(cbcPriceAmount);

            Attr attrPriceAmount = doc.createAttribute("currencyID");
            attrPriceAmount.setValue(map.get("Moneda").toString());
            cbcPriceAmount.setAttributeNode(attrPriceAmount);

            //// REGUNTAR QUE CODIGO VA IR !!!!!! 2017-11-26
            Element cbcPriceTypeCode = doc.createElement("cbc:PriceTypeCode");
            cbcPriceTypeCode.appendChild(doc.createTextNode("01"));// CATALOGO
            // N� 16
            // (01-
            // Precio
            // unitario
            // (incluye
            // el IGV))
            // (02 -
            // Valor
            // referencial
            // unitario
            // en
            // operaciones
            // no
            // onerosas)
            cacAlternativeConditionPrice.appendChild(cbcPriceTypeCode);

            // AFECTACION AL IGV POR ITEM
            Element cacTaxTotal = doc.createElement("cac:TaxTotal");
            cacInvoiceLine.appendChild(cacTaxTotal);

            Element cbcTaxAmount = doc.createElement("cbc:TaxAmount");
            cbcTaxAmount.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// MONTO
            /// DEL
            /// IGV
            /// (PARA
            /// PASAJES
            /// 0)
            cacTaxTotal.appendChild(cbcTaxAmount);

            Attr attrTaxAmount = doc.createAttribute("currencyID");
            attrTaxAmount.setValue(map.get("Moneda").toString());
            cbcTaxAmount.setAttributeNode(attrTaxAmount);

            Element cacTaxSubtotal = doc.createElement("cac:TaxSubtotal");
            cacTaxTotal.appendChild(cacTaxSubtotal);

            Element cbcTaxAmountSubtotal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotal.appendChild(cbcTaxAmountSubtotal);

            Attr attrTaxAmountSubtotal = doc.createAttribute("currencyID");
            attrTaxAmountSubtotal.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotal.setAttributeNode(attrTaxAmountSubtotal);

            Element cacTaxtCategory = doc.createElement("cac:TaxCategory");
            cacTaxSubtotal.appendChild(cacTaxtCategory);

            Element cbcTaxExemptionReasonCode = doc.createElement("cbc:TaxExemptionReasonCode");
            cbcTaxExemptionReasonCode.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR
            // CATALOGO
            // N�
            // 7
            // (
            // CODIGO
            // 20
            // PARA
            // PASAJES
            // -
            // CODIGO
            // 10
            // PARA
            // ENCOMIENDAS)
            cacTaxtCategory.appendChild(cbcTaxExemptionReasonCode);

            Element cacTaxScheme = doc.createElement("cac:TaxScheme");
            cacTaxtCategory.appendChild(cacTaxScheme);

            Element cacTaxSchemeID = doc.createElement("cbc:ID");
            cacTaxSchemeID.appendChild(doc.createTextNode(map.get("Servicio").equals("B")
                    ? Utils.CODIGO_TIPO_TRIBUTO_PASAJES : Utils.CODIGO_INTERNACIONAL_TRIBUTO_ENCOMIENDA));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            // //codigo
            // para
            // pasajes
            // o
            // encimiendas
            // NUMERO
            // 100
            // |
            // 9997
            cacTaxScheme.appendChild(cacTaxSchemeID);

            Element cbcTaxSchemeName = doc.createElement("cbc:Name");
            cbcTaxSchemeName.appendChild(doc.createTextNode(
                    map.get("Servicio").equals("B") ? Utils.NOMBRE_TRIBUTO_PASAJES : Utils.NOMBRE_TRIBUTO_ENCOMIENDA));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxScheme.appendChild(cbcTaxSchemeName);

            Element cbcTaxTypeCode = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCode.appendChild(doc.createTextNode(map.get("Servicio").equals("B")
                    ? Utils.CODIGO_INTERNACIONAL_TRIBUTO_PASAJES : Utils.CODIGO_INTERNACIONAL_TRIBUTO_ENCOMIENDA));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxScheme.appendChild(cbcTaxTypeCode);

            // DESCRIPCION DEL ITEM
            Element cacItem = doc.createElement("cac:Item");
            cacInvoiceLine.appendChild(cacItem);

            Element cbcDescription = doc.createElement("cbc:Description");

            if (map.get("Servicio").toString().trim().equals("B")) {
                cbcDescription.appendChild(doc.createCDATASection("SERVICIO DE TRANSPORTE EN LA RUTA: "
                        + map.get("DestinoD") /* venta.getDestinoD() */));
                cacItem.appendChild(cbcDescription);
            } else {

                String Descripcion = "";
                if (!map.get("Cantidad1").toString().trim().equals("")
                        && !map.get("Descripcion1").toString().trim().equals("")) {

                    Descripcion = map.get("Cantidad1").toString().trim() + " "
                            + map.get("Descripcion1").toString().trim();
                }
                if (!map.get("Cantidad2").toString().trim().equals("")
                        && !map.get("Descripcion2").toString().trim().equals("")) {

                    Descripcion = Descripcion + "/" + map.get("Cantidad2").toString().trim() + " "
                            + map.get("Descripcion2").toString().trim();
                }
                if (!map.get("Cantidad3").toString().trim().equals("")
                        && !map.get("Descripcion3").toString().trim().equals("")) {

                    Descripcion = Descripcion + "/" + map.get("Cantidad3").toString().trim() + " "
                            + map.get("Descripcion3").toString().trim();
                }

                cbcDescription.appendChild(doc.createCDATASection("SERVICIO DE ENVIO DE ENCOMIENDA: " + Descripcion));
                cacItem.appendChild(cbcDescription);
            }

            // VALOR UNITARIO POR ITEM
            Element cacPrice = doc.createElement("cac:Price");
            cacInvoiceLine.appendChild(cacPrice);

            Element cbcPriceAmountItem = doc.createElement("cbc:PriceAmount");
            cbcPriceAmountItem.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));
            cacPrice.appendChild(cbcPriceAmountItem);

            Attr attrPriceAmountItem = doc.createAttribute("currencyID");
            attrPriceAmountItem.setValue(map.get("Moneda").toString());
            cbcPriceAmountItem.setAttributeNode(attrPriceAmountItem);

            // ******* GENERANDO LA FIRMA DIGITAL

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(empresa.getRutaEnvioSunat() + empresa.getRuc() + "-"
                    + map.get("TipoDocumento") + "-" + map.get("DocumentoElectronico") + ".xml"));
            // StreamResult result = new StreamResult(new
            // File(empresa.getRutaEnvioSunat()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml")+".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioSunat() + empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");
            // String RutaXML =
            // empresa.getRutaEnvioSunat()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml");

            String NombreFile = empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");
            // String NombreFile =
            // empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml");
            // String RutaZip =
            // empresa.getRutaEnvioSunat()+map.get("DocumentoZip").toString();

            Init.init();

            DocumentBuilderFactory dof = DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            // Si el Documento XML no tiene la estructura Correcta entra al
            // catch SAXException
            Document doc1 = dof.newDocumentBuilder().parse(new FileInputStream(RutaXML + ".xml"));

            Constants.setSignatureSpecNSprefix("ds"); // Sino, pone por defecto
            // como prefijo: "ns"

            System.out.println("LINEA 1");
            // Cargamos el almacen de claves
            KeyStore ks = KeyStore.getInstance(Utils.KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()), empresa.getKeystorePassword().toCharArray());

            System.out.println("LINEA 2");
            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),
                    empresa.getKeystorePassword().toCharArray());
            File signatureFile = new File(RutaXML + ".xml");
            // @SuppressWarnings("deprecation")
            String baseURI = signatureFile.toString(); // BaseURI para las URL
            // Relativas.

            System.out.println("LINEA 3");
            // Instanciamos un objeto XMLSignature desde el Document. El
            // algoritmo de firma sera RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);

            ///// VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA 2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(1).appendChild(xmlSignature.getElement());

            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Anadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
            System.out.println("LINEA 4");
            // Anadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias());
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os = new FileOutputStream(RutaXML + ".xml");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();

            /*
             * NodeList nodeListhash =
             * doc1.getElementsByTagName("ds:DigestValue"); Node nodohash =
             * nodeListhash.item(0);
             *
             * if (nodohash instanceof Element){ respuesta.put("codehash",
             * nodohash.getTextContent().trim()); System.out.println(
             * "CODIGO HASH --> "+ nodohash.getTextContent().trim()); }
             */

            crearZip(RutaXML, signatureFile, NombreFile/* ,RutaZip */);

        } catch (Exception e) {
            log.info(Utils.printStackTraceToString(e));
        }

        // return respuesta;

    }

    public static void/* Map<String, Object> */ GeneraDocumentoNotaCreditoXML(Map<String, Object> map,
                                                                              V_Varios_FacturacionBean empresa) {

        // Map<String, Object> respuesta = new HashMap<>();

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

            // ****************************** VERIFICAR CON LA SUNAT SI SON
            // MANDATORIOS ESTOS CAMPOS DE TOTALES
            // *******************************************************************

            // TOTAL DE VENTA OPERACIONES GRAVADAS (MANDATORIO)
            Element sacAdditionalMonetaryTotalGravadas = doc.createElement("sac:AdditionalMonetaryTotal");
            sacAdditionalInformation.appendChild(sacAdditionalMonetaryTotalGravadas);

            Element cbcIDGravadas = doc.createElement("cbc:ID");
            cbcIDGravadas.appendChild(doc.createTextNode(map.get("CodigoTotalVenta").toString()));// CODIGO
            // 1003
            // ES
            // PARA
            // PASAJES
            // -
            // CODIGO
            // PARA
            // ENCOMIENDAS
            // ES
            // 1001
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcIDGravadas);

            Element cbcPayableAmountGravadas = doc.createElement("cbc:PayableAmount");
            cbcPayableAmountGravadas.appendChild(doc.createTextNode(String.valueOf(map.get("Total"))));
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcPayableAmountGravadas);

            Attr attrGravadas = doc.createAttribute("currencyID");
            attrGravadas.setValue(map.get("Moneda").toString());
            cbcPayableAmountGravadas.setAttributeNode(attrGravadas);

            // TOTAL DE VENTA MONTO POR VENTA (EN LETRAS)
            Element sacAdditionalProperty = doc.createElement("sac:AdditionalProperty");
            sacAdditionalInformation.appendChild(sacAdditionalProperty);

            Element cbcIDProperty = doc.createElement("cbc:ID");
            cbcIDProperty.appendChild(doc.createTextNode(Utils.CODIGO_MONTO_LETRAS));
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

            // *****************************************************************

            // VERSION DE UBL (2.0)
            Element ublVersionID = doc.createElement("cbc:UBLVersionID");
            ublVersionID.appendChild(doc.createTextNode("2.0"));
            rootElementCreditNote.appendChild(ublVersionID);

            // VERSION DE LA ESTRUCTURA DEL DOCUMENTO (VER PAG. 57)
            Element cbcCustomizationID = doc.createElement("cbc:CustomizationID");
            cbcCustomizationID.appendChild(doc.createTextNode("1.0"));
            rootElementCreditNote.appendChild(cbcCustomizationID);

            // NUMERO DEL DOCUMENTO DE LA NOTA DE CREDITO (SERIE Y NUMERO --
            // CARACTER INICIAL --> "F" CUANDO AFECTA A UNA FACTURA Y "B" CUANDO
            // AFECTA A UNA BOLETA)
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

            // *************************************************************
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

            // VALOR DEL TIPO DE DOCUEMTO A QUE SE APLICA
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
            cbcCustomerAssignedAccountID.appendChild(doc.createTextNode(empresa.getRuc())); // RUC
            // PALOMINO
            // (EJEMPLO)
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

            // NUMERO DE DOCUMENTO DEL ADQUIRIENTE (CUANDO ES A UNA FACTURA SE
            // LE ASIGNA EL RUC DEL ADQUIRIENTE, SI ES UNA BOLETA SE LE COLOCA
            // UN GUION "-")
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
            cbcTaxAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA
            /// DEL
            /// IGV
            /// TOTAL
            /// (PARA
            /// PASAJES
            /// 0)
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
            cbcTaxExemptionReasonCodeGlobal.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR
            // CATALOGO
            // N�
            // 7
            // (
            // CODIGO
            // 20
            // PARA
            // PASAJES
            // -
            // CODIGO
            // 10
            // PARA
            // ENCOMIENDAS)
            cacTaxtCategoryGlobal.appendChild(cbcTaxExemptionReasonCodeGlobal);

            Element cacTaxSchemeGlobal = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobal.appendChild(cacTaxSchemeGlobal);

            Element cacTaxSchemeIDGlobal = doc.createElement("cbc:ID");
            // cacTaxSchemeIDGlobal.appendChild(doc.createTextNode(Utils.CODIGO_TIPO_TRIBUTO));//
            // VERIFICAR CATALOGO N� 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cacTaxSchemeIDGlobal);

            Element cbcTaxSchemeNameGlobal = doc.createElement("cbc:Name");
            // cbcTaxSchemeNameGlobal.appendChild(doc.createTextNode(Utils.NOMBRE_TRIBUTO));//
            // VERIFICAR CATALOGO N� 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxSchemeNameGlobal);

            Element cbcTaxTypeCodeGlobal = doc.createElement("cbc:TaxTypeCode");
            // cbcTaxTypeCodeGlobal.appendChild(doc.createTextNode(Utils.CODIGO_INTERNACIONAL_TRIBUTO));//
            // VERIFICAR CATALOGO N� 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxTypeCodeGlobal);

            // *************************************************************

            Element cacPartyLegalEntityReceptor = doc.createElement("cac:PartyLegalEntity");
            cacPartyReceptor.appendChild(cacPartyLegalEntityReceptor);

            Element cbcRegistrationNameReceptor = doc.createElement("cbc:RegistrationName");
            cbcRegistrationNameReceptor.appendChild(doc.createCDATASection(map.get("Razon").toString()));
            cacPartyLegalEntityReceptor.appendChild(cbcRegistrationNameReceptor);

            // *************************************************************
            // IMPORTE TOTAL A PAGAR POR EL DOCUMENTO

            Element cacLegalMonetaryTotal = doc.createElement("cac:LegalMonetaryTotal");
            rootElementCreditNote.appendChild(cacLegalMonetaryTotal);

            Element cbcPayableAmount = doc.createElement("cbc:PayableAmount");
            cbcPayableAmount.appendChild(doc.createTextNode(String.valueOf(map.get("Total"))));
            cacLegalMonetaryTotal.appendChild(cbcPayableAmount);

            Attr attrMonetaryTotal = doc.createAttribute("currencyID");
            attrMonetaryTotal.setValue(map.get("Moneda").toString()/* "PEN" */);
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
            cbcTaxAmountGlobalCredit.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA
            /// DEL
            /// IGV
            /// TOTAL
            /// (PARA
            /// PASAJES
            /// 0)
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
            cbcTaxExemptionReasonCodeGlobalCredit
                    .appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR
            // CATALOGO
            // N�
            // 7
            // (
            // CODIGO
            // 20
            // PARA
            // PASAJES
            // -
            // CODIGO
            // 10
            // PARA
            // ENCOMIENDAS)
            cacTaxtCategoryGlobalCredit.appendChild(cbcTaxExemptionReasonCodeGlobalCredit);

            Element cacTaxSchemeGlobalCredit = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobalCredit.appendChild(cacTaxSchemeGlobalCredit);

            Element cacTaxSchemeIDGlobalCredit = doc.createElement("cbc:ID");
            // cacTaxSchemeIDGlobalCredit.appendChild(doc.createTextNode(Utils.CODIGO_TIPO_TRIBUTO));//
            // VERIFICAR CATALOGO N� 5 (CODIGO 1000)
            cacTaxSchemeGlobalCredit.appendChild(cacTaxSchemeIDGlobalCredit);

            Element cbcTaxSchemeNameGlobalCredit = doc.createElement("cbc:Name");
            // cbcTaxSchemeNameGlobalCredit.appendChild(doc.createTextNode(Utils.NOMBRE_TRIBUTO));//
            // VERIFICAR CATALOGO N� 5 (CODIGO 1000)
            cacTaxSchemeGlobalCredit.appendChild(cbcTaxSchemeNameGlobalCredit);

            Element cbcTaxTypeCodeGlobalCredit = doc.createElement("cbc:TaxTypeCode");
            // cbcTaxTypeCodeGlobalCredit.appendChild(doc.createTextNode(Utils.CODIGO_INTERNACIONAL_TRIBUTO));//
            // VERIFICAR CATALOGO N� 5 (CODIGO 1000)
            cacTaxSchemeGlobalCredit.appendChild(cbcTaxTypeCodeGlobalCredit);

            // ******* GENERANDO LA FIRMA DIGITAL

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(empresa.getRutaEnvioSunat() + empresa.getRuc() + "-"
                    + map.get("TipoDocumento") + "-" + map.get("DocumentoElectronico") + ".xml"));
            // StreamResult result = new StreamResult(new
            // File(empresa.getRutaEnvioSunat()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml")+".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioSunat() + empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");
            // String RutaXML =
            // empresa.getRutaEnvioSunat()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml");
            String NombreFile = empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");
            // String NombreFile =
            // empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml");
            // String RutaZip =
            // empresa.getRutaEnvioSunat()+map.get("DocumentoZip").toString();

            Init.init();

            DocumentBuilderFactory dof = DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            // Si el Documento XML no tiene la estructura Correcta entra al
            // catch SAXException
            Document doc1 = dof.newDocumentBuilder().parse(new FileInputStream(RutaXML + ".xml"));

            Constants.setSignatureSpecNSprefix("ds"); // Sino, pone por defecto
            // como prefijo: "ns"

            System.out.println("LINEA 1");
            // Cargamos el almacen de claves
            KeyStore ks = KeyStore.getInstance(Utils.KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()), empresa.getKeystorePassword().toCharArray());

            System.out.println("LINEA 2");
            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),
                    empresa.getKeystorePassword().toCharArray());
            File signatureFile = new File(RutaXML + ".xml");
            // @SuppressWarnings("deprecation")
            String baseURI = signatureFile.toString(); // BaseURI para las URL
            // Relativas.

            System.out.println("LINEA 3");
            // Instanciamos un objeto XMLSignature desde el Document. El
            // algoritmo de firma sera RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);

            ///// VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA 2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(1).appendChild(xmlSignature.getElement());

            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Anadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
            System.out.println("LINEA 4");
            // Anadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias());
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os = new FileOutputStream(RutaXML + ".xml");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();

            /*
             * NodeList nodeListhash =
             * doc1.getElementsByTagName("ds:DigestValue"); Node nodohash =
             * nodeListhash.item(0);
             *
             * if (nodohash instanceof Element){ respuesta.put("codehash",
             * nodohash.getTextContent().trim()); }
             */

            crearZip(RutaXML, signatureFile, NombreFile/* ,RutaZip */);

        } catch (Exception e) {
            log.info(Utils.printStackTraceToString(e));
        }

        // return respuesta;

    }

    public static void/* Map<String, Object> */ GeneraDocumentoNotaDebitoXML(Map<String, Object> map,
                                                                             V_Varios_FacturacionBean empresa) {

        // Map<String, Object> respuesta = new HashMap<>();

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
            cbcIDGravadas.appendChild(doc.createTextNode(map.get("CodigoTotalVenta").toString()));// CODIGO
            // 1003
            // ES
            // PARA
            // PASAJES
            // -
            // CODIGO
            // PARA
            // ENCOMIENDAS
            // ES
            // 1001
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
            cbcIDProperty.appendChild(doc.createTextNode(Utils.CODIGO_MONTO_LETRAS));
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

            // *****************************************************************

            // VERSION DE UBL (2.0)
            Element ublVersionID = doc.createElement("cbc:UBLVersionID");
            ublVersionID.appendChild(doc.createTextNode("2.0"));
            rootElementDebitNote.appendChild(ublVersionID);

            // VERSION DE LA ESTRUCTURA DEL DOCUMENTO (VER PAG. 57)
            Element cbcCustomizationID = doc.createElement("cbc:CustomizationID");
            cbcCustomizationID.appendChild(doc.createTextNode("1.0"));
            rootElementDebitNote.appendChild(cbcCustomizationID);

            // NUMERO DEL DOCUMENTO DE LA NOTA DE CREDITO (SERIE Y NUMERO --
            // CARACTER INICIAL --> "F" CUANDO AFECTA A UNA FACTURA Y "B" CUANDO
            // AFECTA A UNA BOLETA)
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

            // *************************************************************
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

            // NUMERO DE DOCUMENTO DEL ADQUIRIENTE (CUANDO ES A UNA FACTURA SE
            // LE ASIGNA EL RUC DEL ADQUIRIENTE, SI ES UNA BOLETA SE LE COLOCA
            // UN GUION "-")
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
            cbcTaxAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA
            /// DEL
            /// IGV
            /// TOTAL
            /// (PARA
            /// PASAJES
            /// 0)
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
            cbcTaxExemptionReasonCodeGlobal.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR
            // CATALOGO
            // No
            // 7
            // (
            // CODIGO
            // 20
            // PARA
            // PASAJES
            // -
            // CODIGO
            // 10
            // PARA
            // ENCOMIENDAS)
            cacTaxtCategoryGlobal.appendChild(cbcTaxExemptionReasonCodeGlobal);

            Element cacTaxSchemeGlobal = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobal.appendChild(cacTaxSchemeGlobal);

            Element cacTaxSchemeIDGlobal = doc.createElement("cbc:ID");
            // cacTaxSchemeIDGlobal.appendChild(doc.createTextNode(Utils.CODIGO_TIPO_TRIBUTO));//
            // VERIFICAR CATALOGO No 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cacTaxSchemeIDGlobal);

            Element cbcTaxSchemeNameGlobal = doc.createElement("cbc:Name");
            // cbcTaxSchemeNameGlobal.appendChild(doc.createTextNode(Utils.NOMBRE_TRIBUTO));//
            // VERIFICAR CATALOGO No 5 (CODIGO 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxSchemeNameGlobal);

            Element cbcTaxTypeCodeGlobal = doc.createElement("cbc:TaxTypeCode");
            // cbcTaxTypeCodeGlobal.appendChild(doc.createTextNode(Utils.CODIGO_INTERNACIONAL_TRIBUTO));//
            // VERIFICAR CATALOGO No 5 (CODIGO 1000)
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

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(empresa.getRutaEnvioSunat() + empresa.getRuc() + "-"
                    + map.get("TipoDocumento") + "-" + map.get("DocumentoElectronico") + ".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioSunat() + empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");
            String NombreFile = empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");

            Init.init();

            DocumentBuilderFactory dof = DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            // Si el Documento XML no tiene la estructura Correcta entra al
            // catch SAXException
            Document doc1 = dof.newDocumentBuilder().parse(new FileInputStream(RutaXML + ".xml"));

            Constants.setSignatureSpecNSprefix("ds"); // Sino, pone por defecto
            // como prefijo: "ns"

            System.out.println("LINEA 1");
            // Cargamos el almacen de claves
            KeyStore ks = KeyStore.getInstance(Utils.KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()), empresa.getKeystorePassword().toCharArray());

            System.out.println("LINEA 2");
            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),
                    empresa.getKeystorePassword().toCharArray());
            File signatureFile = new File(RutaXML + ".xml");
            // @SuppressWarnings("deprecation")
            String baseURI = signatureFile.toString(); // BaseURI para las URL
            // Relativas.

            System.out.println("LINEA 3");
            // Instanciamos un objeto XMLSignature desde el Document. El
            // algoritmo de firma sera RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);

            ///// VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA 2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(1).appendChild(xmlSignature.getElement());

            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Anadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
            System.out.println("LINEA 4");
            // Anadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias());
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os = new FileOutputStream(RutaXML + ".xml");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();

            /*
             * NodeList nodeListhash =
             * doc1.getElementsByTagName("ds:DigestValue"); Node nodohash =
             * nodeListhash.item(0);
             *
             * if (nodohash instanceof Element){ respuesta.put("codehash",
             * nodohash.getTextContent().trim()); }
             */

            crearZip(RutaXML, signatureFile, NombreFile);

        } catch (Exception e) {
            log.info(Utils.printStackTraceToString(e));
        }

        // return respuesta;

    }

    public static void GeneraDocumentoComunicacionBajaXMLRC(Map<String, Object> map, V_Varios_FacturacionBean empresa) {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element rootElementVoidedDocument = doc.createElement("SummaryDocuments");
            doc.appendChild(rootElementVoidedDocument);

            // GENERANDO ATRIBUTOS VoidedDocuments

            Attr attrVoidedDocuments = doc.createAttribute("xmlns");
            attrVoidedDocuments.setValue("urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1");
            rootElementVoidedDocument.setAttributeNode(attrVoidedDocuments);

            Attr attrVoidedCac = doc.createAttribute("xmlns:cac");
            attrVoidedCac.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");
            rootElementVoidedDocument.setAttributeNode(attrVoidedCac);

            Attr attrVoidedcacadd = doc.createAttribute("xmlns:cacadd");
            attrVoidedcacadd.setValue("urn:e-billing:aggregates");
            rootElementVoidedDocument.setAttributeNode(attrVoidedcacadd);

            Attr attrVoidedCbc = doc.createAttribute("xmlns:cbc");
            attrVoidedCbc.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
            rootElementVoidedDocument.setAttributeNode(attrVoidedCbc);

            Attr attrVoidedcbcadd = doc.createAttribute("xmlns:cbcadd");
            attrVoidedcbcadd.setValue("urn:e-billing:basics");
            rootElementVoidedDocument.setAttributeNode(attrVoidedcbcadd);

            Attr attrVoidedccts = doc.createAttribute("xmlns:ccts");
            attrVoidedccts.setValue("urn:un:unece:uncefact:documentation:2");
            rootElementVoidedDocument.setAttributeNode(attrVoidedccts);

            Attr attrVoidedDs = doc.createAttribute("xmlns:ds");
            attrVoidedDs.setValue("http://www.w3.org/2000/09/xmldsig#");
            rootElementVoidedDocument.setAttributeNode(attrVoidedDs);

            Attr attrVoidedExt = doc.createAttribute("xmlns:ext");
            attrVoidedExt.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2");
            rootElementVoidedDocument.setAttributeNode(attrVoidedExt);

            Attr attrVoidedextadd = doc.createAttribute("xmlns:extadd");
            attrVoidedextadd.setValue("urn:e-billing:extension");
            rootElementVoidedDocument.setAttributeNode(attrVoidedextadd);

            Attr attrVoideqdt = doc.createAttribute("xmlns:qdt");
            attrVoideqdt.setValue("urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2");
            rootElementVoidedDocument.setAttributeNode(attrVoideqdt);

            Attr attrVoidedSac = doc.createAttribute("xmlns:sac");
            attrVoidedSac.setValue("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1");
            rootElementVoidedDocument.setAttributeNode(attrVoidedSac);

            Attr attrVoidedudt = doc.createAttribute("xmlns:udt");
            attrVoidedudt.setValue("urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2");
            rootElementVoidedDocument.setAttributeNode(attrVoidedudt);

            Element ublExtensions = doc.createElement("ext:UBLExtensions");
            rootElementVoidedDocument.appendChild(ublExtensions);

            Element ublExtension = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtension);

            Text texto = doc.createTextNode(" ");
            Element extExtensionContent = doc.createElement("ext:ExtensionContent");
            extExtensionContent.appendChild(texto);
            ublExtension.appendChild(extExtensionContent);

            /*
             * Element ublExtensionSignature =
             * doc.createElement("ext:UBLExtension");
             * ublExtensions.appendChild(ublExtensionSignature);
             *
             * Text texto = doc.createTextNode(" "); Element
             * ublExtensionContentSignature =
             * doc.createElement("ext:ExtensionContent");
             * ublExtensionContentSignature.appendChild(texto);
             * ublExtensionSignature.appendChild(ublExtensionContentSignature);
             */

            // *****************************************************************

            // VERSION DE UBL (2.0)
            Element ublVersionID = doc.createElement("cbc:UBLVersionID");
            ublVersionID.appendChild(doc.createTextNode("2.0"));
            rootElementVoidedDocument.appendChild(ublVersionID);

            // VERSION DE LA ESTRUCTURA DEL DOCUMENTO (VER PAG. 57)
            Element cbcCustomizationID = doc.createElement("cbc:CustomizationID");
            cbcCustomizationID.appendChild(doc.createTextNode("1.1"));
            rootElementVoidedDocument.appendChild(cbcCustomizationID);

            // IDENTIFICADOR DE GENERACION DE LA COMUNICACION
            Element cbcID = doc.createElement("cbc:ID");
            cbcID.appendChild(doc.createTextNode(map.get("DocumentoElectronico").toString()));
            rootElementVoidedDocument.appendChild(cbcID);

            // FECHA DE GENERACION DEL DOCUMENTO DADO DE BAJA
            Element cbcReferenceDate = doc.createElement("cbc:ReferenceDate");
            cbcReferenceDate.appendChild(doc.createTextNode(map.get("FechaEmisionDocumentoBaja").toString()));
            rootElementVoidedDocument.appendChild(cbcReferenceDate);

            // FECHA DE EMISION DE LA COMUNICACION DE BAJA
            Element cbcIssueDate = doc.createElement("cbc:IssueDate");
            cbcIssueDate.appendChild(doc.createTextNode(map.get("FechaEmision").toString()));
            rootElementVoidedDocument.appendChild(cbcIssueDate);

            // *************************************************************

            // CAC SIGNATURE

            Element cacSignature = doc.createElement("cac:Signature");
            rootElementVoidedDocument.appendChild(cacSignature);

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
            rootElementVoidedDocument.appendChild(cacAccountingSupplierParty);

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

            // DATOS DEL DOCUMENTODADO DE BAJA
            Element sacVoidedDocumentsLine = doc.createElement("sac:SummaryDocumentsLine");
            rootElementVoidedDocument.appendChild(sacVoidedDocumentsLine);

            // DATOS DEL DOCUMENTODADO DE BAJA
            Element cbcLineID = doc.createElement("cbc:LineID");
            cbcLineID.appendChild(doc.createTextNode("1"));
            sacVoidedDocumentsLine.appendChild(cbcLineID);

            // DATOS DEL DOCUMENTODADO DE BAJA
            Element cbcDocumentTypeCode = doc.createElement("cbc:DocumentTypeCode");
            cbcDocumentTypeCode.appendChild(doc.createTextNode(map.get("TipoDocumento").toString()));
            sacVoidedDocumentsLine.appendChild(cbcDocumentTypeCode);

            Element cbcDocumentID = doc.createElement("cbc:ID");
            cbcDocumentID
                    .appendChild(doc.createTextNode(map.get("Serie").toString() + "-" + map.get("Numero").toString()));
            sacVoidedDocumentsLine.appendChild(cbcDocumentID);

            Element cacAccountingCustomerParty = doc.createElement("cac:AccountingCustomerParty");
            sacVoidedDocumentsLine.appendChild(cacAccountingCustomerParty);
            // <CustomerAssignedAccountID>
            // AdditionalAccountID
            Element cbcCustomerAssignedAccountID2 = doc.createElement("cbc:CustomerAssignedAccountID"); // NUMERO
            // DE
            // DOCUMENTO
            // DE
            // RECEPTOR
            cbcCustomerAssignedAccountID2.appendChild(doc.createTextNode("71807093"));
            cacAccountingCustomerParty.appendChild(cbcCustomerAssignedAccountID2);

            Element cbcAdditionalAccountID2 = doc.createElement("cbc:AdditionalAccountID"); // TIPO
            // DE
            // DOCUMENTOD
            // E
            // RECEPTOR
            cbcAdditionalAccountID2.appendChild(doc.createTextNode("1"));
            cacAccountingCustomerParty.appendChild(cbcAdditionalAccountID2);

            Element cacStatus = doc.createElement("cac:Status");
            sacVoidedDocumentsLine.appendChild(cacStatus);

            Element cbcConditionCode = doc.createElement("cbc:ConditionCode");
            cbcConditionCode.appendChild(doc.createTextNode("3"));// 1|2| 3
            // CATALOGO
            // NUMERO 19
            // |ADICIONAR|MODIFICAR|ANULAR|ANULADO
            // EN EL DIA
            cacStatus.appendChild(cbcConditionCode);

            Element sacTotalAmount = doc.createElement("sac:TotalAmount");
            sacTotalAmount.appendChild(doc.createTextNode("3"));// PRECIO CON
            // IGV
            sacVoidedDocumentsLine.appendChild(sacTotalAmount);

            Attr attrTotalAmountcurrencyID = doc.createAttribute("currencyID");
            attrTotalAmountcurrencyID.setValue("PEN");
            sacTotalAmount.setAttributeNode(attrTotalAmountcurrencyID);

            Element sacBillingPayment = doc.createElement("sac:BillingPayment");
            sacVoidedDocumentsLine.appendChild(sacBillingPayment);

            Element cbcPaidAmount = doc.createElement("cbc:PaidAmount");
            cbcPaidAmount.appendChild(doc.createTextNode("3"));// MONTO SIN IGV
            sacBillingPayment.appendChild(cbcPaidAmount);

            Attr attrcbcPaidAmountcurrencyID = doc.createAttribute("currencyID");
            attrcbcPaidAmountcurrencyID.setValue("PEN");
            cbcPaidAmount.setAttributeNode(attrcbcPaidAmountcurrencyID);

            Element cbcInstructionID = doc.createElement("cbc:InstructionID");
            cbcInstructionID.appendChild(doc.createTextNode("01"));// CAT�LOGO
            // NUMERO 10
            // //TIPO DE
            // IMPORTE
            // IGV //01
            // ENOCMIENDAS
            // || 02
            // PASAJES
            sacBillingPayment.appendChild(cbcInstructionID);

            Element cacTaxTotal = doc.createElement("cac:TaxTotal");
            sacVoidedDocumentsLine.appendChild(cacTaxTotal);

            Element cbcTaxAmount = doc.createElement("cbc:TaxAmount");
            cbcTaxAmount.appendChild(doc.createTextNode("7.80"));// MONTO TOTAL
            // DEL IGV
            cacTaxTotal.appendChild(cbcTaxAmount);

            Attr attrcbcTaxAmountcurrencyID = doc.createAttribute("currencyID");
            attrcbcTaxAmountcurrencyID.setValue("PEN");
            cbcTaxAmount.setAttributeNode(attrcbcTaxAmountcurrencyID);

            Element cacTaxSubtotal = doc.createElement("cac:TaxSubtotal");
            cacTaxTotal.appendChild(cacTaxSubtotal);

            Element cbcTaxAmountsub = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountsub.appendChild(doc.createTextNode("7.80"));// MONTO
            // TOTAL DEL
            // IGV
            cacTaxSubtotal.appendChild(cbcTaxAmountsub);

            Attr attrcbcTaxAmountcurrencyIDsub = doc.createAttribute("currencyID");
            attrcbcTaxAmountcurrencyIDsub.setValue("PEN");
            cbcTaxAmountsub.setAttributeNode(attrcbcTaxAmountcurrencyIDsub);

            Element cacTaxCategory = doc.createElement("cac:TaxCategory");
            cacTaxSubtotal.appendChild(cacTaxCategory);

            Element cacTaxScheme = doc.createElement("cac:TaxScheme");
            cacTaxCategory.appendChild(cacTaxScheme);

            Element cbTaxSchemecID = doc.createElement("cbc:ID");
            cbTaxSchemecID.appendChild(doc.createTextNode("1000"));// MONTO
            // TOTAL DEL
            // IGV
            cacTaxScheme.appendChild(cbTaxSchemecID);

            Element cbcTaxSchemeName = doc.createElement("cbc:Name");
            cbcTaxSchemeName.appendChild(doc.createTextNode("7.80"));// TIPO DE
            // AFECTACION
            // DE
            // IGV
            cacTaxScheme.appendChild(cbcTaxSchemeName);

            Element cbcTaxSchemeTaxTypeCode = doc.createElement("cbc:TaxTypeCode");
            cbcTaxSchemeTaxTypeCode.appendChild(doc.createTextNode("VAT"));// TIPO
            // DE
            // AFECTACI�N
            // DE
            // IGV
            cacTaxScheme.appendChild(cbcTaxSchemeTaxTypeCode);

            // *************************************************************

            // ******* GENERANDO LA FIRMA DIGITAL

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(
                    empresa.getRutaEnvioSunat() + empresa.getRuc() + "-" + map.get("DocumentoElectronico") + ".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioSunat() + empresa.getRuc() + "-" + map.get("DocumentoElectronico");
            String NombreFile = empresa.getRuc() + "-" + map.get("DocumentoElectronico");

            Init.init();

            DocumentBuilderFactory dof = DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            // Si el Documento XML no tiene la estructura Correcta entra al
            // catch SAXException
            Document doc1 = dof.newDocumentBuilder().parse(new FileInputStream(RutaXML + ".xml"));

            Constants.setSignatureSpecNSprefix("ds"); // Sino, pone por defecto
            // como prefijo: "ns"

            // Cargamos el almacen de claves
            KeyStore ks = KeyStore.getInstance(Utils.KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()), empresa.getKeystorePassword().toCharArray());

            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),
                    empresa.getKeystorePassword().toCharArray());
            File signatureFile = new File(RutaXML + ".xml");
            // @SuppressWarnings("deprecation")
            String baseURI = signatureFile.toString(); // BaseURI para las URL
            // Relativas.

            // Instanciamos un objeto XMLSignature desde el Document. El
            // algoritmo de firma sera RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);

            ///// VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA 2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(0).appendChild(xmlSignature.getElement());

            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Anadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

            // Anadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias());
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os = new FileOutputStream(RutaXML + ".xml");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();

            crearZip(RutaXML, signatureFile, NombreFile);

        } catch (Exception e) {
            log.info(Utils.printStackTraceToString(e));
        }

    }

    public static void GeneraDocumentoComunicacionBajaXML(Map<String, Object> map, V_Varios_FacturacionBean empresa) {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element rootElementVoidedDocument = doc.createElement("VoidedDocuments");
            doc.appendChild(rootElementVoidedDocument);

            // GENERANDO ATRIBUTOS VoidedDocuments

            Attr attrVoidedDocuments = doc.createAttribute("xmlns");
            attrVoidedDocuments.setValue("urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1");
            rootElementVoidedDocument.setAttributeNode(attrVoidedDocuments);

            Attr attrVoidedCac = doc.createAttribute("xmlns:cac");
            attrVoidedCac.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2");
            rootElementVoidedDocument.setAttributeNode(attrVoidedCac);

            Attr attrVoidedCbc = doc.createAttribute("xmlns:cbc");
            attrVoidedCbc.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2");
            rootElementVoidedDocument.setAttributeNode(attrVoidedCbc);

            Attr attrVoidedDs = doc.createAttribute("xmlns:ds");
            attrVoidedDs.setValue("http://www.w3.org/2000/09/xmldsig#");
            rootElementVoidedDocument.setAttributeNode(attrVoidedDs);

            Attr attrVoidedExt = doc.createAttribute("xmlns:ext");
            attrVoidedExt.setValue("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2");
            rootElementVoidedDocument.setAttributeNode(attrVoidedExt);

            Attr attrVoidedSac = doc.createAttribute("xmlns:sac");
            attrVoidedSac.setValue("urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1");
            rootElementVoidedDocument.setAttributeNode(attrVoidedSac);

            Attr attrVoidedXsi = doc.createAttribute("xmlns:xsi");
            attrVoidedXsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
            rootElementVoidedDocument.setAttributeNode(attrVoidedXsi);

            Element ublExtensions = doc.createElement("ext:UBLExtensions");
            rootElementVoidedDocument.appendChild(ublExtensions);

            Element ublExtension = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtension);

            Text texto = doc.createTextNode(" ");
            Element extExtensionContent = doc.createElement("ext:ExtensionContent");
            extExtensionContent.appendChild(texto);
            ublExtension.appendChild(extExtensionContent);

            /*
             * Element ublExtensionSignature =
             * doc.createElement("ext:UBLExtension");
             * ublExtensions.appendChild(ublExtensionSignature);
             *
             * Text texto = doc.createTextNode(" "); Element
             * ublExtensionContentSignature =
             * doc.createElement("ext:ExtensionContent");
             * ublExtensionContentSignature.appendChild(texto);
             * ublExtensionSignature.appendChild(ublExtensionContentSignature);
             */

            // *****************************************************************

            // VERSION DE UBL (2.0)
            Element ublVersionID = doc.createElement("cbc:UBLVersionID");
            ublVersionID.appendChild(doc.createTextNode("2.0"));
            rootElementVoidedDocument.appendChild(ublVersionID);

            // VERSION DE LA ESTRUCTURA DEL DOCUMENTO (VER PAG. 57)
            Element cbcCustomizationID = doc.createElement("cbc:CustomizationID");
            cbcCustomizationID.appendChild(doc.createTextNode("1.0"));
            rootElementVoidedDocument.appendChild(cbcCustomizationID);

            // IDENTIFICADOR DE GENERACION DE LA COMUNICACION
            Element cbcID = doc.createElement("cbc:ID");
            cbcID.appendChild(doc.createTextNode(map.get("DocumentoElectronico").toString()));
            rootElementVoidedDocument.appendChild(cbcID);

            // FECHA DE GENERACION DEL DOCUMENTO DADO DE BAJA
            Element cbcReferenceDate = doc.createElement("cbc:ReferenceDate");
            cbcReferenceDate.appendChild(doc.createTextNode(map.get("FechaEmisionDocumentoBaja").toString()));
            rootElementVoidedDocument.appendChild(cbcReferenceDate);

            // FECHA DE EMISION DE LA COMUNICACION DE BAJA
            Element cbcIssueDate = doc.createElement("cbc:IssueDate");
            cbcIssueDate.appendChild(doc.createTextNode(map.get("FechaEmision").toString()));
            rootElementVoidedDocument.appendChild(cbcIssueDate);

            // *************************************************************

            // CAC SIGNATURE

            Element cacSignature = doc.createElement("cac:Signature");
            rootElementVoidedDocument.appendChild(cacSignature);

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
            rootElementVoidedDocument.appendChild(cacAccountingSupplierParty);

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
            cbcStreetName.appendChild(doc.createCDATASection(empresa.getDireccion()));
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

            // DATOS DEL DOCUMENTODADO DE BAJA
            Element sacVoidedDocumentsLine = doc.createElement("sac:VoidedDocumentsLine");
            rootElementVoidedDocument.appendChild(sacVoidedDocumentsLine);

            // DATOS DEL DOCUMENTODADO DE BAJA
            Element cbcLineID = doc.createElement("cbc:LineID");
            cbcLineID.appendChild(doc.createTextNode("1"));
            sacVoidedDocumentsLine.appendChild(cbcLineID);

            // DATOS DEL DOCUMENTODADO DE BAJA
            Element cbcDocumentTypeCode = doc.createElement("cbc:DocumentTypeCode");
            cbcDocumentTypeCode.appendChild(doc.createTextNode(map.get("TipoDocumento").toString()));
            sacVoidedDocumentsLine.appendChild(cbcDocumentTypeCode);

            // DATOS DEL DOCUMENTODADO DE BAJA
            Element cbcDocumentSerialID = doc.createElement("sac:DocumentSerialID");
            cbcDocumentSerialID.appendChild(doc.createTextNode(map.get("Serie").toString()));
            sacVoidedDocumentsLine.appendChild(cbcDocumentSerialID);

            // DATOS DEL DOCUMENTODADO DE BAJA
            Element cbcDocumentNumberID = doc.createElement("sac:DocumentNumberID");
            cbcDocumentNumberID.appendChild(doc.createTextNode(map.get("Numero").toString()));
            sacVoidedDocumentsLine.appendChild(cbcDocumentNumberID);

            // DATOS DEL DOCUMENTODADO DE BAJA
            Element cbcVoidReasonDescription = doc.createElement("sac:VoidReasonDescription");
            cbcVoidReasonDescription.appendChild(doc.createTextNode("Error en el Sistema"));
            sacVoidedDocumentsLine.appendChild(cbcVoidReasonDescription);

            // *************************************************************

            // ******* GENERANDO LA FIRMA DIGITAL

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(
                    empresa.getRutaEnvioSunat() + empresa.getRuc() + "-" + map.get("DocumentoElectronico") + ".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioSunat() + empresa.getRuc() + "-" + map.get("DocumentoElectronico");
            String NombreFile = empresa.getRuc() + "-" + map.get("DocumentoElectronico");

            Init.init();

            DocumentBuilderFactory dof = DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            // Si el Documento XML no tiene la estructura Correcta entra al
            // catch SAXException
            Document doc1 = dof.newDocumentBuilder().parse(new FileInputStream(RutaXML + ".xml"));

            Constants.setSignatureSpecNSprefix("ds"); // Sino, pone por defecto
            // como prefijo: "ns"

            // Cargamos el almacen de claves
            KeyStore ks = KeyStore.getInstance(Utils.KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()), empresa.getKeystorePassword().toCharArray());

            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),
                    empresa.getKeystorePassword().toCharArray());
            File signatureFile = new File(RutaXML + ".xml");
            // @SuppressWarnings("deprecation")
            String baseURI = signatureFile.toString(); // BaseURI para las URL
            // Relativas.

            // Instanciamos un objeto XMLSignature desde el Document. El
            // algoritmo de firma sera RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);

            ///// VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA 2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(0).appendChild(xmlSignature.getElement());

            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Anadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

            // Anadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias());
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os = new FileOutputStream(RutaXML + ".xml");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();

            crearZip(RutaXML, signatureFile, NombreFile);

        } catch (Exception e) {
            log.info(Utils.printStackTraceToString(e));
        }

    }

    public static void crearZip(String rutaXML, File signatureFile,
                                String nombreFile/* ,String RutaZip */) {

        byte[] buffer = new byte[1024];

        try {

            // FileOutputStream fos = new FileOutputStream(RutaZip);
            FileOutputStream fos = new FileOutputStream(rutaXML + ".zip");
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(nombreFile + ".xml");
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(rutaXML + ".xml");

            int len;
            while ((len = in.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            in.close();
            zos.closeEntry();
            zos.close();
            // fos.close();

            /* ELIMINANDO EL ARCHIVO XML (SOLO QUEDARA EL ZIP) */

            File file = new File(rutaXML + ".xml");

            if (file.exists()) {
                file.delete();
            }

        } catch (IOException e) {
            log.info(Utils.printStackTraceToString(e));
        }

    }

    public static byte[] CodigoQR(V_Varios_FacturacionBean facturacion, Map<String, Object> ventas,
                                  Map<String, Object> respuestaXML) {

        // ByteArrayInputStream inputStream = null;
        byte[] imageBytes = null;

        String datos = facturacion.getRuc() + "|" + ventas.get("TipoDocumento") + "|" + ventas.get("SerieElectronica")
                + "|" + ventas.get("Numero") + "|" + ventas.get("IGV") + "|" + ventas.get("Total") + "|"
                + ventas.get("FechaEmision").toString().replace("-", "") + "|" + ventas.get("TipoDocumentoReceptor")
                + "|" + (ventas.get("TipoDocumentoReceptor").toString().trim().equals("6") ? ventas.get("Ruc")
                : ventas.get("DNI"))
                + "|" + respuestaXML.get("codehash");

        try {

            Map<EncodeHintType, Object> correctionlevel = new HashMap<>();
            correctionlevel.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(datos, BarcodeFormat.QR_CODE, 180, 180, correctionlevel);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", pngOutputStream);

            imageBytes = pngOutputStream.toByteArray();

            // inputStream = new ByteArrayInputStream(imageBytes);

        } catch (Exception e) {
            log.info(Utils.printStackTraceToString(e));
        }

        return imageBytes;

    }

    public static byte[] CodigoBarras(V_Varios_FacturacionBean facturacion, Map<String, Object> ventas,
                                      Map<String, Object> respuestaXML) {

        String datos = facturacion.getRuc() + "|" + ventas.get("TipoDocumento") + "|" + ventas.get("SerieElectronica")
                + "|" + ventas.get("Numero") + "|" + ventas.get("IGV") + "|" + ventas.get("Total") + "|"
                + ventas.get("FechaEmision").toString().replace("-", "") + "|" + ventas.get("TipoDocumentoReceptor")
                + "|"
                + (ventas.get("TipoDocumentoReceptor").toString().trim().equals("6") ? ventas.get("Ruc")
                : ventas.get("DNI"))
                + "|" + respuestaXML.get("codehash") + "|" + respuestaXML.get("signaturevalue");

        BarcodePDF417 barcode = new BarcodePDF417();
        // ByteArrayInputStream inputStream = null;
        byte[] pngImageData = null;

        barcode.setText(datos);
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();

        try {

            Image img = barcode.createAwtImage(Color.BLACK, Color.WHITE);

            BufferedImage outImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);

            barcode.setErrorLevel(5);

            barcode.setLenCodewords(BarcodePDF417.PDF417_FORCE_BINARY);
            outImage.getGraphics().drawImage(img, 0, 0, null);

            ImageIO.write(outImage, "png", bytesOut);
            bytesOut.flush();
            bytesOut.close();

            pngImageData = bytesOut.toByteArray();

            // inputStream = new ByteArrayInputStream(pngImageData);

        } catch (Exception e) {
            log.info(Utils.printStackTraceToString(e));
        }

        return pngImageData;

    }

    static String CODIGO_TIPO_TRIBUTO;
    static String NOMBRE_TRIBUTO;
    static String CODIGO_INTERNACIONAL_TRIBUTO;
    static String PORCENTAJE;
    // String

    /*
     *
     * public static final String CODIGO_TIPO_TRIBUTO_EXCENTO = "9996"; public
     * static final String NOMBRE_TRIBUTO_EXCENTO = "GRA"; public static final
     * String CODIGO_INTERNACIONAL_EXCENTO = "FRE"; public static final String
     * CODIGO_TIPO_TRIBUTO_PASAJES = "9997";//para factura y para boleta /**
     * CODIGO TIPO DE TRIBUTO (CATALOGO 5 - cac:TaxScheme/cbc:Name) public
     * static final String NOMBRE_TRIBUTO_PASAJES = "EXO";//para factura y para
     * boleta //CODIGO TIPO DE TRIBUTO (CATALOGO 5 -
     * cac:TaxScheme/cbc:TaxTypeCode) public static final String
     * CODIGO_INTERNACIONAL_TRIBUTO_PASAJES = "VAT";//para factura y para boleta
     */
    public static void TributoCondicionubl21(Map<String, Object> Mapa) {
        System.out.println(Mapa.get("TotalSinIGV").toString());
        System.out.println(Mapa.get("Total").toString());
        System.out.println(Mapa.get("Servicio").toString());

        log.info("Total sin IGV)" + Mapa.get("Servicio").toString());

        log.info("Total sin IGV)" + Mapa.get("TotalSinIGV").toString());
        log.info("Total sin IGV)" + Mapa.get("Total").toString());
        log.info("Tipo de Servicio)" + Mapa.get("Servicio").toString());

        switch (Mapa.get("Servicio").toString()) {
            case "B":
                CODIGO_TIPO_TRIBUTO = "9997";
                NOMBRE_TRIBUTO = "EXO";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "0.00";
                break;
            case "E":
                log.info("Total sin IGV)" + Mapa.get("TotalSinIGV").toString());
                log.info("Total )" + Mapa.get("Total").toString());

                CODIGO_TIPO_TRIBUTO = "1000";
                NOMBRE_TRIBUTO = "IGV";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "18.00";
                log.info("ELSE)" + Mapa.get("TotalSinIGV").toString());
                break;
            case "C":
                log.info("Total sin IGV)" + Mapa.get("TotalSinIGV").toString());
                log.info("Total )" + Mapa.get("Total").toString());

                CODIGO_TIPO_TRIBUTO = "1000";
                NOMBRE_TRIBUTO = "IGV";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "18.00";
                log.info("ELSE)" + Mapa.get("TotalSinIGV DEFAULT").toString());
                break;
            case "N":
                log.info("Total sin IGV)" + Mapa.get("TotalSinIGV").toString());
                log.info("Total )" + Mapa.get("Total").toString());

                CODIGO_TIPO_TRIBUTO = "1000";
                NOMBRE_TRIBUTO = "IGV";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "18.00";
                log.info("ELSE)" + Mapa.get("TotalSinIGV DEFAULT").toString());
                break;
            default:
                log.info("Total sin IGV)" + Mapa.get("TotalSinIGV").toString());
                log.info("Total )" + Mapa.get("Total").toString());
                CODIGO_TIPO_TRIBUTO = "1000";
                NOMBRE_TRIBUTO = "IGV";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "18.00";
                log.info("ELSE)" + Mapa.get("TotalSinIGV DEFAULT").toString());
                break;
        }
    }


    public static void GeneraDocumentoFacturaXMLPruebaUBL21(Map<String, Object> map, V_Varios_FacturacionBean empresa, Map<String, Object> mapvarios) {

        //int accion  =0;
        switch (map.get("CodigoAfectacionIGV").toString()) {
            case "10":
                CODIGO_TIPO_TRIBUTO = "1000";
                NOMBRE_TRIBUTO = "IGV";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "18.00";

				/*accion=Integer.parseInt(map.get("Exceso").toString());
				if (accion ==1) {
					NOMBRE_TRIBUTO = "EXO";
					CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
					PORCENTAJE = "0.00";
					map.put("CodigoAfectacionIGV",20);
					} */
                break;
            case "20":
                CODIGO_TIPO_TRIBUTO = "9997";
                NOMBRE_TRIBUTO = "EXO";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "0.00";
                break;
        }

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

            // Extensions XML
            Element ublExtensions = doc.createElement("ext:UBLExtensions");
            rootElementInvoice.appendChild(ublExtensions);

            Element ublExtension = doc.createElement("ext:UBLExtension");
            //ublExtensions.appendChild(ublExtension);

            Element extExtensionContent = doc.createElement("ext:ExtensionContent");
            ublExtension.appendChild(extExtensionContent);

            Element sacAdditionalInformation = doc.createElement("sac:AdditionalInformation");
            extExtensionContent.appendChild(sacAdditionalInformation);

            // ****************************** VERIFICAR CON LA SUNAT SI SON
            // MANDATORIOS ESTOS CAMPOS DE TOTALES
            // *******************************************************************

            // TOTAL DE VENTA OPERACIONES GRAVADAS (MANDATORIO)
            Element sacAdditionalMonetaryTotalGravadas = doc.createElement("sac:AdditionalMonetaryTotal");
            sacAdditionalInformation.appendChild(sacAdditionalMonetaryTotalGravadas);

            Element cbcIDGravadas = doc.createElement("cbc:ID");
            cbcIDGravadas.appendChild(doc.createTextNode(map.get("CodigoTotalVenta").toString()));// CODIGO
            // 1003
            // ES
            // PARA
            // PASAJES
            // -
            // CODIGO
            // PARA
            // ENCOMIENDAS
            // ES
            // 1001
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcIDGravadas);

            Element cbcPayableAmountGravadas = doc.createElement("cbc:PayableAmount");
            cbcPayableAmountGravadas.appendChild(doc.createTextNode(String.valueOf(map.get("Total")))); // "0.00"
            // ///
            // VERIFICAR
            // ES
            // TOTAL
            // SIN
            // IGV
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcPayableAmountGravadas);

            Attr attrGravadas = doc.createAttribute("currencyID");
            attrGravadas.setValue(map.get("Moneda").toString());
            cbcPayableAmountGravadas.setAttributeNode(attrGravadas);

            // TOTAL DE VENTA MONTO POR VENTA (EN LETRAS)
            Element sacAdditionalProperty = doc.createElement("sac:AdditionalProperty");
            sacAdditionalInformation.appendChild(sacAdditionalProperty);

            Element cbcIDProperty = doc.createElement("cbc:ID");
            cbcIDProperty.appendChild(doc.createTextNode(Utils.CODIGO_MONTO_LETRAS));
            sacAdditionalProperty.appendChild(cbcIDProperty);

            Element cbcValue = doc.createElement("cbc:Value");
            cbcValue.appendChild(doc.createTextNode(map.get("MontoLetras").toString().trim()));// "CUATROCIENTOS
            // VEINTITRES
            // MIL
            // DOSCIENTOS
            // VEINTICINCO
            // Y
            // 00/100"
            sacAdditionalProperty.appendChild(cbcValue);

            // EXTENSION PARA LA FIRMA DIGITAL

            Element ublExtensionSignature = doc.createElement("ext:UBLExtension");
            ublExtensions.appendChild(ublExtensionSignature);

            Text texto = doc.createTextNode(" ");
            Element ublExtensionContentSignature = doc.createElement("ext:ExtensionContent");
            ublExtensionContentSignature.appendChild(texto);
            ublExtensionSignature.appendChild(ublExtensionContentSignature);

            // ********************************************************************************

            // VERSION DE UBL (2.1)
            Element ublVersionID = doc.createElement("cbc:UBLVersionID");
            ublVersionID.appendChild(doc.createTextNode("2.1"));
            rootElementInvoice.appendChild(ublVersionID);

            // VERSION DE LA ESTRUCTURA DEL DOCUMENTO (VER PAG. 57)
            Element cbcCustomizationID = doc.createElement("cbc:CustomizationID");
            cbcCustomizationID.appendChild(doc.createTextNode("2.0"));
            rootElementInvoice.appendChild(cbcCustomizationID);

            // TIPO DE OPERACION
            Element cbcProfileID = doc.createElement("cbc:ProfileID");

            if (map.get("TipoDocumento").toString().equals("01") && Double.parseDouble(map.get("Total").toString()) > Double.parseDouble(mapvarios.get("MontoDetraccion").toString())) {
                cbcProfileID.appendChild(doc.createTextNode("1001")); // VENTA sujeta a detraccion
            } else {
                cbcProfileID.appendChild(doc.createTextNode("0101")); // VENTA interna
            }

            rootElementInvoice.appendChild(cbcProfileID);

            // ********ATRIBUTOS

            Attr schemeIDProfile = doc.createAttribute("schemeID");
            schemeIDProfile.setValue("6");
            cbcProfileID.setAttributeNode(schemeIDProfile);

            Attr schemeNameProfile = doc.createAttribute("schemeName");
            schemeNameProfile.setValue("SUNAT:Identificador de Tipo de Operaci�n");
            cbcProfileID.setAttributeNode(schemeNameProfile);

            Attr schemeURIProfile = doc.createAttribute("schemeURI");
            schemeURIProfile.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo17");
            cbcProfileID.setAttributeNode(schemeURIProfile);

            Attr schemeAgencyNameProfile = doc.createAttribute("schemeAgencyName");
            schemeAgencyNameProfile.setValue("PE:SUNAT");
            cbcProfileID.setAttributeNode(schemeAgencyNameProfile);

            // NUMERO DEL DOCUMENTO (SERIE Y NUMERO)
            Element cbcID = doc.createElement("cbc:ID");
            cbcID.appendChild(doc.createTextNode(map.get("DocumentoElectronico").toString().trim())); // "F703-0000689"
            rootElementInvoice.appendChild(cbcID);

            // FECHA DE EMISION
            Element cbcIssueDate = doc.createElement("cbc:IssueDate");
            cbcIssueDate.appendChild(doc.createTextNode(map.get("FechaEmision").toString().trim()));// FECHA
            // DE
            // EMISI�N
            rootElementInvoice.appendChild(cbcIssueDate);

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();

            // FECHA DE EMISION
            Element cbcIssuetTime = doc.createElement("cbc:IssueTime");
            cbcIssuetTime.appendChild(doc.createTextNode(dateFormat.format(date)));// HORA
            // DE
            // EMISI�N
            rootElementInvoice.appendChild(cbcIssuetTime);

            // TIPO DE DOCUMENTO (01 FACTURA ...)
            Element cbcInvoiceTypeCode = doc.createElement("cbc:InvoiceTypeCode");
            cbcInvoiceTypeCode.appendChild(doc.createTextNode(map.get("TipoDocumento").toString().trim())); // Factura:
            // 01
            // //Boleta:03
            rootElementInvoice.appendChild(cbcInvoiceTypeCode);

            Attr listID = doc.createAttribute("listID");

            if (map.get("TipoDocumento").toString().equals("01") && Double.parseDouble(map.get("Total").toString()) > Double.parseDouble(mapvarios.get("MontoDetraccion").toString())) {
                //cbcProfileID.appendChild(doc.createTextNode("1001")); // VENTA sujeta a detraccion
                listID.setValue("1001");// VENTA sujeta a detraccion
            } else {
                listID.setValue("0101");// VENTA interna
            }

            ; // venta interna
            cbcInvoiceTypeCode.setAttributeNode(listID);

            Element cbcNote = doc.createElement("cbc:Note");
            cbcNote.appendChild(doc.createCDATASection(map.get("MontoLetras").toString().trim())); // Monto

            //languageLocaleID="1000"
            Attr languageLocaleIDNote1 = doc.createAttribute("languageLocaleID");
            languageLocaleIDNote1.setValue("1000"); // venta interna
            cbcNote.setAttributeNode(languageLocaleIDNote1);


            if (map.get("TipoDocumento").toString().equals("01") && Double.parseDouble(map.get("Total").toString()) > Double.parseDouble(mapvarios.get("MontoDetraccion").toString())) {
                //Detracción solo para facturas.

                Element cbcNotedetransaccion = doc.createElement("cbc:Note");
                cbcNotedetransaccion.appendChild(doc.createCDATASection("Operación sujeta a detracción")); // Monto

                //languageLocaleID="1000"
                Attr languageLocaleIDNotedetraccion = doc.createAttribute("languageLocaleID");
                languageLocaleIDNotedetraccion.setValue("2006"); // venta interna
                cbcNotedetransaccion.setAttributeNode(languageLocaleIDNotedetraccion);

                rootElementInvoice.appendChild(cbcNotedetransaccion);
            }

            // en
            // Letras
            rootElementInvoice.appendChild(cbcNote);

            // CODIGO DE MONEDA (CATALOGO N°. 2)
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
            cacPartyIdentificationID.appendChild(doc.createTextNode(empresa.getRuc()));
            cacPartyIdentification.appendChild(cacPartyIdentificationID);

            Element cacPartySignatureName = doc.createElement("cac:PartyName");
            cacSignatoryParty.appendChild(cacPartySignatureName);

            // RAZON SOCIAL DE LA EMPRESA EMISORA
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
            /* PRUEBA DE ERROR*/
            Element cacAccountingSupplierParty = doc.createElement("cac:AccountingSupplierParty");
            rootElementInvoice.appendChild(cacAccountingSupplierParty);

            Element cacParty = doc.createElement("cac:Party");
            cacAccountingSupplierParty.appendChild(cacParty);

            Element cacPartyIdentificationEmisor = doc.createElement("cac:PartyIdentification");
            cacParty.appendChild(cacPartyIdentificationEmisor);

            Element cbcIDEmisor = doc.createElement("cbc:ID");
            cbcIDEmisor.appendChild(doc.createTextNode(empresa.getRuc()));
            cacPartyIdentificationEmisor.appendChild(cbcIDEmisor);

            // *************** atributos que dicen que nuestra identificaci�n es
            // con RUC

            Attr schemeAgencyName = doc.createAttribute("schemeAgencyName");
            schemeAgencyName.setValue("PE:SUNAT");
            cbcIDEmisor.setAttributeNode(schemeAgencyName);

            Attr schemeID = doc.createAttribute("schemeID");
            schemeID.setValue("6");
            cbcIDEmisor.setAttributeNode(schemeID);

            Attr schemeName = doc.createAttribute("schemeName");
            schemeName.setValue("Documento de Identidad");
            cbcIDEmisor.setAttributeNode(schemeName);

            Attr schemeURI = doc.createAttribute("schemeURI");
            schemeURI.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06");
            cbcIDEmisor.setAttributeNode(schemeURI);

            // ********************************************************

            Element cacPartyName = doc.createElement("cac:PartyName");
            cacParty.appendChild(cacPartyName);

            Element cbcName = doc.createElement("cbc:Name");
            cbcName.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartyName.appendChild(cbcName);

            Element cacPartyLegalEntity = doc.createElement("cac:PartyLegalEntity");
            cacParty.appendChild(cacPartyLegalEntity);

            Element cbcRegistrationNameEmisor = doc.createElement("cbc:RegistrationName");
            cbcRegistrationNameEmisor.appendChild(doc.createTextNode(empresa.getRazon()));
            cacPartyLegalEntity.appendChild(cbcRegistrationNameEmisor);

            Element cacRegistrationAddressEmisor = doc.createElement("cac:RegistrationAddress");
            cacPartyLegalEntity.appendChild(cacRegistrationAddressEmisor);

            Element cbcAddressTypeCode = doc.createElement("cbc:AddressTypeCode");
            cbcAddressTypeCode.appendChild(doc.createTextNode(empresa.getUbigeo()));
            cacRegistrationAddressEmisor.appendChild(cbcAddressTypeCode);

            // DATOS DEL RECEPTOR
            Element cacAccountingCustomerParty = doc.createElement("cac:AccountingCustomerParty");
            rootElementInvoice.appendChild(cacAccountingCustomerParty);

            /*
             * Element cbcCustomerAssignedAccountIDReceptor =
             * doc.createElement("cbc:CustomerAssignedAccountID");
             * cbcCustomerAssignedAccountIDReceptor.appendChild(doc.
             * createTextNode(map.get("TipoDocumento").toString().trim().equals(
             * "01")? map.get("Ruc").toString() :map.get("DNI").toString()));
             * //"20341198217" cacAccountingCustomerParty.appendChild(
             * cbcCustomerAssignedAccountIDReceptor);
             *
             * Element cbcAdditionalAccountIDReceptor =
             * doc.createElement("cbc:AdditionalAccountID");
             * cbcAdditionalAccountIDReceptor.appendChild(doc.createTextNode(map
             * .get("TipoDocumentoReceptor").toString())); //"6"
             * cacAccountingCustomerParty.appendChild(
             * cbcAdditionalAccountIDReceptor);
             */

            Element cacPartyReceptor = doc.createElement("cac:Party");
            cacAccountingCustomerParty.appendChild(cacPartyReceptor);

            Element cacPartyIdentificationReceptor = doc.createElement("cac:PartyIdentification");
            cacPartyReceptor.appendChild(cacPartyIdentificationReceptor);

            Element cbcIDReceptor = doc.createElement("cbc:ID");
            cbcIDReceptor.appendChild(doc.createTextNode(map.get("TipoDocumento").toString().trim().equals("01")
                    ? map.get("Ruc").toString() : map.get("DNI").toString()));
            cacPartyIdentificationReceptor.appendChild(cbcIDReceptor);
            // *************** atributos que dicen que nuestra identificaci�n es
            // con DNI o ruc

            Attr schemeAgencyNameReceptor = doc.createAttribute("schemeAgencyName");
            schemeAgencyNameReceptor.setValue("PE:SUNAT");
            cbcIDReceptor.setAttributeNode(schemeAgencyNameReceptor);

            Attr schemeIDReceptor = doc.createAttribute("schemeID");
            schemeIDReceptor.setValue(map.get("TipoDocumentoReceptor").toString());
            cbcIDReceptor.setAttributeNode(schemeIDReceptor);

            Attr schemeNameReceptor = doc.createAttribute("schemeName");
            schemeNameReceptor.setValue("Documento de Identidad");
            cbcIDReceptor.setAttributeNode(schemeNameReceptor);

            Attr schemeURIReceptor = doc.createAttribute("schemeURI");
            schemeURIReceptor.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06");
            cbcIDReceptor.setAttributeNode(schemeURIReceptor);

            Element cacPartyLegalEntityReceptor = doc.createElement("cac:PartyLegalEntity");
            cacPartyReceptor.appendChild(cacPartyLegalEntityReceptor);

            String datoName = map.get("TipoDocumento").toString().trim().equals("01") ? map.get("Razon").toString()
                    : map.get("Nombre").toString();
            Element cbcRegistrationNameReceptor = doc.createElement("cbc:RegistrationName");
            cbcRegistrationNameReceptor
                    .appendChild(doc.createCDATASection(datoName.trim().equals("-") ? "---" : datoName));// "VISANET
            // -
            // CIA
            // PERUANA
            // DE
            // MEDIOS
            // DE
            // PAGO
            // SAC"
            cacPartyLegalEntityReceptor.appendChild(cbcRegistrationNameReceptor);

            // *******************************************************************************************
            // INFORMACION DE SUMATORIA IVG GLOBAL


            if (map.get("TipoDocumento").toString().equals("01") && Double.parseDouble(map.get("Total").toString()) > Double.parseDouble(mapvarios.get("MontoDetraccion").toString())) {
                //Detracción solo para facturas.
                Element cacPaymentTermsdetracciones = doc.createElement("cac:PaymentMeans");
                rootElementInvoice.appendChild(cacPaymentTermsdetracciones);

                Element cacPaymentTermsdetracciones_cacID = doc.createElement("cbc:ID");
                cacPaymentTermsdetracciones_cacID.appendChild(doc.createTextNode("Detraccion"));
                cacPaymentTermsdetracciones.appendChild(cacPaymentTermsdetracciones_cacID);

                //cbc:PaymentMeansCode
                Element cacPaymentTermsdetracciones_cbcPaymentMeansCode = doc.createElement("cbc:PaymentMeansCode");
                cacPaymentTermsdetracciones_cbcPaymentMeansCode.appendChild(doc.createTextNode("001"));//MODIFICAR
                cacPaymentTermsdetracciones.appendChild(cacPaymentTermsdetracciones_cbcPaymentMeansCode);


                Element cacPaymentTermsdetracciones_cacPayeeFinancialAccount = doc.createElement("cac:PayeeFinancialAccount");
                cacPaymentTermsdetracciones.appendChild(cacPaymentTermsdetracciones_cacPayeeFinancialAccount);

                Element cacPaymentTermsdetracciones_cacPayeeFinancialAccount_cbcID = doc.createElement("cbc:ID");
                //cacPaymentTermsdetracciones_cacPayeeFinancialAccount_cbcID.appendChild(doc.createTextNode("00-005-123704"));
                cacPaymentTermsdetracciones_cacPayeeFinancialAccount_cbcID.appendChild(doc.createTextNode(mapvarios.get("CuentaBancaria").toString()));
                cacPaymentTermsdetracciones_cacPayeeFinancialAccount.appendChild(cacPaymentTermsdetracciones_cacPayeeFinancialAccount_cbcID);

                //<cac:PayeeFinancialAccount><cbc:ID>00-005-123704</cbc:ID></cac:PayeeFinancialAccount>

                //=======================================================================

                Element cacPaymentTermsdetracciones2 = doc.createElement("cac:PaymentTerms");
                rootElementInvoice.appendChild(cacPaymentTermsdetracciones2);

                Element cacPaymentTermsdetracciones2_cacID = doc.createElement("cbc:ID");
                cacPaymentTermsdetracciones2_cacID.appendChild(doc.createTextNode("Detraccion"));
                cacPaymentTermsdetracciones2.appendChild(cacPaymentTermsdetracciones2_cacID);

                //cbc:PaymentMeansCode
                Element cacPaymentTermsdetracciones2_cbcPaymentMeansID = doc.createElement("cbc:PaymentMeansID");
                cacPaymentTermsdetracciones2_cbcPaymentMeansID.appendChild(doc.createTextNode("027"));//MODIFICAR
                cacPaymentTermsdetracciones2.appendChild(cacPaymentTermsdetracciones2_cbcPaymentMeansID);

                Attr listAgencyNameGlobal = doc.createAttribute("schemeAgencyName");
                listAgencyNameGlobal.setValue("PE:SUNAT");
                cacPaymentTermsdetracciones2_cbcPaymentMeansID.setAttributeNode(listAgencyNameGlobal);

                Attr listNameGlobal = doc.createAttribute("schemeName");
                listNameGlobal.setValue("Codigo de detraccion");
                cacPaymentTermsdetracciones2_cbcPaymentMeansID.setAttributeNode(listNameGlobal);

                Attr listURIGlobal = doc.createAttribute("schemeURI");
                listURIGlobal.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo54");
                cacPaymentTermsdetracciones2_cbcPaymentMeansID.setAttributeNode(listURIGlobal);

                Element cacPaymentTermsdetracciones2_cbcPaymentPercent = doc.createElement("cbc:PaymentPercent");
                cacPaymentTermsdetracciones2_cbcPaymentPercent.appendChild(doc.createTextNode(mapvarios.get("PorcentajeDetraccion").toString()));//MODIFICAR
                cacPaymentTermsdetracciones2.appendChild(cacPaymentTermsdetracciones2_cbcPaymentPercent);


                Attr aatrCurrencydetr = doc.createAttribute("currencyID");
                aatrCurrencydetr.setValue(map.get("Moneda").toString());


                Element cacPaymentTermsdetracciones2_cbcAmount = doc.createElement("cbc:Amount");
                cacPaymentTermsdetracciones2_cbcAmount.setAttributeNode(aatrCurrencydetr);
                cacPaymentTermsdetracciones2_cbcAmount.appendChild(doc.createTextNode(map.get("Detraccion").toString()));//MODIFICAR
                cacPaymentTermsdetracciones2.appendChild(cacPaymentTermsdetracciones2_cbcAmount);


            }
            Element cacPaymentTerms = doc.createElement("cac:PaymentTerms");
            rootElementInvoice.appendChild(cacPaymentTerms);

            Element cacPaymentTerms_cacID = doc.createElement("cbc:ID");
            cacPaymentTerms_cacID.appendChild(doc.createTextNode("FormaPago"));
            cacPaymentTerms.appendChild(cacPaymentTerms_cacID);

            Element cacPaymentTerms_PaymentMeansID = doc.createElement("cbc:PaymentMeansID");

            if (map.get("Servicio").equals("C")) { // SI es Canje , es crédito a 40 días. Se generan las cuotas de pago
                cacPaymentTerms_PaymentMeansID.appendChild(doc.createTextNode("Credito"));//MODIFICAR
                cacPaymentTerms.appendChild(cacPaymentTerms_PaymentMeansID);


                Attr aatrCurrencyamout = doc.createAttribute("currencyID");
                aatrCurrencyamout.setValue(map.get("Moneda").toString());

                Element cacPaymentTerms_cbcAmount = doc.createElement("cbc:Amount");
                cacPaymentTerms_cbcAmount.setAttributeNode(aatrCurrencyamout);

                if (map.get("TipoDocumento").toString().equals("01") && Double.parseDouble(map.get("Total").toString()) > Double.parseDouble(mapvarios.get("MontoDetraccion").toString())) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    Double precioSinDetraccion = Double.parseDouble(map.get("Total").toString()) - Double.parseDouble(map.get("Detraccion").toString());
                    cacPaymentTerms_cbcAmount.appendChild(doc.createTextNode(df.format(precioSinDetraccion)));//MODIFICAR
                } else {
                    cacPaymentTerms_cbcAmount.appendChild(doc.createTextNode(map.get("Total").toString()));//MODIFICAR
                }


                cacPaymentTerms.appendChild(cacPaymentTerms_cbcAmount);

                Attr aatrCurrencyIDAmount_1 = doc.createAttribute("currencyID");
                aatrCurrencyIDAmount_1.setValue(map.get("Moneda").toString());
                cacPaymentTerms_cbcAmount.setAttributeNode(aatrCurrencyIDAmount_1);

                Element cacPaymentTermsCuota = doc.createElement("cac:PaymentTerms");
                rootElementInvoice.appendChild(cacPaymentTermsCuota);

                Element cacPaymentTermsCuota_ID = doc.createElement("cbc:ID");
                cacPaymentTermsCuota_ID.appendChild(doc.createTextNode("FormaPago"));//MODIFICAR
                cacPaymentTermsCuota.appendChild(cacPaymentTermsCuota_ID);

                Element cacPaymentTermsCuota_PaymentMeansID = doc.createElement("cbc:PaymentMeansID");
                cacPaymentTermsCuota_PaymentMeansID.appendChild(doc.createTextNode("Cuota001"));//MODIFICAR
                cacPaymentTermsCuota.appendChild(cacPaymentTermsCuota_PaymentMeansID);

                Element cacPaymentTermsCuota_cbcAmount = doc.createElement("cbc:Amount");

                if (map.get("TipoDocumento").toString().equals("01") && Double.parseDouble(map.get("Total").toString()) > Double.parseDouble(mapvarios.get("MontoDetraccion").toString())) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    Double precioSinDetraccion = Double.parseDouble(map.get("Total").toString()) - Double.parseDouble(map.get("Detraccion").toString());
                    cacPaymentTermsCuota_cbcAmount.appendChild(doc.createTextNode(df.format(precioSinDetraccion)));//MODIFICAR
                } else {
                    cacPaymentTermsCuota_cbcAmount.appendChild(doc.createTextNode(map.get("Total").toString()));//MODIFICAR
                }


                cacPaymentTermsCuota.appendChild(cacPaymentTermsCuota_cbcAmount);

                Attr aatrCurrencyIDAmount_2 = doc.createAttribute("currencyID");
                aatrCurrencyIDAmount_2.setValue(map.get("Moneda").toString());
                cacPaymentTermsCuota_cbcAmount.setAttributeNode(aatrCurrencyIDAmount_2);

                Element cacPaymentTermsCuota_cbcPaymentDueDate = doc.createElement("cbc:PaymentDueDate");
                cacPaymentTermsCuota_cbcPaymentDueDate.appendChild(doc.createTextNode(map.get("FechaVencimiento").toString()));//MODIFICAR
                cacPaymentTermsCuota.appendChild(cacPaymentTermsCuota_cbcPaymentDueDate);

            } else {
                cacPaymentTerms_PaymentMeansID.appendChild(doc.createTextNode("Contado"));//MODIFICAR
                cacPaymentTerms.appendChild(cacPaymentTerms_PaymentMeansID);
            }


            Element cacTaxTotalGlobal = doc.createElement("cac:TaxTotal");
            rootElementInvoice.appendChild(cacTaxTotalGlobal);

            // <cbc:TaxableAmount currencyID="PEN">348199.15</cbc:TaxableAmount>
            // -------------

            Element cbcTaxAmountGlobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA
            /// DEL
            /// IGV
            /// TOTAL
            /// (PARA
            /// PASAJES
            /// 0)
            cacTaxTotalGlobal.appendChild(cbcTaxAmountGlobal);

            Attr attrTaxAmountGlobal = doc.createAttribute("currencyID");
            attrTaxAmountGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountGlobal.setAttributeNode(attrTaxAmountGlobal);

            Element cacTaxSubtotalGlobal = doc.createElement("cac:TaxSubtotal");
            cacTaxTotalGlobal.appendChild(cacTaxSubtotalGlobal);

            Element cbcTaxableAmount = doc.createElement("cbc:TaxableAmount");
            cbcTaxableAmount.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));/// COSTO
            /// POR
            /// TRANSACCI�N
            cacTaxSubtotalGlobal.appendChild(cbcTaxableAmount);

            Attr currencyIDAmount = doc.createAttribute("currencyID");
            currencyIDAmount.setValue(map.get("Moneda").toString());
            cbcTaxableAmount.setAttributeNode(currencyIDAmount);

            Element cbcTaxAmountSubtotalGblobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotalGblobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotalGlobal.appendChild(cbcTaxAmountSubtotalGblobal);

            Attr attrTaxAmountSubtotalGlobal = doc.createAttribute("currencyID");
            attrTaxAmountSubtotalGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotalGblobal.setAttributeNode(attrTaxAmountSubtotalGlobal);

            Element cacTaxtCategoryGlobal = doc.createElement("cac:TaxCategory");
            cacTaxSubtotalGlobal.appendChild(cacTaxtCategoryGlobal);

            Element cbcTaxExemptionReasonCodeGlobal = doc.createElement("cbc:TaxExemptionReasonCode");
            cbcTaxExemptionReasonCodeGlobal.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR
            // CATALOGO
            // N�
            // 7
            // (
            // CODIGO
            // 20
            // PARA
            // PASAJES
            // -
            // CODIGO
            // 10
            // PARA
            // ENCOMIENDAS)
            cacTaxtCategoryGlobal.appendChild(cbcTaxExemptionReasonCodeGlobal);
/*
            Attr listAgencyNameGlobal = doc.createAttribute("listAgencyName");
            listAgencyNameGlobal.setValue("PE:SUNAT");
            cbcTaxExemptionReasonCodeGlobal.setAttributeNode(listAgencyNameGlobal);

            Attr listNameGlobal = doc.createAttribute("listName");
            listNameGlobal.setValue("SUNAT:Codigo de Tipo de Afectación del IGV");
            cbcTaxExemptionReasonCodeGlobal.setAttributeNode(listNameGlobal);

            Attr listURIGlobal = doc.createAttribute("listURI");
            listURIGlobal.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo07");
            cbcTaxExemptionReasonCodeGlobal.setAttributeNode(listURIGlobal);*/

            Element cacTaxSchemeGlobal = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobal.appendChild(cacTaxSchemeGlobal);

            Element cacTaxSchemeIDGlobal = doc.createElement("cbc:ID");
            cacTaxSchemeIDGlobal.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            // //codigo
            // para
            // pasajes
            // o
            // encimiendas
            // NUMERO
            // 100
            // |
            // 9997
            cacTaxSchemeGlobal.appendChild(cacTaxSchemeIDGlobal);

            Element cbcTaxSchemeNameGlobal = doc.createElement("cbc:Name");
            cbcTaxSchemeNameGlobal.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// VERIFICAR

            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxSchemeNameGlobal);

            Element cbcTaxTypeCodeGlobal = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCodeGlobal.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxTypeCodeGlobal);

            // *************************************************************
            // IMPORTE TOTAL DE LA VENTA CESION EN USO O DEL SERVICIO PRESTADO

            Element cacLegalMonetaryTotal = doc.createElement("cac:LegalMonetaryTotal");
            rootElementInvoice.appendChild(cacLegalMonetaryTotal);

//CREANDO MONEDA ATTR

            Attr attrMonetaryTotalLineExtensionAmount = doc.createAttribute("currencyID");
            attrMonetaryTotalLineExtensionAmount.setValue(map.get("Moneda").toString());


            Element LineExtensionAmount = doc.createElement("cbc:LineExtensionAmount");
            LineExtensionAmount.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV")))); // "150.00"
            cacLegalMonetaryTotal.appendChild(LineExtensionAmount);
            LineExtensionAmount.setAttributeNode(attrMonetaryTotalLineExtensionAmount);

            Attr attrMonetaryTotalTaxInclusiveAmount = doc.createAttribute("currencyID");
            attrMonetaryTotalTaxInclusiveAmount.setValue(map.get("Moneda").toString());

            Element TaxInclusiveAmount = doc.createElement("cbc:TaxInclusiveAmount");
            TaxInclusiveAmount.appendChild(doc.createTextNode(String.valueOf(map.get("Total")))); // "150.00"
            cacLegalMonetaryTotal.appendChild(TaxInclusiveAmount);
            TaxInclusiveAmount.setAttributeNode(attrMonetaryTotalTaxInclusiveAmount);

            Attr attrMonetaryPayableAmount = doc.createAttribute("currencyID");
            attrMonetaryPayableAmount.setValue(map.get("Moneda").toString());

            Element cbcPayableAmount = doc.createElement("cbc:PayableAmount");
            cbcPayableAmount.appendChild(doc.createTextNode(String.valueOf(map.get("Total")))); // "150.00"
            cacLegalMonetaryTotal.appendChild(cbcPayableAmount);
            cbcPayableAmount.setAttributeNode(attrMonetaryPayableAmount);

            // *************************************************************
            // INFORMACI�N DEL ARTICULO
            Element cacInvoiceLine = doc.createElement("cac:InvoiceLine");
            rootElementInvoice.appendChild(cacInvoiceLine);

            Element cbcIDInvoiceLine = doc.createElement("cbc:ID");
            cbcIDInvoiceLine.appendChild(doc.createTextNode("1"));// SIMPRE VA
            // SER 1 (YA
            // QUE ES UN
            // SOLO
            // ITEM)
            cacInvoiceLine.appendChild(cbcIDInvoiceLine);

            // CANTIDA DE ARTICULOS
            Element cbcInvoicedQuantity = doc.createElement("cbc:InvoicedQuantity");
            cbcInvoicedQuantity.appendChild(doc.createTextNode("1"));// SIMPRE
            // VA
            // SER 1
            // (YA
            // QUE
            // ES UN
            // SOLO
            // ITEM)
            cacInvoiceLine.appendChild(cbcInvoicedQuantity);

            Attr attrInvoicedQuantity = doc.createAttribute("unitCode");
            attrInvoicedQuantity.setValue(empresa.getCodigoUnidadMedida()); // CODIGO
            // DE
            // UNIDAD
            // DE
            // MEDIDA
            cbcInvoicedQuantity.setAttributeNode(attrInvoicedQuantity);

            // VALOR DE VENTA DEL ITEM
            Element cbcLineExtensionAmount = doc.createElement("cbc:LineExtensionAmount");
            cbcLineExtensionAmount.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));
            cacInvoiceLine.appendChild(cbcLineExtensionAmount);

            Attr attrLineExtensionAmount = doc.createAttribute("currencyID");
            attrLineExtensionAmount.setValue(map.get("Moneda").toString());
            cbcLineExtensionAmount.setAttributeNode(attrLineExtensionAmount);

            // PRECIO UNITARIO O VALOR REFERENCIAL UNITARIO EN OPERACIONES NO
            // ONEROSAS
            Element cacPricingReference = doc.createElement("cac:PricingReference");
            cacInvoiceLine.appendChild(cacPricingReference);

            Element cacAlternativeConditionPrice = doc.createElement("cac:AlternativeConditionPrice");
            cacPricingReference.appendChild(cacAlternativeConditionPrice);

            Element cbcPriceAmount = doc.createElement("cbc:PriceAmount");
            //cbcPriceAmount.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));
            cbcPriceAmount.appendChild(doc.createTextNode(String.valueOf(map.get("Total"))));
            cacAlternativeConditionPrice.appendChild(cbcPriceAmount);

            Attr attrPriceAmount = doc.createAttribute("currencyID");
            attrPriceAmount.setValue(map.get("Moneda").toString());
            cbcPriceAmount.setAttributeNode(attrPriceAmount);

            //// REGUNTAR QUE CODIGO VA IR !!!!!! 2017-11-26
            Element cbcPriceTypeCode = doc.createElement("cbc:PriceTypeCode");
            cbcPriceTypeCode.appendChild(doc.createTextNode("01"));// CATALOGO
            // N� 16
            // (01-
            // Precio
            // unitario
            // (incluye
            // el IGV))
            // (02 -
            // Valor
            // referencial
            // unitario
            // en
            // operaciones
            // no
            // onerosas)
            cacAlternativeConditionPrice.appendChild(cbcPriceTypeCode);

            // AFECTACION AL IGV POR ITEM
            Element cacTaxTotal = doc.createElement("cac:TaxTotal");
            cacInvoiceLine.appendChild(cacTaxTotal);

            Element cbcTaxAmount = doc.createElement("cbc:TaxAmount");
            cbcTaxAmount.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// MONTO
            /// DEL
            /// IGV
            /// (PARA
            /// PASAJES
            /// 0)
            cacTaxTotal.appendChild(cbcTaxAmount);

            Attr attrTaxAmount = doc.createAttribute("currencyID");
            attrTaxAmount.setValue(map.get("Moneda").toString());
            cbcTaxAmount.setAttributeNode(attrTaxAmount);

            Element cacTaxSubtotal = doc.createElement("cac:TaxSubtotal");
            cacTaxTotal.appendChild(cacTaxSubtotal);

            Element cbcTaxableAmountHijo = doc.createElement("cbc:TaxableAmount");
            cbcTaxableAmountHijo.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));/// COSTO
            /// POR
            /// TRANSACCI�N
            cacTaxSubtotal.appendChild(cbcTaxableAmountHijo);

            Attr currencyIDAmountHijo = doc.createAttribute("currencyID");
            currencyIDAmountHijo.setValue(map.get("Moneda").toString());
            cbcTaxableAmountHijo.setAttributeNode(currencyIDAmountHijo);

            Element cbcTaxAmountSubtotal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotal.appendChild(cbcTaxAmountSubtotal);

            Attr attrTaxAmountSubtotal = doc.createAttribute("currencyID");
            attrTaxAmountSubtotal.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotal.setAttributeNode(attrTaxAmountSubtotal);

            Element cacTaxtCategory = doc.createElement("cac:TaxCategory");
            cacTaxSubtotal.appendChild(cacTaxtCategory);

            // ***A�ADIDO

            Element cbcIDCategory = doc.createElement("cbc:ID");
            cbcIDCategory.appendChild(doc.createTextNode("S"));
            cacTaxtCategory.appendChild(cbcIDCategory);

            Attr schemeIDImpuesto = doc.createAttribute("schemeID");
            schemeIDImpuesto.setValue("UN/ECE 5305");
            cbcIDCategory.setAttributeNode(schemeIDImpuesto);

            Attr schemeNameImpuesto = doc.createAttribute("schemeName");
            schemeNameImpuesto.setValue("Tax Category Identifier");
            cbcIDCategory.setAttributeNode(schemeNameImpuesto);

            Attr schemeAgencyNameImpuesto = doc.createAttribute("schemeAgencyName");
            schemeAgencyNameImpuesto.setValue("United Nations Economic Commission for Europe");
            cbcIDCategory.setAttributeNode(schemeAgencyNameImpuesto);

            Element cbcPercent = doc.createElement("cbc:Percent");
            cbcPercent.appendChild(doc.createTextNode(PORCENTAJE)); // porcentaje
            // de IGV
            cacTaxtCategory.appendChild(cbcPercent);
            // ****

            Element cbcTaxExemptionReasonCode = doc.createElement("cbc:TaxExemptionReasonCode");
            cbcTaxExemptionReasonCode.appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR
            // CATALOGO
            // N�
            // 7
            // (
            // CODIGO
            // 20
            // PARA
            // PASAJES
            // -
            // CODIGO
            // 10
            // PARA
            // ENCOMIENDAS)
            cacTaxtCategory.appendChild(cbcTaxExemptionReasonCode);
/*
            Attr listAgencyNameImpuesto = doc.createAttribute("listAgencyName");
            listAgencyNameImpuesto.setValue("PE:SUNAT");
            cbcTaxExemptionReasonCode.setAttributeNode(listAgencyNameImpuesto);

            Attr listNameImpuesto = doc.createAttribute("listName");
            listNameImpuesto.setValue("SUNAT:Codigo de Tipo de Afectación del IGV");
            cbcTaxExemptionReasonCode.setAttributeNode(listNameImpuesto);

            Attr listURIImpuesto = doc.createAttribute("listURI");
            listURIImpuesto.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo07");
            cbcTaxExemptionReasonCode.setAttributeNode(listURIImpuesto);
*/


            // schemeID="UN/ECE 5305" schemeName="Tax Category Identifier"
            // schemeAgencyName="United Nations Economic Commission for Europe"

            Element cacTaxScheme = doc.createElement("cac:TaxScheme");
            cacTaxtCategory.appendChild(cacTaxScheme);

            Element cacTaxSchemeID = doc.createElement("cbc:ID");
            cacTaxSchemeID.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            // //codigo
            // para
            // pasajes
            // o
            // encimiendas
            // NUMERO
            // 100
            // |
            // 9997
            cacTaxScheme.appendChild(cacTaxSchemeID);
            Element cbcTaxSchemeName = doc.createElement("cbc:Name");
            cbcTaxSchemeName.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// map.get("Servicio").equals("B")?Utils.NOMBRE_TRIBUTO_PASAJES:Utils.NOMBRE_TRIBUTO_ENCOMIENDA));//
            // VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxScheme.appendChild(cbcTaxSchemeName);

            Element cbcTaxTypeCode = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCode.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxScheme.appendChild(cbcTaxTypeCode);

            // DESCRIPCION DEL ITEM
            Element cacItem = doc.createElement("cac:Item");
            cacInvoiceLine.appendChild(cacItem);

            Element cbcDescription = doc.createElement("cbc:Description");

            if (map.get("Servicio").toString().trim().equals("B")) {
                cbcDescription.appendChild(doc.createCDATASection("SERVICIO DE TRANSPORTE EN LA RUTA: "
                        + map.get("DestinoD") /* venta.getDestinoD() */));
                cacItem.appendChild(cbcDescription);
            } else {

                String Descripcion = "";
                if (!map.get("Cantidad1").toString().trim().equals("")
                        && !map.get("Descripcion1").toString().trim().equals("")) {

                    Descripcion = map.get("Cantidad1").toString().trim() + " "
                            + map.get("Descripcion1").toString().trim();
                }
                if (!map.get("Cantidad2").toString().trim().equals("")
                        && !map.get("Descripcion2").toString().trim().equals("")) {

                    Descripcion = Descripcion + "/" + map.get("Cantidad2").toString().trim() + " "
                            + map.get("Descripcion2").toString().trim();
                }
                if (!map.get("Cantidad3").toString().trim().equals("")
                        && !map.get("Descripcion3").toString().trim().equals("")) {

                    Descripcion = Descripcion + "/" + map.get("Cantidad3").toString().trim() + " "
                            + map.get("Descripcion3").toString().trim();
                }

                if (Integer.parseInt(map.get("Exceso").toString()) == 1) {
                    cbcDescription.appendChild(doc.createCDATASection("BOLETO DE EXCESO: " + Descripcion));
                } else {
                    cbcDescription.appendChild(doc.createCDATASection("SERVICIO DE ENVIO DE ENCOMIENDA: " + Descripcion));
                }
                cacItem.appendChild(cbcDescription);
            }

            // VALOR UNITARIO POR ITEM
            Element cacPrice = doc.createElement("cac:Price");
            cacInvoiceLine.appendChild(cacPrice);

            Element cbcPriceAmountItem = doc.createElement("cbc:PriceAmount");
            cbcPriceAmountItem.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));
            cacPrice.appendChild(cbcPriceAmountItem);

            Attr attrPriceAmountItem = doc.createAttribute("currencyID");
            attrPriceAmountItem.setValue(map.get("Moneda").toString());
            cbcPriceAmountItem.setAttributeNode(attrPriceAmountItem);

            // ******* GENERANDO LA FIRMA DIGITAL

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(empresa.getRutaEnvioSunat() + empresa.getRuc() + "-"
                    + map.get("TipoDocumento") + "-" + map.get("DocumentoElectronico") + ".xml"));
            // StreamResult result = new StreamResult(new
            // File(empresa.getRutaEnvioSunat()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml")+".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioSunat() + empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");

            String NombreFile = empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");
            // String NombreFile =
            // empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml");
            // String RutaZip =
            // empresa.getRutaEnvioSunat()+map.get("DocumentoZip").toString();

            Init.init();

            DocumentBuilderFactory dof = DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            // Si el Documento XML no tiene la estructura Correcta entra al
            // catch SAXException
            Document doc1 = dof.newDocumentBuilder().parse(new FileInputStream(RutaXML + ".xml"));

            Constants.setSignatureSpecNSprefix("ds"); // Sino, pone por defecto
            // como prefijo: "ns"

            System.out.println("LINEA 1");
            //

            //KeyStore ks1 = KeyStore.getInstance("PFX");
            //ks1.load);
            // Cargamos el almacen de claves
            KeyStore ks = KeyStore.getInstance(Utils.KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()), empresa.getKeystorePassword().toCharArray());

            System.out.println("LINEA 2");
            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),
                    empresa.getKeystorePassword().toCharArray());
            File signatureFile = new File(RutaXML + ".xml");
            // @SuppressWarnings("deprecation")
            String baseURI = signatureFile.toString(); // BaseURI para las URL
            // Relativas.

            System.out.println("LINEA 3");
            // Instanciamos un objeto XMLSignature desde el Document. El
            // algoritmo de firma sera RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);

            ///// VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA 2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(0).appendChild(xmlSignature.getElement());

            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Anadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
            System.out.println("LINEA 4");
            // Anadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias());
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os = new FileOutputStream(RutaXML + ".xml");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();

            /*
             * NodeList nodeListhash =
             * doc1.getElementsByTagName("ds:DigestValue"); Node nodohash =
             * nodeListhash.item(0);
             *
             * if (nodohash instanceof Element){ respuesta.put("codehash",
             * nodohash.getTextContent().trim()); System.out.println(
             * "CODIGO HASH --> "+ nodohash.getTextContent().trim()); }
             */

            crearZip(RutaXML, signatureFile, NombreFile/* ,RutaZip */);

        } catch (Exception e) {
            log.info(Utils.printStackTraceToString(e));
        }

        // return respuesta;

    }

    public static void/* Map<String, Object> */ GeneraDocumentoNotaCreditoXMLUBL21(Map<String, Object> map,
                                                                                   V_Varios_FacturacionBean empresa) {

        // Map<String, Object> respuesta = new HashMap<>();

        switch (map.get("CodigoAfectacionIGV").toString()) {
            case "10":
                CODIGO_TIPO_TRIBUTO = "1000";
                NOMBRE_TRIBUTO = "IGV";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "18.00";
                break;
            case "20":
                CODIGO_TIPO_TRIBUTO = "9997";
                NOMBRE_TRIBUTO = "EXO";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "0.00";
                break;
        }
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

            Attr attrcacaddUdt = doc.createAttribute("xmlns:cacadd");
            attrcacaddUdt.setValue("urn:e-billing:aggregates");
            rootElementCreditNote.setAttributeNode(attrcacaddUdt);

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

            // <cacadd:ExtraParameters
            // xmlns:sac='urn:sunat.names.specification.ubl_2_0.2_0.peru:schema:xsd:SunatAggregateComponents-1'
            // />
            /*
             * Element cacaddExtraParameters =
             * doc.createElement("cacadd:ExtraParameters");
             * extExtensionContent.appendChild(cacaddExtraParameters);
             *
             * Attr xmlnssac = doc.createAttribute("xmlns:sac");
             * xmlnssac.setValue(
             * "urn:sunat.names.specification.ubl_2_0.2_0.peru:schema:xsd:SunatAggregateComponents-1"
             * ); cacaddExtraParameters.setAttributeNode(xmlnssac);
             */

            // ****************************** VERIFICAR CON LA SUNAT SI SON
            // MANDATORIOS ESTOS CAMPOS DE TOTALES
            // *******************************************************************

            // TOTAL DE VENTA OPERACIONES GRAVADAS (MANDATORIO)
            Element sacAdditionalMonetaryTotalGravadas = doc.createElement("sac:AdditionalMonetaryTotal");
            sacAdditionalInformation.appendChild(sacAdditionalMonetaryTotalGravadas);

            Element cbcIDGravadas = doc.createElement("cbc:ID");
            cbcIDGravadas.appendChild(doc.createTextNode(map.get("CodigoTotalVenta").toString()));// CODIGO
            // 1003
            // ES
            // PARA
            // PASAJES
            // -
            // CODIGO
            // PARA
            // ENCOMIENDAS
            // ES
            // 1001
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcIDGravadas);

            Element cbcPayableAmountGravadas = doc.createElement("cbc:PayableAmount");
            cbcPayableAmountGravadas.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));
            sacAdditionalMonetaryTotalGravadas.appendChild(cbcPayableAmountGravadas);

            Attr attrGravadas = doc.createAttribute("currencyID");
            attrGravadas.setValue(map.get("Moneda").toString().trim());
            cbcPayableAmountGravadas.setAttributeNode(attrGravadas);

            // TOTAL DE VENTA MONTO POR VENTA (EN LETRAS)
            Element sacAdditionalProperty = doc.createElement("sac:AdditionalProperty");
            sacAdditionalInformation.appendChild(sacAdditionalProperty);

            Element cbcIDProperty = doc.createElement("cbc:ID");
            cbcIDProperty.appendChild(doc.createTextNode(Utils.CODIGO_MONTO_LETRAS));
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

            // *****************************************************************

            // VERSION DE UBL (2.1)
            Element ublVersionID = doc.createElement("cbc:UBLVersionID");
            ublVersionID.appendChild(doc.createTextNode("2.1"));
            rootElementCreditNote.appendChild(ublVersionID);

            // VERSION DE LA ESTRUCTURA DEL DOCUMENTO (VER PAG. 57)
            Element cbcCustomizationID = doc.createElement("cbc:CustomizationID");
            cbcCustomizationID.appendChild(doc.createTextNode("2.0"));

            rootElementCreditNote.appendChild(cbcCustomizationID);

            Attr schemeAgencyNameGlobal;
            schemeAgencyNameGlobal = doc.createAttribute("schemeAgencyName");
            schemeAgencyNameGlobal.setValue("PE:SUNAT"); // Ruc
            cbcCustomizationID.setAttributeNode(schemeAgencyNameGlobal);

            // NUMERO DEL DOCUMENTO DE LA NOTA DE CREDITO (SERIE Y NUMERO --
            // CARACTER INICIAL --> "F" CUANDO AFECTA A UNA FACTURA Y "B" CUANDO
            // AFECTA A UNA BOLETA)
            Element cbcID = doc.createElement("cbc:ID");
            cbcID.appendChild(doc.createTextNode(map.get("DocumentoElectronico").toString().trim()));
            rootElementCreditNote.appendChild(cbcID);

            // FECHA DE EMISION
            Element cbcIssueDate = doc.createElement("cbc:IssueDate");
            cbcIssueDate.appendChild(doc.createTextNode(map.get("FechaEmision").toString().trim()));
            rootElementCreditNote.appendChild(cbcIssueDate);

            // HORA DE EMISION

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();

            Element cbcIssuetTime = doc.createElement("cbc:IssueTime");
            cbcIssuetTime.appendChild(doc.createTextNode(dateFormat.format(date)));// HORA
            // DE
            // EMISI�N
            rootElementCreditNote.appendChild(cbcIssuetTime);

            // CODIGO DE LEYENDA
            Element cbcNote = doc.createElement("cbc:Note");
            cbcNote.appendChild(doc.createCDATASection(map.get("MontoLetras").toString().trim()));
            rootElementCreditNote.appendChild(cbcNote);
            // falta
            Attr languageLocaleID;
            languageLocaleID = doc.createAttribute("languageLocaleID");
            languageLocaleID.setValue("1000"); // Ruc
            cbcNote.setAttributeNode(languageLocaleID);

            // CODIGO DE MONEDA (CATALOGO N. 2)
            Element cbcDocumentCurrencyCode = doc.createElement("cbc:DocumentCurrencyCode");
            cbcDocumentCurrencyCode.appendChild(doc.createTextNode(map.get("Moneda").toString().trim()));
            rootElementCreditNote.appendChild(cbcDocumentCurrencyCode);
            // *************************************************************
            // DATOS DEL DOCUMENTO A APLICAR
            Element cacDiscrepancyResponse = doc.createElement("cac:DiscrepancyResponse");
            rootElementCreditNote.appendChild(cacDiscrepancyResponse);

            // DOCUMENTO APLICABLE (FACTURA O BOLETA)
            Element cbcReferenceID = doc.createElement("cbc:ReferenceID");
            cbcReferenceID.appendChild(doc.createTextNode(map.get("DocumentoElectronicoAplicar").toString().trim()));
            cacDiscrepancyResponse.appendChild(cbcReferenceID);

            // CODIGO DE TIPO DE NOTA DE CREDITO (CATALOGO No 9)
            Element cbcRespondeCode = doc.createElement("cbc:ResponseCode");
            cbcRespondeCode.appendChild(doc.createTextNode(map.get("CodigoMotivoNota").toString().trim()));// falta
            // atributos
            cacDiscrepancyResponse.appendChild(cbcRespondeCode);

            // listAgencyName="PE:SUNAT" listName="Tipo de nota de credito"
            // listURI="urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo09"
            Attr listAgencyName, listName, listURI1;
            listAgencyName = doc.createAttribute("listAgencyName");
            listAgencyName.setValue("PE:SUNAT"); // Ruc
            cbcRespondeCode.setAttributeNode(listAgencyName);

            listName = doc.createAttribute("listName");
            listName.setValue("Tipo de nota de credito"); // Ruc
            cbcRespondeCode.setAttributeNode(listName);

            listURI1 = doc.createAttribute("listURI");
            listURI1.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo09"); // Ruc
            cbcRespondeCode.setAttributeNode(listURI1);

            // CODIGO DE TIPO DE NOTA DE CREDITO (CATALOGO No 9)
            Element cbcDescription = doc.createElement("cbc:Description");
            cbcDescription.appendChild(doc.createCDATASection(map.get("Descripcion1").toString().trim()));
            cacDiscrepancyResponse.appendChild(cbcDescription);

            // *************************************************************

            Element cacBillingReference = doc.createElement("cac:BillingReference");
            rootElementCreditNote.appendChild(cacBillingReference);

            Element cacInvoiceDocumentReference = doc.createElement("cac:InvoiceDocumentReference");
            cacBillingReference.appendChild(cacInvoiceDocumentReference);

            // VALOR DEL DOCUEMTO A QUE SE APLICA
            Element cbcInvoiceID = doc.createElement("cbc:ID");
            cbcInvoiceID.appendChild(doc.createTextNode(map.get("DocumentoElectronicoAplicar").toString().trim()));
            cacInvoiceDocumentReference.appendChild(cbcInvoiceID);

            // VALOR DEL TIPO DE DOCUEMTO A QUE SE APLICA
            Element cbcDocumentTypeCode = doc.createElement("cbc:DocumentTypeCode");
            cbcDocumentTypeCode.appendChild(doc.createTextNode(map.get("TipoDocumentoAplicar").toString().trim()));// falta
            // atributos
            cacInvoiceDocumentReference.appendChild(cbcDocumentTypeCode);

            listAgencyName = doc.createAttribute("listAgencyName");
            listAgencyName.setValue("PE:SUNAT"); // Ruc
            cbcDocumentTypeCode.setAttributeNode(listAgencyName);

            listName = doc.createAttribute("listName");
            listName.setValue("Tipo de Documento"); // Ruc
            cbcDocumentTypeCode.setAttributeNode(listName);

            listURI1 = doc.createAttribute("listURI");
            listURI1.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo01"); // Ruc
            cbcDocumentTypeCode.setAttributeNode(listURI1);

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

            Element cacParty = doc.createElement("cac:Party");
            cacAccountingSupplierParty.appendChild(cacParty);

            Element cacPartyIdentification_Supplier = doc.createElement("cac:PartyIdentification");
            cacParty.appendChild(cacPartyIdentification_Supplier);

            Element cbcID_suplier = doc.createElement("cbc:ID");
            cbcID_suplier.appendChild(doc.createTextNode(empresa.getRuc()));
            cacPartyIdentification_Supplier.appendChild(cbcID_suplier);

            Attr schemeID, schemeName, schemeAgencyName, schemeURI;

            schemeID = doc.createAttribute("schemeID");
            schemeID.setValue("6"); // Ruc
            cbcID_suplier.setAttributeNode(schemeID);

            schemeName = doc.createAttribute("schemeName");
            schemeName.setValue("SUNAT:Identificador de Documento de Identidad");
            cbcID_suplier.setAttributeNode(schemeName);

            schemeAgencyName = doc.createAttribute("schemeAgencyName");
            schemeAgencyName.setValue("PE:SUNAT");
            cbcID_suplier.setAttributeNode(schemeAgencyName);

            schemeURI = doc.createAttribute("schemeURI");
            schemeURI.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06");
            cbcID_suplier.setAttributeNode(schemeURI);

            Element cacPartyName_Supplier = doc.createElement("cac:PartyName");
            cacParty.appendChild(cacPartyName_Supplier);

            Element cbcName_suplier = doc.createElement("cbc:Name");
            cbcName_suplier.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartyName_Supplier.appendChild(cbcName_suplier);

            // cac:PartyLegalEntity
            Element cacPartyLegalEntity_Supplier = doc.createElement("cac:PartyLegalEntity");
            cacParty.appendChild(cacPartyLegalEntity_Supplier);

            // cbc:RegistrationName
            Element cbcRegistrationName_Supplier = doc.createElement("cbc:RegistrationName");
            cbcRegistrationName_Supplier.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartyLegalEntity_Supplier.appendChild(cbcRegistrationName_Supplier);
            // cacPartyLegalEntity_Supplier

            Element cacRegistrationAddress_Supplier = doc.createElement("cac:RegistrationAddress");
            cacPartyLegalEntity_Supplier.appendChild(cacRegistrationAddress_Supplier);

            Element cbcAddressTypeCode_Supplier = doc.createElement("cbc:AddressTypeCode");
            cbcAddressTypeCode_Supplier.appendChild(doc.createTextNode(empresa.getUbigeo()));
            cacRegistrationAddress_Supplier.appendChild(cbcAddressTypeCode_Supplier);

            listAgencyName = doc.createAttribute("listAgencyName");
            listAgencyName.setValue("PE:SUNAT");
            cbcAddressTypeCode_Supplier.setAttributeNode(listAgencyName);

            listName = doc.createAttribute("listName");
            listName.setValue("Establecimientos anexos"); // Ruc
            cbcAddressTypeCode_Supplier.setAttributeNode(listName);

            // *************************************************************
            // DATOS DEL RECEPTOR DE LA NOTA DE CREDITO

            Element cacAccountingCustomerParty = doc.createElement("cac:AccountingCustomerParty");
            rootElementCreditNote.appendChild(cacAccountingCustomerParty);

            Element cacParty_Customer = doc.createElement("cac:Party");
            cacAccountingCustomerParty.appendChild(cacParty_Customer);

            Element cacPartyIdentification_Customer = doc.createElement("cac:PartyIdentification");
            cacParty_Customer.appendChild(cacPartyIdentification_Customer);

            Element cbcID_Customer = doc.createElement("cbc:ID");// boleta o
            // factura
            cbcID_Customer.appendChild(doc.createTextNode(map.get("TipoDocumentoAplicar").toString() == "03"
                    ? map.get("DNI").toString() : map.get("Ruc").toString()));
            cacPartyIdentification_Customer.appendChild(cbcID_Customer);

            schemeID = doc.createAttribute("schemeID");
            schemeID.setValue(map.get("TipoDocumentoReceptor").toString()); // Ruc
            cbcID_Customer.setAttributeNode(schemeID);

            schemeName = doc.createAttribute("schemeName");
            schemeName.setValue("SUNAT:Identificador de Documento de Identidad");
            cbcID_Customer.setAttributeNode(schemeName);

            schemeAgencyName = doc.createAttribute("schemeAgencyName");
            schemeAgencyName.setValue("PE:SUNAT");
            cbcID_Customer.setAttributeNode(schemeAgencyName);

            schemeURI = doc.createAttribute("schemeURI");
            schemeURI.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06");
            cbcID_Customer.setAttributeNode(schemeURI);

            Element cacPartyLegalEntity_Customer = doc.createElement("cac:PartyLegalEntity");
            cacParty_Customer.appendChild(cacPartyLegalEntity_Customer);

            Element cbcRegistrationName_Customer = doc.createElement("cbc:RegistrationName");
            cbcRegistrationName_Customer
                    .appendChild(doc.createCDATASection(map.get("TipoDocumentoAplicar").toString() == "03"
                            ? map.get("DNI").toString().equals("-") ? "---" : map.get("DNI").toString()
                            : map.get("Razon").equals("-") ? "---" : map.get("Razon").toString()));

            cacPartyLegalEntity_Customer.appendChild(cbcRegistrationName_Customer);

            // *******************************************************************************************
            // INFORMACION DE SUMATORIA IVG GLOBAL

            Element cacTaxTotalGlobal = doc.createElement("cac:TaxTotal");
            rootElementCreditNote.appendChild(cacTaxTotalGlobal);

            Element cbcTaxAmountGlobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));
            cacTaxTotalGlobal.appendChild(cbcTaxAmountGlobal);

            Attr attrTaxAmountGlobal = doc.createAttribute("currencyID");
            attrTaxAmountGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountGlobal.setAttributeNode(attrTaxAmountGlobal);

            Element cacTaxSubtotalGlobal = doc.createElement("cac:TaxSubtotal");
            cacTaxTotalGlobal.appendChild(cacTaxSubtotalGlobal);

            Element cbcTaxableAmountGlobal = doc.createElement("cbc:TaxableAmount");
            cbcTaxableAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));/// COSTO
            /// POR
            /// TRANSACCI�N
            cacTaxSubtotalGlobal.appendChild(cbcTaxableAmountGlobal);

            Attr attrTaxableAmountGlobal = doc.createAttribute("currencyID");
            attrTaxableAmountGlobal.setValue(map.get("Moneda").toString());
            cbcTaxableAmountGlobal.setAttributeNode(attrTaxableAmountGlobal);

            Element cbcTaxAmountSubtotalGblobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotalGblobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotalGlobal.appendChild(cbcTaxAmountSubtotalGblobal);

            Attr attrTaxAmountSubtotalGlobal = doc.createAttribute("currencyID");
            attrTaxAmountSubtotalGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotalGblobal.setAttributeNode(attrTaxAmountSubtotalGlobal);

            Element cacTaxtCategoryGlobal = doc.createElement("cac:TaxCategory");
            cacTaxSubtotalGlobal.appendChild(cacTaxtCategoryGlobal);
            /*

             */
            Element cbcIDGlobal = doc.createElement("cbc:ID");
            cbcIDGlobal.appendChild(doc.createTextNode("S"));
            cacTaxtCategoryGlobal.appendChild(cbcIDGlobal);

            Element cacTaxSchemeGlobal = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobal.appendChild(cacTaxSchemeGlobal);

            Element cacTaxSchemeIDGlobal = doc.createElement("cbc:ID");
            cacTaxSchemeIDGlobal.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobal.appendChild(cacTaxSchemeIDGlobal);

            // schemeAgencyName="PE:SUNAT" schemeName="Codigo de tributos"
            // schemeURI="urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05"

            Attr attrschemeAgencyNameGlobal = doc.createAttribute("schemeAgencyName");
            attrschemeAgencyNameGlobal.setValue("PE:SUNAT");
            cacTaxSchemeIDGlobal.setAttributeNode(attrschemeAgencyNameGlobal);

            Attr attrschemeName = doc.createAttribute("schemeName");
            attrschemeName.setValue("Codigo de tributos");
            cacTaxSchemeIDGlobal.setAttributeNode(attrschemeName);

            Attr attrschemeURI = doc.createAttribute("schemeURI");
            attrschemeURI.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05");
            cacTaxSchemeIDGlobal.setAttributeNode(attrschemeURI);

            Element cbcTaxSchemeNameGlobal = doc.createElement("cbc:Name");
            cbcTaxSchemeNameGlobal.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxSchemeNameGlobal);

            Element cbcTaxTypeCodeGlobal = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCodeGlobal.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxTypeCodeGlobal);

            // *************************************************************

            /*
             * Element cacPartyLegalEntityReceptor =
             * doc.createElement("cac:PartyLegalEntity");
             * cacPartyReceptor.appendChild(cacPartyLegalEntityReceptor);
             *
             * Element cbcRegistrationNameReceptor =
             * doc.createElement("cbc:RegistrationName");
             * cbcRegistrationNameReceptor.appendChild(doc.createCDATASection(
             * map.get("Razon").toString()));
             * cacPartyLegalEntityReceptor.appendChild(
             * cbcRegistrationNameReceptor);
             */

            // *************************************************************
            // IMPORTE TOTAL A PAGAR POR EL DOCUMENTO

            Element cacLegalMonetaryTotal = doc.createElement("cac:LegalMonetaryTotal");
            rootElementCreditNote.appendChild(cacLegalMonetaryTotal);
            /*
             * Element cbcLineExtensionAmount =
             * doc.createElement("cbc:LineExtensionAmount");
             * cbcLineExtensionAmount.appendChild(doc.createTextNode(String.
             * valueOf(map.get("TotalSinIGV"))));
             * cacLegalMonetaryTotal.appendChild(cbcLineExtensionAmount);
             *
             * Attr attrcurrencyID = doc.createAttribute("currencyID");
             * attrcurrencyID.setValue(map.get("Moneda").toString()/*"PEN");
             * cbcLineExtensionAmount.setAttributeNode(attrcurrencyID);
             */

            Element cbcPayableAmount = doc.createElement("cbc:PayableAmount");
            cbcPayableAmount.appendChild(doc.createTextNode(String.valueOf(map.get("Total"))));
            cacLegalMonetaryTotal.appendChild(cbcPayableAmount);

            Attr attrMonetaryTotal = doc.createAttribute("currencyID");
            attrMonetaryTotal.setValue(map.get("Moneda").toString()/* "PEN" */);
            cbcPayableAmount.setAttributeNode(attrMonetaryTotal);

            // *************************************************************
            Element cacCreditNoteLine = doc.createElement("cac:CreditNoteLine");
            rootElementCreditNote.appendChild(cacCreditNoteLine);

            // CALOR DEL ITEM SIEMPRE VA SER 1
            Element cbcCreditNoteLineID = doc.createElement("cbc:ID");
            cbcCreditNoteLineID.appendChild(doc.createTextNode("1"));
            cacCreditNoteLine.appendChild(cbcCreditNoteLineID);

            Element cbcCreditedQuantity = doc.createElement("cbc:CreditedQuantity");
            cbcCreditedQuantity.appendChild(doc.createTextNode("1"));
            cacCreditNoteLine.appendChild(cbcCreditedQuantity);

            // unitCode="NIU"
            Attr unitCodeQuantity = doc.createAttribute("unitCode");
            unitCodeQuantity.setValue("ZZ");
            cbcCreditedQuantity.setAttributeNode(unitCodeQuantity);

            Element cbcLineExtensionAmountNote = doc.createElement("cbc:LineExtensionAmount");
            cbcLineExtensionAmountNote.appendChild(doc.createTextNode(map.get("TotalSinIGV").toString()));
            cacCreditNoteLine.appendChild(cbcLineExtensionAmountNote);

            Attr currencyIDAmountNote = doc.createAttribute("currencyID");
            currencyIDAmountNote.setValue(map.get("Moneda").toString());
            cbcLineExtensionAmountNote.setAttributeNode(currencyIDAmountNote);

            Element cacPricingReference = doc.createElement("cac:PricingReference");
            // cacPricingReference.appendChild(doc.createTextNode("1"));
            cacCreditNoteLine.appendChild(cacPricingReference);

            Element cacAlternativeConditionPrice = doc.createElement("cac:AlternativeConditionPrice");
            // cacPricingReference.appendChild(doc.createTextNode("1"));
            cacPricingReference.appendChild(cacAlternativeConditionPrice);

            Element cbcPriceAmount = doc.createElement("cbc:PriceAmount");
            cbcPriceAmount.appendChild(doc.createTextNode(map.get("Total").toString()));
            cacAlternativeConditionPrice.appendChild(cbcPriceAmount);

            // currencyID="PEN"

            Attr AtPriceAmountLast = doc.createAttribute("currencyID");
            AtPriceAmountLast.setValue(map.get("Moneda").toString());
            cbcPriceAmount.setAttributeNode(AtPriceAmountLast);

            Element cbcPriceTypeCode = doc.createElement("cbc:PriceTypeCode");
            cbcPriceTypeCode.appendChild(doc.createTextNode("01"));
            cacAlternativeConditionPrice.appendChild(cbcPriceTypeCode);

            Attr listAgencyNamePriceTypeCode = doc.createAttribute("listAgencyName");
            listAgencyNamePriceTypeCode.setValue("PE:SUNAT");
            cbcPriceTypeCode.setAttributeNode(listAgencyNamePriceTypeCode);

            Attr listNamePriceTypeCode = doc.createAttribute("listName");
            listNamePriceTypeCode.setValue("Tipo de Precio");
            cbcPriceTypeCode.setAttributeNode(listNamePriceTypeCode);

            Attr listURIPriceTypeCode = doc.createAttribute("listURI");
            listURIPriceTypeCode.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo16");
            cbcPriceTypeCode.setAttributeNode(listURIPriceTypeCode);

            Element cacTaxTotalGlobalCredit = doc.createElement("cac:TaxTotal");
            cacCreditNoteLine.appendChild(cacTaxTotalGlobalCredit);

            Element cbcTaxAmountGlobalCredit = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountGlobalCredit.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA
            /// DEL
            /// IGV
            /// TOTAL
            /// (PARA
            /// PASAJES
            /// 0)
            cacTaxTotalGlobalCredit.appendChild(cbcTaxAmountGlobalCredit);

            Attr attrTaxAmountGlobalCredit = doc.createAttribute("currencyID");
            attrTaxAmountGlobalCredit.setValue(map.get("Moneda").toString());
            cbcTaxAmountGlobalCredit.setAttributeNode(attrTaxAmountGlobalCredit);

            Element cacTaxSubtotalGlobalCredit = doc.createElement("cac:TaxSubtotal");
            cacTaxTotalGlobalCredit.appendChild(cacTaxSubtotalGlobalCredit);

            Element cbcTaxableAmount = doc.createElement("cbc:TaxableAmount");
            cbcTaxableAmount.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));/// COSTO
            /// POR
            /// TRANSACCI�N
            cacTaxSubtotalGlobalCredit.appendChild(cbcTaxableAmount);

            Attr attrTaxableAmount = doc.createAttribute("currencyID");
            attrTaxableAmount.setValue(map.get("Moneda").toString());
            cbcTaxableAmount.setAttributeNode(attrTaxableAmount);

            Element cbcTaxAmountSubtotalGblobalCredit = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotalGblobalCredit.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotalGlobalCredit.appendChild(cbcTaxAmountSubtotalGblobalCredit);

            Attr attrTaxAmountSubtotalGlobalCredit = doc.createAttribute("currencyID");
            attrTaxAmountSubtotalGlobalCredit.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotalGblobalCredit.setAttributeNode(attrTaxAmountSubtotalGlobalCredit);

            Element cacTaxtCategoryGlobalCredit = doc.createElement("cac:TaxCategory");
            cacTaxSubtotalGlobalCredit.appendChild(cacTaxtCategoryGlobalCredit);

            // <cbc:Percent>18.00</cbc:Percent>
            Element cacTaxtPercent = doc.createElement("cbc:Percent");
            cacTaxtPercent.appendChild(doc.createTextNode(PORCENTAJE));
            cacTaxtCategoryGlobalCredit.appendChild(cacTaxtPercent);

            Element cbcTaxExemptionReasonCodeGlobalCredit = doc.createElement("cbc:TaxExemptionReasonCode");
            cbcTaxExemptionReasonCodeGlobalCredit
                    .appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR
            // CATALOGO
            // N�
            // 7
            // (
            // CODIGO
            // 20
            // PARA
            // PASAJES
            // -
            // CODIGO
            // 10
            // PARA
            // ENCOMIENDAS)
            cacTaxtCategoryGlobalCredit.appendChild(cbcTaxExemptionReasonCodeGlobalCredit);

            Element cacTaxSchemeGlobalCredit = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobalCredit.appendChild(cacTaxSchemeGlobalCredit);

            Element cacTaxSchemeIDGlobalCredit = doc.createElement("cbc:ID");
            cacTaxSchemeIDGlobalCredit.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobalCredit.appendChild(cacTaxSchemeIDGlobalCredit);

            Element cbcTaxSchemeNameGlobalCredit = doc.createElement("cbc:Name");
            cbcTaxSchemeNameGlobalCredit.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobalCredit.appendChild(cbcTaxSchemeNameGlobalCredit);

            Element cbcTaxTypeCodeGlobalCredit = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCodeGlobalCredit.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobalCredit.appendChild(cbcTaxTypeCodeGlobalCredit);

            Element cacitem = doc.createElement("cac:Item");
            cacCreditNoteLine.appendChild(cacitem);

            Element cbcDescriptionLast = doc.createElement("cbc:Description");
            // cbcDescriptionLast.appendChild(doc.createCDATASection("EJEMPLO"));
            // cacitem.appendChild(cbcDescriptionLast);

            if (map.get("ServicioAplicar").toString().trim().equals("B")) {
                cbcDescriptionLast.appendChild(doc.createCDATASection("SERVICIO DE TRANSPORTE EN LA RUTA: "
                        + map.get("DestinoDA") /* venta.getDestinoD() */));
                cacitem.appendChild(cbcDescriptionLast);
                System.out.println("ENTRO A CONDICION");
            } else {

                String Descripcion = "";
                if (!map.get("Cantidad1A").toString().trim().equals("")
                        && !map.get("Descripcion1A").toString().trim().equals("")) {

                    Descripcion += "/" + map.get("Cantidad1A").toString().trim() + " "
                            + map.get("Descripcion1A").toString().trim();
                }
                if (!map.get("Cantidad2A").toString().trim().equals("")
                        || !map.get("Descripcion2A").toString().trim().equals("")) {

                    Descripcion += "/" + map.get("Cantidad2A").toString().trim() + " "
                            + map.get("Descripcion2A").toString().trim();
                }
                if (!map.get("Cantidad3A").toString().trim().equals("")
                        || !map.get("Descripcion3A").toString().trim().equals("")) {

                    Descripcion += "/" + map.get("Cantidad3A").toString().trim() + " "
                            + map.get("Descripcion3A").toString().trim();
                }
                System.out.println("ENTRO A CONDICION 2");
                System.out.println("ENTRO A CONDICION 2 " + map.get("Descripcion2A").toString().trim());

                cbcDescriptionLast
                        .appendChild(doc.createCDATASection("SERVICIO DE ENVIO DE ENCOMIENDA: " + Descripcion));
                cacitem.appendChild(cbcDescriptionLast);
            }
            cacCreditNoteLine.appendChild(cacitem);

            Element cacSellersItemIdentification = doc.createElement("cac:SellersItemIdentification");
            cacitem.appendChild(cacSellersItemIdentification);

            Element cbcIDlast = doc.createElement("cbc:ID");
            cbcIDlast.appendChild(doc.createCDATASection("1"));
            cacSellersItemIdentification.appendChild(cbcIDlast);

            /*
             *
             *
             * <cbc:Description>P001 DESCRIPCI�N DEL PRODUCTO</cbc:Description>
             * <cac:CommodityClassification> <cbc:ItemClassificationCode
             * listID="UNSPSC" listAgencyName="GS1 US" listName=
             * "Item Classification">10000000</cbc:ItemClassificationCode>
             * </cac:CommodityClassification>
             */
            Element cacprice = doc.createElement("cac:Price");
            cacCreditNoteLine.appendChild(cacprice);

            Element cbcPriceAmountLast = doc.createElement("cbc:PriceAmount");
            cbcPriceAmountLast.appendChild(doc.createTextNode(map.get("TotalSinIGV").toString()));
            cacprice.appendChild(cbcPriceAmountLast);

            Attr attrGravadasLast = doc.createAttribute("currencyID");
            attrGravadasLast.setValue(map.get("Moneda").toString());
            cbcPriceAmountLast.setAttributeNode(attrGravadasLast);

            // <cbc:PriceAmount currencyID="PEN">100.0</cbc:PriceAmount>

            // ******* GENERANDO LA FIRMA DIGITAL

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(empresa.getRutaEnvioSunat() + empresa.getRuc() + "-"
                    + map.get("TipoDocumento") + "-" + map.get("DocumentoElectronico") + ".xml"));
            // StreamResult result = new StreamResult(new
            // File(empresa.getRutaEnvioSunat()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml")+".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioSunat() + empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");
            // String RutaXML =
            // empresa.getRutaEnvioSunat()+empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml");
            String NombreFile = empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");
            // String NombreFile =
            // empresa.getRuc()+"-"+map.get("TipoDocumento")+"-"+map.get("DocumentoElectronicoXml");
            // String RutaZip =
            // empresa.getRutaEnvioSunat()+map.get("DocumentoZip").toString();

            Init.init();

            DocumentBuilderFactory dof = DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            // Si el Documento XML no tiene la estructura Correcta entra al
            // catch SAXException
            Document doc1 = dof.newDocumentBuilder().parse(new FileInputStream(RutaXML + ".xml"));

            Constants.setSignatureSpecNSprefix("ds"); // Sino, pone por defecto
            // como prefijo: "ns"

            System.out.println("LINEA 1");
            // Cargamos el almacen de claves
            KeyStore ks = KeyStore.getInstance(Utils.KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()), empresa.getKeystorePassword().toCharArray());

            System.out.println("LINEA 2");
            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),
                    empresa.getKeystorePassword().toCharArray());
            File signatureFile = new File(RutaXML + ".xml");
            // @SuppressWarnings("deprecation")
            String baseURI = signatureFile.toString(); // BaseURI para las URL
            // Relativas.

            System.out.println("LINEA 3");
            // Instanciamos un objeto XMLSignature desde el Document. El
            // algoritmo de firma sera RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);

            ///// VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA 2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(1).appendChild(xmlSignature.getElement());

            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Anadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
            System.out.println("LINEA 4");
            // Anadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias());
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os = new FileOutputStream(RutaXML + ".xml");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();

            /*
             * NodeList nodeListhash =
             * doc1.getElementsByTagName("ds:DigestValue"); Node nodohash =
             * nodeListhash.item(0);
             *
             * if (nodohash instanceof Element){ respuesta.put("codehash",
             * nodohash.getTextContent().trim()); }
             */

            crearZip(RutaXML, signatureFile, NombreFile/* ,RutaZip */);

        } catch (Exception e) {
            log.info(Utils.printStackTraceToString(e));
        }

        // return respuesta;

    }

    public static void/* Map<String, Object> */ GeneraDocumentoNotaDebitoXMLUBL21(Map<String, Object> map,
                                                                                  V_Varios_FacturacionBean empresa) {

        switch (map.get("CodigoAfectacionIGV").toString()) {
            case "10":
                CODIGO_TIPO_TRIBUTO = "1000";
                NOMBRE_TRIBUTO = "IGV";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "18.00";
                break;
            case "20":
                CODIGO_TIPO_TRIBUTO = "9997";
                NOMBRE_TRIBUTO = "EXO";
                CODIGO_INTERNACIONAL_TRIBUTO = "VAT";
                PORCENTAJE = "0.00";
                break;
        }
        // Map<String, Object> respuesta = new HashMap<>();
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
            cbcIDGravadas.appendChild(doc.createTextNode(map.get("CodigoTotalVenta").toString()));// CODIGO
            // 1003
            // ES
            // PARA
            // PASAJES
            // -
            // CODIGO
            // PARA
            // ENCOMIENDAS
            // ES
            // 1001
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
            cbcIDProperty.appendChild(doc.createTextNode(Utils.CODIGO_MONTO_LETRAS));
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

            // *****************************************************************

            // VERSION DE UBL (2.0)
            Element ublVersionID = doc.createElement("cbc:UBLVersionID");
            ublVersionID.appendChild(doc.createTextNode("2.1"));
            rootElementDebitNote.appendChild(ublVersionID);

            // VERSION DE LA ESTRUCTURA DEL DOCUMENTO (VER PAG. 57)
            Element cbcCustomizationID = doc.createElement("cbc:CustomizationID");
            cbcCustomizationID.appendChild(doc.createTextNode("2.0"));
            rootElementDebitNote.appendChild(cbcCustomizationID);

            // NUMERO DEL DOCUMENTO DE LA NOTA DE CREDITO (SERIE Y NUMERO --
            // CARACTER INICIAL --> "F" CUANDO AFECTA A UNA FACTURA Y "B" CUANDO
            // AFECTA A UNA BOLETA)
            Element cbcID = doc.createElement("cbc:ID");
            cbcID.appendChild(doc.createTextNode(map.get("DocumentoElectronico").toString()));
            rootElementDebitNote.appendChild(cbcID);

            // FECHA DE EMISION
            Element cbcIssueDate = doc.createElement("cbc:IssueDate");
            cbcIssueDate.appendChild(doc.createTextNode(map.get("FechaEmision").toString()));
            rootElementDebitNote.appendChild(cbcIssueDate);

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();

            // HORA DE EMISION
            Element cbcIssuetTime = doc.createElement("cbc:IssueTime");
            cbcIssuetTime.appendChild(doc.createTextNode(dateFormat.format(date)));// HORA
            // DE
            // EMISI�N
            rootElementDebitNote.appendChild(cbcIssuetTime);

            // *************************************************************

            Element cbcNote = doc.createElement("cbc:Note");
            cbcNote.appendChild(doc.createCDATASection(map.get("MontoLetras").toString().trim()));
            rootElementDebitNote.appendChild(cbcNote);
            // falta
            Attr languageLocaleID;
            languageLocaleID = doc.createAttribute("languageLocaleID");
            languageLocaleID.setValue("1000"); // Ruc
            cbcNote.setAttributeNode(languageLocaleID);

            // CODIGO DE MONEDA (CATALOGO NO. 2)
            Element cbcDocumentCurrencyCode = doc.createElement("cbc:DocumentCurrencyCode");
            cbcDocumentCurrencyCode.appendChild(doc.createTextNode(map.get("Moneda").toString()));
            rootElementDebitNote.appendChild(cbcDocumentCurrencyCode);

            // *************************************************************
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

            /*
             * // DATOS DEL EMISOR DEL DOCUMENTO Element
             * cacAccountingSupplierParty =
             * doc.createElement("cac:AccountingSupplierParty");
             * rootElementDebitNote.appendChild(cacAccountingSupplierParty);
             *
             * // NUMERO DE DOCUMENTO DE IDENTIDAD (RUC) -- EMISOR Element
             * cbcCustomerAssignedAccountID =
             * doc.createElement("cbc:CustomerAssignedAccountID");
             * cbcCustomerAssignedAccountID.appendChild(doc.createTextNode(
             * empresa.getRuc())); cacAccountingSupplierParty.appendChild(
             * cbcCustomerAssignedAccountID);
             *
             * // TIPO DE DOCUMENTO DE IDENTIFICACION -- EMISOR Element
             * cbcAdditionalAccountID =
             * doc.createElement("cbc:AdditionalAccountID");
             * cbcAdditionalAccountID.appendChild(doc.createTextNode("6"));
             * cacAccountingSupplierParty.appendChild(cbcAdditionalAccountID);
             *
             * // TIPO DE DOCUMENTO DE IDENTIFICACION Element cacParty =
             * doc.createElement("cac:Party");
             * cacAccountingSupplierParty.appendChild(cacParty);
             *
             * Element cacPartyName = doc.createElement("cac:PartyName");
             * cacParty.appendChild(cacPartyName);
             *
             * Element cbcName = doc.createElement("cbc:Name");
             * cbcName.appendChild(doc.createCDATASection(empresa.getRazon()));
             * cacPartyName.appendChild(cbcName);
             *
             * // CODIGO POSTAL Element cacPostalAddress =
             * doc.createElement("cac:PostalAddress");
             * cacParty.appendChild(cacPostalAddress);
             *
             * Element cbcPostalAddressID = doc.createElement("cbc:ID");
             * cbcPostalAddressID.appendChild(doc.createTextNode(empresa.
             * getUbigeo())); cacPostalAddress.appendChild(cbcPostalAddressID);
             *
             * // DIRECCION POSTAL Element cbcStreetName =
             * doc.createElement("cbc:StreetName");
             * cbcStreetName.appendChild(doc.createTextNode(empresa.getDireccion
             * ())); cacPostalAddress.appendChild(cbcStreetName);
             *
             * // CODIGO DE PAIS Element cbcCountry =
             * doc.createElement("cac:Country");
             * cacPostalAddress.appendChild(cbcCountry);
             *
             * Element cbcIdentificationCode =
             * doc.createElement("cbc:IdentificationCode");
             * cbcIdentificationCode.appendChild(doc.createTextNode(empresa.
             * getCodigoPais())); cbcCountry.appendChild(cbcIdentificationCode);
             *
             * // DATOS DEL EMISOR Element cacPartyLegalEntity =
             * doc.createElement("cac:PartyLegalEntity");
             * cacParty.appendChild(cacPartyLegalEntity);
             *
             *
             * // DATOS DEL EMISOR (DESCRIPCION) Element cbcRegistrationName =
             * doc.createElement("cbc:RegistrationName");
             * cbcRegistrationName.appendChild(doc.createCDATASection(empresa.
             * getRazon()));
             * cacPartyLegalEntity.appendChild(cbcRegistrationName);
             */

            Element cacAccountingSupplierParty = doc.createElement("cac:AccountingSupplierParty");
            rootElementDebitNote.appendChild(cacAccountingSupplierParty);

            Element cacParty = doc.createElement("cac:Party");
            cacAccountingSupplierParty.appendChild(cacParty);

            Element cacPartyIdentification_Supplier = doc.createElement("cac:PartyIdentification");
            cacParty.appendChild(cacPartyIdentification_Supplier);

            Element cbcID_suplier = doc.createElement("cbc:ID");
            cbcID_suplier.appendChild(doc.createTextNode(empresa.getRuc()));
            cacPartyIdentification_Supplier.appendChild(cbcID_suplier);

            Attr schemeID, schemeName, schemeAgencyName, schemeURI;

            schemeID = doc.createAttribute("schemeID");
            schemeID.setValue("6"); // Ruc
            cbcID_suplier.setAttributeNode(schemeID);

            schemeName = doc.createAttribute("schemeName");
            schemeName.setValue("SUNAT:Identificador de Documento de Identidad");
            cbcID_suplier.setAttributeNode(schemeName);

            schemeAgencyName = doc.createAttribute("schemeAgencyName");
            schemeAgencyName.setValue("PE:SUNAT");
            cbcID_suplier.setAttributeNode(schemeAgencyName);

            schemeURI = doc.createAttribute("schemeURI");
            schemeURI.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06");
            cbcID_suplier.setAttributeNode(schemeURI);

            Element cacPartyName_Supplier = doc.createElement("cac:PartyName");
            cacParty.appendChild(cacPartyName_Supplier);

            Element cbcName_suplier = doc.createElement("cbc:Name");
            cbcName_suplier.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartyName_Supplier.appendChild(cbcName_suplier);

            // cac:PartyLegalEntity
            Element cacPartyLegalEntity_Supplier = doc.createElement("cac:PartyLegalEntity");
            cacParty.appendChild(cacPartyLegalEntity_Supplier);

            // cbc:RegistrationName
            Element cbcRegistrationName_Supplier = doc.createElement("cbc:RegistrationName");
            cbcRegistrationName_Supplier.appendChild(doc.createCDATASection(empresa.getRazon()));
            cacPartyLegalEntity_Supplier.appendChild(cbcRegistrationName_Supplier);
            // cacPartyLegalEntity_Supplier

            Element cacRegistrationAddress_Supplier = doc.createElement("cac:RegistrationAddress");
            cacPartyLegalEntity_Supplier.appendChild(cacRegistrationAddress_Supplier);

            Element cbcAddressTypeCode_Supplier = doc.createElement("cbc:AddressTypeCode");
            cbcAddressTypeCode_Supplier.appendChild(doc.createTextNode(empresa.getUbigeo()));
            cacRegistrationAddress_Supplier.appendChild(cbcAddressTypeCode_Supplier);

            Attr listAgencyName = doc.createAttribute("listAgencyName");
            listAgencyName.setValue("PE:SUNAT");
            cbcAddressTypeCode_Supplier.setAttributeNode(listAgencyName);

            Attr listName = doc.createAttribute("listName");
            listName.setValue("Establecimientos anexos"); // Ruc
            cbcAddressTypeCode_Supplier.setAttributeNode(listName);

            // *************************************************************
            // DATOS DEL RECEPTOR DE LA NOTA DE DEBITO
            /*
             * Element cacAccountingCustomerParty =
             * doc.createElement("cac:AccountingCustomerParty");
             * rootElementDebitNote.appendChild(cacAccountingCustomerParty);
             *
             * // NUMERO DE DOCUMENTO DEL ADQUIRIENTE (CUANDO ES A UNA FACTURA
             * SE LE ASIGNA EL RUC DEL ADQUIRIENTE, SI ES UNA BOLETA SE LE
             * COLOCA UN GUION "-") Element cbcCustomerAssignedAccountIDReceptor
             * = doc.createElement("cbc:CustomerAssignedAccountID");
             * cbcCustomerAssignedAccountIDReceptor.appendChild(doc.
             * createTextNode(map.get("Ruc").toString()));
             * cacAccountingCustomerParty.appendChild(
             * cbcCustomerAssignedAccountIDReceptor);
             *
             * Element cbcAdditionalAccountIDReceptor =
             * doc.createElement("cbc:AdditionalAccountID");
             * cbcAdditionalAccountIDReceptor.appendChild(doc.createTextNode(map
             * .get("TipoDocumentoReceptor").toString()));
             * cacAccountingCustomerParty.appendChild(
             * cbcAdditionalAccountIDReceptor);
             *
             * Element cacPartyReceptor = doc.createElement("cac:Party");
             * cacAccountingCustomerParty.appendChild(cacPartyReceptor);
             *
             * Element cacPartyLegalEntityReceptor =
             * doc.createElement("cac:PartyLegalEntity");
             * cacPartyReceptor.appendChild(cacPartyLegalEntityReceptor);
             *
             * Element cbcRegistrationNameReceptor =
             * doc.createElement("cbc:RegistrationName");
             * cbcRegistrationNameReceptor.appendChild(doc.createCDATASection(
             * map.get("Razon").toString()));
             * cacPartyLegalEntityReceptor.appendChild(
             * cbcRegistrationNameReceptor);
             *
             */

            Element cacAccountingCustomerParty = doc.createElement("cac:AccountingCustomerParty");
            rootElementDebitNote.appendChild(cacAccountingCustomerParty);

            Element cacParty_Customer = doc.createElement("cac:Party");
            cacAccountingCustomerParty.appendChild(cacParty_Customer);

            Element cacPartyIdentification_Customer = doc.createElement("cac:PartyIdentification");
            cacParty_Customer.appendChild(cacPartyIdentification_Customer);

            Element cbcID_Customer = doc.createElement("cbc:ID");// boleta o
            // factura
            cbcID_Customer.appendChild(doc.createTextNode(map.get("TipoDocumentoAplicar").toString() == "03"
                    ? map.get("DNI").toString() : map.get("Ruc").toString()));
            cacPartyIdentification_Customer.appendChild(cbcID_Customer);

            schemeID = doc.createAttribute("schemeID");
            schemeID.setValue(map.get("TipoDocumentoReceptor").toString()); // Ruc
            cbcID_Customer.setAttributeNode(schemeID);

            schemeName = doc.createAttribute("schemeName");
            schemeName.setValue("SUNAT:Identificador de Documento de Identidad");
            cbcID_Customer.setAttributeNode(schemeName);

            schemeAgencyName = doc.createAttribute("schemeAgencyName");
            schemeAgencyName.setValue("PE:SUNAT");
            cbcID_Customer.setAttributeNode(schemeAgencyName);

            schemeURI = doc.createAttribute("schemeURI");
            schemeURI.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo06");
            cbcID_Customer.setAttributeNode(schemeURI);

            Element cacPartyLegalEntity_Customer = doc.createElement("cac:PartyLegalEntity");
            cacParty_Customer.appendChild(cacPartyLegalEntity_Customer);

            Element cbcRegistrationName_Customer = doc.createElement("cbc:RegistrationName");
            cbcRegistrationName_Customer
                    .appendChild(doc.createCDATASection(map.get("TipoDocumentoAplicar").toString() == "03"
                            ? map.get("DNI").toString().equals("-") ? "---" : map.get("DNI").toString()
                            : map.get("Razon").equals("-") ? "---" : map.get("Razon").toString()));

            cacPartyLegalEntity_Customer.appendChild(cbcRegistrationName_Customer);

            // *******************************************************************************************
            // INFORMACION DE SUMATORIA IVG GLOBAL
            /*
             * Element cacTaxTotalGlobal = doc.createElement("cac:TaxTotal");
             * rootElementDebitNote.appendChild(cacTaxTotalGlobal);
             *
             * Element cbcTaxAmountGlobal = doc.createElement("cbc:TaxAmount");
             * cbcTaxAmountGlobal.appendChild(doc.createTextNode(String.valueOf(
             * map.get("IGV"))));/// SUMATORIA DEL IGV TOTAL (PARA PASAJES 0)
             * cacTaxTotalGlobal.appendChild(cbcTaxAmountGlobal);
             *
             * Attr attrTaxAmountGlobal = doc.createAttribute("currencyID");
             * attrTaxAmountGlobal.setValue(map.get("Moneda").toString());
             * cbcTaxAmountGlobal.setAttributeNode(attrTaxAmountGlobal);
             *
             * Element cacTaxSubtotalGlobal =
             * doc.createElement("cac:TaxSubtotal");
             * cacTaxTotalGlobal.appendChild(cacTaxSubtotalGlobal);
             *
             * Element cbcTaxAmountSubtotalGblobal =
             * doc.createElement("cbc:TaxAmount");
             * cbcTaxAmountSubtotalGblobal.appendChild(doc.createTextNode(String
             * .valueOf(map.get("IGV")))); // "100"
             * cacTaxSubtotalGlobal.appendChild(cbcTaxAmountSubtotalGblobal);
             *
             * Attr attrTaxAmountSubtotalGlobal =
             * doc.createAttribute("currencyID");
             * attrTaxAmountSubtotalGlobal.setValue(map.get("Moneda").toString()
             * ); cbcTaxAmountSubtotalGblobal.setAttributeNode(
             * attrTaxAmountSubtotalGlobal);
             *
             * Element cacTaxtCategoryGlobal =
             * doc.createElement("cac:TaxCategory");
             * cacTaxSubtotalGlobal.appendChild(cacTaxtCategoryGlobal);
             *
             * Element cbcTaxExemptionReasonCodeGlobal =
             * doc.createElement("cbc:TaxExemptionReasonCode");
             * cbcTaxExemptionReasonCodeGlobal.appendChild(doc.createTextNode(
             * map.get("CodigoAfectacionIGV").toString()));// VERIFICAR CATALOGO
             * No 7 ( CODIGO 20 PARA PASAJES - CODIGO 10 PARA ENCOMIENDAS)
             * cacTaxtCategoryGlobal.appendChild(cbcTaxExemptionReasonCodeGlobal
             * );
             *
             * Element cacTaxSchemeGlobal = doc.createElement("cac:TaxScheme");
             * cacTaxtCategoryGlobal.appendChild(cacTaxSchemeGlobal);
             *
             * Element cacTaxSchemeIDGlobal = doc.createElement("cbc:ID");
             * //cacTaxSchemeIDGlobal.appendChild(doc.createTextNode(Utils.
             * CODIGO_TIPO_TRIBUTO));// VERIFICAR CATALOGO No 5 (CODIGO 1000)
             * cacTaxSchemeGlobal.appendChild(cacTaxSchemeIDGlobal);
             *
             * Element cbcTaxSchemeNameGlobal = doc.createElement("cbc:Name");
             * //cbcTaxSchemeNameGlobal.appendChild(doc.createTextNode(Utils.
             * NOMBRE_TRIBUTO));// VERIFICAR CATALOGO No 5 (CODIGO 1000)
             * cacTaxSchemeGlobal.appendChild(cbcTaxSchemeNameGlobal);
             *
             * Element cbcTaxTypeCodeGlobal =
             * doc.createElement("cbc:TaxTypeCode");
             * //cbcTaxTypeCodeGlobal.appendChild(doc.createTextNode(Utils.
             * CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR CATALOGO No 5 (CODIGO
             * 1000) cacTaxSchemeGlobal.appendChild(cbcTaxTypeCodeGlobal);
             *
             */

            Element cacTaxTotalGlobal = doc.createElement("cac:TaxTotal");
            rootElementDebitNote.appendChild(cacTaxTotalGlobal);

            Element cbcTaxAmountGlobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));
            cacTaxTotalGlobal.appendChild(cbcTaxAmountGlobal);

            Attr attrTaxAmountGlobal = doc.createAttribute("currencyID");
            attrTaxAmountGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountGlobal.setAttributeNode(attrTaxAmountGlobal);

            Element cacTaxSubtotalGlobal = doc.createElement("cac:TaxSubtotal");
            cacTaxTotalGlobal.appendChild(cacTaxSubtotalGlobal);

            Element cbcTaxableAmountGlobal = doc.createElement("cbc:TaxableAmount");
            cbcTaxableAmountGlobal.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));/// COSTO
            /// POR
            /// TRANSACCI�N
            cacTaxSubtotalGlobal.appendChild(cbcTaxableAmountGlobal);

            Attr attrTaxableAmountGlobal = doc.createAttribute("currencyID");
            attrTaxableAmountGlobal.setValue(map.get("Moneda").toString());
            cbcTaxableAmountGlobal.setAttributeNode(attrTaxableAmountGlobal);

            Element cbcTaxAmountSubtotalGblobal = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotalGblobal.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotalGlobal.appendChild(cbcTaxAmountSubtotalGblobal);

            Attr attrTaxAmountSubtotalGlobal = doc.createAttribute("currencyID");
            attrTaxAmountSubtotalGlobal.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotalGblobal.setAttributeNode(attrTaxAmountSubtotalGlobal);

            Element cacTaxtCategoryGlobal = doc.createElement("cac:TaxCategory");
            cacTaxSubtotalGlobal.appendChild(cacTaxtCategoryGlobal);
            /*

             */
            Element cbcIDGlobal = doc.createElement("cbc:ID");
            cbcIDGlobal.appendChild(doc.createTextNode("S"));
            cacTaxtCategoryGlobal.appendChild(cbcIDGlobal);

            Element cacTaxSchemeGlobal = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobal.appendChild(cacTaxSchemeGlobal);

            Element cacTaxSchemeIDGlobal = doc.createElement("cbc:ID");
            cacTaxSchemeIDGlobal.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobal.appendChild(cacTaxSchemeIDGlobal);

            // schemeAgencyName="PE:SUNAT" schemeName="Codigo de tributos"
            // schemeURI="urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05"

            Attr attrschemeAgencyNameGlobal = doc.createAttribute("schemeAgencyName");
            attrschemeAgencyNameGlobal.setValue("PE:SUNAT");
            cacTaxSchemeIDGlobal.setAttributeNode(attrschemeAgencyNameGlobal);

            Attr attrschemeName = doc.createAttribute("schemeName");
            attrschemeName.setValue("Codigo de tributos");
            cacTaxSchemeIDGlobal.setAttributeNode(attrschemeName);

            Attr attrschemeURI = doc.createAttribute("schemeURI");
            attrschemeURI.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo05");
            cacTaxSchemeIDGlobal.setAttributeNode(attrschemeURI);

            Element cbcTaxSchemeNameGlobal = doc.createElement("cbc:Name");
            cbcTaxSchemeNameGlobal.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobal.appendChild(cbcTaxSchemeNameGlobal);

            Element cbcTaxTypeCodeGlobal = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCodeGlobal.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
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

            // **************************************************************
            /*
             * Element cacCreditNoteLine =
             * doc.createElement("cac:DebitNoteLine");
             * rootElementDebitNote.appendChild(cacCreditNoteLine);
             *
             * // CALOR DEL ITEM SIEMPRE VA SER 1 Element cbcCreditNoteLineID =
             * doc.createElement("cbc:ID");
             * cbcCreditNoteLineID.appendChild(doc.createTextNode("1"));
             * cacCreditNoteLine.appendChild(cbcCreditNoteLineID);
             *
             * Element cbcLineExtensionAmount =
             * doc.createElement("cbc:LineExtensionAmount");
             * cbcLineExtensionAmount.appendChild(doc.createTextNode(String.
             * valueOf(map.get("TotalSinIGV"))));
             * cacCreditNoteLine.appendChild(cbcLineExtensionAmount);
             *
             * Attr attrLineExtensionAmount = doc.createAttribute("currencyID");
             * attrLineExtensionAmount.setValue(map.get("Moneda").toString());
             * cbcLineExtensionAmount.setAttributeNode(attrLineExtensionAmount);
             */

            // *************************************************************
            Element cacCreditNoteLine = doc.createElement("cac:DebitNoteLine");
            rootElementDebitNote.appendChild(cacCreditNoteLine);

            // CALOR DEL ITEM SIEMPRE VA SER 1
            Element cbcCreditNoteLineID = doc.createElement("cbc:ID");
            cbcCreditNoteLineID.appendChild(doc.createTextNode("1"));
            cacCreditNoteLine.appendChild(cbcCreditNoteLineID);

            Element cbcCreditedQuantity = doc.createElement("cbc:DebitedQuantity");
            cbcCreditedQuantity.appendChild(doc.createTextNode("1"));
            cacCreditNoteLine.appendChild(cbcCreditedQuantity);

            // unitCode="NIU"
            Attr unitCodeQuantity = doc.createAttribute("unitCode");
            unitCodeQuantity.setValue("ZZ");
            cbcCreditedQuantity.setAttributeNode(unitCodeQuantity);

            Element cbcLineExtensionAmountNote = doc.createElement("cbc:LineExtensionAmount");
            cbcLineExtensionAmountNote.appendChild(doc.createTextNode(map.get("TotalSinIGV").toString()));
            cacCreditNoteLine.appendChild(cbcLineExtensionAmountNote);

            Attr currencyIDAmountNote = doc.createAttribute("currencyID");
            currencyIDAmountNote.setValue(map.get("Moneda").toString());
            cbcLineExtensionAmountNote.setAttributeNode(currencyIDAmountNote);

            Element cacPricingReference = doc.createElement("cac:PricingReference");
            // cacPricingReference.appendChild(doc.createTextNode("1"));
            cacCreditNoteLine.appendChild(cacPricingReference);

            Element cacAlternativeConditionPrice = doc.createElement("cac:AlternativeConditionPrice");
            // cacPricingReference.appendChild(doc.createTextNode("1"));
            cacPricingReference.appendChild(cacAlternativeConditionPrice);

            Element cbcPriceAmount = doc.createElement("cbc:PriceAmount");
            cbcPriceAmount.appendChild(doc.createTextNode(map.get("Total").toString()));
            cacAlternativeConditionPrice.appendChild(cbcPriceAmount);

            // currencyID="PEN"

            Attr AtPriceAmountLast = doc.createAttribute("currencyID");
            AtPriceAmountLast.setValue(map.get("Moneda").toString());
            cbcPriceAmount.setAttributeNode(AtPriceAmountLast);

            Element cbcPriceTypeCode = doc.createElement("cbc:PriceTypeCode");
            cbcPriceTypeCode.appendChild(doc.createTextNode("01"));
            cacAlternativeConditionPrice.appendChild(cbcPriceTypeCode);

            Attr listAgencyNamePriceTypeCode = doc.createAttribute("listAgencyName");
            listAgencyNamePriceTypeCode.setValue("PE:SUNAT");
            cbcPriceTypeCode.setAttributeNode(listAgencyNamePriceTypeCode);

            Attr listNamePriceTypeCode = doc.createAttribute("listName");
            listNamePriceTypeCode.setValue("Tipo de Precio");
            cbcPriceTypeCode.setAttributeNode(listNamePriceTypeCode);

            Attr listURIPriceTypeCode = doc.createAttribute("listURI");
            listURIPriceTypeCode.setValue("urn:pe:gob:sunat:cpe:see:gem:catalogos:catalogo16");
            cbcPriceTypeCode.setAttributeNode(listURIPriceTypeCode);

            Element cacTaxTotalGlobalCredit = doc.createElement("cac:TaxTotal");
            cacCreditNoteLine.appendChild(cacTaxTotalGlobalCredit);

            Element cbcTaxAmountGlobalCredit = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountGlobalCredit.appendChild(doc.createTextNode(String.valueOf(map.get("IGV"))));/// SUMATORIA
            /// DEL
            /// IGV
            /// TOTAL
            /// (PARA
            /// PASAJES
            /// 0)
            cacTaxTotalGlobalCredit.appendChild(cbcTaxAmountGlobalCredit);

            Attr attrTaxAmountGlobalCredit = doc.createAttribute("currencyID");
            attrTaxAmountGlobalCredit.setValue(map.get("Moneda").toString());
            cbcTaxAmountGlobalCredit.setAttributeNode(attrTaxAmountGlobalCredit);

            Element cacTaxSubtotalGlobalCredit = doc.createElement("cac:TaxSubtotal");
            cacTaxTotalGlobalCredit.appendChild(cacTaxSubtotalGlobalCredit);

            Element cbcTaxableAmount = doc.createElement("cbc:TaxableAmount");
            cbcTaxableAmount.appendChild(doc.createTextNode(String.valueOf(map.get("TotalSinIGV"))));/// COSTO
            /// POR
            /// TRANSACCI�N
            cacTaxSubtotalGlobalCredit.appendChild(cbcTaxableAmount);

            Attr attrTaxableAmount = doc.createAttribute("currencyID");
            attrTaxableAmount.setValue(map.get("Moneda").toString());
            cbcTaxableAmount.setAttributeNode(attrTaxableAmount);

            Element cbcTaxAmountSubtotalGblobalCredit = doc.createElement("cbc:TaxAmount");
            cbcTaxAmountSubtotalGblobalCredit.appendChild(doc.createTextNode(String.valueOf(map.get("IGV")))); // "100"
            cacTaxSubtotalGlobalCredit.appendChild(cbcTaxAmountSubtotalGblobalCredit);

            Attr attrTaxAmountSubtotalGlobalCredit = doc.createAttribute("currencyID");
            attrTaxAmountSubtotalGlobalCredit.setValue(map.get("Moneda").toString());
            cbcTaxAmountSubtotalGblobalCredit.setAttributeNode(attrTaxAmountSubtotalGlobalCredit);

            Element cacTaxtCategoryGlobalCredit = doc.createElement("cac:TaxCategory");
            cacTaxSubtotalGlobalCredit.appendChild(cacTaxtCategoryGlobalCredit);

            // <cbc:Percent>18.00</cbc:Percent>
            Element cacTaxtPercent = doc.createElement("cbc:Percent");
            cacTaxtPercent.appendChild(doc.createTextNode(PORCENTAJE));
            cacTaxtCategoryGlobalCredit.appendChild(cacTaxtPercent);

            Element cbcTaxExemptionReasonCodeGlobalCredit = doc.createElement("cbc:TaxExemptionReasonCode");
            cbcTaxExemptionReasonCodeGlobalCredit
                    .appendChild(doc.createTextNode(map.get("CodigoAfectacionIGV").toString()));// VERIFICAR
            // CATALOGO
            // N�
            // 7
            // (
            // CODIGO
            // 20
            // PARA
            // PASAJES
            // -
            // CODIGO
            // 10
            // PARA
            // ENCOMIENDAS)
            cacTaxtCategoryGlobalCredit.appendChild(cbcTaxExemptionReasonCodeGlobalCredit);

            Element cacTaxSchemeGlobalCredit = doc.createElement("cac:TaxScheme");
            cacTaxtCategoryGlobalCredit.appendChild(cacTaxSchemeGlobalCredit);

            Element cacTaxSchemeIDGlobalCredit = doc.createElement("cbc:ID");
            cacTaxSchemeIDGlobalCredit.appendChild(doc.createTextNode(CODIGO_TIPO_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobalCredit.appendChild(cacTaxSchemeIDGlobalCredit);

            Element cbcTaxSchemeNameGlobalCredit = doc.createElement("cbc:Name");
            cbcTaxSchemeNameGlobalCredit.appendChild(doc.createTextNode(NOMBRE_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobalCredit.appendChild(cbcTaxSchemeNameGlobalCredit);

            Element cbcTaxTypeCodeGlobalCredit = doc.createElement("cbc:TaxTypeCode");
            cbcTaxTypeCodeGlobalCredit.appendChild(doc.createTextNode(CODIGO_INTERNACIONAL_TRIBUTO));// VERIFICAR
            // CATALOGO
            // N�
            // 5
            // (CODIGO
            // 1000)
            cacTaxSchemeGlobalCredit.appendChild(cbcTaxTypeCodeGlobalCredit);

            Element cacitem = doc.createElement("cac:Item");
            cacCreditNoteLine.appendChild(cacitem);

            Element cbcDescriptionLast = doc.createElement("cbc:Description");
            // cbcDescriptionLast.appendChild(doc.createCDATASection("EJEMPLO"));
            // cacitem.appendChild(cbcDescriptionLast);

            if (map.get("ServicioAplicar").toString().trim().equals("B")) {
                cbcDescriptionLast.appendChild(doc.createCDATASection("SERVICIO DE TRANSPORTE EN LA RUTA: "
                        + map.get("DestinoDA") /* venta.getDestinoD() */));
                cacitem.appendChild(cbcDescriptionLast);
                System.out.println("ENTRO A CONDICION");
            } else {

                String Descripcion = "";
                if (!map.get("Cantidad1A").toString().trim().equals("")
                        && !map.get("Descripcion1A").toString().trim().equals("")) {

                    Descripcion += "/" + map.get("Cantidad1A").toString().trim() + " "
                            + map.get("Descripcion1A").toString().trim();
                }
                if (!map.get("Cantidad2A").toString().trim().equals("")
                        || !map.get("Descripcion2A").toString().trim().equals("")) {

                    Descripcion += "/" + map.get("Cantidad2A").toString().trim() + " "
                            + map.get("Descripcion2A").toString().trim();
                }
                if (!map.get("Cantidad3A").toString().trim().equals("")
                        || !map.get("Descripcion3A").toString().trim().equals("")) {

                    Descripcion += "/" + map.get("Cantidad3A").toString().trim() + " "
                            + map.get("Descripcion3A").toString().trim();
                }
                System.out.println("ENTRO A CONDICION 2");
                System.out.println("ENTRO A CONDICION 2 " + map.get("Descripcion2A").toString().trim());

                cbcDescriptionLast.appendChild(doc.createCDATASection("SERVICIO DE ENVIO DE ENCOMIENDA: " + Descripcion));
                cacitem.appendChild(cbcDescriptionLast);
            }
            cacCreditNoteLine.appendChild(cacitem);

            Element cacSellersItemIdentification = doc.createElement("cac:SellersItemIdentification");
            cacitem.appendChild(cacSellersItemIdentification);

            Element cbcIDlast = doc.createElement("cbc:ID");
            cbcIDlast.appendChild(doc.createCDATASection("1"));
            cacSellersItemIdentification.appendChild(cbcIDlast);

            Element cacprice = doc.createElement("cac:Price");
            cacCreditNoteLine.appendChild(cacprice);

            Element cbcPriceAmountLast = doc.createElement("cbc:PriceAmount");
            cbcPriceAmountLast.appendChild(doc.createTextNode(map.get("TotalSinIGV").toString()));
            cacprice.appendChild(cbcPriceAmountLast);

            Attr attrGravadasLast = doc.createAttribute("currencyID");
            attrGravadasLast.setValue(map.get("Moneda").toString());
            cbcPriceAmountLast.setAttributeNode(attrGravadasLast);
            // ******* GENERANDO LA FIRMA DIGITAL

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "YES");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(empresa.getRutaEnvioSunat() + empresa.getRuc() + "-"
                    + map.get("TipoDocumento") + "-" + map.get("DocumentoElectronico") + ".xml"));

            transformer.transform(source, result);

            String RutaXML = empresa.getRutaEnvioSunat() + empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");
            String NombreFile = empresa.getRuc() + "-" + map.get("TipoDocumento") + "-"
                    + map.get("DocumentoElectronico");

            Init.init();

            DocumentBuilderFactory dof = DocumentBuilderFactory.newInstance();
            dof.setNamespaceAware(true);

            // Si el Documento XML no tiene la estructura Correcta entra al
            // catch SAXException
            Document doc1 = dof.newDocumentBuilder().parse(new FileInputStream(RutaXML + ".xml"));

            Constants.setSignatureSpecNSprefix("ds"); // Sino, pone por defecto
            // como prefijo: "ns"

            System.out.println("LINEA 1");
            // Cargamos el almacen de claves
            KeyStore ks = KeyStore.getInstance(Utils.KEYSTORE_TYPE);
            ks.load(new FileInputStream(empresa.getKeystoreFile()), empresa.getKeystorePassword().toCharArray());

            System.out.println("LINEA 2");
            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(empresa.getPrivateKeyAlias(),
                    empresa.getKeystorePassword().toCharArray());
            File signatureFile = new File(RutaXML + ".xml");
            // @SuppressWarnings("deprecation")
            String baseURI = signatureFile.toString(); // BaseURI para las URL
            // Relativas.

            System.out.println("LINEA 3");
            // Instanciamos un objeto XMLSignature desde el Document. El
            // algoritmo de firma sera RSA
            XMLSignature xmlSignature = new XMLSignature(doc1, baseURI, XMLSignature.ALGO_ID_SIGNATURE_RSA);

            ///// VERIFICAR AUN FALTA LA UBICACION DE LA FIRMA 2017-11-12 5:PM
            doc1.getElementsByTagName("ext:ExtensionContent").item(1).appendChild(xmlSignature.getElement());

            xmlSignature.setId("signatureKG");
            // Creamos el objeto que mapea: Document/Reference
            Transforms transforms = new Transforms(doc1);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);

            // Anadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
            System.out.println("LINEA 4");
            // Anadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(empresa.getPrivateKeyAlias());
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            xmlSignature.sign(privateKey);

            OutputStream os = new FileOutputStream(RutaXML + ".xml");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc1), new StreamResult(os));
            os.close();

            /*
             * NodeList nodeListhash =
             * doc1.getElementsByTagName("ds:DigestValue"); Node nodohash =
             * nodeListhash.item(0);
             *
             * if (nodohash instanceof Element){ respuesta.put("codehash",
             * nodohash.getTextContent().trim()); }
             */

            crearZip(RutaXML, signatureFile, NombreFile);

        } catch (Exception e) {
            log.info(Utils.printStackTraceToString(e));
        }

        // return respuesta;

    }

}
