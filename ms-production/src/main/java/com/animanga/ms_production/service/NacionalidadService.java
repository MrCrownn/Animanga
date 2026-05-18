package com.animanga.ms_production.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_production.dto.AuditRequest;
import com.animanga.ms_production.model.Nacionalidad;
import com.animanga.ms_production.repository.NacionalidadRepository;

@Service
public class NacionalidadService {

    @Autowired
    private NacionalidadRepository nacionalidadRepository;

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

    public String guardar(Nacionalidad nacionalidad) {
        if (nacionalidad.getPais() == null || nacionalidad.getPais().trim().isEmpty()) {
            return "El país es obligatorio";
        }

        if (nacionalidadRepository.existsByPais(nacionalidad.getPais())) {
            return "La nacionalidad '" + nacionalidad.getPais() + "' ya existe";
        }

        nacionalidadRepository.save(nacionalidad);
        auditar("Nacionalidad '" + nacionalidad.getPais() + "' creada", "nacionalidad");
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
        auditar("Nacionalidad '" + nacionalidadExistente.getPais() + "' actualizada", "nacionalidad");
        return "Nacionalidad actualizada exitosamente";
    }

    public boolean eliminar(Integer id) {
        if (nacionalidadRepository.existsById(id)) {
            String pais = nacionalidadRepository.findById(id).get().getPais();
            nacionalidadRepository.deleteById(id);
            auditar("Nacionalidad '" + pais + "' eliminada", "nacionalidad");
            return true;
        }
        return false;
    }
}