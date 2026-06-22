package com.animanga.ms_media.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recurso_multimedia")
public class RecursoMultimedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRecurso;

    @Column(nullable = false)
    private Long idAnimanga;

    @Column(nullable = false)
    private String tipoRecurso;

    @Column(nullable = false)
    private String urlRecurso;

    private String optimizacion;

    public RecursoMultimedia() {
    }

    public Long getIdRecurso() {
        return idRecurso;
    }

    public void setIdRecurso(Long idRecurso) {
        this.idRecurso = idRecurso;
    }

    public Long getIdAnimanga() {
        return idAnimanga;
    }

    public void setIdAnimanga(Long idAnimanga) {
        this.idAnimanga = idAnimanga;
    }

    public String getTipoRecurso() {
        return tipoRecurso;
    }

    public void setTipoRecurso(String tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }

    public String getUrlRecurso() {
        return urlRecurso;
    }

    public void setUrlRecurso(String urlRecurso) {
        this.urlRecurso = urlRecurso;
    }

    public String getOptimizacion() {
        return optimizacion;
    }

    public void setOptimizacion(String optimizacion) {
        this.optimizacion = optimizacion;
    }
}
