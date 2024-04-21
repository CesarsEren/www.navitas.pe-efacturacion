package com.alo.digital.transportes.webapp.efact.beans;

public class V_Varios_FacturacionBean {

    private String empresa;
    private String empresaD;
    private String ruc;
    private String razon;
    private String direccion;
    private String ubigeo;
    private String codigoPais;
    private String codigoMoneda;
    private String codigoUnidadMedida;
    private String rutaEnvioSunat;
    private String rutaEnvioTemporal;
    private String rutaEnvioSunatBackup;
    private String rutaRespuestaSunat;
    private String keystoreFile;
    private String keystorePassword;
    private String PrivateKeyAlias;
    private String UsernameSunat;
    private String PasswordSunat;


    public String getRutaEnvioTemporal() {
        return rutaEnvioTemporal;
    }

    public void setRutaEnvioTemporal(String rutaEnvioTemporal) {
        this.rutaEnvioTemporal = rutaEnvioTemporal;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEmpresaD() {
        return empresaD;
    }

    public void setEmpresaD(String empresaD) {
        this.empresaD = empresaD;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getCodigoMoneda() {
        return codigoMoneda;
    }

    public void setCodigoMoneda(String codigoMoneda) {
        this.codigoMoneda = codigoMoneda;
    }

    public String getCodigoUnidadMedida() {
        return codigoUnidadMedida;
    }

    public void setCodigoUnidadMedida(String codigoUnidadMedida) {
        this.codigoUnidadMedida = codigoUnidadMedida;
    }

    public String getRutaEnvioSunat() {
        return rutaEnvioSunat;
    }

    public void setRutaEnvioSunat(String rutaEnvioSunat) {
        this.rutaEnvioSunat = rutaEnvioSunat;
    }

    public String getRutaRespuestaSunat() {
        return rutaRespuestaSunat;
    }

    public void setRutaRespuestaSunat(String rutaRespuestaSunat) {
        this.rutaRespuestaSunat = rutaRespuestaSunat;
    }

    public String getKeystoreFile() {
        return keystoreFile;
    }

    public void setKeystoreFile(String keystoreFile) {
        this.keystoreFile = keystoreFile;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getPrivateKeyAlias() {
        return PrivateKeyAlias;
    }

    public void setPrivateKeyAlias(String privateKeyAlias) {
        PrivateKeyAlias = privateKeyAlias;
    }

    public void setUsernameSunat(String usernameSunat) {
        UsernameSunat = usernameSunat;
    }

    public String getUsernameSunat() {
        return UsernameSunat;
    }

    public void setPasswordSunat(String passwordSunat) {
        PasswordSunat = passwordSunat;
    }

    public String getPasswordSunat() {
        return PasswordSunat;
    }

    public void setRutaEnvioSunatBackup(String rutaEnvioSunatBackup) {
        this.rutaEnvioSunatBackup = rutaEnvioSunatBackup;
    }

    public String getRutaEnvioSunatBackup() {
        return rutaEnvioSunatBackup;
    }

    @Override
    public String toString() {
        return "V_Varios_FacturacionBean [empresa=" + empresa + ", empresaD=" + empresaD + ", ruc=" + ruc + ", razon="
                + razon + ", direccion=" + direccion + ", ubigeo=" + ubigeo + ", codigoPais=" + codigoPais
                + ", codigoMoneda=" + codigoMoneda + ", codigoUnidadMedida=" + codigoUnidadMedida + ", rutaEnvioSunat="
                + rutaEnvioSunat + ", rutaEnvioSunatBackup=" + rutaEnvioSunatBackup + ", rutaRespuestaSunat="
                + rutaRespuestaSunat + ", keystoreFile=" + keystoreFile + ", keystorePassword=" + keystorePassword
                + ", PrivateKeyAlias=" + PrivateKeyAlias + ", UsernameSunat=" + UsernameSunat + ", PasswordSunat="
                + PasswordSunat + "]";
    }


}