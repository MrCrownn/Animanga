package com.animanga.ms_library.model;

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
@Table(name = "biblioteca_usuario")
public class BibliotecaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBiblioteca;

    @Column(nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private Long idAnimanga;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSeguimiento estadoSeguimiento;

    @Column(nullable = false)
    private LocalDateTime fechaAgregado;

    public BibliotecaUsuario() {
    }

    public Long getIdBiblioteca() { return idBiblioteca; }
    public void setIdBiblioteca(Long idBiblioteca) { this.idBiblioteca = idBiblioteca; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public Long getIdAnimanga() { return idAnimanga; }
    public void setIdAnimanga(Long idAnimanga) { this.idAnimanga = idAnimanga; }

    public EstadoSeguimiento getEstadoSeguimiento() { return estadoSeguimiento; }
    public void setEstadoSeguimiento(EstadoSeguimiento estadoSeguimiento) { this.estadoSeguimiento = estadoSeguimiento; }

    public LocalDateTime getFechaAgregado() { return fechaAgregado; }
    public void setFechaAgregado(LocalDateTime fechaAgregado) { this.fechaAgregado = fechaAgregado; }
}
