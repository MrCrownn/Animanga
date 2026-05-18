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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_catalog.dto.AnimangaResumen;
import com.animanga.ms_catalog.model.Animanga;
import com.animanga.ms_catalog.service.AnimangaService;

@RestController
@RequestMapping("/api/animanga")
public class AnimangaController {
    
    @Autowired
    private AnimangaService animangaService;
    
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Animanga animanga) {
        if (animanga == null || animanga.getTitulo() == null || animanga.getTitulo().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El título del Animanga es obligatorio");
        }
        if (animanga.getFechaEstreno() == null) {
            return ResponseEntity.badRequest().body("La fecha de estreno es obligatoria");
        }
        if (animanga.getTipoAnimanga() == null || animanga.getTipoAnimanga().getIdTipo() == null) {
            return ResponseEntity.badRequest().body("El tipo de Animanga es obligatorio");
        }
        
        String respuesta = animangaService.guardar(animanga);
        
        if (respuesta.equals("Animanga guardado exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
    }
    
    @GetMapping
    public List<Animanga> obtenerTodos() {
        return animangaService.obtenerTodos();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Animanga> animanga = animangaService.obtenerPorId(id);
        if (animanga.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animanga no encontrado");
        }
        return ResponseEntity.ok(animanga.get());
    }
    
    @GetMapping("/resumen")
    public List<AnimangaResumen> listarResumen() {
        return animangaService.listarResumen();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Animanga>> buscarPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(animangaService.buscarPorTitulo(titulo));
    }
    
    @GetMapping("/tipo")
    public ResponseEntity<List<Animanga>> buscarPorTipo(@RequestParam String nombre) {
        return ResponseEntity.ok(animangaService.buscarPorTipo(nombre));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Animanga animanga) {
        if (animanga == null) {
            return ResponseEntity.badRequest().body("El cuerpo de la petición es obligatorio");
        }
        
        String resultado = animangaService.actualizar(id, animanga);
        
        if (resultado.equals("Animanga no encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }
        if (resultado.equals("Animanga actualizado exitosamente")) {
            return ResponseEntity.ok().body(resultado);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean eliminado = animangaService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Animanga eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Animanga no encontrado");
        }
    }
}