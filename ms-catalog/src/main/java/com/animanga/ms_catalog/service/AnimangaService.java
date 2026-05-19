package com.animanga.ms_catalog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_catalog.dto.AnimangaResumen;
import com.animanga.ms_catalog.dto.AuditRequest;
import com.animanga.ms_catalog.model.Animanga;
import com.animanga.ms_catalog.model.TipoAnimanga;
import com.animanga.ms_catalog.repository.AnimangaRepository;
import com.animanga.ms_catalog.repository.TipoAnimangaRepository;

@Service
public class AnimangaService {
    
    @Autowired
    private AnimangaRepository animangaRepository;

    @Autowired
    private TipoAnimangaRepository tipoAnimangaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private String validarEstadoSegunTipo(Animanga.EstadoEmision estado, TipoAnimanga tipo) {
        if (estado == null || tipo == null) return null;
        if ("Anime".equalsIgnoreCase(tipo.getNombre())) {
            if (estado == Animanga.EstadoEmision.EN_PAUSA || estado == Animanga.EstadoEmision.DISCONTINUADO) {
                return "El estado " + estado + " no es válido para Anime. Use EN_CURSO, FINALIZADO, PROXIMAMENTE o NO_ESPECIFICADO";
            }
        }
        return null;
    }

    private String validarEntidadProduccion(Long id, String tipo) {
        if (id == null) return null;
        try {
            String url = "http://ms-production/api/entidades/" + id + "/existe";
            Boolean existe = restTemplate.getForObject(url, Boolean.class);
            if (!existe) {
                return "El " + tipo + " con id " + id + " no existe";
            }
        } catch (Exception e) {
            return "Error al validar " + tipo + ": " + e.getMessage();
        }
        return null;
    }

    private void auditar(String accion, String tabla) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(null, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String guardar(Animanga animanga) {
        if (animanga.getTitulo() == null || animanga.getTitulo().trim().isEmpty()) {
            return "El título del Animanga es obligatorio";
        }
        if (animanga.getFechaEstreno() == null) {
            return "La fecha de estreno es obligatoria";
        }
        if (animanga.getTipoAnimanga() == null || animanga.getTipoAnimanga().getIdTipo() == null) {
            return "El tipo de Animanga es obligatorio";
        }

        TipoAnimanga tipoAnimanga = tipoAnimangaRepository.findById(animanga.getTipoAnimanga().getIdTipo()).orElse(null);
        if (tipoAnimanga == null) {
            return "El tipo de Animanga con id " + animanga.getTipoAnimanga().getIdTipo() + " no existe";
        }
        animanga.setTipoAnimanga(tipoAnimanga);

        String errorEstado = validarEstadoSegunTipo(animanga.getEstadoEmision(), tipoAnimanga);
        if (errorEstado != null) return errorEstado;
        
        if (animangaRepository.existsByTitulo(animanga.getTitulo())) {
            return "El Animanga '" + animanga.getTitulo() + "' ya existe";
        }

        String errorEstudio = validarEntidadProduccion(animanga.getIdEstudio(), "estudio");
        if (errorEstudio != null) return errorEstudio;

        String errorAutor = validarEntidadProduccion(animanga.getIdAutor(), "autor");
        if (errorAutor != null) return errorAutor;
        
        animangaRepository.save(animanga);
        auditar("Animanga '" + animanga.getTitulo() + "' creado", "animanga");
        return "Animanga guardado exitosamente";
    }

    public List<Animanga> obtenerTodos() {
        return animangaRepository.findAll();
    }

    public Optional<Animanga> obtenerPorId(Long id) {
        return animangaRepository.findById(id);
    }

    public List<Animanga> buscarPorTitulo(String titulo) {
        return animangaRepository.findByTitulo(titulo);
    }

    public List<Animanga> buscarPorTipo(String nombreTipo) {
        return animangaRepository.findByTipoAnimanga_Nombre(nombreTipo);
    }

    public String actualizar(Long id, Animanga animangaActualizado) {
        Animanga animangaExistente = animangaRepository.findById(id).orElse(null);
        if (animangaExistente == null) {
            return "Animanga no encontrado";
        }
        
        if (animangaActualizado.getTitulo() != null && 
            !animangaActualizado.getTitulo().equals(animangaExistente.getTitulo())) {
            if (animangaRepository.existsByTitulo(animangaActualizado.getTitulo())) {
                return "Error: El título de Animanga ya está en uso";
            }
            animangaExistente.setTitulo(animangaActualizado.getTitulo());
        }
        
        if (animangaActualizado.getDescripcion() != null) {
            animangaExistente.setDescripcion(animangaActualizado.getDescripcion());
        }
        if (animangaActualizado.getFechaEstreno() != null) {
            animangaExistente.setFechaEstreno(animangaActualizado.getFechaEstreno());
        }
        if (animangaActualizado.getEstadoEmision() != null) {
            TipoAnimanga tipoParaValidar = animangaExistente.getTipoAnimanga();
            if (animangaActualizado.getTipoAnimanga() != null && animangaActualizado.getTipoAnimanga().getIdTipo() != null) {
                tipoParaValidar = tipoAnimangaRepository.findById(animangaActualizado.getTipoAnimanga().getIdTipo()).orElse(animangaExistente.getTipoAnimanga());
            }
            String errorEstado = validarEstadoSegunTipo(animangaActualizado.getEstadoEmision(), tipoParaValidar);
            if (errorEstado != null) return errorEstado;
            animangaExistente.setEstadoEmision(animangaActualizado.getEstadoEmision());
        }
        if (animangaActualizado.getTipoAnimanga() != null && animangaActualizado.getTipoAnimanga().getIdTipo() != null) {
            TipoAnimanga tipoAnimanga = tipoAnimangaRepository.findById(animangaActualizado.getTipoAnimanga().getIdTipo()).orElse(null);
            if (tipoAnimanga == null) {
                return "El tipo de Animanga con id " + animangaActualizado.getTipoAnimanga().getIdTipo() + " no existe";
            }
            animangaExistente.setTipoAnimanga(tipoAnimanga);
        }
        if (animangaActualizado.getIdEstudio() != null) {
            String errorEstudio = validarEntidadProduccion(animangaActualizado.getIdEstudio(), "estudio");
            if (errorEstudio != null) return errorEstudio;
            animangaExistente.setIdEstudio(animangaActualizado.getIdEstudio());
        }
        if (animangaActualizado.getIdAutor() != null) {
            String errorAutor = validarEntidadProduccion(animangaActualizado.getIdAutor(), "autor");
            if (errorAutor != null) return errorAutor;
            animangaExistente.setIdAutor(animangaActualizado.getIdAutor());
        }
        
        animangaRepository.save(animangaExistente);
        auditar("Animanga '" + animangaExistente.getTitulo() + "' actualizado", "animanga");
        return "Animanga actualizado exitosamente";
    }

    public List<AnimangaResumen> listarResumen() {
        List<Animanga> todos = animangaRepository.findAll();
        List<AnimangaResumen> resumenes = new ArrayList<>();
        for (Animanga a : todos) {
            List<String> generos= new ArrayList<>();
            a.getGeneros().forEach(g -> generos.add(g.getNombre()));
            resumenes.add(new AnimangaResumen(
                a.getIdAnimanga(),
                a.getTitulo(),
                a.getTipoAnimanga().getNombre(),
                a.getEstadoEmision().name(),
                generos
           
            ));
        }
        return resumenes;
    }

    public boolean eliminar(Long id) {
        Animanga animanga = animangaRepository.findById(id).orElse(null);
        if (animanga == null) {
            return false;
        }
        animangaRepository.delete(animanga);
        auditar("Animanga '" + animanga.getTitulo() + "' eliminado", "animanga");
        return true;
    }
}