package com.animanga.ms_catalog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.animanga.ms_catalog.model.Animanga;
import com.animanga.ms_catalog.repository.AnimangaRepository;

@Service
public class AnimangaService {
    
    @Autowired
    private AnimangaRepository animangaRepository;

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
        
        animangaRepository.save(animanga);
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
            animangaExistente.setIdEstudio(animangaActualizado.getIdEstudio());
        }
        if (animangaActualizado.getIdAutor() != null) {
            animangaExistente.setIdAutor(animangaActualizado.getIdAutor());
        }
        
        animangaRepository.save(animangaExistente);
        return "Animanga actualizado exitosamente";
    }

    public boolean eliminar(Long id) {
        if (animangaRepository.existsById(id)) {
            animangaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}