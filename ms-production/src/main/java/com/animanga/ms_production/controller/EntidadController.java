package com.animanga.ms_production.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_production.model.Entidad;
import com.animanga.ms_production.service.EntidadService;

@RestController
@RequestMapping("/api/entidades")
public class EntidadController {

    @Autowired
    private EntidadService entidadService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Entidad entidad, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        if (entidad == null || entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre de la entidad es obligatorio");
        }
        if (entidad.getTipoEntidad() == null || entidad.getTipoEntidad().getIdTipo() == null) {
            return ResponseEntity.badRequest().body("El tipo de entidad es obligatorio");
        }

        String respuesta = entidadService.guardar(entidad, userId);

        if (respuesta.equals("Entidad guardada exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
    }
    
    @GetMapping
    public List<Entidad> obtenerTodos() {
        return entidadService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        Optional<Entidad> entidad = entidadService.obtenerPorId(id);
        if (entidad.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entidad no encontrada");
        }
        return ResponseEntity.ok(entidad.get());
    }

    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existe(@PathVariable Integer id) {
        return ResponseEntity.ok(entidadService.existePorId(id));
    }

    @GetMapping("/tipo")
    public ResponseEntity<List<Entidad>> buscarPorTipo(@RequestParam String nombre) {
        return ResponseEntity.ok(entidadService.buscarPorTipo(nombre));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Entidad entidad, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        if (entidad == null) {
            return ResponseEntity.badRequest().body("El cuerpo de la petición es obligatorio");
        }

        String resultado = entidadService.actualizar(id, entidad, userId);

        if (resultado.equals("Entidad no encontrada")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        if (resultado.equals("Entidad actualizada exitosamente")) {
            return ResponseEntity.ok().body(resultado);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        boolean eliminado = entidadService.eliminar(id, userId);
        if (eliminado) {
            return ResponseEntity.ok("Entidad eliminada exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entidad no encontrada");
        }
    }
}