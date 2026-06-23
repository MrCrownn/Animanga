package com.animanga.ms_social.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_social.dto.AuditRequest;
import com.animanga.ms_social.dto.ResenasPorAnimangaResponse;
import com.animanga.ms_social.model.Resena;
import com.animanga.ms_social.repository.ResenaRepository;

@Service
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private void auditar(String accion, String tabla, Long idUsuario) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(idUsuario, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String guardar(Resena resena, Long idUsuarioAuth) {
        if (resena.getIdUsuario() == null) {
            return "El ID del usuario es obligatorio";
        }
        resena.setIdUsuario(idUsuarioAuth);
        if (resena.getIdAnimanga() == null) {
            return "El ID del animanga es obligatorio";
        }
        if (resena.getTitulo() == null || resena.getTitulo().trim().isEmpty()) {
            return "El titulo de la resena es obligatorio";
        }
        if (resena.getPuntuacion() == null || resena.getPuntuacion().compareTo(BigDecimal.ZERO) < 0 || resena.getPuntuacion().compareTo(new BigDecimal("10.0")) > 0) {
            return "La puntuacion debe estar entre 0.0 y 10.0";
        }

        resena.setLikeCount(0);
        resena.setComentarioCount(0);
        resena.setFechaCreacion(LocalDateTime.now());

        resenaRepository.save(resena);
        auditar("Resena " + resena.getIdResena() + " creada por usuario " + idUsuarioAuth, "resena", idUsuarioAuth);
        return "Resena guardada exitosamente";
    }

    public List<Resena> obtenerTodas() {
        return resenaRepository.findAll();
    }

    public Optional<Resena> obtenerPorId(Long id) {
        return resenaRepository.findById(id);
    }

    public List<Resena> obtenerPorUsuario(Long idUsuario) {
        return resenaRepository.findByIdUsuario(idUsuario);
    }

    public List<Resena> obtenerPorAnimanga(Long idAnimanga) {
        return resenaRepository.findByIdAnimanga(idAnimanga);
    }

    public ResenasPorAnimangaResponse obtenerResenasConPromedio(Long idAnimanga) {
        List<Resena> resenas = resenaRepository.findByIdAnimanga(idAnimanga);
        return new ResenasPorAnimangaResponse(idAnimanga, resenas);
    }

    public String actualizar(Long id, Resena resenaActualizada, Long idUsuarioAuth) {
        Resena resenaExistente = resenaRepository.findById(id).orElse(null);
        if (resenaExistente == null) {
            return "Resena no encontrada";
        }

        if (resenaActualizada.getTitulo() != null) {
            resenaExistente.setTitulo(resenaActualizada.getTitulo());
        }
        if (resenaActualizada.getPuntuacion() != null) {
            if (resenaActualizada.getPuntuacion().compareTo(BigDecimal.ZERO) < 0 || resenaActualizada.getPuntuacion().compareTo(new BigDecimal("10.0")) > 0) {
                return "La puntuacion debe estar entre 0.0 y 10.0";
            }
            resenaExistente.setPuntuacion(resenaActualizada.getPuntuacion());
        }
        if (resenaActualizada.getComentario() != null) {
            resenaExistente.setComentario(resenaActualizada.getComentario());
        }

        resenaRepository.save(resenaExistente);
        auditar("Resena " + id + " actualizada por usuario " + idUsuarioAuth, "resena", idUsuarioAuth);
        return "Resena actualizada exitosamente";
    }

    public boolean eliminar(Long id, Long idUsuarioAuth) {
        Resena resena = resenaRepository.findById(id).orElse(null);
        if (resena == null) {
            return false;
        }
        resenaRepository.delete(resena);
        auditar("Resena " + id + " eliminada por usuario " + idUsuarioAuth, "resena", idUsuarioAuth);
        return true;
    }
}
