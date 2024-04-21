$(function(){
	
	$('#dateRangePickerDocumentos').on('click',function(){
		$("#fechaDocumentos").datepicker('show');
		
	});
	
	$("#fechaDocumentos").datepicker({
		language : 'es',
		autoclose: true,
		todayHighlight: true
		
	});
	
});

$('#btnConsultaDocumentosElectronicos').on('click',function(){
	
	$('#tblDocumentosElectronicos').bootstrapTable('removeAll');
	$("#tblDocumentosElectronicos").bootstrapTable('refresh',
		{silent: false,
		 height: 450,
		 url :  '/facturacion/documentos/cosultadocumentosenviados', 
		 query : {
				 	empresa: $('#empresa').val(),
					fechaemision: $('#fechaDocumentos').val(),
					serie: $('#serie').val(),
					numero: $('#numero').val(),
					tipodocumento:  $('#tipodocumento').val(),
					ruc : $('#ruc').val()
			 	  }
		}
	);
});

function queryParams(params){
	params['empresa'] = $('#empresa').val();
	params['fechaemision'] = $('#fechaDocumentos').val();
	params['serie'] = $('#serie').val();
	params['numero'] = $('#numero').val();
	params['tipodocumento'] = $('#tipodocumento').val();
	params['ruc'] = $('#ruc').val();
	return params;
}

function responseHandlerDocumentos(res){
	
	if(!res.respuestaServer){
		
		 BootstrapDialog.show({
			  	title :'Mensaje de Advertencia!',
			  	type : BootstrapDialog.TYPE_DANGER,
	            message: res.mensajeServer,
	            closable: false,
	            buttons: [{
	                label: 'OK',
	                cssClass: 'btn btn-danger',
	                action: function(event){
	                    event.close();
	                }
	            }]
	        });
		return false;
		
	}
	
return res;
}


function FormatterEstado(valor){
	var html = [];
	if(valor == "S"){
		html.push("<span class='glyphicon glyphicon-ok' style='color:#5cb85c;'></span>");
	}else{
		html.push("<span class='glyphicon glyphicon-remove' style='color:#FF0000;'></span>");
	}
	html.push("</label>");
	
	return html.join('');
}

function RucExistente(event){
	
	var ruc=event.value.trim();
	var razon='';
	
	if(ruc!=''){
		
			$.ajax({
					//url : 'ClientesRUC',
					 //url : '/facturacion/documentos/enviosunat',
					url : '/facturacion/consultas/cliente',
					contentType: "application/json",
				 	//data : { RUC : ruc },
					data : JSON.stringify({ruc : ruc}),
				 	type : 'POST',
				 	dataType : 'json',
			success : function(json) {
				 		if(json.respuesta){
					 		razon =json.bean['razon'];
							Habilitar_Componentes_Cliente(true,razon);
					 	}else{
					 		Habilitar_Componentes_Cliente(false,razon);
					 	}
				    },
			error : function(xhr, status) {
			        	alert('Disculpe, existió un problema al realizar la petición al servidor.');
			    },
		});
	}else{
		Habilitar_Componentes_Cliente(false,'');
	}
}

function Habilitar_Componentes_Cliente(enabled,razon){
	$('#razon').val(razon);
}

function AumentarCerosNumero(event){
	
	var numero= event.value;
	var tamanio = 7-(numero.length);
	var ceros = '0000000';
	var resultado='';
	
	
	if(parseInt(numero)<0){
		resultado=ceros;
		$('#numero').val(resultado);
		return;
	}
	
	if (tamanio == 7){
		resultado = numero;
	}
	else{
		resultado = String(ceros).substring(0,tamanio)+numero;
	}
	
	$('#numero').val(resultado);
	
}
function AumentarCerosSerie(event){
	
	var numero= event.value;
	var tamanio = 3-(numero.length);
	var ceros = '000';
	var resultado='';
	
	
	if(parseInt(numero)<0){
		resultado=ceros;
		$('#serie').val(resultado);
		return;
	}
	
	if (tamanio == 3){
		resultado = numero;
	}
	else{
		resultado = String(ceros).substring(0,tamanio)+numero;
	}
	
	$('#serie').val(resultado);
	
}

function FormatterDescargar(value,row){
	
	var html = [];
	
	if(row.enviado == 'S'){
		var btnXML = "<a href='/facturacion/documentos/457e5f18b6ae40ca920770214f?empresa="+row.empresa+"&nro="+row.id+"&tipoOperacion="+row.servicio+"' class='btn btn-sm btn-warning'>XML</a>&nbsp;";
		html.push(btnXML);
		
	}
	
	console.log(value);
	console.log(row);
	
	if(row.servicio == 'C' || row.servicio == 'N' || row.servicio == 'T' || row.servicio == 'E'){
		
		var btnPDFNormal = "<a href='/facturacion/documentos/ccdf8a28d6554fe3af3cc0aa84?empresa="+row.empresa+"&nro="+row.id+"&tipoOperacion="+row.servicio+ "' class='btn btn-sm btn-primary'>PDF A-4</a>&nbsp;";
		html.push(btnPDFNormal);
		
	}if (row.servicio == 'B' || row.servicio == 'E' || row.servicio == 'C'){
		
		var btnPDFTicket = "<a href='/facturacion/documentos/80ddc3cbfe7d45aa8ae95e87ed?empresa="+row.empresa+"&nro="+row.id+"&tipoOperacion="+row.servicio+"' class='btn btn-sm btn-danger'>PDF TICKET</a>&nbsp;";
		html.push(btnPDFTicket);
		
	}if (row.servicio == 'P'){
		
		var btnPDFTicket = "<a href='/facturacion/documentos/80ddc3cbfe7d45aa8ae95e87ed?empresa="+row.empresa+"&nro="+row.id+"&tipoOperacion="+row.servicio+"' class='btn btn-sm btn-danger'>PDF TICKET</a>&nbsp;";
		html.push(btnPDFTicket);
		var btnPDFNormal = "<a href='/facturacion/documentos/ccdf8a28d6554fe3af3cc0aa84?empresa="+row.empresa+"&nro="+row.id+"&tipoOperacion="+row.servicio+ "' class='btn btn-sm btn-primary'>PDF A-4</a>&nbsp;";
		html.push(btnPDFNormal);
		
	}
	
	return html.join('');
	
	
}
