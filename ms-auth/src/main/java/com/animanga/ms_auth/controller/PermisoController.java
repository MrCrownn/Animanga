package com.animanga.ms_auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_auth.model.Permiso;
import com.animanga.ms_auth.service.PermisoService;

@RestController
@RequestMapping("/api/permisos")
public class PermisoController {
 @Autowired
    private PermisoService permisoService;

   
    @PostMapping
    public ResponseEntity<Permiso> guardar(@RequestBody Permiso permiso) {
        return ResponseEntity.ok(permisoService.guardarPermiso(permiso));
    }

    @GetMapping
    public ResponseEntity<List<Permiso>> listar() {
        return ResponseEntity.ok(permisoService.listarTodos());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        Permiso permiso = permisoService.buscarPorId(id);
        if (permiso == null) {
            return ResponseEntity.status(404).body("Permiso no encontrado");
        }
        return ResponseEntity.ok(permiso);
    }

    // Buscar por Acción (útil para validaciones rápidas)
    @GetMapping("/accion/{accion}")
    public ResponseEntity<Permiso> obtenerPorAccion(@PathVariable String accion) {
        Permiso permiso = permisoService.buscarPorAccion(accion);
        return permiso != null ? ResponseEntity.ok(permiso) : ResponseEntity.notFound().build();
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        Boolean respuesta=permisoService.eliminarPermiso(id);
        if (respuesta) {
            return ResponseEntity.ok("Permiso eliminado exitosamente");
        } else {
            return ResponseEntity.status(404).body("Permiso no encontrado ");
        }
        
    }
}
