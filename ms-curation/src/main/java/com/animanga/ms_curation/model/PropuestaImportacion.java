package com.animanga.ms_curation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "propuesta_importacion")
public class PropuestaImportacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPropuesta;

    @Column(nullable = false)
    private Long idUsuarioPropone;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String datosJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuracion estadoCuracion = EstadoCuracion.PENDIENTE;

    @Column(columnDefinition = "TEXT")
    private String comentarioRechazo;

    public PropuestaImportacion() {
        this.datosJson = "";
    }

    public Long getIdPropuesta() {
        return idPropuesta;
    }

    public void setIdPropuesta(Long idPropuesta) {
        this.idPropuesta = idPropuesta;
    }

    public Long getIdUsuarioPropone() {
        return idUsuarioPropone;
    }

    public void setIdUsuarioPropone(Long idUsuarioPropone) {
        this.idUsuarioPropone = idUsuarioPropone;
    }

    public String getDatosJson() {
        return datosJson;
    }

    public void setDatosJson(String datosJson) {
        this.datosJson = datosJson;
    }

    public EstadoCuracion getEstadoCuracion() {
        return estadoCuracion;
    }

    public void setEstadoCuracion(EstadoCuracion estadoCuracion) {
        this.estadoCuracion = estadoCuracion;
    }

    public String getComentarioRechazo() {
        return comentarioRechazo;
    }

    public void setComentarioRechazo(String comentarioRechazo) {
        this.comentarioRechazo = comentarioRechazo;
    }
}
