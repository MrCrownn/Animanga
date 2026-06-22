package com.animanga.ms_media.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animanga.ms_media.model.RecursoMultimedia;

@Repository
public interface RecursoMultimediaRepository extends JpaRepository<RecursoMultimedia, Long> {
    List<RecursoMultimedia> findByIdAnimanga(Long idAnimanga);
    List<RecursoMultimedia> findByIdAnimangaAndTipoRecurso(Long idAnimanga, String tipoRecurso);
}
