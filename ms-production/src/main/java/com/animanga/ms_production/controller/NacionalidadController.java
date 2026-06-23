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
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_production.model.Nacionalidad;
import com.animanga.ms_production.service.NacionalidadService;

@RestController
@RequestMapping("/api/nacionalidades")
public class NacionalidadController {

    @Autowired
    private NacionalidadService nacionalidadService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Nacionalidad nacionalidad, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        if (nacionalidad == null || nacionalidad.getPais() == null || nacionalidad.getPais().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El país es obligatorio");
        }

        String respuesta = nacionalidadService.guardar(nacionalidad, userId);

        if (respuesta.equals("Nacionalidad guardada exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
    }

    @GetMapping
    public List<Nacionalidad> obtenerTodos() {
        return nacionalidadService.obtenerTodos();
    }
    @GetMapping("/existe/{pais}")
    public ResponseEntity<?> existePorPais(@PathVariable String pais) {
        boolean existe = nacionalidadService.existePorPais(pais);
        return ResponseEntity.ok(existe);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        Optional<Nacionalidad> nacionalidad = nacionalidadService.obtenerPorId(id);
        if (nacionalidad.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nacionalidad no encontrada");
        }
        return ResponseEntity.ok(nacionalidad.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Nacionalidad nacionalidad, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        if (nacionalidad == null || nacionalidad.getPais() == null || nacionalidad.getPais().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El país es obligatorio");
        }

        String resultado = nacionalidadService.actualizar(id, nacionalidad, userId);

        if (resultado.equals("Nacionalidad no encontrada")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        if (resultado.equals("Nacionalidad actualizada exitosamente")) {
            return ResponseEntity.ok().body(resultado);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        boolean eliminado = nacionalidadService.eliminar(id, userId);
        if (eliminado) {
            return ResponseEntity.ok("Nacionalidad eliminada exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nacionalidad no encontrada");
        }
    }
}