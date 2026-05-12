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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_production.model.TipoEntidad;
import com.animanga.ms_production.service.TipoEntidadService;

@RestController
@RequestMapping("/api/tipos-entidad")
public class TipoEntidadController {

    @Autowired
    private TipoEntidadService tipoEntidadService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody TipoEntidad tipoEntidad) {
        if (tipoEntidad == null || tipoEntidad.getNombre() == null || tipoEntidad.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del tipo de entidad es obligatorio");
        }

        String respuesta = tipoEntidadService.guardar(tipoEntidad);

        if (respuesta.equals("Tipo de entidad guardado exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
    }
    
    @GetMapping
    public List<TipoEntidad> obtenerTodos() {
        return tipoEntidadService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        Optional<TipoEntidad> tipo = tipoEntidadService.obtenerPorId(id);
        if (tipo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de entidad no encontrado");
        }
        return ResponseEntity.ok(tipo.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody TipoEntidad tipoEntidad) {
        if (tipoEntidad == null || tipoEntidad.getNombre() == null || tipoEntidad.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del tipo de entidad es obligatorio");
        }

        String resultado = tipoEntidadService.actualizar(id, tipoEntidad);

        if (resultado.equals("Tipo de entidad no encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        if (resultado.equals("Tipo de entidad actualizado exitosamente")) {
            return ResponseEntity.ok().body(resultado);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        boolean eliminado = tipoEntidadService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Tipo de entidad eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tipo de entidad no encontrado");
        }
    }
}