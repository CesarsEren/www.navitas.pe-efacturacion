function queryParams(e) {
    return e.em = $("#empresa").val(), e.fechaemision = $("#fechaDocumentos").val(), e.serie = $("#serie").val(), e.numero = $("#numero").val(), e.tipodocumento = $("#tipodocumento").val(), e.ruc = $("#ruc").val(), e.monto = $("#monto").val(), e
}

function responseHandler(e) {
    return e.respuestaServer ? e : ($("#divMensaje").css({
        display: "block"
    }), $("#mensaje").append($("<strong>" + e.mensajeServer + "</strong>")), !1)
}

function RucExistente(e) {
    var r = e.value.trim(),
        t = "";
    "" != r ? $.ajax({
        url: "/facturacion/consultasrastreo/cliente",
        data: JSON.stringify({
            ruc: r
        }),
        contentType: "application/json",
        type: "POST",
        dataType: "json",
        success: function(e) {
            e.respuesta ? Habilitar_Componentes_Cliente(!0, t = e.bean.razon) : Habilitar_Componentes_Cliente(!1, t)
        },
        error: function(e, r) {
            alert("Disculpe, existio un problema al realizar la peticion al servidor.")
        }
    }) : Habilitar_Componentes_Cliente(!1, "")
}

function Habilitar_Componentes_Cliente(e, r) {
    $("#razon").val(r)
}

function validarSoloNumerosEnteros(e) {
    return tecla = document.all ? e.keyCode : e.which, 8 == tecla || (patron = /[0123456789]/, te = String.fromCharCode(tecla), patron.test(te))
}

function validarNumerosReales(e) {
    return tecla = document.all ? e.keyCode : e.which, 8 == tecla || (patron = /[0123456789.]/, te = String.fromCharCode(tecla), patron.test(te))
}

function AumentarCerosNumero(e) {
    var r = e.value,
        t = 7 - r.length,
        o = "0000000",
        a = "";
    if (parseInt(r) < 0) return a = o, void $("#numero").val(a);
    a = 7 == t ? r : String(o).substring(0, t) + r, $("#numero").val(a)
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
        d = {
            _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
            encode: function(e) {
                var r, t, o, a, n, i, c, s = "",
                    l = 0;
                for (e = d._utf8_encode(e); l < e.length;) a = (r = e.charCodeAt(l++)) >> 2, n = (3 & r) << 4 | (t = e.charCodeAt(l++)) >> 4, i = (15 & t) << 2 | (o = e.charCodeAt(l++)) >> 6, c = 63 & o, isNaN(t) ? i = c = 64 : isNaN(o) && (c = 64), s = s + this._keyStr.charAt(a) + this._keyStr.charAt(n) + this._keyStr.charAt(i) + this._keyStr.charAt(c);
                return s
            },
            decode: function(e) {
                var r, t, o, a, n, i, c = "",
                    s = 0;
                for (e = e.replace(/[^A-Za-z0-9+/=]/g, ""); s < e.length;) r = this._keyStr.indexOf(e.charAt(s++)) << 2 | (a = this._keyStr.indexOf(e.charAt(s++))) >> 4, t = (15 & a) << 4 | (n = this._keyStr.indexOf(e.charAt(s++))) >> 2, o = (3 & n) << 6 | (i = this._keyStr.indexOf(e.charAt(s++))), c += String.fromCharCode(r), 64 != n && (c += String.fromCharCode(t)), 64 != i && (c += String.fromCharCode(o));
                return d._utf8_decode(c)
            },
            _utf8_encode: function(e) {
                e = e.replace(/rn/g, "n");
                for (var r = "", t = 0; t < e.length; t++) {
                    var o = e.charCodeAt(t);
                    o < 128 ? r += String.fromCharCode(o) : (127 < o && o < 2048 ? r += String.fromCharCode(o >> 6 | 192) : (r += String.fromCharCode(o >> 12 | 224), r += String.fromCharCode(o >> 6 & 63 | 128)), r += String.fromCharCode(63 & o | 128))
                }
                return r
            },
            _utf8_decode: function(e) {
                for (var r = "", t = 0, o = c1 = c2 = 0; t < e.length;)(o = e.charCodeAt(t)) < 128 ? (r += String.fromCharCode(o), t++) : 191 < o && o < 224 ? (c2 = e.charCodeAt(t + 1), r += String.fromCharCode((31 & o) << 6 | 63 & c2), t += 2) : (c2 = e.charCodeAt(t + 1), c3 = e.charCodeAt(t + 2), r += String.fromCharCode((15 & o) << 12 | (63 & c2) << 6 | 63 & c3), t += 3);
                return r
            }
        };
    if (console.log("DATA  --\x3e  - " + r + " - " + d.decode(r.servicio)), "S" == r.enviado) {
        var o = "<a href='" + r.data2 + "?em=" + r.em + "&id=" + r.id + "&ope=" + r.servicio + "' class='btn btn-sm btn-warning'>XML</a>&nbsp;";
        t.push(o)
    }
    if ("C" == d.decode(r.servicio) || "N" == d.decode(r.servicio) || "T" == d.decode(r.servicio)|| "E" == d.decode(r.servicio)) {
        var a = "<a href='" + r.data3 + "?em=" + r.em + "&id=" + r.id + "&ope=" + r.servicio + "' class='btn btn-sm btn-primary'>PDF A-4</a>&nbsp;";
        t.push(a)
    }
    if ("B" == d.decode(r.servicio) || "E" == d.decode(r.servicio) || "C" == d.decode(r.servicio)) {
        console.log(r.servicio);
        var n = "<a href='" + r.data1 + "?em=" + r.em + "&id=" + r.id + "&ope=" + r.servicio + "' class='btn btn-sm btn-danger'>PDF TICKET</a>&nbsp;";
        t.push(n)
    }
    if ("P" == d.decode(r.servicio)) {
        n = "<a href='" + r.data1 + "?em=" + r.em + "&id=" + r.id + "&ope=" + r.servicio + "' class='btn btn-sm btn-danger'>PDF TICKET</a>&nbsp;";
        var i = "<a href='" + r.data3 + "?em=" + r.em + "&id=" + r.id + "&ope=" + r.servicio + "' class='btn btn-sm btn-primary'>PDF A-4</a>&nbsp;";
        t.push(i), t.push(n)
    }
    return t.join("")
}
$(function() {
    $("#dateRangePickerDocumentos").on("click", function() {
        $("#fechaDocumentos").datepicker("show")
    }), $('[data-toggle="tooltip"]').tooltip(), $("#fechaDocumentos").datepicker({
        language: "es",
        autoclose: !0,
        todayHighlight: !0
    }), $("#tipodocumento").on("change", function() {
        "01" == this.value || "03" == this.value || "05" == this.value || "06" == this.value ? ($("#divFactura").css({
            display: "block"
        }), $("#serie").attr("placeholder", "F000")) : ($("#divFactura").css({
            display: "none"
        }), $("#serie").attr("placeholder", "B000"), $("#ruc").val(""), $("#razon").val(""))
    })
}), $("#btnConsultaDocumentos").on("click", function() {
    $("#divMensaje").css({
        display: "none"
    }), $("#mensaje").empty(), $("#tblDocumentosElectronicosClientes").bootstrapTable("removeAll"), $("#tblDocumentosElectronicosClientes").bootstrapTable("refresh", {
        silent: !1,
        height: 350,
        url: "/facturacion/descarga/documento/electronico/Y29zdWx0YWRvY3VtZW50b3NkZXNjYXJnYQ==",
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