package cl.animanga.ms_auth.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.animanga.ms_auth.model.Rol;
import cl.animanga.ms_auth.service.RolService;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    
    @Autowired
    private RolService rolService;
    
    // Crear nuevo rol
    @PostMapping
    public ResponseEntity<Rol> crear(@RequestBody Rol rol) {
        Rol guardado = rolService.guardar(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
    
    // Listar todos los roles
    @GetMapping
    public List<Rol> obtenerTodos() {
        return rolService.obtenerTodos();
    }
    
    // Buscar rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) { // Cambiado a <?>
        Optional<Rol> rol = rolService.obtenerPorId(id);
        if(rol.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado");
        }
        return ResponseEntity.ok(rol.get());
    }
    // Buscar rol por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Rol> obtenerPorNombre(@PathVariable String nombre) {
        Optional<Rol> rol = rolService.obtenerPorNombre(nombre);
        return rol.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    // Eliminar rol
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        boolean eliminado = rolService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build()
                         : ResponseEntity.notFound().build();
    }
}