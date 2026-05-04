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
    
    
    @PostMapping("/{idRol}/permisos/{idPermiso}")
    public ResponseEntity<String> asignarPermiso(@PathVariable Integer idRol, @PathVariable Integer idPermiso) {
        String respuesta = rolService.asignarPermiso(idRol, idPermiso);
        if (respuesta.equals("Permiso Asignado")) {
            return ResponseEntity.ok(respuesta);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

   @PostMapping
    public ResponseEntity<?> crear(@RequestBody Rol rol) {
    // Validación de entrada
    if (rol == null || rol.getNombre() == null || rol.getNombre().trim().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre del rol es obligatorio");
    }
    
    // Aquí recibes el String del servicio
    String respuesta = rolService.guardar(rol);
    
    if (respuesta.equals("Rol guardado exitosamente")) {
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    } else {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }
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
    public ResponseEntity<?> obtenerPorNombre(@PathVariable String nombre) {
        Rol rol = rolService.obtenerPorNombre(nombre).orElse(null);
        if(rol == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado");
        }
        return ResponseEntity.ok(rol);
    }
    
    // Eliminar rol
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        boolean eliminado = rolService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Rol eliminado exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rol no encontrado");
        }
    }
    @DeleteMapping("/{idRol}/permisos/{idPermiso}")
    public ResponseEntity<String> removerPermiso(@PathVariable Integer idRol, @PathVariable Integer idPermiso) {
        String respuesta = rolService.removerPermiso(idRol, idPermiso);
        if (respuesta.equals("Permiso Removido")) 
            return ResponseEntity.ok(respuesta);
        else 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        
    }

}