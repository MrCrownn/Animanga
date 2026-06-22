package com.animanga.ms_social.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_social.model.Resena;
import com.animanga.ms_social.service.ResenaService;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Resena resena) {
        if (resena.getIdUsuario() == null) {
            return ResponseEntity.badRequest().body("El ID del usuario es obligatorio");
        }
        if (resena.getIdAnimanga() == null) {
            return ResponseEntity.badRequest().body("El ID del animanga es obligatorio");
        }
        if (resena.getTitulo() == null || resena.getTitulo().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El titulo de la resena es obligatorio");
        }
        if (resena.getPuntuacion() == null || resena.getPuntuacion() < 0.0 || resena.getPuntuacion() > 10.0) {
            return ResponseEntity.badRequest().body("La puntuacion debe estar entre 0.0 y 10.0");
        }

        String respuesta = resenaService.guardar(resena);
        if (respuesta.equals("Resena guardada exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @GetMapping
    public List<Resena> obtenerTodas() {
        return resenaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Resena> resena = resenaService.obtenerPorId(id);
        if (resena.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resena no encontrada");
        }
        return ResponseEntity.ok(resena.get());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Resena> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return resenaService.obtenerPorUsuario(idUsuario);
    }

    @GetMapping("/animanga/{idAnimanga}")
    public List<Resena> obtenerPorAnimanga(@PathVariable Long idAnimanga) {
        return resenaService.obtenerPorAnimanga(idAnimanga);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Resena resena) {
        if (resena == null) {
            return ResponseEntity.badRequest().body("El cuerpo de la peticion es obligatorio");
        }

        String resultado = resenaService.actualizar(id, resena);
        if (resultado.equals("Resena no encontrada")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        if (resultado.contains("puntuacion")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean eliminado = resenaService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Resena eliminada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resena no encontrada");
    }
}
