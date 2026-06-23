package com.animanga.ms_social.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "resena")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResena;

    @Column(nullable = false)
    private Long idUsuario;

    @Column(nullable = false)
    private Long idAnimanga;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal puntuacion;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    private Integer likeCount = 0;

    private Integer comentarioCount = 0;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    public Resena() {
        this.titulo = "";
        this.comentario = "";
    }

    public Long getIdResena() {
        return idResena;
    }

    public void setIdResena(Long idResena) {
        this.idResena = idResena;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdAnimanga() {
        return idAnimanga;
    }

    public void setIdAnimanga(Long idAnimanga) {
        this.idAnimanga = idAnimanga;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public BigDecimal getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(BigDecimal puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getComentarioCount() {
        return comentarioCount;
    }

    public void setComentarioCount(Integer comentarioCount) {
        this.comentarioCount = comentarioCount;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
