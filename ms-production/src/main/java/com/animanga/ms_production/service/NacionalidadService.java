package com.animanga.ms_production.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.animanga.ms_production.model.Nacionalidad;
import com.animanga.ms_production.repository.NacionalidadRepository;

@Service
public class NacionalidadService {

    @Autowired
    private NacionalidadRepository nacionalidadRepository;

    public String guardar(Nacionalidad nacionalidad) {
        if (nacionalidad.getPais() == null || nacionalidad.getPais().trim().isEmpty()) {
            return "El país es obligatorio";
        }

        if (nacionalidadRepository.existsByPais(nacionalidad.getPais())) {
            return "La nacionalidad '" + nacionalidad.getPais() + "' ya existe";
        }

        nacionalidadRepository.save(nacionalidad);
        return "Nacionalidad guardada exitosamente";
    }
    public boolean existePorPais(String pais) {
        return nacionalidadRepository.existsByPais(pais);
    }
    public List<Nacionalidad> obtenerTodos() {
        return nacionalidadRepository.findAll();
    }

    public Optional<Nacionalidad> obtenerPorId(Integer id) {
        return nacionalidadRepository.findById(id);
    }

    public String actualizar(Integer id, Nacionalidad nacionalidadActualizada) {
        Nacionalidad nacionalidadExistente = nacionalidadRepository.findById(id).orElse(null);
        if (nacionalidadExistente == null) {
            return "Nacionalidad no encontrada";
        }

        if (nacionalidadActualizada.getPais() != null &&
            !nacionalidadActualizada.getPais().equals(nacionalidadExistente.getPais())) {
            if (nacionalidadRepository.existsByPais(nacionalidadActualizada.getPais())) {
                return "Error: El país ya está registrado";
            }
            nacionalidadExistente.setPais(nacionalidadActualizada.getPais());
        }

        nacionalidadRepository.save(nacionalidadExistente);
        return "Nacionalidad actualizada exitosamente";
    }

    public boolean eliminar(Integer id) {
        if (nacionalidadRepository.existsById(id)) {
            nacionalidadRepository.deleteById(id);
            return true;
        }
        return false;
    }
}