package com.animanga.ms_auth.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_auth.dto.PasswordRequest;
import com.animanga.ms_auth.dto.UsuarioResponse;
import com.animanga.ms_auth.model.Rol;
import com.animanga.ms_auth.model.Usuario;
import com.animanga.ms_auth.service.JwtService;
import com.animanga.ms_auth.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/registro")
    public ResponseEntity <?>  registrar(@RequestBody Usuario usuario){
        String respuesta= this.usuarioService.registrarUsuario(usuario);
        if(respuesta.equals("Usuario registrado exitosamente")){
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
    
    else{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
    }
   @GetMapping()
   public List <Usuario> obtenerTodos(){
    return usuarioService.obtenerTodos();
   } 
   @GetMapping("/{id}")
   public ResponseEntity <?> obtenerUsuario(@PathVariable Long id){
        Usuario usuario= usuarioService.obtenerUsuario(id);
        if (usuario == null){
            return ResponseEntity.status(404)
                .body("Usuario no encontrado con id: " + id);
        }
        return ResponseEntity.ok(usuario);
   }
    @GetMapping("/{id}/info")
    public ResponseEntity<?> obtenerUsuarioInfo(@PathVariable Long id) {
        java.util.Optional<UsuarioResponse> usuario = usuarioService.obtenerUsuarioInfo(id);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(404).body("Usuario no encontrado con id: " + id);
        }
        return ResponseEntity.ok(usuario.get());
    }

    @GetMapping("/{id}/status")
    public ResponseEntity <String> obtenerEstado(@PathVariable Long id){
    String estado= usuarioService.obtenerEstado(id);

    if ("INEXISTENTE".equals(estado)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(estado);
    }
    return ResponseEntity.ok(estado);
   }
   @PutMapping("/{id}")
   public ResponseEntity <?> actualizaUsuario(@PathVariable Long id, @RequestBody Usuario usuario){
    String resultado= usuarioService.actualizaUsuario(id, usuario);
    if(resultado.equals("Usuario no encontrado")){
        return ResponseEntity.status(404).body(resultado);
    }
    if(resultado.equals("Usuario actualizado exitosamente")){
        return ResponseEntity.ok().body(resultado);
    }
    if(resultado.equals("Error: El nombre de usuario ocupado") || 
        resultado.equals("Error: Correo electrónico ocupado")){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(resultado);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
    
   }
   @PutMapping("/{id}/password")
   public ResponseEntity <?> actualizaPassword(@PathVariable Long id, @RequestBody PasswordRequest request){
    String resultado= usuarioService.actualizaPassword(id, request.getPassword());
    if(resultado.equals("Usuario no encontrado")){
        return ResponseEntity.status(404).body(resultado);
    }
    if(resultado.equals("Contraseña actualizada exitosamente")){
        return ResponseEntity.ok().body(resultado);
    }
    else{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
    }
   }
    @PutMapping("/{id}/desactivar")
    public ResponseEntity <?> desactivarUsuario(@PathVariable Long id){
     Usuario usuario= usuarioService.cambiarEstado(id, Usuario.EstadoCuenta.INACTIVO);
     if (usuario == null){
          return ResponseEntity.status(404).body("Usuario no encontrado con id: " + id);
     }
     return ResponseEntity.ok("Desactivado");
    }
    @PutMapping("/{id}/activar")
    public ResponseEntity <?> activarUsuario(@PathVariable Long id){
        Usuario usuario= usuarioService.cambiarEstado(id, Usuario.EstadoCuenta.ACTIVO);
        if (usuario == null){
            return ResponseEntity.status(404).body("Usuario no encontrado con id: " + id);
        }
        return ResponseEntity.ok("Activado");
    }
    @PutMapping("/{id}/rol")
    public ResponseEntity <?> asignarRol(@PathVariable Long id, @RequestBody Rol nuevoRol){
        Integer idRol= nuevoRol.getId();
        String resultado= usuarioService.cambiarRol(id, idRol);
        if(resultado.equals("Usuario no encontrado") || resultado.equals("Rol no encontrado")){
            return ResponseEntity.status(404).body(resultado);
        }
        return ResponseEntity.ok("Rol asignado correctamente");
    }
    @GetMapping("/existe")
    public ResponseEntity <?> existeUsuario(
            @RequestParam(required=false) String username,
             @RequestParam(required=false) String email){
        
        if ((username == null || username.isBlank()) && (email == null || email.isBlank())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Debe proporcionar al menos un parámetro: username o email");
        }

        boolean existe = false;
        if (username != null && !username.isBlank()) {
            existe = usuarioService.existeUsuario(username);
        }
        if (!existe && email != null && !email.isBlank()) {
            existe = usuarioService.existeEmail(email);
        }

        return ResponseEntity.ok(existe);
    }
   @DeleteMapping("/{id}")
   public ResponseEntity <?> eliminarUsuario(@PathVariable Long id){
    if (usuarioService.eliminarUsuario(id)) {
        return ResponseEntity.ok("Usuario eliminado");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con id: " + id);
   }
   @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody Usuario loginData) {
        if (loginData == null || loginData.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Debe proporcionar email");
        }

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginData.getEmail(),
                    loginData.getPassword_hash()
                )
            );

            Usuario usuario = usuarioService.obtenerPorEmail(loginData.getEmail());
            String token = jwtService.generarToken(usuario);

            return ResponseEntity.ok(Map.of(
                "token", token,
                "userId", usuario.getId(),
                "username", usuario.getUsername(),
                "role", usuario.getRol().getNombre()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }

}
