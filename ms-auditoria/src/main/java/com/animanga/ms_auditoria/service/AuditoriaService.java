package com.animanga.ms_auditoria.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.animanga.ms_auditoria.model.AuditoriaSistema;
import com.animanga.ms_auditoria.repository.AuditoriaRepository;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public AuditoriaSistema registrarLog(AuditoriaSistema auditoria) {

        auditoria.setFechaHora(LocalDateTime.now());

        return auditoriaRepository.save(auditoria);
    }

    public List<AuditoriaSistema> obtenerTodos() {
        return auditoriaRepository.findAll();
    }

    public AuditoriaSistema obtenerPorId(Long id) {
        return auditoriaRepository.findById(id).orElse(null);
    }

    public List<AuditoriaSistema> obtenerPorUsuario(Long idUsuario) {
        return auditoriaRepository.findByIdUsuario(idUsuario);
    }

    public List<AuditoriaSistema> obtenerPorTabla(String tabla) {
        return auditoriaRepository.findByTablaAfectada(tabla);
    }

    public String eliminarLog(Long id) {

        if (auditoriaRepository.existsById(id)) {

            auditoriaRepository.deleteById(id);

            return "Log eliminado";
        }

        return "El log no existe";
    }
}
