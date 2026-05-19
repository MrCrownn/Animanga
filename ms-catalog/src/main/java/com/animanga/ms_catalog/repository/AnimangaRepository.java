package com.animanga.ms_catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.animanga.ms_catalog.dto.AnimangaResumen;
import com.animanga.ms_catalog.model.Animanga;


@Repository
public interface AnimangaRepository  extends JpaRepository<Animanga, Long> {
    List <Animanga> findByTitulo(String titulo);
    List<Animanga> findByTipoAnimanga_Nombre(String nombreTipo);
    boolean existsByTitulo(String titulo);

    @Query(value = """
            SELECT a.id_animanga, a.titulo, t.nombre AS nombreTipo, a.estado_emision
            FROM animanga a
            JOIN tipo_animanga t ON a.idTipo = t.id_tipo
            """, nativeQuery = true)
    List<AnimangaResumen> listarResumen();
}   
