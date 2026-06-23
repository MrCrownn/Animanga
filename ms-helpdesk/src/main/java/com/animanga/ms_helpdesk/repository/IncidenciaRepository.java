package com.animanga.ms_helpdesk.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.animanga.ms_helpdesk.model.Incidencia;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    List<Incidencia> findByIdUsuarioReporta(Long idUsuarioReporta);
}
