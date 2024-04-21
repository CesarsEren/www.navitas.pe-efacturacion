package com.alo.digital.transportes.webapp.efact.util;

import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//import java.util.Map;
//import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;/*
												import org.joda.time.DateTime;
												import org.joda.time.format.DateTimeFormat;
												import org.joda.time.format.DateTimeFormatter;*/
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Utils {

	public static final String WEBCONTENT = "/WEB-INF/content/";
	public static final String RESERVABOLETO = "/WEB-INF/content/ReservaBoleto/";
	public static final String BOLETAJE = "/WEB-INF/content/Boletaje/";
	public static final String VENTAS = "/WEB-INF/content/Consultas/";
	public static final String REUTILIZABLES = "/WEB-INF/content/Reutilizables/";
	public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
	public static final String SITE_KEY = "6LeBNDEUAAAAABr9VEM1kxGSqDJrxBa0cpBgOMAs";
	public static final String SECRET_KEY = "6LeBNDEUAAAAAOq1S-5wPDR63P-ubeoMg-v31BTy";

	// PROPIEDADES PARA LA FACTURACI�N ELECTRONICA
	public static String USERNAME = "";
	public static String PASSWORD = "";
	public static String rutazip = "";// temporal
	public static final String KEYSTORE_TYPE = "JKS";

	/** CODIGO DE MONTO EN LETRAS (CATALOGO 15 - sac:AdditionalProperty/cbc:ID) **/

	// PASAJES
	public static final String CODIGO_MONTO_LETRAS = "1000";
	/** CODIGO TIPO DE TRIBUTO (CATALOGO 5 - cac:TaxScheme/cbc:ID) **/
	public static final String CODIGO_TIPO_TRIBUTO_PASAJES = "9997";// para factura y para boleta
	/** CODIGO TIPO DE TRIBUTO (CATALOGO 5 - cac:TaxScheme/cbc:Name) **/
	public static final String NOMBRE_TRIBUTO_PASAJES = "EXO";// para factura y para boleta
	/** CODIGO TIPO DE TRIBUTO (CATALOGO 5 - cac:TaxScheme/cbc:TaxTypeCode) **/
	public static final String CODIGO_INTERNACIONAL_TRIBUTO_PASAJES = "VAT";// para factura y para boleta

	// ENCOMIENDA
	public static final String CODIGO_TIPO_TRIBUTO_ENCOMIENDA = "1000";// para factura y para boleta
	/** CODIGO TIPO DE TRIBUTO (CATALOGO 5 - cac:TaxScheme/cbc:Name) **/
	public static final String NOMBRE_TRIBUTO_ENCOMIENDA = "IGV";// para factura y para boleta
	/** CODIGO TIPO DE TRIBUTO (CATALOGO 5 - cac:TaxScheme/cbc:TaxTypeCode) **/
	public static final String CODIGO_INTERNACIONAL_TRIBUTO_ENCOMIENDA = "VAT";// para factura y para boleta

	public static final String URL_PDF_TICKET_CLIENTES = "d3sc4rg4d3d0cum3t0s3l3ctr0n1c0st1ck3tcl13nt3s";
	public static final String URL_XML_CLIENTES = "d3sc4rg4d3d0cum3t0s3l3ctr0n1c0sxmlcl13nt3s";
	public static final String URL_CDR_CLIENTES = "d3sc4rg4d3d0cum3t0s3l3ctr0n1c0scdrcl13nt3s";
	public static final String URL_PDF_NORMAL_CLIENTES = "d3sc4rg4d3d0cum3t0s3l3ctr0n1c0sn0rm4lcl13nt3s";

	// PASAJES Y/O ENCOMIENDAS POR CARIDAD

	public static final String CODIGO_TIPO_TRIBUTO_EXCENTO = "9996";
	public static final String NOMBRE_TRIBUTO_EXCENTO = "GRA";
	public static final String CODIGO_INTERNACIONAL_EXCENTO = "FRE";

	private static final Log log = LogFactory.getLog(Utils.class);

	public static String printStackTraceToString(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));

		return errors.toString();
	}

	public static String FormatoFecha(String strFecha) {
		String resultado = "";
		try {
			if ((strFecha != null) && (!(strFecha.trim().equals("")))) {
				SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat from = new SimpleDateFormat("dd/MM/yyyy");
				resultado = to.format(from.parse(strFecha));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado;
	}

}
