package com.animanga.ms_helpdesk.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_helpdesk.dto.AuditRequest;
import com.animanga.ms_helpdesk.model.EstadoIncidencia;
import com.animanga.ms_helpdesk.model.Incidencia;
import com.animanga.ms_helpdesk.repository.IncidenciaRepository;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private void auditar(Long idUsuarioAuth, String accion, String tabla) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(idUsuarioAuth, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String crear(Incidencia incidencia, Long idUsuarioAuth) {
        incidencia.setIdUsuarioReporta(idUsuarioAuth);
        if (incidencia.getTitulo() == null || incidencia.getTitulo().trim().isEmpty()) {
            return "El titulo es obligatorio";
        }
        if (incidencia.getCategoria() == null) {
            return "La categoria es obligatoria";
        }
        if (incidencia.getPrioridad() == null) {
            return "La prioridad es obligatoria";
        }

        incidencia.setFechaReporte(LocalDateTime.now());
        incidencia.setEstado(EstadoIncidencia.ABIERTO);
        repository.save(incidencia);
        auditar(idUsuarioAuth, "Incidencia " + incidencia.getIdIncidencia() + " creada por usuario " + idUsuarioAuth, "incidencia");
        return "Incidencia creada exitosamente";
    }

    public List<Incidencia> listar() {
        return repository.findAll();
    }

    public Optional<Incidencia> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public List<Incidencia> buscarPorUsuario(Long idUsuario) {
        return repository.findByIdUsuarioReporta(idUsuario);
    }

    public String actualizarEstado(Long id, EstadoIncidencia estado, Long idUsuarioAuth) {
        Incidencia incidencia = repository.findById(id).orElse(null);
        if (incidencia == null) {
            return "Incidencia no encontrada";
        }

        incidencia.setEstado(estado);
        if (estado == EstadoIncidencia.RESUELTO || estado == EstadoIncidencia.CERRADO) {
            incidencia.setFechaResolucion(LocalDateTime.now());
        }
        repository.save(incidencia);
        auditar(idUsuarioAuth, "Incidencia " + id + " actualizada a " + estado.name(), "incidencia");
        return "Incidencia actualizada a " + estado.name();
    }

    public boolean eliminar(Long id, Long idUsuarioAuth) {
        Incidencia incidencia = repository.findById(id).orElse(null);
        if (incidencia == null) {
            return false;
        }
        repository.delete(incidencia);
        auditar(idUsuarioAuth, "Incidencia " + id + " eliminada", "incidencia");
        return true;
    }
}
