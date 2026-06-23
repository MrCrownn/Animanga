package com.animanga.ms_catalog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_catalog.dto.AuditRequest;
import com.animanga.ms_catalog.model.Genero;
import com.animanga.ms_catalog.repository.GeneroRepository;

@Service
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

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

    public String guardar(Genero genero, Long idUsuarioAuth) {
        if (genero.getNombre() == null || genero.getNombre().trim().isEmpty()) {
            return "El nombre del genero es obligatorio";
        }

        if (generoRepository.existsByNombre(genero.getNombre())) {
            return "El genero '" + genero.getNombre() + "' ya existe";
        }

        generoRepository.save(genero);
        auditar("Genero '" + genero.getNombre() + "' creado", "genero", idUsuarioAuth);
        return "Genero guardado exitosamente";
    }

    public List<Genero> obtenerTodos() {
        return generoRepository.findAll();
    }

    public Optional<Genero> obtenerPorId(Integer id) {
        return generoRepository.findById(id);
    }

    public String actualizar(Integer id, Genero generoActualizado, Long idUsuarioAuth) {
        Genero generoExistente = generoRepository.findById(id).orElse(null);
        if (generoExistente == null) {
            return "Genero no encontrado";
        }

        if (generoActualizado.getNombre() != null &&
            !generoActualizado.getNombre().equals(generoExistente.getNombre())) {
            if (generoRepository.existsByNombre(generoActualizado.getNombre())) {
                return "Error: El nombre del genero ya esta en uso";
            }
            generoExistente.setNombre(generoActualizado.getNombre());
        }

        generoRepository.save(generoExistente);
        auditar("Genero '" + generoExistente.getNombre() + "' actualizado", "genero", idUsuarioAuth);
        return "Genero actualizado exitosamente";
    }

    public boolean eliminar(Integer id, Long idUsuarioAuth) {
        Genero genero = generoRepository.findById(id).orElse(null);
        if (genero == null) {
            return false;
        }
        generoRepository.delete(genero);
        auditar("Genero '" + genero.getNombre() + "' eliminado", "genero", idUsuarioAuth);
        return true;
    }
}
