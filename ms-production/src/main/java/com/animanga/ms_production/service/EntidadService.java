package com.animanga.ms_production.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.animanga.ms_production.model.Entidad;
import com.animanga.ms_production.repository.EntidadRepository;

@Service
public class EntidadService {

    @Autowired
    private EntidadRepository entidadRepository;

    public String guardar(Entidad entidad) {
        if (entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()) {
            return "El nombre de la entidad es obligatorio";
        }
        if (entidad.getTipoEntidad() == null || entidad.getTipoEntidad().getIdTipo() == null) {
            return "El tipo de entidad es obligatorio";
        }

        if (entidadRepository.existsByNombreAndTipoEntidad_IdTipo(
                entidad.getNombre(), entidad.getTipoEntidad().getIdTipo())) {
            return "La entidad '" + entidad.getNombre() + "' ya existe con ese tipo";
        }

        entidadRepository.save(entidad);
        return "Entidad guardada exitosamente";
    }

    public List<Entidad> obtenerTodos() {
        return entidadRepository.findAll();
    }

    public Optional<Entidad> obtenerPorId(Integer id) {
        return entidadRepository.findById(id);
    }

    public List<Entidad> buscarPorTipo(String nombreTipo) {
        return entidadRepository.findByTipoEntidad_Nombre(nombreTipo);
    }

    public String actualizar(Integer id, Entidad entidadActualizada) {
        Entidad entidadExistente = entidadRepository.findById(id).orElse(null);
        if (entidadExistente == null) {
            return "Entidad no encontrada";
        }

        if (entidadActualizada.getNombre() != null &&
            !entidadActualizada.getNombre().equals(entidadExistente.getNombre())) {
            if (entidadRepository.existsByNombreAndTipoEntidad_IdTipo(
                    entidadActualizada.getNombre(), entidadExistente.getTipoEntidad().getIdTipo())) {
                return "Error: El nombre ya está en uso para este tipo de entidad";
            }
            entidadExistente.setNombre(entidadActualizada.getNombre());
        }
        if (entidadActualizada.getTipoEntidad() != null && entidadActualizada.getTipoEntidad().getIdTipo() != null) {
            entidadExistente.setTipoEntidad(entidadActualizada.getTipoEntidad());
        }
        if (entidadActualizada.getNacionalidad() != null && entidadActualizada.getNacionalidad().getIdNacionalidad() != null) {
            entidadExistente.setNacionalidad(entidadActualizada.getNacionalidad());
        }
        if (entidadActualizada.getFechaNacimiento() != null) {
            entidadExistente.setFechaNacimiento(entidadActualizada.getFechaNacimiento());
        }
        if (entidadActualizada.getDescripcion() != null) {
            entidadExistente.setDescripcion(entidadActualizada.getDescripcion());
        }

        entidadRepository.save(entidadExistente);
        return "Entidad actualizada exitosamente";
    }

    public List <Entidad> buscarPorPais(String pais) {
        return entidadRepository.findByTipoEntidad_Pais(pais);
    }

    public boolean eliminar(Integer id) {
        if (entidadRepository.existsById(id)) {
            entidadRepository.deleteById(id);
            return true;
        }
        return false;
    }
}