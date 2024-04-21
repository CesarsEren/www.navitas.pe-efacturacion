package com.alo.digital.transportes.webapp.efact.util;

import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alo.digital.transportes.webapp.efact.beans.SpringSecurityUser;

public class Funciones {

	
	
	public static HSSFWorkbook FuncionesExcel(List<Map<String, Object>> data,SpringSecurityUser usuario) {
		
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFCellStyle style = wb.createCellStyle();
		HSSFFont font =wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		
		//--*********
		HSSFCellStyle styletitulo = wb.createCellStyle();
		HSSFFont fonttitulo =wb.createFont();
		fonttitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styletitulo.setFont(font);
		
		//--*********
		
		style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
         
		HSSFSheet sheet;
		
		sheet = wb.createSheet("CARGA");
		
		HSSFRow row1= sheet.createRow(2);
		HSSFRow rowCliente= sheet.createRow(3);
		HSSFRow rowRuc = sheet.createRow(4);
		
		
		HSSFCell titulo = row1.createCell(5);
		titulo.setCellValue("RASTREO DE ENVIOS CARGA");
		titulo.setCellStyle(styletitulo);
			
			HSSFCell Cliente = rowCliente.createCell(4);
			Cliente.setCellValue("CLIENTE : " + usuario.getRazonSocial());
			Cliente.setCellStyle(styletitulo);
			
			HSSFCell Ruc = rowRuc.createCell(4);
			Ruc.setCellValue("RUC : " + usuario.getRuc());
			Ruc.setCellStyle(styletitulo);
			
			HSSFRow row = sheet.createRow(6);
			HSSFCell documento = row.createCell(0);
			HSSFCell serieguia = row.createCell(1);
			HSSFCell numeroguia = row.createCell(2);
			HSSFCell remitente = row.createCell(3);
			HSSFCell consignado = row.createCell(4);
			HSSFCell fechaemision = row.createCell(5);
			HSSFCell fechaentrega = row.createCell(6);
			HSSFCell bultos = row.createCell(7);
			HSSFCell kilos = row.createCell(8);
			HSSFCell estado = row.createCell(9);
			HSSFCell origen = row.createCell(10);
			HSSFCell destino = row.createCell(11);
			HSSFCell empresa = row.createCell(12);
			HSSFCell serie = row.createCell(13);
			HSSFCell numero = row.createCell(14);
			HSSFCell flete = row.createCell(15);
			
			documento.setCellValue("DOCUMENTO");
			documento.setCellStyle(style);
			documento.getSheet().autoSizeColumn(0);
			
			serieguia.setCellValue("SERIE GUIA");
			serieguia.setCellStyle(style);
			serieguia.getSheet().autoSizeColumn(1);
			
			numeroguia.setCellValue("NUMERO GUIA");
			numeroguia.setCellStyle(style);
			numeroguia.getSheet().autoSizeColumn(2);
			
			remitente.setCellValue("REMITENTE");
			remitente.setCellStyle(style);
			remitente.getSheet().autoSizeColumn(3);
			
			consignado.setCellValue("CONSIGNADO");
			consignado.setCellStyle(style);
			consignado.getSheet().autoSizeColumn(4);
			
			fechaemision.setCellValue("FECHA EMISION");
			fechaemision.setCellStyle(style);
			fechaemision.getSheet().autoSizeColumn(5);
			
			fechaentrega.setCellValue("FECHA ENTREGA");
			fechaentrega.setCellStyle(style);
			fechaentrega.getSheet().autoSizeColumn(6);
			
			bultos.setCellValue("BULTOS");
			bultos.setCellStyle(style);
			bultos.getSheet().autoSizeColumn(7);
			
			kilos.setCellValue("KILOS");
			kilos.setCellStyle(style);
			kilos.getSheet().autoSizeColumn(8);
			
			estado.setCellValue("ESTADO");
			estado.setCellStyle(style);
			estado.getSheet().autoSizeColumn(9);
			
			origen.setCellValue("ORIGEN");
			origen.setCellStyle(style);
			origen.getSheet().autoSizeColumn(10);
			
			destino.setCellValue("DESTINO");
			destino.setCellStyle(style);
			destino.getSheet().autoSizeColumn(11);
			
			empresa.setCellValue("EMPRESA");
			empresa.setCellStyle(style);
			empresa.getSheet().autoSizeColumn(12);

			serie.setCellValue("SERIE");
			serie.setCellStyle(style);
			serie.getSheet().autoSizeColumn(13);
			
			numero.setCellValue("NUMERO");
			numero.setCellStyle(style);
			numero.getSheet().autoSizeColumn(14);
			
			flete.setCellValue("FLETE");
			flete.setCellStyle(style);
			flete.getSheet().autoSizeColumn(15);
		
		try {
			
				
				int e = 7 ;
				
				int h = 0 ;
				
				for (Map<String, Object> bean : data) {
					
					HSSFRow rowdato = sheet.createRow(e);
					
					HSSFCell documentod = rowdato.createCell(h);
					documentod.setCellValue(bean.get("DocumentoD").toString());
					//destinod1.getSheet().autoSizeColumn(h);
					
					h++;
					
					HSSFCell serieguiad = rowdato.createCell(h);
					serieguiad.setCellValue(bean.get("SerieGuia").toString());
					//origend1.getSheet().autoSizeColumn(h);
					
					h++;
					
					HSSFCell numeroguiad = rowdato.createCell(h);
					numeroguiad.setCellValue(bean.get("NumeroGuia").toString());
					//fechaemision1.getSheet().autoSizeColumn(h);
					
					h++;
					
					HSSFCell remitented = rowdato.createCell(h);
					remitented.setCellValue(bean.get("Remitente").toString());
					//fecha1.getSheet().autoSizeColumn(h);
					
					h++;
					
					HSSFCell consignadod = rowdato.createCell(h);
					consignadod.setCellValue(bean.get("Consignado").toString());
					//fechacancelacion1.getSheet().autoSizeColumn(h);
					
					h++;
					
					HSSFCell fechaemisiond = rowdato.createCell(h);
					fechaemisiond.setCellValue(bean.get("FechaEmision").toString());
					//consignado1.getSheet().autoSizeColumn(h);
					
					h++;
					
					
					HSSFCell fechaentregad = rowdato.createCell(h);
					fechaentregad.setCellValue(bean.get("FechaEntrega").toString());
					//guiacliente1.getSheet().autoSizeColumn(h);
					
					h++;
					
					HSSFCell bultosd = rowdato.createCell(h);
					bultosd.setCellValue(bean.get("Bultos").toString());
					//guiawari1.getSheet().autoSizeColumn(h);
					
					h++;
					
					HSSFCell kilosd = rowdato.createCell(h);
					kilosd.setCellValue(bean.get("Kilos").toString());
					//cantidad1.getSheet().autoSizeColumn(h);
					
					h++;
					
					HSSFCell estadod = rowdato.createCell(h);
					estadod.setCellValue(bean.get("Estado").toString());
					//peso1.getSheet().autoSizeColumn(h);
					
					h++;
					
					HSSFCell origend = rowdato.createCell(h);
					origend.setCellValue(bean.get("Origen").toString());
					
					h++;
					
					HSSFCell destinod = rowdato.createCell(h);
					destinod.setCellValue(bean.get("Destino").toString());
					
					h++;
					
					HSSFCell empresad = rowdato.createCell(h);
					empresad.setCellValue(bean.get("EmpresaD").toString());
					
					h++;
					
					HSSFCell seried = rowdato.createCell(h);
					seried.setCellValue(bean.get("Serie").toString());
					
					h++;
					
					HSSFCell numerod = rowdato.createCell(h);
					numerod.setCellValue(bean.get("Numero").toString());
					
					h++;
					
					HSSFCell fleted = rowdato.createCell(h);
					fleted.setCellValue(bean.get("Flete").toString());
					
					h = 0;
					e++;
				}
				
		}catch (Exception e) {
			e.printStackTrace();
		}
			
			return wb;
		
	}
	
}
