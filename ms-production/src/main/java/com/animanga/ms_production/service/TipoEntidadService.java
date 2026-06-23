package com.animanga.ms_production.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_production.dto.AuditRequest;
import com.animanga.ms_production.model.TipoEntidad;
import com.animanga.ms_production.repository.TipoEntidadRepository;

@Service
public class TipoEntidadService {

    @Autowired
    private TipoEntidadRepository tipoEntidadRepository;

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

    public String guardar(TipoEntidad tipoEntidad, Long idUsuarioAuth) {
        if (tipoEntidad.getNombre() == null || tipoEntidad.getNombre().trim().isEmpty()) {
            return "El nombre del tipo de entidad es obligatorio";
        }

        if (tipoEntidadRepository.existsByNombre(tipoEntidad.getNombre())) {
            return "El tipo de entidad '" + tipoEntidad.getNombre() + "' ya existe";
        }

        tipoEntidadRepository.save(tipoEntidad);
        auditar("TipoEntidad '" + tipoEntidad.getNombre() + "' creado", "tipo_entidad", idUsuarioAuth);
        return "Tipo de entidad guardado exitosamente";
    }

    public List<TipoEntidad> obtenerTodos() {
        return tipoEntidadRepository.findAll();
    }

    public Optional<TipoEntidad> obtenerPorId(Integer id) {
        return tipoEntidadRepository.findById(id);
    }

    public String actualizar(Integer id, TipoEntidad tipoActualizado, Long idUsuarioAuth) {
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
        auditar("TipoEntidad '" + tipoExistente.getNombre() + "' actualizado", "tipo_entidad", idUsuarioAuth);
        return "Tipo de entidad actualizado exitosamente";
    }

    public boolean eliminar(Integer id, Long idUsuarioAuth) {
        TipoEntidad tipoEntidad = tipoEntidadRepository.findById(id).orElse(null);
        if (tipoEntidad == null) {
            return false;
        }
        tipoEntidadRepository.delete(tipoEntidad);
        auditar("TipoEntidad '" + tipoEntidad.getNombre() + "' eliminado", "tipo_entidad", idUsuarioAuth);
        return true;
    }
}