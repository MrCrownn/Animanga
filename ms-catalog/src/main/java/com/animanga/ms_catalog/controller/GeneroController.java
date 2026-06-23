package com.animanga.ms_catalog.controller;

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

import com.animanga.ms_catalog.model.Genero;
import com.animanga.ms_catalog.service.GeneroService;

@RestController
@RequestMapping("/api/generos")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Genero genero, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        if (genero == null || genero.getNombre() == null || genero.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del genero es obligatorio");
        }

        String respuesta = generoService.guardar(genero, userId);

        if (respuesta.equals("Genero guardado exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
    }

    @GetMapping
    public List<Genero> obtenerTodos() {
        return generoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        Optional<Genero> genero = generoService.obtenerPorId(id);
        if (genero.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Genero no encontrado");
        }
        return ResponseEntity.ok(genero.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Genero genero, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        if (genero == null || genero.getNombre() == null || genero.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del genero es obligatorio");
        }

        String resultado = generoService.actualizar(id, genero, userId);

        if (resultado.equals("Genero no encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        if (resultado.equals("Genero actualizado exitosamente")) {
            return ResponseEntity.ok().body(resultado);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        boolean eliminado = generoService.eliminar(id, userId);
        if (eliminado) {
            return ResponseEntity.ok("Genero eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Genero no encontrado");
        }
    }
}
