function Listarusuarios(){
	
	$('#tblUsuarios').bootstrapTable('removeAll');
	$("#tblUsuarios").bootstrapTable('refresh',
		{silent: false,
		 height: 350,
		 pagination:true,
		 url :  '/encargaefact/usuarios/listar'
		
		  }
	);
	
}
function RucExistente(event){
	
	var ruc=event.value.trim();
	var razon='';
	
	if(ruc!=''){
		
			$.ajax({
					url : '/encargaefact/consultasrastreo/cliente',
					contentType: "application/json",
				 	data : {ruc : ruc},
				 	type : 'GET',
				 	dataType : 'json',
			success : function(json) {
						if(json.respuesta){
					 		razon =json.bean['razon'];
							Habilitar_Componentes_Cliente(false,razon);
					 	}else{
					 		Habilitar_Componentes_Cliente(true,razon);
					 	}
				    },
			error : function(xhr, status) {
			        	alert('Disculpe, existió un problema al realizar la petición al servidor.');
			    },
		});
	}else{
		Habilitar_Componentes_Cliente(true,'');
	}
}
function Habilitar_Componentes_Cliente(enabled,razon){
	$('#razonSocial').val(razon);
	$('#btnRegistrarUsuarios').prop('disabled', enabled);
}

$('#btnRegistrarUsuarios').on('click',function(){
	$('#divMensaje').css({"display":"none"});
	$("#mensaje").empty();
	
	var data = {"nombres":$('#nombres').val(),
				"username":$('#username').val(),
				"nivel":$('#nivel').val(),
				"ruc":$('#ruc').val(),
				"razonSocial":$('#razonSocial').val(),
				"password":$('#password').val()}
	
	$.ajax({
		url: "/encargaefact/usuarios/verificaUsuario",
		contentType: "application/json",
	 	data : {ruc : $('#ruc').val()},
	 	type : 'GET',
	 	dataType : 'json',
		success : function(json) {
					
					if(json.respuesta == 1){
						
						 BootstrapDialog.show({
							 title: 'Mensaje de Advertencia',
					         message: json.mensaje,
					         type :BootstrapDialog.TYPE_DANGER,
					         buttons: [{
					             label: 'OK',
					             cssClass: 'btn-success',
					             action: function(dialog) {
					            	 dialog.close();
					            	 $.ajax({
		        	                        type: "POST",
		        	                        contentType: "application/json;charset=utf-8",
		        	                        url: "/encargaefact/usuarios/registrar",
		        	                        data: JSON.stringify(data),
		        	                        dataType: 'json',
		        	                        success: function (res) {
		        	                        	
		        	                        	if(res.respuesta == -1){
		        	                    			$('#divMensaje').css({"display":"block"});
		        	                    			$('#mensaje').addClass("alert alert-danger");
		        	                    			$("#mensaje").append($("<strong>"+res.mensaje+"</strong>"));
		        	                    			
		        	                    			return false;
		        	                    		}
		        	                        	
		        	                        	if(res.respuesta == 1){
		        	                    			$('#divMensaje').css({"display":"block"});
		        	                    			$("#mensaje").append($("<strong>"+res.mensaje+"</strong>"));
		        	                    			return false;
		        	                    		}else{
		        	                    			$('#divMensaje').css({"display":"block"});
		        	                    			$('#mensaje').addClass("alert alert-success");
		        	                    			$("#mensaje").append($("<strong>"+res.mensaje+"</strong>"));
		        	                    			$('#btnRegistrarUsuarios').prop('disabled', true);
		        	                    			LimpiarDatos();
		        	                    			Listarusuarios();
		        	                    			
		        	                    		}
		        	                        	
		        	                        },
		        	                        error: function (e) {
		        	                        	
		        	                        }
		        	                    });
					             }
					         }, {
		        	                label: 'Cancelar',
		        	                action: function(dialog) {
		        	                	
		        	                	dialog.close();
		        	                   
		        	                }
		        	            }]
					     });
						
					}else if(json.respuesta == -1){
	        			$('#divMensaje').css({"display":"block"});
	        			$('#mensaje').addClass("alert alert-danger");
	        			$("#mensaje").append($("<strong>"+res.mensaje+"</strong>"));
	        			return false;
	        		
					}else{
						
						$.ajax({
	                        type: "POST",
	                        contentType: "application/json;charset=utf-8",
	                        url: "/encargaefact/usuarios/registrar",
	                        data: JSON.stringify(data),
	                        dataType: 'json',
	                        success: function (response) {
	                        	
	                        	if(response.respuesta == -1){
	                    			$('#divMensaje').css({"display":"block"});
	                    			$('#mensaje').addClass("alert alert-danger");
	                    			$("#mensaje").append($("<strong>"+response.mensaje+"</strong>"));
	                    			
	                    			return false;
	                    		}
	                        	
	                        	if(response.respuesta == 1){
	                    			$('#divMensaje').css({"display":"block"});
	                    			$("#mensaje").append($("<strong>"+response.mensaje+"</strong>"));
	                    			return false;
	                    		}else{
	                    			$('#divMensaje').css({"display":"block"});
	                    			$('#mensaje').addClass("alert alert-success");
	                    			$("#mensaje").append($("<strong>"+response.mensaje+"</strong>"));
	                    			$('#btnRegistrarUsuarios').prop('disabled', true);
	                    			LimpiarDatos();
	                    			Listarusuarios();
	                    			
	                    		}
	                        	
	                        },
	                        error: function (e) {
	                        	
	                        }
	                    });
					}
				}
		});
	
});

function LimpiarDatos(){
	$('#nombres').val('');
	$('#ruc').val('');
	$('#razonSocial').val('');
	$('#password').val('');
}

$('#btnCerrarUpdate').on('click',function(){
	Listarusuarios();
});

$('#btnActualizarUsuarios').on('click',function(){
	
	$("#mensaje").empty();
	
	var data = {"nombres":$('#nombresupdate').val(),
				"estado":$('#estadoupdate').val(),
				"password":$('#passwordupdate').val(),
				"id":$('#id').val()}
	
	
	$.ajax({
        type: "POST",
        contentType: "application/json;charset=utf-8",
        url: "/encargaefact/usuarios/actualizar",
        data: JSON.stringify(data),
        dataType: 'json',
        success: function (res) {
        	
        	if(res.respuesta == -1){
    			$('#divMensajeUpdate').css({"display":"block"});
    			$('#mensajeupdate').addClass("alert alert-danger");
    			$("#mensajeupdate").append($("<strong>"+res.mensaje+"</strong>"));
    			
    		}else{
    			$('#divMensajeUpdate').css({"display":"block"});
    			$("#mensajeupdate").append($("<strong>"+res.mensaje+"</strong>"));
    		}
        }
    });
	
});

function queryParams(params){
	return params;
}

function responseHandler(res){
	
		if(!res.respuesta){
			$('#divMensaje').css({"display":"block"});
			$('#mensaje').addClass("alert alert-danger");
			$("#mensaje").append($("<strong>"+res.mensaje+"</strong>"));
			
			return false;
		}
		
	return res;
}


function validarSoloNumerosEnteros(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla==8) return true; 
    patron = /[0123456789]/;
    te = String.fromCharCode(tecla);
    return patron.test(te); 
}

window.operateEvents = {
		  "click .update": function(event, value, row, index) {
			  				UpdateUsuario(row);
			
		  }
		
};


function FormatterUsuario(value,row){
	
	var html = [];
	
	var btnMas = "<a href='javascript:void(0);' class='btn btn-sm btn-warning update'><span class='glyphicon glyphicon-pencil'/></a>&nbsp;";
	html.push(btnMas);
		
	return html.join('');
}
function estadoUsuario(valor){
	var html = [];
	
	if(valor == "Y"){
		html.push("<label class='label label-primary'>");
		html.push ("ACTIVO");
	}else{
		html.push("<label class='label label-danger'>");
		html.push ("INACTIVO");
	}
	html.push("</label>");
	
	return html.join('');
}

function UpdateUsuario(data){
	$('#nombresupdate').val(data.nombres);
	$('#id').val(data.id);
	$('#modalUsuario').modal('show');
	
	
}

