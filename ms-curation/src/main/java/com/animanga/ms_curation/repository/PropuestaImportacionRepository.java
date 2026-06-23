package com.animanga.ms_curation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.animanga.ms_curation.model.EstadoCuracion;
import com.animanga.ms_curation.model.PropuestaImportacion;

public interface PropuestaImportacionRepository extends JpaRepository<PropuestaImportacion, Long> {

    List<PropuestaImportacion> findByIdUsuarioPropone(Long idUsuarioPropone);

    List<PropuestaImportacion> findByEstadoCuracion(EstadoCuracion estadoCuracion);
}
