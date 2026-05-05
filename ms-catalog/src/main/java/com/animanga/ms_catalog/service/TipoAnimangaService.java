package com.animanga.ms_catalog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.animanga.ms_catalog.model.TipoAnimanga;
import com.animanga.ms_catalog.repository.TipoAnimangaRepository;

@Service
public class TipoAnimangaService {
    
    @Autowired
    private TipoAnimangaRepository tipoAnimangaRepository;

    public String guardar(TipoAnimanga tipo) {
        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            return "El nombre del TipoAnimanga es obligatorio";
        }
        
        if (tipoAnimangaRepository.existsByNombre(tipo.getNombre())) {
            return "El TipoAnimanga '" + tipo.getNombre() + "' ya existe";
        }
        
        tipoAnimangaRepository.save(tipo);
        return "TipoAnimanga guardado exitosamente";
    }

    public List<TipoAnimanga> obtenerTodos() {
        return tipoAnimangaRepository.findAll();
    }

    public Optional<TipoAnimanga> obtenerPorId(Integer id) {
        return tipoAnimangaRepository.findById(id);
    }

    public String actualizar(Integer id, TipoAnimanga tipoActualizado) {
        TipoAnimanga tipoExistente = tipoAnimangaRepository.findById(id).orElse(null);
        if (tipoExistente == null) {
            return "TipoAnimanga no encontrado";
        }
        
        if (tipoActualizado.getNombre() != null && 
            !tipoActualizado.getNombre().equals(tipoExistente.getNombre())) {
            if (tipoAnimangaRepository.existsByNombre(tipoActualizado.getNombre())) {
                return "Error: El nombre de TipoAnimanga ya está en uso";
            }
            tipoExistente.setNombre(tipoActualizado.getNombre());
        }
        
        tipoAnimangaRepository.save(tipoExistente);
        return "TipoAnimanga actualizado exitosamente";
    }

    public boolean eliminar(Integer id) {
        if (tipoAnimangaRepository.existsById(id)) {
            tipoAnimangaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}