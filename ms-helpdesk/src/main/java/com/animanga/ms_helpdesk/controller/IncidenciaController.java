package com.animanga.ms_helpdesk.controller;

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

import com.animanga.ms_helpdesk.model.EstadoIncidencia;
import com.animanga.ms_helpdesk.model.Incidencia;
import com.animanga.ms_helpdesk.service.IncidenciaService;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaController {

    @Autowired
    private IncidenciaService service;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Incidencia incidencia, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        String respuesta = service.crear(incidencia, userId);
        if (respuesta.equals("Incidencia creada exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        return ResponseEntity.badRequest().body(respuesta);
    }

    @GetMapping
    public List<Incidencia> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Incidencia> incidencia = service.obtenerPorId(id);
        if (incidencia.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incidencia no encontrada");
        }
        return ResponseEntity.ok(incidencia.get());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Incidencia> buscarPorUsuario(@PathVariable Long idUsuario) {
        return service.buscarPorUsuario(idUsuario);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody String estado, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        EstadoIncidencia estadoEnum;
        try {
            estadoEnum = EstadoIncidencia.valueOf(estado.toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Estado invalido. Use ABIERTO, EN_PROCESO, RESUELTO o CERRADO");
        }
        String resultado = service.actualizarEstado(id, estadoEnum, userId);
        if (resultado.equals("Incidencia no encontrada")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        boolean eliminado = service.eliminar(id, userId);
        if (eliminado) {
            return ResponseEntity.ok("Incidencia eliminada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incidencia no encontrada");
    }
}
