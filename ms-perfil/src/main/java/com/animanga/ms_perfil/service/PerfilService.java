package com.animanga.ms_perfil.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_perfil.dto.AuditRequest;
import com.animanga.ms_perfil.model.Perfil;
import com.animanga.ms_perfil.repository.PerfilRepository;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private RestTemplate restTemplate;

    private String validarUsuario(Long idUsuario) {
        if (idUsuario == null) return null;
        try {
            String url = "http://ms-auth/api/usuarios/" + idUsuario;
            ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                return "El usuario con id " + idUsuario + " no existe";
            }
        } catch (Exception e) {
            return "Error al validar usuario: " + e.getMessage();
        }
        return null;
    }

    private void auditar(Long idUsuario, String accion, String tabla) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(idUsuario, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String guardar(Perfil perfil) {
        if (perfil.getIdUsuario() == null) {
            return "El id del usuario es obligatorio";
        }

        String errorUsuario = validarUsuario(perfil.getIdUsuario());
        if (errorUsuario != null) return errorUsuario;

        perfil.setFechaRegistro(LocalDateTime.now());

        perfilRepository.save(perfil);
        auditar(perfil.getIdUsuario(), "Perfil creado para usuario " + perfil.getIdUsuario(), "perfil");
        return "Perfil guardado exitosamente";
    }

    public List<Perfil> obtenerTodos() {
        return perfilRepository.findAll();
    }

    public Optional<Perfil> obtenerPorId(Long id) {
        return perfilRepository.findById(id);
    }

    public Optional<Perfil> obtenerPorUsuario(Long idUsuario) {
        return perfilRepository.findByIdUsuario(idUsuario);
    }

    public String actualizar(Long id, Perfil nuevoPerfil) {
        Perfil perfilExistente = perfilRepository.findById(id).orElse(null);
        if (perfilExistente == null) {
            return "Perfil no encontrado";
        }

        if (nuevoPerfil.getAvatarUrl() != null) {
            perfilExistente.setAvatarUrl(nuevoPerfil.getAvatarUrl());
        }
        if (nuevoPerfil.getBiografia() != null) {
            perfilExistente.setBiografia(nuevoPerfil.getBiografia());
        }
        if (nuevoPerfil.getPreferencias() != null) {
            perfilExistente.setPreferencias(nuevoPerfil.getPreferencias());
        }

        perfilRepository.save(perfilExistente);
        auditar(perfilExistente.getIdUsuario(), "Perfil actualizado", "perfil");
        return "Perfil actualizado exitosamente";
    }

    public boolean eliminar(Long id) {
        Perfil perfil = perfilRepository.findById(id).orElse(null);
        if (perfil == null) {
            return false;
        }
        perfilRepository.delete(perfil);
        auditar(perfil.getIdUsuario(), "Perfil eliminado", "perfil");
        return true;
    }
}
