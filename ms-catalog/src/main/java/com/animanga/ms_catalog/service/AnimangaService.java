package com.animanga.ms_catalog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_catalog.dto.AnimangaResumen;
import com.animanga.ms_catalog.dto.AuditRequest;
import com.animanga.ms_catalog.model.Animanga;
import com.animanga.ms_catalog.repository.AnimangaRepository;

@Service
public class AnimangaService {
    
    @Autowired
    private AnimangaRepository animangaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private String validarEntidadProduccion(Long id, String tipo) {
        if (id == null) return null;
        try {
            String url = "http://ms-production/api/entidades/" + id;
            ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return "El " + tipo + " con id " + id + " no existe";
            }
        } catch (Exception e) {
            return "Error al validar " + tipo + ": " + e.getMessage();
        }
        return null;
    }

    private void auditar(String accion, String tabla) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(null, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String guardar(Animanga animanga) {
        if (animanga.getTitulo() == null || animanga.getTitulo().trim().isEmpty()) {
            return "El título del Animanga es obligatorio";
        }
        if (animanga.getFechaEstreno() == null) {
            return "La fecha de estreno es obligatoria";
        }
        if (animanga.getTipoAnimanga() == null || animanga.getTipoAnimanga().getIdTipo() == null) {
            return "El tipo de Animanga es obligatorio";
        }
        
        if (animangaRepository.existsByTitulo(animanga.getTitulo())) {
            return "El Animanga '" + animanga.getTitulo() + "' ya existe";
        }

        String errorEstudio = validarEntidadProduccion(animanga.getIdEstudio(), "estudio");
        if (errorEstudio != null) return errorEstudio;

        String errorAutor = validarEntidadProduccion(animanga.getIdAutor(), "autor");
        if (errorAutor != null) return errorAutor;
        
        animangaRepository.save(animanga);
        auditar("Animanga '" + animanga.getTitulo() + "' creado", "animanga");
        return "Animanga guardado exitosamente";
    }

    public List<Animanga> obtenerTodos() {
        return animangaRepository.findAll();
    }

    public Optional<Animanga> obtenerPorId(Long id) {
        return animangaRepository.findById(id);
    }

    public List<Animanga> buscarPorTitulo(String titulo) {
        return animangaRepository.findByTitulo(titulo);
    }

    public List<Animanga> buscarPorTipo(String nombreTipo) {
        return animangaRepository.findByTipoAnimanga_Nombre(nombreTipo);
    }

    public String actualizar(Long id, Animanga animangaActualizado) {
        Animanga animangaExistente = animangaRepository.findById(id).orElse(null);
        if (animangaExistente == null) {
            return "Animanga no encontrado";
        }
        
        if (animangaActualizado.getTitulo() != null && 
            !animangaActualizado.getTitulo().equals(animangaExistente.getTitulo())) {
            if (animangaRepository.existsByTitulo(animangaActualizado.getTitulo())) {
                return "Error: El título de Animanga ya está en uso";
            }
            animangaExistente.setTitulo(animangaActualizado.getTitulo());
        }
        
        if (animangaActualizado.getDescripcion() != null) {
            animangaExistente.setDescripcion(animangaActualizado.getDescripcion());
        }
        if (animangaActualizado.getFechaEstreno() != null) {
            animangaExistente.setFechaEstreno(animangaActualizado.getFechaEstreno());
        }
        if (animangaActualizado.getEstadoEmision() != null) {
            animangaExistente.setEstadoEmision(animangaActualizado.getEstadoEmision());
        }
        if (animangaActualizado.getTipoAnimanga() != null && animangaActualizado.getTipoAnimanga().getIdTipo() != null) {
            animangaExistente.setTipoAnimanga(animangaActualizado.getTipoAnimanga());
        }
        if (animangaActualizado.getIdEstudio() != null) {
            String errorEstudio = validarEntidadProduccion(animangaActualizado.getIdEstudio(), "estudio");
            if (errorEstudio != null) return errorEstudio;
            animangaExistente.setIdEstudio(animangaActualizado.getIdEstudio());
        }
        if (animangaActualizado.getIdAutor() != null) {
            String errorAutor = validarEntidadProduccion(animangaActualizado.getIdAutor(), "autor");
            if (errorAutor != null) return errorAutor;
            animangaExistente.setIdAutor(animangaActualizado.getIdAutor());
        }
        
        animangaRepository.save(animangaExistente);
        auditar("Animanga '" + animangaExistente.getTitulo() + "' actualizado", "animanga");
        return "Animanga actualizado exitosamente";
    }

    public List<AnimangaResumen> listarResumen() {
        return animangaRepository.listarResumen();
    }

    public boolean eliminar(Long id) {
        if (animangaRepository.existsById(id)) {
            Optional<Animanga> animanga = animangaRepository.findById(id);
            animangaRepository.deleteById(id);
            animanga.ifPresent(a -> auditar("Animanga '" + a.getTitulo() + "' eliminado", "animanga"));
            return true;
        }
        return false;
    }
}