package com.animanga.ms_media.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_media.dto.AuditRequest;
import com.animanga.ms_media.model.RecursoMultimedia;
import com.animanga.ms_media.repository.RecursoMultimediaRepository;

@Service
public class RecursoMultimediaService {

    @Autowired
    private RecursoMultimediaRepository recursoRepository;

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

    public String guardar(RecursoMultimedia recurso) {
        if (recurso.getIdAnimanga() == null) {
            return "El ID del animanga es obligatorio";
        }
        if (recurso.getTipoRecurso() == null || recurso.getTipoRecurso().trim().isEmpty()) {
            return "El tipo de recurso es obligatorio";
        }
        if (recurso.getUrlRecurso() == null || recurso.getUrlRecurso().trim().isEmpty()) {
            return "La URL del recurso es obligatoria";
        }

        recursoRepository.save(recurso);
        auditar("Recurso " + recurso.getIdRecurso() + " creado para animanga " + recurso.getIdAnimanga(), "recurso_multimedia");
        return "Recurso multimedia guardado exitosamente";
    }

    public List<RecursoMultimedia> obtenerTodos() {
        return recursoRepository.findAll();
    }

    public Optional<RecursoMultimedia> obtenerPorId(Long id) {
        return recursoRepository.findById(id);
    }

    public List<RecursoMultimedia> obtenerPorAnimanga(Long idAnimanga) {
        return recursoRepository.findByIdAnimanga(idAnimanga);
    }

    public List<RecursoMultimedia> obtenerPorAnimangaYTipo(Long idAnimanga, String tipo) {
        return recursoRepository.findByIdAnimangaAndTipoRecurso(idAnimanga, tipo);
    }

    public String actualizar(Long id, RecursoMultimedia recursoActualizado) {
        RecursoMultimedia recursoExistente = recursoRepository.findById(id).orElse(null);
        if (recursoExistente == null) {
            return "Recurso no encontrado";
        }

        if (recursoActualizado.getIdAnimanga() != null) {
            recursoExistente.setIdAnimanga(recursoActualizado.getIdAnimanga());
        }
        if (recursoActualizado.getTipoRecurso() != null) {
            recursoExistente.setTipoRecurso(recursoActualizado.getTipoRecurso());
        }
        if (recursoActualizado.getUrlRecurso() != null) {
            recursoExistente.setUrlRecurso(recursoActualizado.getUrlRecurso());
        }
        if (recursoActualizado.getOptimizacion() != null) {
            recursoExistente.setOptimizacion(recursoActualizado.getOptimizacion());
        }

        recursoRepository.save(recursoExistente);
        auditar("Recurso " + id + " actualizado", "recurso_multimedia");
        return "Recurso multimedia actualizado exitosamente";
    }

    public boolean eliminar(Long id) {
        RecursoMultimedia recurso = recursoRepository.findById(id).orElse(null);
        if (recurso == null) {
            return false;
        }
        recursoRepository.delete(recurso);
        auditar("Recurso " + id + " eliminado", "recurso_multimedia");
        return true;
    }
}
