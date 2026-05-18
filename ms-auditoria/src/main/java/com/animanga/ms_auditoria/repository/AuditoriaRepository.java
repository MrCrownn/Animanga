package com.animanga.ms_auditoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.animanga.ms_auditoria.model.AuditoriaSistema;

public interface AuditoriaRepository extends JpaRepository<AuditoriaSistema, Long> {

    List<AuditoriaSistema> findByIdUsuario(Long idUsuario);

    List<AuditoriaSistema> findByTablaAfectada(String tablaAfectada);
}
