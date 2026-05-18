package com.animanga.ms_catalog.dto;

public class AnimangaResumen {

    private Long idAnimanga;
    private String titulo;
    private String nombreTipo;
    private String estadoEmision;

    public AnimangaResumen(Long idAnimanga, String titulo, String nombreTipo, String estadoEmision) {
        this.idAnimanga = idAnimanga;
        this.titulo = titulo;
        this.nombreTipo = nombreTipo;
        this.estadoEmision = estadoEmision;
    }

    public Long getIdAnimanga() {
        return idAnimanga;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public String getEstadoEmision() {
        return estadoEmision;
    }
}
