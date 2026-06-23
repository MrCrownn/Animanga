package com.animanga.ms_production.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_production.dto.AuditRequest;
import com.animanga.ms_production.model.Entidad;
import com.animanga.ms_production.model.Nacionalidad;
import com.animanga.ms_production.model.TipoEntidad;
import com.animanga.ms_production.repository.EntidadRepository;
import com.animanga.ms_production.repository.NacionalidadRepository;
import com.animanga.ms_production.repository.TipoEntidadRepository;

@Service
public class EntidadService {

    @Autowired
    private EntidadRepository entidadRepository;

    @Autowired
    private TipoEntidadRepository tipoEntidadRepository;

    @Autowired
    private NacionalidadRepository nacionalidadRepository;

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

    public String guardar(Entidad entidad, Long idUsuarioAuth) {
        if (entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()) {
            return "El nombre de la entidad es obligatorio";
        }
        if (entidad.getTipoEntidad() == null || entidad.getTipoEntidad().getIdTipo() == null) {
            return "El tipo de entidad es obligatorio";
        }

        TipoEntidad tipoEntidad = tipoEntidadRepository.findById(entidad.getTipoEntidad().getIdTipo()).orElse(null);
        if (tipoEntidad == null) {
            return "El tipo de entidad con id " + entidad.getTipoEntidad().getIdTipo() + " no existe";
        }
        entidad.setTipoEntidad(tipoEntidad);

        if (entidad.getNacionalidad() != null && entidad.getNacionalidad().getIdNacionalidad() != null) {
            Nacionalidad nacionalidad = nacionalidadRepository.findById(entidad.getNacionalidad().getIdNacionalidad()).orElse(null);
            if (nacionalidad == null) {
                return "La nacionalidad con id " + entidad.getNacionalidad().getIdNacionalidad() + " no existe";
            }
            entidad.setNacionalidad(nacionalidad);
        }

        if (entidadRepository.existsByNombreAndTipoEntidad_IdTipo(
                entidad.getNombre(), entidad.getTipoEntidad().getIdTipo())) {
            return "La entidad '" + entidad.getNombre() + "' ya existe con ese tipo";
        }

        entidadRepository.save(entidad);
        auditar("Entidad '" + entidad.getNombre() + "' creada", "entidad", idUsuarioAuth);
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

    public String actualizar(Integer id, Entidad entidadActualizada, Long idUsuarioAuth) {
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
            TipoEntidad tipoEntidad = tipoEntidadRepository.findById(entidadActualizada.getTipoEntidad().getIdTipo()).orElse(null);
            if (tipoEntidad == null) {
                return "El tipo de entidad con id " + entidadActualizada.getTipoEntidad().getIdTipo() + " no existe";
            }
            entidadExistente.setTipoEntidad(tipoEntidad);
        }
        if (entidadActualizada.getNacionalidad() != null && entidadActualizada.getNacionalidad().getIdNacionalidad() != null) {
            Nacionalidad nacionalidad = nacionalidadRepository.findById(entidadActualizada.getNacionalidad().getIdNacionalidad()).orElse(null);
            if (nacionalidad == null) {
                return "La nacionalidad con id " + entidadActualizada.getNacionalidad().getIdNacionalidad() + " no existe";
            }
            entidadExistente.setNacionalidad(nacionalidad);
        }
        if (entidadActualizada.getFechaNacimiento() != null) {
            entidadExistente.setFechaNacimiento(entidadActualizada.getFechaNacimiento());
        }
        if (entidadActualizada.getDescripcion() != null) {
            entidadExistente.setDescripcion(entidadActualizada.getDescripcion());
        }

        entidadRepository.save(entidadExistente);
        auditar("Entidad '" + entidadExistente.getNombre() + "' actualizada", "entidad", idUsuarioAuth);
        return "Entidad actualizada exitosamente";
    }

    public boolean existePorId(Integer id) {
        return entidadRepository.existsById(id);
    }

    public List <Entidad> buscarPorPais(String pais) {
        return entidadRepository.findByNacionalidad_Pais(pais);
    }

    public boolean eliminar(Integer id, Long idUsuarioAuth) {
        Entidad entidad = entidadRepository.findById(id).orElse(null);
        if (entidad == null) {
            return false;
        }
        entidadRepository.delete(entidad);
        auditar("Entidad '" + entidad.getNombre() + "' eliminada", "entidad", idUsuarioAuth);
        return true;
    }
}