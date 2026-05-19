package com.animanga.ms_catalog.dto;

import java.util.List;

public class AnimangaResumen {

    private Long idAnimanga;
    private String titulo;
    private String nombreTipo;
    private String estadoEmision;
    private List<String> generos;


    public AnimangaResumen(Long idAnimanga, String titulo, String nombreTipo,
                           String estadoEmision, List<String> generos) {
        this.idAnimanga = idAnimanga;
        this.titulo = titulo;
        this.nombreTipo = nombreTipo;
        this.estadoEmision = estadoEmision;
        this.generos = generos;
    }

    public List<String> getGeneros() {
        return generos;
    }
    public void setGeneros(List<String> generos) {
        this.generos = generos;
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
