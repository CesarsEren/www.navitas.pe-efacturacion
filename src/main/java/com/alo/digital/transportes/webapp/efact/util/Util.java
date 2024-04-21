package com.alo.digital.transportes.webapp.efact.util;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.alo.digital.transportes.webapp.efact.beans.B_VentaBean;
import com.alo.digital.transportes.webapp.efact.beans.SpringSecurityUser;
import com.alo.digital.transportes.webapp.efact.beans.V_EncomTrackingBean;


public class Util {
	
	public static final String URL_PDF_TICKET_CLIENTES = "d3sc4rg4d0cum3nt0scl13nt3st1ck3t42018";
	public static final String URL_XML_CLIENTES = "d3sc4rg4d0cum3nt0scl13nt3sxml42018";
	public static final String URL_PDF_NROMAL_CLIENTES = "d3sc4rg4d0cum3nt0scl13nt3sn0rm4l42018";
	
	/*public static void main(String[] args) {
		

		try {
			
			UUID.randomUUID().toString().trim();
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			//System.out.println(Encryptor.CifrarBase64(UUID.randomUUID().toString()+new Date().getTime()+UUID.randomUUID().toString()));
			System.out.println(encoder.encode("ADMIN"));
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}*/
	
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
	
	public static String ImagesEncomiendas (V_EncomTrackingBean encomTracking) {
		
		String Images = "";
		
		if(encomTracking.getImagen1()!= null) {
			
			Images = Images +"<div class='col-xs-6 col-md-3' style='padding:4px 4px 4px 4px'>" +
					"<a download='"+UUID.randomUUID().toString().replace("-", "")+".png' href='data:image/png;base64,"+
								    Encryptor.BytesToBase64(encomTracking.getImagen1()).trim()+"'>"+
						"<img src='data:image/png;base64,"+Encryptor.BytesToBase64(encomTracking.getImagen1()).trim()+"' class='img-fluid img-thumbnail' >" +
					"</a>"+
				"</div>";	
			
		}
       if(encomTracking.getImagen2()!= null) {
			
			Images = Images +"<div class='col-xs-6 col-md-3' style='padding:4px 4px 4px 4px'>" +
					        "<a download='"+UUID.randomUUID().toString().replace("-", "")+".png' href='data:image/png;base64,"+
								    Encryptor.BytesToBase64(encomTracking.getImagen2()).trim()+"'>"+
						          "<img src='data:image/png;base64,"+Encryptor.BytesToBase64(encomTracking.getImagen2()).trim()+"' class='img-fluid img-thumbnail' >" +
					          "</a>"+
				          "</div>";	
			
		}
        
		
		if(encomTracking.getImagen3()!= null) {
		
		Images = Images +"<div class='col-xs-6 col-md-3' style='padding:4px 4px 4px 4px'>" +
				"<a download='"+UUID.randomUUID().toString().replace("-", "")+".png' href='data:image/png;base64,"+
							    Encryptor.BytesToBase64(encomTracking.getImagen3()).trim()+"'>"+
					"<img src='data:image/png;base64,"+Encryptor.BytesToBase64(encomTracking.getImagen3()).trim()+"' class='img-fluid img-thumbnail' >" +
				"</a>"+
			"</div>";	
		
		}
		if(encomTracking.getImagen4()!= null) {
		
		Images = Images +"<div class='col-xs-6 col-md-3' style='padding:4px 4px 4px 4px'>" +
				"<a download='"+UUID.randomUUID().toString().replace("-", "")+".png' href='data:image/png;base64,"+
							    Encryptor.BytesToBase64(encomTracking.getImagen4()).trim()+"'>"+
					"<img src='data:image/png;base64,"+Encryptor.BytesToBase64(encomTracking.getImagen4()).trim()+"' class='img-fluid img-thumbnail' >" +
				"</a>"+
			"</div>";	
		
		}
		if(encomTracking.getImagen5()!= null) {
		
		Images = Images +"<div class='col-xs-6 col-md-3' style='padding:4px 4px 4px 4px'>" +
				"<a download='"+UUID.randomUUID().toString().replace("-", "")+".png' href='data:image/png;base64,"+
							    Encryptor.BytesToBase64(encomTracking.getImagen5()).trim()+"'>"+
					"<img src='data:image/png;base64,"+Encryptor.BytesToBase64(encomTracking.getImagen5()).trim()+"' class='img-fluid img-thumbnail' >" +
				"</a>"+
			"</div>";	
		
		}
		if(encomTracking.getImagen6()!= null) {
		
		Images = Images +"<div class='col-xs-6 col-md-3' style='padding:4px 4px 4px 4px'>" +
				"<a download='"+UUID.randomUUID().toString().replace("-", "")+".png' href='data:image/png;base64,"+
							    Encryptor.BytesToBase64(encomTracking.getImagen6()).trim()+"'>"+
					"<img src='data:image/png;base64,"+Encryptor.BytesToBase64(encomTracking.getImagen6()).trim()+"' class='img-fluid img-thumbnail' >" +
				"</a>"+
			"</div>";	
		
		}
		if(encomTracking.getImagen7()!= null) {
		
		Images = Images +"<div class='col-xs-6 col-md-3' style='padding:4px 4px 4px 4px'>" +
				"<a download='"+UUID.randomUUID().toString().replace("-", "")+".png' href='data:image/png;base64,"+
							    Encryptor.BytesToBase64(encomTracking.getImagen7()).trim()+"'>"+
					"<img src='data:image/png;base64,"+Encryptor.BytesToBase64(encomTracking.getImagen7()).trim()+"' class='img-fluid img-thumbnail' >" +
				"</a>"+
			"</div>";	
		
		}
		if(encomTracking.getImagen8()!= null) {
		
		Images = Images +"<div class='col-xs-6 col-md-3' style='padding:4px 4px 4px 4px'>" +
				"<a download='"+UUID.randomUUID().toString().replace("-", "")+".png' href='data:image/png;base64,"+
							    Encryptor.BytesToBase64(encomTracking.getImagen8()).trim()+"'>"+
					"<img src='data:image/png;base64,"+Encryptor.BytesToBase64(encomTracking.getImagen8()).trim()+"' class='img-fluid img-thumbnail' >" +
				"</a>"+
			"</div>";	
		
		}
		if(encomTracking.getImagen9()!= null) {
		
		Images = Images +"<div class='col-xs-6 col-md-3' style='padding:4px 4px 4px 4px'>" +
				"<a download='"+UUID.randomUUID().toString().replace("-", "")+".png' href='data:image/png;base64,"+
							    Encryptor.BytesToBase64(encomTracking.getImagen9()).trim()+"'>"+
					"<img src='data:image/png;base64,"+Encryptor.BytesToBase64(encomTracking.getImagen9()).trim()+"' class='img-fluid img-thumbnail' >" +
				"</a>"+
			"</div>";	
		
		}
				
		if(encomTracking.getImagen10()!= null) {
		
		Images = Images +"<div class='col-xs-6 col-md-3' style='padding:4px 4px 4px 4px'>" +
		                     "<a download='"+UUID.randomUUID().toString().replace("-", "")+".png' href='data:image/png;base64,"+
				                              Encryptor.BytesToBase64(encomTracking.getImagen10()).trim()+"'>"+
		                           "<img src='data:image/png;base64,"+Encryptor.BytesToBase64(encomTracking.getImagen10()).trim()+"' class='img-fluid img-thumbnail' >" +
		                     "</a>"+
		                  "</div>";	
		
		}

		return Images;
		
		
	}
	
	
   /* public static byte[] obtieneBytesPDF(B_VentaBean ventaVisa,ResourceLoader resourceLoader){
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] FileData = null;
		
		try {
				
				com.itextpdf.text.Document documento = new com.itextpdf.text.Document(PageSize.A4.rotate());
				//com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
			
				PdfWriter.getInstance(documento, baos);
				documento.open();
				
				//GENERAMOS EL VOUCHER DE IDA
				
				Image logo = Image.getInstance(IOUtils.toByteArray(resourceLoader.getResource("classpath:/static/images/logo.png").getInputStream()));
				logo.scalePercent(50f);
				logo.setAbsolutePosition(25f, 520f);
				logo.setAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
				documento.add(logo);
				
				
				float[] columnWidths = {5,5,5,6,6,4,7,4,3,3,3,3,5,5,5,5,7,5,7};
				
				PdfPTable table = new PdfPTable(columnWidths);
				
				//PdfPTable table = new PdfPTable(19);
				//table.setWidthPercentage(110f);
				table.setWidthPercentage(105f);
				
				table = FuncionesFacturacionPDF.F_Facturacion_Electronica_FormatoGrande(table, null, null, null);
				
				documento.add(table);
				documento.close();
				
				FileData = baos.toByteArray();
		} 
		catch (Exception e) {
			//log.info(ErrorLog.printStackTraceToString(e));
			
		}
		
		return FileData;
	}*/
    
    public static byte[] obtieneBytesEXCEL (List<Map<String,Object>> data, SpringSecurityUser usuario) {
    	
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       byte[] FileData = null;
		
		try {
			
			HSSFWorkbook wb = Funciones.FuncionesExcel(data,usuario);
			wb.write(baos);
			
	        FileData = baos.toByteArray();
	        baos.flush();
	        baos.close(); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return FileData;
    	
    }

}
