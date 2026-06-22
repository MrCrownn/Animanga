package com.animanga.ms_social.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "voto_resena")
public class VotoResena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVoto;

    @ManyToOne
    @JoinColumn(name = "idResena", nullable = false)
    private Resena resena;

    @Column(nullable = false)
    private Long idUsuarioVota;

    @Column(nullable = false)
    private Boolean esUtil;

    public VotoResena() {
    }

    public Long getIdVoto() {
        return idVoto;
    }

    public void setIdVoto(Long idVoto) {
        this.idVoto = idVoto;
    }

    public Resena getResena() {
        return resena;
    }

    public void setResena(Resena resena) {
        this.resena = resena;
    }

    public Long getIdUsuarioVota() {
        return idUsuarioVota;
    }

    public void setIdUsuarioVota(Long idUsuarioVota) {
        this.idUsuarioVota = idUsuarioVota;
    }

    public Boolean getEsUtil() {
        return esUtil;
    }

    public void setEsUtil(Boolean esUtil) {
        this.esUtil = esUtil;
    }
}
