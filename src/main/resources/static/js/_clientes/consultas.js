
function queryParams(e) {
    return e.em = $("#empresa").val(), e.fechaemision = $("#fechaDocumentos").val(), e.serie = $("#serie").val(), e.numero = $("#numero").val(), e.tipodocumento = $("#tipodocumento").val(), e.ruc = $("#ruc").val(), e.monto = $("#monto").val(), e
}

function responseHandlerDocumentos(e) {
    return e.respuestaServer ? e.rows : ($("#divMensaje").css({
        display: "block"
    }), $("#mensaje").append($("<strong>" + e.mensajeServer + "</strong>")), !1)
}

function RucExistente(e) {
    var r = e.value.trim(),
        t = "";
    "" != r ? $.ajax({
        url: "ClientesRUC",
        data: {
            RUC: r
        },
        type: "GET",
        dataType: "json",
        success: function(e) {
            e.respuestaAjaxCliente ? Habilitar_Componentes_Cliente(!0, t = e.beancliente.razon) : Habilitar_Componentes_Cliente(!1, t)
        },
        error: function(e, r) {
            alert("Disculpe, existió un problema al realizar la petición al servidor.")
        }
    }) : Habilitar_Componentes_Cliente(!1, "")
}

function Habilitar_Componentes_Cliente(e, r) {
    $("#razon").val(r)
}

function validarSoloNumerosEnteros(e) {
    return tecla = document.all ? e.keyCode : e.which, 8 == tecla || (patron = /[0123456789]/, te = String.fromCharCode(tecla), patron.test(te))
}

function AumentarCerosNumero(e) {
    var r = e.value,
        t = 7 - r.length,
        o = "0000000",
        n = "";
    if (parseInt(r) < 0) return n = o, void $("#numero").val(n);
    n = 7 == t ? r : String(o).substring(0, t) + r, $("#numero").val(n)
}

function AumentarCerosSerie(e) {
    var r = e.value,
        t = 4 - r.length,
        o = "";
    if (parseInt(r) < 0) return o = "000", void $("#serie").val(o);
    o = 4 == t ? r : r.substring(0, 1) + String("000").substring(0, t) + r.substring(1, 4), $("#serie").val(o)
}

function FormatterDescargar(e, r) {
    var t = [],
        o = {
            _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
            encode: function(e) {
                var r, t, n, a, i, c, s, u = "",
                    l = 0;
                for (e = o._utf8_encode(e); l < e.length;) a = (r = e.charCodeAt(l++)) >> 2, i = (3 & r) << 4 | (t = e.charCodeAt(l++)) >> 4, c = (15 & t) << 2 | (n = e.charCodeAt(l++)) >> 6, s = 63 & n, isNaN(t) ? c = s = 64 : isNaN(n) && (s = 64), u = u + this._keyStr.charAt(a) + this._keyStr.charAt(i) + this._keyStr.charAt(c) + this._keyStr.charAt(s);
                return u
            },
            decode: function(e) {
                var r, t, n, a, i, c, s = "",
                    u = 0;
                for (e = e.replace(/[^A-Za-z0-9+/=]/g, ""); u < e.length;) r = this._keyStr.indexOf(e.charAt(u++)) << 2 | (a = this._keyStr.indexOf(e.charAt(u++))) >> 4, t = (15 & a) << 4 | (i = this._keyStr.indexOf(e.charAt(u++))) >> 2, n = (3 & i) << 6 | (c = this._keyStr.indexOf(e.charAt(u++))), s += String.fromCharCode(r), 64 != i && (s += String.fromCharCode(t)), 64 != c && (s += String.fromCharCode(n));
                return s = o._utf8_decode(s)
            },
            _utf8_encode: function(e) {
                e = e.replace(/rn/g, "n");
                for (var r = "", t = 0; t < e.length; t++) {
                    var o = e.charCodeAt(t);
                    o < 128 ? r += String.fromCharCode(o) : o > 127 && o < 2048 ? (r += String.fromCharCode(o >> 6 | 192), r += String.fromCharCode(63 & o | 128)) : (r += String.fromCharCode(o >> 12 | 224), r += String.fromCharCode(o >> 6 & 63 | 128), r += String.fromCharCode(63 & o | 128))
                }
                return r
            },
            _utf8_decode: function(e) {
                for (var r = "", t = 0, o = c1 = c2 = 0; t < e.length;)(o = e.charCodeAt(t)) < 128 ? (r += String.fromCharCode(o), t++) : o > 191 && o < 224 ? (c2 = e.charCodeAt(t + 1), r += String.fromCharCode((31 & o) << 6 | 63 & c2), t += 2) : (c2 = e.charCodeAt(t + 1), c3 = e.charCodeAt(t + 2), r += String.fromCharCode((15 & o) << 12 | (63 & c2) << 6 | 63 & c3), t += 3);
                return r
            }
        };
    if ("S" == r.enviado) {
        var n = "<a href='" + r.data2 + "?em=" + r.em + "&id=" + r.id + "&ope=" + r.servicio + "' class='btn btn-sm btn-warning'>XML</a>&nbsp;";
        t.push(n)
        var n = "<a href='" + r.data4 + "?em=" + r.em + "&id=" + r.id + "&ope=" + r.servicio + "' class='btn btn-sm btn-success'>CDR</a>&nbsp;";
        t.push(n)
    }
    if ("C" == o.decode(r.servicio) || "N" == o.decode(r.servicio) || "T" == o.decode(r.servicio)) {
        var a = "<a href='" + r.data3 + "?em=" + r.em + "&id=" + r.id + "&ope=" + r.servicio + "' class='btn btn-sm btn-primary'>PDF A-4</a>&nbsp;";
        t.push(a)
    } else if ("B" == o.decode(r.servicio) || "E" == o.decode(r.servicio)) {
        var i = "<a href='" + r.data1 + "?em=" + r.em + "&id=" + r.id + "&ope=" + r.servicio + "' class='btn btn-sm btn-danger'>PDF TICKET</a>&nbsp;";
        t.push(i)
    }
    return t.join("")
}
$(function() {
    $("#dateRangePickerDocumentos").on("click", function() {
        $("#fechaDocumentos").datepicker("show")
    }), $("#fechaDocumentos").datepicker({
        language: "es",
        autoclose: !0,
        todayHighlight: !0
    })
    $('#tipodocumento').on('change', function() {
		if(this.value=='01' || this.value=='03' || this.value=='05' || this.value=='06'){
			$('#divFactura').css({"display":"block"});
			$('#serie').attr("placeholder", "F000");
		}else{
			$('#divFactura').css({"display":"none"});	
			$('#serie').attr("placeholder", "B000");
		}
	})
}),
    $("#btnConsultaDocumentos").on("click", function() {
    $("#divMensaje").css({
        display: "none"
    }), $("#mensaje").empty(), $("#tblDocumentosElectronicos").bootstrapTable("removeAll"), $("#tblDocumentosElectronicos").bootstrapTable("refresh", {
        silent: !1,
        height: 350,
        url: "59f788970fNTlmNzg4OTcwZm",
        query: {
            em: $("#empresa").val(),
            fechaemision: $("#fechaDocumentos").val(),
            serie: $("#serie").val(),
            numero: $("#numero").val(),
            tipodocumento: $("#tipodocumento").val(),
            ruc: $("#ruc").val(),
            monto: $("#monto").val()
        }
    })
});

function validarNumerosReales(e) {
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla==8) return true;
    patron = /[0123456789.]/;
    te = String.fromCharCode(tecla);
    return patron.test(te);
}
