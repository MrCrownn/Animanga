package com.animanga.ms_catalog.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "animanga")
public class Animanga {

    public enum EstadoEmision {
        EN_CURSO, FINALIZADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnimanga;

    @Column(nullable = false, unique = true)
    private String titulo;

    private String descripcion;

    private LocalDate fechaEstreno;

    @Column(name= "estado_emision")
    private EstadoEmision estadoEmision;

    @ManyToOne
    @JoinColumn(name = "idTipo")
    private TipoAnimanga tipoAnimanga;

    @Column(name = "id_estudio") //Fk remota a EntidadProduccion
    private Long idEstudio;

    @Column(name = "id_autor") //Fk remota a EntidadProduccion
    private Long idAutor;

    @ManyToMany
    @JoinTable(
        name = "animanga_genero",
        joinColumns = @JoinColumn(name = "id_animanga"),
        inverseJoinColumns = @JoinColumn(name = "id_genero")
    )
    private Set<Genero> generos = new HashSet<>();

    public Animanga() {
        this.titulo = "";
        this.descripcion = "";
        
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public EstadoEmision getEstadoEmision() {
        return estadoEmision;
    }

    public void setEstadoEmision(EstadoEmision estadoEmision) {
        this.estadoEmision = estadoEmision;
    }

    public TipoAnimanga getTipoAnimanga() {
        return tipoAnimanga;
    }

    public void setTipoAnimanga(TipoAnimanga tipoAnimanga) {
        this.tipoAnimanga = tipoAnimanga;
    }

    public Long getIdEstudio() {
        return idEstudio;
    }

    public void setIdEstudio(Long idEstudio) {
        this.idEstudio = idEstudio;
    }

    public Long getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Long idAutor) {
        this.idAutor = idAutor;
    }

    public Set<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(Set<Genero> generos) {
        this.generos = generos;
    }

}
