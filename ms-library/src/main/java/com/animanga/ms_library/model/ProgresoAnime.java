package com.animanga.ms_library.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "progreso_anime")
public class ProgresoAnime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProgreso;

    @Column(nullable = false)
    private Long idBiblioteca;

    @Column(nullable = false)
    private Integer capituloActual;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    public ProgresoAnime() {
    }

    public Long getIdProgreso() { return idProgreso; }
    public void setIdProgreso(Long idProgreso) { this.idProgreso = idProgreso; }

    public Long getIdBiblioteca() { return idBiblioteca; }
    public void setIdBiblioteca(Long idBiblioteca) { this.idBiblioteca = idBiblioteca; }

    public Integer getCapituloActual() { return capituloActual; }
    public void setCapituloActual(Integer capituloActual) { this.capituloActual = capituloActual; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
