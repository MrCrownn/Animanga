package cl.animanga.ms_auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.animanga.ms_auth.model.Usuario;
import cl.animanga.ms_auth.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
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
    public String registrarUsuario(Usuario usuario){
        if( usuarioRepository.existsById(usuario.getId_usuario())){
            return "si";
        }
        else{
            return "no";
        }
    }
    public Usuario obtenerUsuario(Long id){
        if(usuarioRepository.existsById(id)){
            return usuarioRepository.findById(id).get();
        }
        else{
            return null;
        }
    }
    public List <Usuario> obtenerTodos(){
        return this.usuarioRepository.findAll();
    }
}
