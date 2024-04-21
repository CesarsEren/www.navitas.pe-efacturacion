$(function(){
	
	$('#btnCargar').prop('disabled', true);
	
	$('#dateRangePickerDocumentos').on('click',function(){
		$("#fechaDocumentos").datepicker('show');
		
	});
	$('#documentos').on('change',function(){
		$("#divVerifica").css("display", this.value == 2 ? "block" : "none");
		$("#divEnvio").css("display", this.value == 0 ? "block" : "none");
	});
	
	$("#fechaDocumentos").datepicker({
		language : 'es',
		autoclose: true,
		todayHighlight: true
		
	});
	
	$("input[name='enviartodos']").on("change", function(){
		if($(this).val() == "true"){
			$("#hidTodos").val(true);
		}
		else{
			$("#hidTodos").val(false);
			
		}
		
	});
	
	 $('#btnCargar').on('click',function(){
		 
		 
		 var data =  {bean : JSON.parse(JSON.stringify($('#tblDocumentos').bootstrapTable('getData')))};
		 
		 if($("#hidTodos").val()== 'true'){
			 $.each(data.bean,function(i,v){       
			        v.todos = true;
			 });
		 }
		 
		 $.ajax({
		        type: "POST",
		        contentType: "application/json;charset=utf-8",
		        url: "/facturacion/documentos/enviosunat",
		        data : JSON.stringify(data),
		        dataType: 'json',
		        beforeSend: function (response) {
		        	
		        	dialog =  new BootstrapDialog({
						title :'Mensaje de Alerta!',
						closable: false,
						animate : true,
			            message: $('<div id="idReclamo" class="row"><div class="col-xs-8">Por favor espere su solicitud se esta procesando...'+
			            					'</div><div class="col-xs-2"><img src="/facturacion/images/load.gif"/></div></div>'),
			            
			            buttons: [{
			                id: 'btn-reclamos-1',
			                label: 'OK',
			                cssClass: 'btn-success'
			            }],
			            onshow: function(dialog) {
			            	button =  dialog.getButton('btn-reclamos-1');
			            	button.disable();
			            },
		        	});
		        	dialog.open();
		        
		        	
				},
		        success: function (res) {
		        	if(res.rows[0].respuesta == false){
		        		
		        		dialog.setMessage('<div class="row">'+'<div class="col-xs-7">'+res.rows[0].mensaje+'</div>'+'<div class="col-xs-2">'+'</div>'+'</div>')
		        		dialog.setType(BootstrapDialog.TYPE_DANGER)
						button.enable();
						$(button).on('click',function(event){
							dialog.close();
							
						});
						return;
		        		
		        	}
		        	
		        	$('#btnCargar').prop('disabled', true);
		        	$("#tblDocumentos").bootstrapTable('removeAll');
		        	
		        	dialog.setMessage('<div class="row">'+'<div class="col-xs-7">'+res.rows[0].mensaje+'</div>'+'<div class="col-xs-2">'+'</div>'+'</div>')
	        		dialog.setType(BootstrapDialog.TYPE_SUCCESS)
					button.enable();
					$(button).on('click',function(event){
						dialog.close();
						
					});

		        },
		        error: function (e) {
		        	
		        	$('#btnCargar').prop('disabled', true);
		        	$("#tblDocumentos").bootstrapTable('removeAll');
		        	$("#btn-search").prop("disabled", false);
		        	dialog.setMessage('<div class="row">'+'<div class="col-xs-7">'+res.rows[0].mensaje+e+'</div>'+'<div class="col-xs-2">'+'</div>'+'</div>')
	        		dialog.setType(BootstrapDialog.TYPE_DANGER)
					button.enable();
					$(button).on('click',function(event){
						dialog.close();
						
					});
		        }
		    });
	});
	 
	 $('#btnVerificar').on('click',function(){
		 
		 $.ajax({
			 	type: "POST",
		        contentType: "application/json;charset=utf-8",
		        url : "/facturacion/documentos/estadodocumentossunat",
		        data : JSON.stringify({empresa : $("#empresa").val(),fechaemision : $("#fechaDocumentos").val(),documentos : $('#documentos').val()}),
		        dataType: 'json',
		        beforeSend: function (response) {
		        	
		        	dialog =  new BootstrapDialog({
						title :'Mensaje de Alerta!',
						closable: false,
						animate : true,
			            message: $('<div id="idVerifica" class="row"><div class="col-xs-8">Por favor espere su solicitud se esta procesando...'+
			            							'</div><div class="col-xs-2"><img src="/facturacion/images/load.gif"/></div></div>'),
			            
			            buttons: [{
			                id: 'btn-verifica',
			                label: 'OK',
			                cssClass: 'btn-success'
			            }],
			            onshow: function(dialog) {
			            	button =  dialog.getButton('btn-verifica');
			            	button.disable();
			            },
		        	});
		        	dialog.open();
		        	
				},
		        success: function (res) {
		        	
		        	$("#btnVerificar").prop("disabled", true);
		        	
		        	if(res.rows[0].respuesta == false){
		        		
		        		dialog.setMessage('<div class="row">'+'<div class="col-xs-7">'+res.rows[0].mensaje+'</div>'+'<div class="col-xs-2">'+'</div>'+'</div>')
		        		dialog.setType(BootstrapDialog.TYPE_DANGER)
						button.enable();
						$(button).on('click',function(event){
							dialog.close();
							
						});
						return;
		        		
		        	}
		        	
		        	$('#btnCargar').prop('disabled', true);
		        	$("#tblDocumentos").bootstrapTable('removeAll');
		        	
		        	dialog.setMessage('<div class="row">'+'<div class="col-xs-7">'+res.rows[0].mensaje+'</div>'+'<div class="col-xs-2">'+'</div>'+'</div>')
	        		dialog.setType(BootstrapDialog.TYPE_SUCCESS)
					button.enable();
					$(button).on('click',function(event){
						dialog.close();
						
					});

		        },
		        error: function (e) {
		        	
		        	$('#btnCargar').prop('disabled', true);
		        	$("#tblDocumentos").bootstrapTable('removeAll');

		        	$("#btn-search").prop("disabled", false);
		            dialog.close();
		        }
		    });
		 
	 });
	 
	 $('#btnConsultaDocumentos').on('click',function(){
		 
		 $('#btnCargar').prop('disabled', false);
		 
		 dialog =  new BootstrapDialog({
						title :'Mensaje de Alerta!',
						closable: false,
						animate : true,
						id : "dialogenvio",
			            message: $('<div id="idReclamo" class="row"><div class="col-xs-8">Por favor espere su solicitud se esta procesando...'+
			            							'</div><div class="col-xs-2"><img src="/facturacion/images/load.gif"/></div></div>'),
			            
			            buttons: [{
			                id: 'btn-reclamos-1',
			                label: 'OK',
			                cssClass: 'btn-success'
			            }],
			            onshow: function(dialog) {
			            	button =  dialog.getButton('btn-reclamos-1');
			            	button.disable();
			            },
		  	});
		  	dialog.open();
		 
		 $('#tblDocumentos').bootstrapTable('removeAll');
			$("#tblDocumentos").bootstrapTable('destroy').bootstrapTable(
				{pagination: true,
				 height: 450,
				 locale: "es-ES",
				 url :  '/facturacion/documentos/cosultadocumentos',
				 queryParams: function(params){
						params['empresa'] = $("#empresa").val();
						params['fechaemision'] = $("#fechaDocumentos").val();
						params['documentos'] = $('#documentos').val();
						return params;
						
				 },responseHandler: function(res){
					 
					 if(!res.respuesta){
			        		dialog.setMessage('<div class="row">'+'<div class="col-xs-7">'+res.mensaje+'</div>'+'<div class="col-xs-2">'+'</div>'+'</div>')
			        		dialog.setType(BootstrapDialog.TYPE_DANGER)
							button.enable();
							$(button).on('click',function(event){
								dialog.close();
								
							});
							return true;
			        		
			        }
					 $("#btnVerificar").prop("disabled", res.rows.length > 0 && res.rows[0].documentos == 2 ? false : true);
					 
					 dialog.setMessage('<div class="row">'+'<div class="col-xs-7">'+res.mensaje+'</div>'+'<div class="col-xs-2">'+'</div>'+'</div>')
			        	dialog.setType(BootstrapDialog.TYPE_SUCCESS)
							button.enable();
							$(button).on('click',function(event){
								dialog.close();
								
						});
					 return res;
					 
				 },onCheck: function (row,element) {
				    	UpdateRow(element[0].dataset['index'],"S");
				    },
				    onUncheck: function(row,element){
				    	UpdateRow(element[0].dataset['index'],"N");
				    },
				    onCheckAll: function(row,element){
				    	$.each(row, function (id, value) {
				    		UpdateRow(id,"S");
				    	});
				    },
				    onUncheckAll: function(row,element){
				    	$.each(row, function (id, value) {
				    		UpdateRow(id,"N");
				    	});
				    },onLoadError: function (e,r){
				    	
				    		if(r.status != 200){
				    			
				    			dialog.setMessage('<div class="row">'+'<div class="col-xs-7">'+'Error interno en el servidor. Estado '+r.status+'</div>'+'<div class="col-xs-2">'+'</div>'+'</div>')
				        		dialog.setType(BootstrapDialog.TYPE_DANGER)
								button.enable();
								$(button).on('click',function(event){
									dialog.close();
									
								});
								return true;
				    		}
				    }
			});
		 
	 });
	 
});

function queryParams(params){
	params['empresa'] = $("#empresa").val();
	params['fechaemision'] = $("#fechaDocumentos").val();
	params['documentos'] = $('#documentos').val();
	return params;
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
function UpdateRow(indice,value){
	
	$('#tblDocumentos').bootstrapTable('updateRow', {
        index: indice,
        row: {enviar: value}
    });
}
