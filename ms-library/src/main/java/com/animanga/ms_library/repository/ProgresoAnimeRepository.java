package com.animanga.ms_library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.animanga.ms_library.model.ProgresoAnime;

public interface ProgresoAnimeRepository extends JpaRepository<ProgresoAnime, Long> {

    List<ProgresoAnime> findByIdBiblioteca(Long idBiblioteca);

    Optional<ProgresoAnime> findTopByIdBibliotecaOrderByFechaActualizacionDesc(Long idBiblioteca);
}
