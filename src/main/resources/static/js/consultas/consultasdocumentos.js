$(function(){
	
	$('#dateRangePickerDesde').on('click',function(){
		$("#fechaDesde").datepicker('show');
		
	});
	
	$('#dateRangePickerHasta').on('click',function(){
		$("#fechaHasta").datepicker('show');
		
	});
	
	$("#fechaDesde").datepicker({
		language : 'es',
		autoclose: true,
		todayHighlight: true
		
	});
	$("#fechaHasta").datepicker({
		language : 'es',
		autoclose: true,
		todayHighlight: true
		
	});
	
});

	
	function validarSoloNumerosEnteros(e) {
	    tecla = (document.all) ? e.keyCode : e.which;
	    if (tecla==8) return true; 
	    patron = /[0123456789]/;
	    te = String.fromCharCode(tecla);
	    return patron.test(te); 
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
			resultado = numero.substring(0,1)+ String(ceros).substring(0,tamanio)+numero.substring(1,3);
		}
		
		$('#serie').val(resultado);
		
}
$('#btnCerrarModal').on('click',function(){
	$('#tblDocumentosDetalle').bootstrapTable('removeAll');
		
});

$('#btnConsultaDocumentos').on('click',function(){
	
	
	$('#btnExcel').prop('disabled', true);
	$('#divMensaje').css({"display":"none"});
	$("#mensaje").empty();
	
	$('#tblDocumentos').bootstrapTable('removeAll');
	$("#tblDocumentos").bootstrapTable('refresh',
		{silent: false,
		 height: 350,
		 pagination:true,
		 url :  '/envialoconsultas/consultasrastreo/documentos', 
		 query : {
			        
			 	    nombres: $('#nombres').val(),
			 	    fechaDesde: $('#fechaDesde').val(),
			 	   	fechaHasta: $('#fechaHasta').val(),
			 	  	tipo: $('#tipo').val(),
			 	 	dt: $('#dt').val(),
			 	 	tipoDocumento: $('#tipoDocumento').val(),
			 	 	serieRemitente: $('#serieRemitente').val(),
			 	 	numeroRemitente: $('#numeroRemitente').val(),
			 	 	seriePalomino: $('#seriePalomino').val(),
			 	 	numeroPalomino: $('#numeroPalomino').val(),
			 	 	ruc: $('#ruc').val()
			 		
				}
		  }
	);
	
	
});

function queryParams(params){
	params['nombres'] = $('#nombres').val();
	params['fechaDesde'] = $('#fechaDesde').val();
	params['fechaHasta'] = $('#fechaHasta').val();
	params['tipo'] = $('#tipo').val();
	params['dt'] = $('#dt').val();
	params['tipoDocumento'] = $('#tipoDocumento').val();
	params['serieRemitente'] = $('#serieRemitente').val();
	params['numeroRemitente'] = $('#numeroRemitente').val();
	params['seriePalomino'] = $('#seriePalomino').val();
	params['numeroPalomino'] = $('#numeroPalomino').val();
	params['ruc'] = $('#ruc').val();
	return params;
}

function responseHandler(res){
		
		$("#divExcel").empty();
		var btnExcel;
		
		if(!res.respuestaServer){
			$('#divMensaje').css({"display":"block"});
			$("#mensaje").append($("<strong>"+res.mensajeServer+"</strong>"));
			btnExcel = "<input id='btnExcel' type='button' class='btn btn-success' value='IMPRIMIR EXCEL' disabled='disabled'>"
			$("#divExcel").append(btnExcel);
			return false;
		}
		
		btnExcel = "<a download='"+res.nameexcel+"'  href='data:application/vnd.ms-excel;base64,"+res.excel+"' class='btn btn-success'>IMPRIMIR EXCEL</a>&nbsp;";
		$("#divExcel").append(btnExcel);
		
		
	return res;
}



window.operateEvents = {
		  "click .detalles": function(event, value, row, index) {
			  				Detalles(row);
			
		  }
		
};

function Formatter(value,row){
	
	var html = [];
	
	var btnMas = "<a href='javascript:void(0);' class='btn btn-sm btn-warning detalles'><span class='glyphicon glyphicon-search'/></a>&nbsp;";
	//var btnExcel = "<a download='"+row.nameexcel+"'  href='data:application/vnd.ms-excel;base64,"+row.excel+"' class='btn btn-sm btn-success imageExcel'> <span class='fas fa-file-excel'/></a>&nbsp;";
	
	html.push(btnMas);
	//html.push(btnExcel);
	return html.join('');
}

function FormatterDescarga(value,row){
	
	var html = [];
	
	if(row.image != ''){
		var btnImage = "<a href='javascript:void(0);' class='btn btn-sm btn-warning imageData'><span class='glyphicon glyphicon-camera'/> Fotos</a>";
		html.push(btnImage);
	}
	return html.join('');
}

function responseHandlerDescarga(res){
	return res;
}

function Detalles(data){
	
	$.ajax({
		url :  '/envialoconsultas/consultasrastreo/modal',
		type : 'GET',
		success   : function(response){
			
					$('#modalPagina').html(response);
					
					$.ajax({
						url : "/envialoconsultas/consultasrastreo/detalle",
						type : 'GET',
						data : {nro:data.id},
						success   : function(response){
									
								$('#tblDocumentosDetalle').bootstrapTable("load", response);
								 CargaDataDetalle(response.rows[0]);
								 $('#modalDocumento').modal('show');
							}
					});
					
			}
	});
	
}

function CargaDataDetalle(data){
	console.log(data);
	$("#fechaEmision").val(data.fechaEmision);
	$("#empresa").val(data.empresa);
	$("#tipoDocumento").val(data.documentoD);
	$("#serieDetalle").val(data.serie);
	$("#numeroDetalle").val(data.numero);
	$("#agenciaOrigen").val(data.agencia);
	$("#usuario").val(data.usuario);
	$("#ruta").val(data.destino);
	$("#ruc").val(data.ruc);
	$("#razon").val(data.razon);
	$("#contenido").val(data.descripcion);
	$("#dni").val(data.dni);
	$("#remitente").val(data.remitente);
	$("#remitenteDireccion").val(data.remitenteD);
	$("#consignadoDocumento").val(data.consignado);
	(data.domiciliado == 'S' ? $("#domiciliado").prop("checked",true):$("#domiciliado").prop("checked",false))
	$("#agenciaDestino").val(data.agenciaD1);
	$("#consignadoDireccion").val(data.consignadoD);
	$("#base").val(data.base);
	$("#total").val(data.total);
	$("#flete").val(data.flete);
	$("#giro").val(data.montoGiro);
	$("#domicilio").val(data.domicilio);
	$("#pago").val(data.pago);
	
}


window.operateEventsDescarga = {
		  "click .imageData": function(event, value, row, index) {
			  
			  				ImagesDescarga(row.image);
		  }
		
};


function ImagesDescarga(data){
	
	imagen = "<div class='row'>"+data+"</div>";
	
	var dialog = new BootstrapDialog({
        	message: function(dialogRef){
            var $message = $(imagen); 
            return $message;
        },
        closable: false,
        buttons: [{
	  		label: 'Cerrar',
	  		cssClass: 'btn btn-danger',
	  		action: function(event){
	  				event.close();
         }
     }]
    });
    dialog.realize();
    dialog.getModalHeader().hide();
    dialog.open();
}



