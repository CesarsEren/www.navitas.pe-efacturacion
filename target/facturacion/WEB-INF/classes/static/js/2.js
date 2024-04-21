$(function(){
	
	$('#dateRangePickerDocumentos').on('click',function(){
		$("#fechaDocumentos").datepicker('show');
		
	});
	
	$('[data-toggle="tooltip"]').tooltip();
	
	$("#fechaDocumentos").datepicker({
		language : 'es',
		autoclose: true,
		todayHighlight: true
		
	});
	
	$('#tipodocumento').on('change', function() {
		if(this.value=='01' || this.value=='03' || this.value=='05' || this.value=='06'){
			$('#divFactura').css({"display":"block"});
			$('#serie').attr("placeholder", "F000");
		}else{
			$('#divFactura').css({"display":"none"});	
			$('#serie').attr("placeholder", "B000");
			$('#ruc').val("");
			$('#razon').val("");
		}
	})
});

$('#btnConsultaDocumentos').on('click',function(){
	$('#divMensaje').css({"display":"none"});
	$("#mensaje").empty();
	
	$('#tblDocumentosElectronicosClientes').bootstrapTable('removeAll');
	$("#tblDocumentosElectronicosClientes").bootstrapTable('refresh',
		{silent: false,
		 height: 350,
		 url :  '/facturacion/descarga/documento/electronico/Y29zdWx0YWRvY3VtZW50b3NkZXNjYXJnYQ==', 
		 query : {
				 	em: $('#empresa').val(),
					fechaemision: $('#fechaDocumentos').val(),
					serie: $('#serie').val(),
					numero: $('#numero').val(),
					tipodocumento:  $('#tipodocumento').val(),
					ruc : $('#ruc').val(),
					monto : $('#monto').val()
			 	  }
		}
	);
});

function queryParams(params){
	params['em'] = $('#empresa').val();
	params['fechaemision'] = $('#fechaDocumentos').val();
	params['serie'] = $('#serie').val();
	params['numero'] = $('#numero').val();
	params['tipodocumento'] = $('#tipodocumento').val();
	params['ruc'] = $('#ruc').val();
	params['monto'] = $('#monto').val();
	return params;
}

function responseHandler(res){
		
		if(!res.respuestaServer){
			$('#divMensaje').css({"display":"block"});
			$("#mensaje").append($("<strong>"+res.mensajeServer+"</strong>"));
			return false;
		}
	return res;
}

function RucExistente(event){
	
	var ruc=event.value.trim();
	var razon='';
	
	if(ruc!=''){
		
			$.ajax({
					url : '/facturacion/consultas/cliente',
					data : JSON.stringify({ruc : ruc}),
					contentType: "application/json",
				 	type : 'POST',
				 	dataType : 'json',
			success : function(json) {
				 		if(json.respuestaAjaxCliente){
					 		razon =json.beancliente['razon'];
							Habilitar_Componentes_Cliente(true,razon);
					 	}else{
					 		Habilitar_Componentes_Cliente(false,razon);
					 	}
				    },
			error : function(xhr, status) {
			        	alert('Disculpe, existio un problema al realizar la peticion al servidor.');
			    },
		});
	}else{
		Habilitar_Componentes_Cliente(false,'');
	}
}

function Habilitar_Componentes_Cliente(enabled,razon){
	$('#razon').val(razon);
}
function validarSoloNumerosEnteros(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla==8) return true; 
    patron = /[0123456789]/;
    te = String.fromCharCode(tecla);
    return patron.test(te); 
}
function validarNumerosReales(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla==8) return true;
    patron = /[0123456789.]/; 
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
	var tamanio = 4-(numero.length);
	var ceros = '000';
	var resultado='';
	
	if(parseInt(numero)<0){
		resultado=ceros;
		$('#serie').val(resultado);
		return;
	}
	
	if (tamanio == 4){
		resultado = numero;
	}
	else{
		resultado = numero.substring(0,1)+ String(ceros).substring(0,tamanio)+numero.substring(1,4);
	}
	
	$('#serie').val(resultado);
	
}

function FormatterDescargar(value,row){
	
	var html = [];
	var Base64={_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=Base64._utf8_encode(e);while(f<e.length){n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)+this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},decode:function(e){var t="";var n,r,i;var s,o,u,a;var f=0;e=e.replace(/[^A-Za-z0-9+/=]/g,"");while(f<e.length){s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));u=this._keyStr.indexOf(e.charAt(f++));a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=Base64._utf8_decode(t);return t},_utf8_encode:function(e){e=e.replace(/rn/g,"n");var t="";for(var n=0;n<e.length;n++){var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048){t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else{t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);t+=String.fromCharCode(r&63|128)}}return t},_utf8_decode:function(e){var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else{c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}return t}}
		
		if(row.enviado == 'S'){
			var btnXML = "<a href='"+row.data2+"?em="+row.em+"&id="+row.id+"&ope="+row.servicio+"' class='btn btn-sm btn-warning'>XML</a>&nbsp;";
			html.push(btnXML);
		}
		
		if(Base64.decode(row.servicio) == 'C' || Base64.decode(row.servicio) == 'N' || Base64.decode(row.servicio) == 'T'){
			
			var btnPDFNormal = "<a href='"+row.data3+"?em="+row.em+"&id="+row.id+"&ope="+row.servicio+ "' class='btn btn-sm btn-primary'>PDF A-4</a>&nbsp;";
			html.push(btnPDFNormal);
			
		}else if (Base64.decode(row.servicio) == 'B' || Base64.decode(row.servicio) == 'E'){
			
			var btnPDFTicket = "<a href='"+row.data1+"?em="+row.em+"&id="+row.id+"&ope="+row.servicio+"' class='btn btn-sm btn-danger'>PDF TICKET</a>&nbsp;";
			html.push(btnPDFTicket);	
		}
		return html.join('');
}
