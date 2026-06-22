package com.animanga.ms_media.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_media.model.RecursoMultimedia;
import com.animanga.ms_media.service.RecursoMultimediaService;

@RestController
@RequestMapping("/api/recursos")
public class RecursoMultimediaController {

    @Autowired
    private RecursoMultimediaService recursoService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody RecursoMultimedia recurso) {
        if (recurso.getIdAnimanga() == null) {
            return ResponseEntity.badRequest().body("El ID del animanga es obligatorio");
        }
        if (recurso.getTipoRecurso() == null || recurso.getTipoRecurso().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El tipo de recurso es obligatorio");
        }
        if (recurso.getUrlRecurso() == null || recurso.getUrlRecurso().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("La URL del recurso es obligatoria");
        }

        String respuesta = recursoService.guardar(recurso);
        if (respuesta.equals("Recurso multimedia guardado exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @GetMapping
    public List<RecursoMultimedia> obtenerTodos(@RequestParam(required = false) Long idAnimanga,
                                                @RequestParam(required = false) String tipo) {
        if (idAnimanga != null && tipo != null) {
            return recursoService.obtenerPorAnimangaYTipo(idAnimanga, tipo);
        }
        if (idAnimanga != null) {
            return recursoService.obtenerPorAnimanga(idAnimanga);
        }
        return recursoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<RecursoMultimedia> recurso = recursoService.obtenerPorId(id);
        if (recurso.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso no encontrado");
        }
        return ResponseEntity.ok(recurso.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody RecursoMultimedia recurso) {
        if (recurso == null) {
            return ResponseEntity.badRequest().body("El cuerpo de la peticion es obligatorio");
        }

        String resultado = recursoService.actualizar(id, recurso);
        if (resultado.equals("Recurso no encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean eliminado = recursoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Recurso eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso no encontrado");
    }
}
