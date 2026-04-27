    package cl.animanga.ms_auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.animanga.ms_auth.model.Usuario;
import cl.animanga.ms_auth.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @PostMapping("/registro")
    public ResponseEntity <?>  registrar(@RequestBody Usuario usuario){
        String respuesta= this.usuarioService.registrarUsuario(usuario);
        if(respuesta.equals("si")){
            return ResponseEntity.ok().body("usuario guardado correctamente ");
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
   public Usuario obtenerUsuario(@PathVariable Long id){
        return usuarioService.obtenerUsuario(id);
   }
   @GetMapping("/{id}/status")
   public ResponseEntity <String> obtenerEstado(@PathVariable Long id){
    String estado= usuarioService.obtenerEstado(id);
    if (estado.equals("INEXISTENTE")){
        return ResponseEntity.status(404).body(estado);
    }
    return ResponseEntity.ok(estado);
   }
   
   @PostMapping("/login")
   public ResponseEntity <String> login (@RequestBody Usuario loginData){
        String autenticador=loginData.getUsername();
        if(autenticador == null || autenticador.isEmpty()){
            autenticador=loginData.getEmail();
        }
        String resultado= usuarioService.login(autenticador, loginData.getPassword_hash());
        if(resultado.equals("No encontrado")){
            return ResponseEntity.status(404).body("Usuario/email no existe");      
        }
        if(resultado.equals("Logeado")){
            return ResponseEntity.ok().body("Usuario Logeado");
        }
        if(resultado.equals("Password Incorrecto")){
            return ResponseEntity.status(401).body("Combinacion usuario/password incorrecta");
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultado);
        }
    }
}
