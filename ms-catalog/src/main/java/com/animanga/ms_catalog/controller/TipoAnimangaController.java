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

import com.animanga.ms_catalog.model.TipoAnimanga;
import com.animanga.ms_catalog.service.TipoAnimangaService;

@RestController
@RequestMapping("/api/tipos")
public class TipoAnimangaController {
    
    @Autowired
    private TipoAnimangaService tipoAnimangaService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody TipoAnimanga tipo, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        if (tipo == null || tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del TipoAnimanga es obligatorio");
        }
        
        String respuesta = tipoAnimangaService.guardar(tipo, userId);
        
        if (respuesta.equals("TipoAnimanga guardado exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
    }
    
    @GetMapping
    public List<TipoAnimanga> obtenerTodos() {
        return tipoAnimangaService.obtenerTodos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        Optional<TipoAnimanga> tipo = tipoAnimangaService.obtenerPorId(id);
        if (tipo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TipoAnimanga no encontrado");
        }
        return ResponseEntity.ok(tipo.get());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody TipoAnimanga tipo, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        if (tipo == null || tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del TipoAnimanga es obligatorio");
        }
        
        String resultado = tipoAnimangaService.actualizar(id, tipo, userId);
        
        if (resultado.equals("TipoAnimanga no encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        if (resultado.equals("TipoAnimanga actualizado exitosamente")) {
            return ResponseEntity.ok().body(resultado);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id, @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        boolean eliminado = tipoAnimangaService.eliminar(id, userId);
        if (eliminado) {
            return ResponseEntity.ok("TipoAnimanga eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TipoAnimanga no encontrado");
        }
    }
}