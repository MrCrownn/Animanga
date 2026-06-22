package com.animanga.ms_media.dto;

public class AuditRequest {

    private Long idUsuario;
    private String descripcionAccion;
    private String tablaAfectada;

    public AuditRequest() {
    }

    public AuditRequest(Long idUsuario, String descripcionAccion, String tablaAfectada) {
        this.idUsuario = idUsuario;
        this.descripcionAccion = descripcionAccion;
        this.tablaAfectada = tablaAfectada;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescripcionAccion() {
        return descripcionAccion;
    }

    public void setDescripcionAccion(String descripcionAccion) {
        this.descripcionAccion = descripcionAccion;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }
}
