package com.animanga.ms_curation.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_curation.model.HistorialCuracion;
import com.animanga.ms_curation.service.HistorialCuracionService;

@RestController
@RequestMapping("/api/historial-curacion")
public class HistorialCuracionController {

    @Autowired
    private HistorialCuracionService historialService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody HistorialCuracion historial, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {

        if (historial.getPropuesta() == null || historial.getPropuesta().getIdPropuesta() == null) {
            return ResponseEntity.badRequest().body("La propuesta es obligatoria");
        }
        if (historial.getDecision() == null || historial.getDecision().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("La decision es obligatoria");
        }

        String respuesta = historialService.guardar(historial, userId);
        if (respuesta.equals("Historial guardado exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        if (respuesta.contains("no existe")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @GetMapping
    public List<HistorialCuracion> obtenerTodos() {
        return historialService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<HistorialCuracion> historial = historialService.obtenerPorId(id);
        if (historial.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Historial no encontrado");
        }
        return ResponseEntity.ok(historial.get());
    }

    @GetMapping("/propuesta/{idPropuesta}")
    public List<HistorialCuracion> obtenerPorPropuesta(@PathVariable Long idPropuesta) {
        return historialService.obtenerPorPropuesta(idPropuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        boolean eliminado = historialService.eliminar(id, userId);
        if (eliminado) {
            return ResponseEntity.ok("Historial eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Historial no encontrado");
    }
}
