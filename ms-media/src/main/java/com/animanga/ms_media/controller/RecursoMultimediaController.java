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
import org.springframework.web.bind.annotation.RequestHeader;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_media.model.RecursoMultimedia;
import com.animanga.ms_media.service.RecursoMultimediaService;

@RestController
@RequestMapping("/api/recursos")
public class RecursoMultimediaController {

    @Autowired
    private RecursoMultimediaService recursoService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody RecursoMultimedia recurso, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        if (recurso.getIdAnimanga() == null) {
            return ResponseEntity.badRequest().body("El ID del animanga es obligatorio");
        }
        if (recurso.getTipoRecurso() == null || recurso.getTipoRecurso().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El tipo de recurso es obligatorio");
        }
        if (recurso.getUrlRecurso() == null || recurso.getUrlRecurso().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("La URL del recurso es obligatoria");
        }

        String respuesta = recursoService.guardar(recurso, userId);
        if (respuesta.equals("Recurso multimedia guardado exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        if (respuesta.contains("no existe")) {
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @GetMapping
    public List<RecursoMultimedia> obtenerTodos() {
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

    @GetMapping("/animanga/{idAnimanga}")
    public ResponseEntity<?> obtenerPorAnimanga(@PathVariable Long idAnimanga) {
        List<RecursoMultimedia> recursos = recursoService.obtenerPorAnimanga(idAnimanga);
        if (recursos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontraron recursos para el animanga " + idAnimanga);
        }
        return ResponseEntity.ok(recursos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody RecursoMultimedia recurso, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        if (recurso == null) {
            return ResponseEntity.badRequest().body("El cuerpo de la peticion es obligatorio");
        }

        String resultado = recursoService.actualizar(id, recurso, userId);
        if (resultado.equals("Recurso no encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        if (resultado.contains("no existe")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        boolean eliminado = recursoService.eliminar(id, userId);
        if (eliminado) {
            return ResponseEntity.ok("Recurso eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recurso no encontrado");
    }
}
