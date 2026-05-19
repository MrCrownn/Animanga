package com.animanga.ms_catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.animanga.ms_catalog.model.Animanga;


@Repository
public interface AnimangaRepository  extends JpaRepository<Animanga, Long> {
    List <Animanga> findByTitulo(String titulo);
    List<Animanga> findByTipoAnimanga_Nombre(String nombreTipo);
    boolean existsByTitulo(String titulo);
}   
