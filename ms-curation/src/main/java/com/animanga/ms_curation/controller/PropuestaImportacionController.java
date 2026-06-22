package com.animanga.ms_curation.controller;

import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_curation.model.PropuestaImportacion;
import com.animanga.ms_curation.service.PropuestaImportacionService;

@RestController
@RequestMapping("/api/propuestas")
public class PropuestaImportacionController {

    @Autowired
    private PropuestaImportacionService propuestaService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PropuestaImportacion propuesta) {
        if (propuesta.getIdUsuarioPropone() == null) {
            return ResponseEntity.badRequest().body("El ID del usuario que propone es obligatorio");
        }
        if (propuesta.getDatosJson() == null || propuesta.getDatosJson().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Los datos JSON son obligatorios");
        }

        String respuesta = propuestaService.guardar(propuesta);
        if (respuesta.equals("Propuesta creada exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @GetMapping
    public List<PropuestaImportacion> obtenerTodas(@RequestParam(required = false) String estado) {
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
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        String comentario = body.get("comentarioRechazo");

        if (estado == null || (!estado.equals("APROBADO") && !estado.equals("RECHAZADO"))) {
            return ResponseEntity.badRequest().body("Estado invalido. Use APROBADO o RECHAZADO");
        }

        String resultado = propuestaService.actualizarEstado(id, estado, comentario);
        if (resultado.equals("Propuesta no encontrada")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean eliminado = propuestaService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Propuesta eliminada exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Propuesta no encontrada");
    }
}
