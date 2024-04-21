$(function () {

    $('#btnCargar').prop('disabled', true);

    $('#dateRangePickerDocumentos').on('click', function () {
        $("#fechaDocumentos").datepicker('show');

    });
    $('#documentos').on('change', function () {
        $("#divVerifica").css("display", this.value == 2 ? "block" : "none");
        $("#divEnvio").css("display", this.value == 0 ? "block" : "none");
        if (this.value == 1) {
            $("#divVerifica").css("display", "none");
            $("#divEnvio").css("display", "none");
        }
    });

    $("#fechaDocumentos").datepicker({
        language: 'es',
        autoclose: true,
        todayHighlight: true

    });

    $("input[name='enviartodos']").on("change", function () {
        if ($(this).val() == "true") {
            $("#hidTodos").val(true);
        } else {
            $("#hidTodos").val(false);
        }

    });

    $('#tblDocumentos').on('page-change.bs.table', function (e, number, size) {
        var data = {bean: JSON.parse(JSON.stringify($('#tblDocumentos').bootstrapTable('getData')))};

        $.each(data.bean, function (i, v) {

            UpdateRow(i, "N");
        });

    });


    $('#btnCargar').on('click', function () {


        var data = {bean: JSON.parse(JSON.stringify($('#tblDocumentos').bootstrapTable('getData')))};

        if ($("#hidTodos").val() == 'true') {

            $.each(data.bean, function (i, v) {
                v.todos = true;
            });

            var employees = {
                "bean": []
            };

            $.each(data.bean, function (i, v) {

                if (i == 0) {

                    employees.bean.push({
                        "id": v.id,
                        "fechaEmision": v.fechaEmision,
                        "empresa": v.empresa,
                        "documentoElectronico": v.documentoElectronico,
                        "agencia": v.agencia,
                        "servicio": v.servicio,
                        "estado": v.estado,
                        "observaciones": v.observaciones,
                        "enviar": v.enviar,
                        "todos": true,
                        "documentos": v.documentos
                    })
                }

            });

        } else {

            var employees = {
                "bean": []
            };

            $.each(data.bean, function (i, v) {

                if (v.enviar == 'S') {

                    employees.bean.push({
                        "id": v.id,
                        "fechaEmision": v.fechaEmision,
                        "empresa": v.empresa,
                        "documentoElectronico": v.documentoElectronico,
                        "agencia": v.agencia,
                        "servicio": v.servicio,
                        "estado": v.estado,
                        "observaciones": v.observaciones,
                        "enviar": v.enviar,
                        "todos": false,
                        "documentos": v.documentos
                    })
                }

            });

        }


        $.ajax({
            type: "GET",
            contentType: "application/json;charset=utf-8",
            //url: "enviosunat?data="+JSON.stringify(employees),
            //url : "/envialo/documentosenvio/enviosunat",
            url: "enviosunat",
            data: {dato: JSON.stringify(employees)},
            dataType: 'json',
            beforeSend: function (response) {

                dialog = new BootstrapDialog({
                    title: 'Mensaje de Alerta!',
                    closable: false,
                    animate: true,
                    message: $('<div id="idReclamo" class="row"><div class="col-xs-8">Por favor espere su solicitud se esta procesando...' +
                        '</div><div class="col-xs-2"><img src="_lib/img/load.gif"/></div></div>'),

                    buttons: [{
                        id: 'btn-reclamos-1',
                        label: 'OK',
                        cssClass: 'btn-success'
                    }],
                    onshow: function (dialog) {
                        button = dialog.getButton('btn-reclamos-1');
                        button.disable();
                    },
                });
                dialog.open();

            },
            success: function (res) {

                if (res.mapaJSONResultado.rows[0].respuesta == false) {

                    dialog.setMessage('<div class="row">' + '<div class="col-xs-7">' + res.mapaJSONResultado.rows[0].mensaje + '</div>' + '<div class="col-xs-2">' + '</div>' + '</div>')
                    dialog.setType(BootstrapDialog.TYPE_DANGER)
                    button.enable();
                    $(button).on('click', function (event) {
                        dialog.close();

                    });
                    return;

                }

                $('#btnCargar').prop('disabled', true);
                $("#tblDocumentos").bootstrapTable('removeAll');

                dialog.setMessage('<div class="row">' + '<div class="col-xs-7">' + res.mapaJSONResultado.rows[0].mensaje + '</div>' + '<div class="col-xs-2">' + '</div>' + '</div>')
                dialog.setType(BootstrapDialog.TYPE_SUCCESS)
                button.enable();
                $(button).on('click', function (event) {
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

    $('#btnVerificar').on('click', function () {


        $.ajax({
            type: "GET",
            contentType: "application/json;charset=utf-8",
            url: "/encargaefact/consultaenvio/estadodocumentossunat",
            data: {
                empresa: $("#empresa").val(),
                fechaemision: $("#fechaDocumentos").val(),
                documentos: $('#documentos').val()
            },
            dataType: 'json',
            beforeSend: function (response) {

                dialog = new BootstrapDialog({
                    title: 'Mensaje de Alerta!',
                    closable: false,
                    animate: true,
                    message: $('<div id="idVerifica" class="row"><div class="col-xs-8">Por favor espere su solicitud se esta procesando...' +
                        '</div><div class="col-xs-2"><img src="_lib/img/load.gif"/></div></div>'),

                    buttons: [{
                        id: 'btn-verifica',
                        label: 'OK',
                        cssClass: 'btn-success'
                    }],
                    onshow: function (dialog) {
                        button = dialog.getButton('btn-verifica');
                        button.disable();
                    },
                });
                dialog.open();

            },
            success: function (res) {

                if (res.mapaJSONResultado.rows[0].respuesta == false) {

                    dialog.setMessage('<div class="row">' + '<div class="col-xs-7">' + res.mapaJSONResultado.rows[0].mensaje + '</div>' + '<div class="col-xs-2">' + '</div>' + '</div>')
                    dialog.setType(BootstrapDialog.TYPE_DANGER)
                    button.enable();
                    $(button).on('click', function (event) {
                        dialog.close();

                    });
                    return;

                }

                $('#btnCargar').prop('disabled', true);
                $("#tblDocumentos").bootstrapTable('removeAll');

                dialog.setMessage('<div class="row">' + '<div class="col-xs-7">' + res.mapaJSONResultado.rows[0].mensaje + '</div>' + '<div class="col-xs-2">' + '</div>' + '</div>')
                dialog.setType(BootstrapDialog.TYPE_SUCCESS)
                button.enable();
                $(button).on('click', function (event) {
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

    $('#btnConsultaDocumentos').on('click', function () {

        $('#btnCargar').prop('disabled', false);
        $('#tblDocumentos').bootstrapTable('removeAll');

        dialog = new BootstrapDialog({
            title: 'Mensaje de Alerta!',
            closable: false,
            animate: true,
            id: "dialogenvio",
            message: $('<div id="idReclamo" class="row"><div class="col-xs-8">Por favor espere su solicitud se esta procesando...' +
                '</div><div class="col-xs-2"><img src="_lib/img/load.gif"/></div></div>'),

            buttons: [{
                id: 'btn-reclamos-1',
                label: 'OK',
                cssClass: 'btn-success'
            }],
            onshow: function (dialog) {
                button = dialog.getButton('btn-reclamos-1');
                button.disable();
            },
        });
        dialog.open();

        $("#tblDocumentos").bootstrapTable('destroy').bootstrapTable(
            {
                pagination: true,
                pageSize: 10,
                height: 380,//400,
                showColumns: true,
                locale: "es-ES",
                url: "consultadocumentos",
                queryParams: function (params) {
                    params['empresa'] = $("#empresa").val();
                    params['fechaemision'] = $("#fechaDocumentos").val();
                    params['documentos'] = $('#documentos').val();
                    return params;

                },
                responseHandler: function (res) {
                    //console.log(res.documentos);
//					console.log(res.total);

                    $("#btnVerificar").prop("disabled", res.documentos == 2 && res.mapaDocumentos.total > 0 ? false : true);
                    //$("#btnVerificar").prop("disabled", res.mapaDocumentos.total > 0?false:true);
                    //$('#btnVerificar').prop('disabled', true);
                    if (!res.mapaDocumentos.respuesta) {

                        dialog.setMessage('<div class="row">' + '<div class="col-xs-7">' + res.mapaDocumentos.mensaje + '</div>' + '<div class="col-xs-2">' + '</div>' + '</div>')
                        dialog.setType(BootstrapDialog.TYPE_DANGER)
                        button.enable();
                        $(button).on('click', function (event) {
                            dialog.close();

                        });
                        return true;

                    }

                    dialog.setMessage('<div class="row">' + '<div class="col-xs-7">' + res.mapaDocumentos.mensaje + '</div>' + '<div class="col-xs-2">' + '</div>' + '</div>')
                    dialog.setType(BootstrapDialog.TYPE_SUCCESS)
                    button.enable();
                    $(button).on('click', function (event) {
                        dialog.close();

                    });
                    return res.mapaDocumentos;

                }, onCheck: function (row, element) {
                    console.log(row);
                    UpdateRow(element[0].dataset['index'], "S");
                },
                onUncheck: function (row, element) {
                    console.log(row);
                    UpdateRow(element[0].dataset['index'], "N");
                },
                onCheckAll: function (row, element) {
                    console.log(row);
                    $.each(row, function (id, value) {
                        UpdateRow(id, "S");
                    });
                },
                onUncheckAll: function (row, element) {
                    console.log(row);
                    $.each(row, function (id, value) {
                        UpdateRow(id, "N");
                    });
                }
            })

    });

});

function UpdateRow(indice, value) {

    $('#tblDocumentos').bootstrapTable('updateRow', {
        index: indice,
        row: {enviar: value}
    });

}

function queryParams(params) {
    params['empresa'] = $("#empresa").val();
    params['fechaemision'] = $("#fechaDocumentos").val();
    params['documentos'] = $('#documentos').val();
    return params;
}

function FormatterEstado(valor) {
    var html = [];
    if (valor == "S") {
        html.push("<span class='glyphicon glyphicon-ok' style='color:#5cb85c;'></span>");
    } else {
        html.push("<span class='glyphicon glyphicon-remove' style='color:#FF0000;'></span>");
    }
    html.push("</label>");

    return html.join('');
}