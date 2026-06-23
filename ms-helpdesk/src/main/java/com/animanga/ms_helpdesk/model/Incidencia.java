package com.animanga.ms_helpdesk.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "incidencia")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIncidencia;

    @Column(nullable = false)
    private Long idUsuarioReporta;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaIncidencia categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadIncidencia prioridad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoIncidencia estado = EstadoIncidencia.ABIERTO;

    @Column(nullable = false)
    private LocalDateTime fechaReporte;

    private LocalDateTime fechaResolucion;

    public Incidencia() {
        this.titulo = "";
        this.descripcion = "";
    }

    public Long getIdIncidencia() { return idIncidencia; }
    public void setIdIncidencia(Long idIncidencia) { this.idIncidencia = idIncidencia; }

    public Long getIdUsuarioReporta() { return idUsuarioReporta; }
    public void setIdUsuarioReporta(Long idUsuarioReporta) { this.idUsuarioReporta = idUsuarioReporta; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public CategoriaIncidencia getCategoria() { return categoria; }
    public void setCategoria(CategoriaIncidencia categoria) { this.categoria = categoria; }

    public PrioridadIncidencia getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadIncidencia prioridad) { this.prioridad = prioridad; }

    public EstadoIncidencia getEstado() { return estado; }
    public void setEstado(EstadoIncidencia estado) { this.estado = estado; }

    public LocalDateTime getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(LocalDateTime fechaReporte) { this.fechaReporte = fechaReporte; }

    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }
}
