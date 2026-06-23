package com.animanga.ms_perfil.controller;

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

import com.animanga.ms_perfil.model.Perfil;
import com.animanga.ms_perfil.service.PerfilService;

@RestController
@RequestMapping("/api/perfiles")
public class PerfilController {

    @Autowired
    private PerfilService perfilService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Perfil perfil, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {

        String respuesta = perfilService.guardar(perfil, userId);

        if (respuesta.equals("Perfil guardado exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
    }

    @GetMapping
    public List<Perfil> obtenerTodos() {
        return perfilService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Perfil> perfil = perfilService.obtenerPorId(id);
        if (perfil.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil no encontrado");
        }
        return ResponseEntity.ok(perfil.get());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long idUsuario) {
        Optional<Perfil> perfil = perfilService.obtenerPorUsuario(idUsuario);
        if (perfil.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil no encontrado para el usuario");
        }
        return ResponseEntity.ok(perfil.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Perfil perfil,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        String resultado = perfilService.actualizar(id, perfil, userId);

        if (resultado.equals("Perfil no encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        if (resultado.equals("Perfil actualizado exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        boolean eliminado = perfilService.eliminar(id, userId);
        if (eliminado) {
            return ResponseEntity.ok("Perfil eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil no encontrado");
        }
    }
}
