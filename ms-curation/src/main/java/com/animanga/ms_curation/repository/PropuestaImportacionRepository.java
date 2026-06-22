package com.animanga.ms_curation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animanga.ms_curation.model.PropuestaImportacion;

@Repository
public interface PropuestaImportacionRepository extends JpaRepository<PropuestaImportacion, Long> {
    List<PropuestaImportacion> findByIdUsuarioPropone(Long idUsuarioPropone);
    List<PropuestaImportacion> findByEstadoCuracion(String estadoCuracion);
}
