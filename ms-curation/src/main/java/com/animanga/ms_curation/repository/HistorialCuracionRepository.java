package com.animanga.ms_curation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animanga.ms_curation.model.HistorialCuracion;

@Repository
public interface HistorialCuracionRepository extends JpaRepository<HistorialCuracion, Long> {
    List<HistorialCuracion> findByPropuesta_IdPropuesta(Long idPropuesta);
    List<HistorialCuracion> findByIdModerador(Long idModerador);
}
