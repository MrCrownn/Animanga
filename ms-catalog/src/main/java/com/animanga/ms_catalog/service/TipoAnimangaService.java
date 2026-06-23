package com.animanga.ms_catalog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_catalog.dto.AuditRequest;
import com.animanga.ms_catalog.model.TipoAnimanga;
import com.animanga.ms_catalog.repository.TipoAnimangaRepository;

@Service
public class TipoAnimangaService {
    
    @Autowired
    private TipoAnimangaRepository tipoAnimangaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private void auditar(String accion, String tabla, Long idUsuarioAuth) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(idUsuarioAuth, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String guardar(TipoAnimanga tipo, Long idUsuarioAuth) {
        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            return "El nombre del TipoAnimanga es obligatorio";
        }
        
        if (tipoAnimangaRepository.existsByNombre(tipo.getNombre())) {
            return "El TipoAnimanga '" + tipo.getNombre() + "' ya existe";
        }
        
        tipoAnimangaRepository.save(tipo);
        auditar("TipoAnimanga '" + tipo.getNombre() + "' creado", "tipo_animanga", idUsuarioAuth);
        return "TipoAnimanga guardado exitosamente";
    }

    public List<TipoAnimanga> obtenerTodos() {
        return tipoAnimangaRepository.findAll();
    }

    public Optional<TipoAnimanga> obtenerPorId(Integer id) {
        return tipoAnimangaRepository.findById(id);
    }

    public String actualizar(Integer id, TipoAnimanga tipoActualizado, Long idUsuarioAuth) {
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
        auditar("TipoAnimanga '" + tipoExistente.getNombre() + "' actualizado", "tipo_animanga", idUsuarioAuth);
        return "TipoAnimanga actualizado exitosamente";
    }

    public boolean eliminar(Integer id, Long idUsuarioAuth) {
        if (tipoAnimangaRepository.existsById(id)) {
            String nombre = tipoAnimangaRepository.findById(id).get().getNombre();
            tipoAnimangaRepository.deleteById(id);
            auditar("TipoAnimanga '" + nombre + "' eliminado", "tipo_animanga", idUsuarioAuth);
            return true;
        }
        return false;
    }
}