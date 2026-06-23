package com.animanga.ms_curation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_curation.dto.AuditRequest;
import com.animanga.ms_curation.model.HistorialCuracion;
import com.animanga.ms_curation.model.PropuestaImportacion;
import com.animanga.ms_curation.repository.HistorialCuracionRepository;
import com.animanga.ms_curation.repository.PropuestaImportacionRepository;

@Service
public class HistorialCuracionService {

    @Autowired
    private HistorialCuracionRepository historialRepository;

    @Autowired
    private PropuestaImportacionRepository propuestaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private void auditar(String accion, String tabla) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(null, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String guardar(HistorialCuracion historial, Long idUsuarioAuth) {
        if (historial.getPropuesta() == null || historial.getPropuesta().getIdPropuesta() == null) {
            return "La propuesta es obligatoria";
        }
        if (historial.getDecision() == null || historial.getDecision().trim().isEmpty()) {
            return "La decision es obligatoria";
        }

        historial.setIdModerador(idUsuarioAuth);

        PropuestaImportacion propuesta = propuestaRepository.findById(historial.getPropuesta().getIdPropuesta()).orElse(null);
        if (propuesta == null) {
            return "La propuesta con id " + historial.getPropuesta().getIdPropuesta() + " no existe";
        }
        historial.setPropuesta(propuesta);
        historial.setFechaDecision(LocalDateTime.now());

        historialRepository.save(historial);
        auditar("Historial de curacion creado para propuesta " + historial.getPropuesta().getIdPropuesta() + " por usuario " + idUsuarioAuth, "historial_curacion");
        return "Historial guardado exitosamente";
    }

    public List<HistorialCuracion> obtenerTodos() {
        return historialRepository.findAll();
    }

    public Optional<HistorialCuracion> obtenerPorId(Long id) {
        return historialRepository.findById(id);
    }

    public List<HistorialCuracion> obtenerPorPropuesta(Long idPropuesta) {
        return historialRepository.findByPropuesta_IdPropuesta(idPropuesta);
    }

    public boolean eliminar(Long id, Long idUsuarioAuth) {
        HistorialCuracion historial = historialRepository.findById(id).orElse(null);
        if (historial == null) {
            return false;
        }
        historialRepository.delete(historial);
        auditar("Historial " + id + " eliminado por usuario " + idUsuarioAuth, "historial_curacion");
        return true;
    }
}
