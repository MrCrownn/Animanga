package com.animanga.ms_library.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_library.dto.AuditRequest;
import com.animanga.ms_library.model.BibliotecaUsuario;
import com.animanga.ms_library.model.EstadoSeguimiento;
import com.animanga.ms_library.model.ProgresoAnime;
import com.animanga.ms_library.repository.BibliotecaUsuarioRepository;
import com.animanga.ms_library.repository.ProgresoAnimeRepository;

@Service
public class BibliotecaService {

    @Autowired
    private BibliotecaUsuarioRepository bibliotecaRepository;

    @Autowired
    private ProgresoAnimeRepository progresoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private void auditar(Long idUsuarioAuth, String accion, String tabla) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(idUsuarioAuth, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String agregarABiblioteca(BibliotecaUsuario entrada, Long idUsuarioAuth) {
        entrada.setIdUsuario(idUsuarioAuth);
        if (entrada.getIdAnimanga() == null) {
            return "El ID del animanga es obligatorio";
        }
        if (entrada.getEstadoSeguimiento() == null) {
            return "El estado de seguimiento es obligatorio";
        }

        Optional<BibliotecaUsuario> existente = bibliotecaRepository
                .findByIdUsuarioAndIdAnimanga(entrada.getIdUsuario(), entrada.getIdAnimanga());
        if (existente.isPresent()) {
            return "El animanga ya esta en la biblioteca del usuario";
        }

        entrada.setFechaAgregado(LocalDateTime.now());
        bibliotecaRepository.save(entrada);
        auditar(idUsuarioAuth, "Animanga " + entrada.getIdAnimanga() + " agregado a biblioteca de usuario " + idUsuarioAuth, "biblioteca_usuario");
        return "Animanga agregado a la biblioteca exitosamente";
    }

    public List<BibliotecaUsuario> obtenerTodas(Long idUsuarioAuth) {
        return bibliotecaRepository.findByIdUsuario(idUsuarioAuth);
    }

    public List<BibliotecaUsuario> obtenerBibliotecaPorUsuario(Long idUsuario) {
        return bibliotecaRepository.findByIdUsuario(idUsuario);
    }

    public List<BibliotecaUsuario> obtenerBibliotecaPorUsuarioYEstado(Long idUsuario, EstadoSeguimiento estado) {
        return bibliotecaRepository.findByIdUsuarioAndEstadoSeguimiento(idUsuario, estado);
    }

    public Optional<BibliotecaUsuario> obtenerEntrada(Long id) {
        return bibliotecaRepository.findById(id);
    }

    public String actualizarEstadoSeguimiento(Long id, EstadoSeguimiento estado, Long idUsuarioAuth) {
        BibliotecaUsuario entrada = bibliotecaRepository.findById(id).orElse(null);
        if (entrada == null) {
            return "Entrada de biblioteca no encontrada";
        }
        entrada.setEstadoSeguimiento(estado);
        bibliotecaRepository.save(entrada);
        auditar(idUsuarioAuth, "Entrada biblioteca " + id + " actualizada a " + estado.name(), "biblioteca_usuario");
        return "Estado de seguimiento actualizado exitosamente";
    }

    public boolean eliminarEntrada(Long id, Long idUsuarioAuth) {
        BibliotecaUsuario entrada = bibliotecaRepository.findById(id).orElse(null);
        if (entrada == null) {
            return false;
        }
        bibliotecaRepository.delete(entrada);
        auditar(idUsuarioAuth, "Entrada biblioteca " + id + " eliminada", "biblioteca_usuario");
        return true;
    }

    public String actualizarProgreso(Long idBiblioteca, Integer capituloActual, Long idUsuarioAuth) {
        BibliotecaUsuario entrada = bibliotecaRepository.findById(idBiblioteca).orElse(null);
        if (entrada == null) {
            return "Entrada de biblioteca no encontrada";
        }
        if (capituloActual == null || capituloActual < 0) {
            return "El capitulo actual debe ser un numero valido";
        }

        Optional<ProgresoAnime> existente = progresoRepository
                .findTopByIdBibliotecaOrderByFechaActualizacionDesc(idBiblioteca);
        ProgresoAnime progreso = existente.orElse(new ProgresoAnime());
        progreso.setIdBiblioteca(idBiblioteca);
        progreso.setCapituloActual(capituloActual);
        progreso.setFechaActualizacion(LocalDateTime.now());

        if (existente.isPresent()) {
            progreso.setIdProgreso(existente.get().getIdProgreso());
        }
        progresoRepository.save(progreso);
        auditar(idUsuarioAuth, "Progreso actualizado a capitulo " + capituloActual + " para biblioteca " + idBiblioteca, "progreso_anime");
        return "Progreso actualizado exitosamente";
    }

    public List<ProgresoAnime> obtenerProgresoPorBiblioteca(Long idBiblioteca) {
        return progresoRepository.findByIdBiblioteca(idBiblioteca);
    }
}
