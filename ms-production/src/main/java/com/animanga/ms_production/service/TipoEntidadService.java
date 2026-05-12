package com.animanga.ms_production.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.animanga.ms_production.model.TipoEntidad;
import com.animanga.ms_production.repository.TipoEntidadRepository;

@Service
public class TipoEntidadService {

    @Autowired
    private TipoEntidadRepository tipoEntidadRepository;

    public String guardar(TipoEntidad tipoEntidad) {
        if (tipoEntidad.getNombre() == null || tipoEntidad.getNombre().trim().isEmpty()) {
            return "El nombre del tipo de entidad es obligatorio";
        }

        if (tipoEntidadRepository.existsByNombre(tipoEntidad.getNombre())) {
            return "El tipo de entidad '" + tipoEntidad.getNombre() + "' ya existe";
        }

        tipoEntidadRepository.save(tipoEntidad);
        return "Tipo de entidad guardado exitosamente";
    }

    public List<TipoEntidad> obtenerTodos() {
        return tipoEntidadRepository.findAll();
    }

    public Optional<TipoEntidad> obtenerPorId(Integer id) {
        return tipoEntidadRepository.findById(id);
    }

    public String actualizar(Integer id, TipoEntidad tipoActualizado) {
        TipoEntidad tipoExistente = tipoEntidadRepository.findById(id).orElse(null);
        if (tipoExistente == null) {
            return "Tipo de entidad no encontrado";
        }

        if (tipoActualizado.getNombre() != null &&
            !tipoActualizado.getNombre().equals(tipoExistente.getNombre())) {
            if (tipoEntidadRepository.existsByNombre(tipoActualizado.getNombre())) {
                return "Error: El nombre del tipo de entidad ya está en uso";
            }
            tipoExistente.setNombre(tipoActualizado.getNombre());
        }

        tipoEntidadRepository.save(tipoExistente);
        return "Tipo de entidad actualizado exitosamente";
    }

    public boolean eliminar(Integer id) {
        if (tipoEntidadRepository.existsById(id)) {
            tipoEntidadRepository.deleteById(id);
            return true;
        }
        return false;
    }
}