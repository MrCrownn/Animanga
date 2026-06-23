package com.animanga.ms_curation.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

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

import com.animanga.ms_curation.model.EstadoCuracion;
import com.animanga.ms_curation.model.PropuestaImportacion;
import com.animanga.ms_curation.service.PropuestaImportacionService;

@RestController
@RequestMapping("/api/propuestas")
public class PropuestaImportacionController {

    @Autowired
    private PropuestaImportacionService propuestaService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        Object datosJson = body.get("datosJson");
        if (datosJson == null) {
            return ResponseEntity.badRequest().body("Los datos JSON son obligatorios");
        }

        try {
            PropuestaImportacion propuesta = new PropuestaImportacion();
            propuesta.setDatosJson(new ObjectMapper().writeValueAsString(datosJson));
            String respuesta = propuestaService.guardar(propuesta, userId);
            if (respuesta.equals("Propuesta creada exitosamente")) {
                return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar datosJson: " + e.getMessage());
        }
    }

    @GetMapping
    public List<PropuestaImportacion> obtenerTodas(@RequestParam(required = false) EstadoCuracion estado) {
        if (estado != null) {
            return propuestaService.obtenerPorEstado(estado);
        }
        return propuestaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<PropuestaImportacion> propuesta = propuestaService.obtenerPorId(id);
        if (propuesta.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propuesta no encontrada");
        }
        return ResponseEntity.ok(propuesta.get());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<PropuestaImportacion> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return propuestaService.obtenerPorIdUsuario(idUsuario);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        String estadoStr = body.get("estado");
        String comentario = body.get("comentarioRechazo");

        EstadoCuracion estado = null;
        if ("APROBADO".equals(estadoStr)) {
            estado = EstadoCuracion.APROBADO;
        } else if ("RECHAZADO".equals(estadoStr)) {
            estado = EstadoCuracion.RECHAZADO;
        } else {
            return ResponseEntity.badRequest().body("Estado invalido. Use APROBADO o RECHAZADO");
        }
        String resultado = propuestaService.actualizarEstado(id, estado, comentario, userId);
        if (resultado.equals("Propuesta no encontrada")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        boolean eliminado = propuestaService.eliminar(id, userId);
        if (eliminado) {
            return ResponseEntity.ok("Propuesta eliminada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propuesta no encontrada");
    }
}
