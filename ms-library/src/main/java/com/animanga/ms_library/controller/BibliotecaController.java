package com.animanga.ms_library.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

import com.animanga.ms_library.model.BibliotecaUsuario;
import com.animanga.ms_library.model.EstadoSeguimiento;
import com.animanga.ms_library.model.ProgresoAnime;
import com.animanga.ms_library.service.BibliotecaService;

@RestController
@RequestMapping("/api/biblioteca")
public class BibliotecaController {

    @Autowired
    private BibliotecaService service;

    @PostMapping
    public ResponseEntity<?> agregar(@RequestBody BibliotecaUsuario entrada, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        String respuesta = service.agregarABiblioteca(entrada, userId);
        if (respuesta.equals("Animanga agregado a la biblioteca exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        return ResponseEntity.badRequest().body(respuesta);
    }

    @GetMapping
    public List<BibliotecaUsuario> obtenerTodas(@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        return service.obtenerTodas(userId);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<BibliotecaUsuario> obtenerPorUsuario(
            @PathVariable Long idUsuario,
            @RequestParam(required = false) EstadoSeguimiento estado) {
        if (estado != null) {
            return service.obtenerBibliotecaPorUsuarioYEstado(idUsuario, estado);
        }
        return service.obtenerBibliotecaPorUsuario(idUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<BibliotecaUsuario> entrada = service.obtenerEntrada(id);
        if (entrada.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrada no encontrada");
        }
        return ResponseEntity.ok(entrada.get());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody EstadoSeguimiento estado, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        String resultado = service.actualizarEstadoSeguimiento(id, estado, userId);
        if (resultado.equals("Entrada de biblioteca no encontrada")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        boolean eliminado = service.eliminarEntrada(id, userId);
        if (eliminado) {
            return ResponseEntity.ok("Entrada eliminada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrada no encontrada");
    }

    @PostMapping("/{idBiblioteca}/progreso")
    public ResponseEntity<?> actualizarProgreso(
            @PathVariable Long idBiblioteca,
            @RequestBody Integer capituloActual,
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        String resultado = service.actualizarProgreso(idBiblioteca, capituloActual, userId);
        if (resultado.equals("Entrada de biblioteca no encontrada")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        if (resultado.contains("numero valido")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{idBiblioteca}/progreso")
    public ResponseEntity<?> obtenerProgreso(@PathVariable Long idBiblioteca) {
        Optional<BibliotecaUsuario> entrada = service.obtenerEntrada(idBiblioteca);
        if (entrada.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrada no encontrada");
        }
        List<ProgresoAnime> progreso = service.obtenerProgresoPorBiblioteca(idBiblioteca);
        return ResponseEntity.ok(progreso);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> manejarErrorLectura(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Estado de seguimiento invalido. Valores permitidos: FAVORITO, LEYENDO, COMPLETADO, RETRASADO, PLAN_A_VER");
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<String> manejarErrorTipoArgumento(TypeMismatchException ex) {
        return ResponseEntity.badRequest().body("Estado de seguimiento invalido. Valores permitidos: FAVORITO, LEYENDO, COMPLETADO, RETRASADO, PLAN_A_VER");
    }
}
