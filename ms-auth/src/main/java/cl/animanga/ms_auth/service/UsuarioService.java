package cl.animanga.ms_auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import cl.animanga.ms_auth.model.Rol;
import cl.animanga.ms_auth.model.Usuario;
import cl.animanga.ms_auth.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RestTemplate restTemplate;
    public String login(String identificador, String password){
        Usuario user= usuarioRepository.findByUsername(identificador);
        if (user==null){
            user=usuarioRepository.findByEmail(identificador);
        }
        if (user == null){
            return "No encontrado";
        }
        if(user.getPassword_hash().equals(password)){
            return "Logeado";
        }
        else{
            return "Password Incorrecto";
        }
    }
    public String obtenerEstado(Long id){
        Usuario user= usuarioRepository.findById(id).orElse(null);
        if (user == null){
            return "INEXISTENTE";
        }
        return user.getEstado_cuenta();
    }
    public Usuario cambiarEstado(long id, String nuevoEstado){
        Usuario user= usuarioRepository.findById(id).orElse(null);
        if (user != null){
            user.setEstado_cuenta(nuevoEstado);
            return usuarioRepository.save(user);
        }
        return null;
    }
   public String registrarUsuario(Usuario usuario) {
    String url = "http://localhost:8080/api/roles/" + usuario.getRol().getId();

    try {
        Rol rol = restTemplate.getForObject(url, Rol.class);
        usuario.setRol(rol);
        if (usuarioRepository.findByUsername(usuario.getUsername())!= null) {
            return "Error: El nombre de usuario ocupado";
        }
        if (usuarioRepository.findByEmail(usuario.getEmail())!= null) {
            return "Error: Correo electrónico ocupado";
        }
        usuarioRepository.save(usuario);
        return "Usuario registrado exitosamente";
    } catch (HttpClientErrorException.NotFound e) {
        return "Rol no existe";
    } catch (Exception e) {
        return "Error de conexión: " + e.getMessage();
    }
    }
    public Usuario obtenerUsuario(Long id){
       return usuarioRepository.findById(id).orElse(null);
    }
    public List <Usuario> obtenerTodos(){
        return this.usuarioRepository.findAll();
    }
}
