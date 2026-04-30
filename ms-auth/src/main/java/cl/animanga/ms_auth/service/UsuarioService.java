package cl.animanga.ms_auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import cl.animanga.ms_auth.model.Rol;
import cl.animanga.ms_auth.model.Usuario;
import cl.animanga.ms_auth.repository.UsuarioRepository;
import cl.animanga.ms_auth.repository.RolRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;
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
    public String cambiarRol(Long idUsuario, Integer idRol){
        Usuario usuario= usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null){
            return "Usuario no encontrado";
        }
        Rol rol= rolRepository.findById(idRol).orElse(null);
        if (rol == null){
            return "Rol no encontrado";
        }
        usuario.setRol(rol);
        usuarioRepository.save(usuario);
        return "ok";
    }
    public boolean existeUsuario(String username){
        return usuarioRepository.existsByUsername(username);
    }
    public boolean existeEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }
    public boolean eliminarUsuario(Long id){
        if (usuarioRepository.existsById(id)){
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
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
        if (usuario.getRol() == null || usuario.getRol().getId() == null) {
            return "Error: El rol es requerido";
        }
        Optional<Rol> rolOpt = rolRepository.findById(usuario.getRol().getId());
        if (rolOpt.isEmpty()) {
            return "Rol no existe";
        }
        Rol rol = rolOpt.get();
        usuario.setRol(rol);
        if (existeUsuario(usuario.getUsername())) {
            return "Error: El nombre de usuario ocupado";
        }
        if (existeEmail(usuario.getEmail())) {
            return "Error: Correo electrónico ocupado";
        }
        usuarioRepository.save(usuario);
        return "Usuario registrado exitosamente";
        }
        public Usuario obtenerUsuario(Long id){
        return usuarioRepository.findById(id).orElse(null);
        }
        public List <Usuario> obtenerTodos(){
            return this.usuarioRepository.findAll();
        }
    public String actualizaUsuario (Long id, Usuario usuarioActualizado){
        Usuario usuarioExistente= usuarioRepository.findById(id).orElse(null);
        if (usuarioExistente == null){
            return "Usuario no encontrado";
        }
        if (usuarioActualizado.getUsername() != null &&
                !usuarioActualizado.getUsername().equals(usuarioExistente.getUsername())){
            if (existeUsuario(usuarioActualizado.getUsername())){
                return "Error: El nombre de usuario ocupado";
            }
            usuarioExistente.setUsername(usuarioActualizado.getUsername());
        }
        if (usuarioActualizado.getEmail() != null &&
                !usuarioActualizado.getEmail().equals(usuarioExistente.getEmail())){
            if (existeEmail(usuarioActualizado.getEmail())){
                return "Error: Correo electrónico ocupado";
            }
            usuarioExistente.setEmail(usuarioActualizado.getEmail());
        }
        if (usuarioActualizado.getPassword_hash() != null){
            usuarioExistente.setPassword_hash(usuarioActualizado.getPassword_hash());
        }
        if (usuarioActualizado.getRol() != null && usuarioActualizado.getRol().getId() != null){
            Optional<Rol> rolOpt= rolRepository.findById(usuarioActualizado.getRol().getId());
            if (rolOpt.isEmpty()){
                return "Rol no existe";
            }
            usuarioExistente.setRol(rolOpt.get());
        }
        usuarioRepository.save(usuarioExistente);
        return "Usuario actualizado exitosamente";
    }
    public String actualizaPassword(Long id, String nuevaPassword){
        Usuario usuario= usuarioRepository.findById(id).orElse(null);
        if (usuario == null){
            return "Usuario no encontrado";
        }
        usuario.setPassword_hash(nuevaPassword);
        usuarioRepository.save(usuario);
        return "Contraseña actualizada exitosamente";
    }
  
}
