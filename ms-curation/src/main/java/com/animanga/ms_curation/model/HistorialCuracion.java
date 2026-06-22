package com.animanga.ms_curation.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial_curacion")
public class HistorialCuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuracion;

    @ManyToOne
    @JoinColumn(name = "idPropuesta", nullable = false)
    private PropuestaImportacion propuesta;

    @Column(nullable = false)
    private Long idModerador;

    @Column(nullable = false)
    private LocalDateTime fechaDecision;

    @Column(nullable = false)
    private String decision;

    public HistorialCuracion() {
    }

    public Long getIdCuracion() {
        return idCuracion;
    }

    public void setIdCuracion(Long idCuracion) {
        this.idCuracion = idCuracion;
    }

    public PropuestaImportacion getPropuesta() {
        return propuesta;
    }

    public void setPropuesta(PropuestaImportacion propuesta) {
        this.propuesta = propuesta;
    }

    public Long getIdModerador() {
        return idModerador;
    }

    public void setIdModerador(Long idModerador) {
        this.idModerador = idModerador;
    }

    public LocalDateTime getFechaDecision() {
        return fechaDecision;
    }

    public void setFechaDecision(LocalDateTime fechaDecision) {
        this.fechaDecision = fechaDecision;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
