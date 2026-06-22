package com.animanga.ms_auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_auth.dto.AuditRequest;
import com.animanga.ms_auth.dto.UsuarioResponse;
import com.animanga.ms_auth.model.Rol;
import com.animanga.ms_auth.model.Usuario;
import com.animanga.ms_auth.repository.RolRepository;
import com.animanga.ms_auth.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void auditar(Long idUsuario, String accion, String tabla) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(idUsuario, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String obtenerEstado(Long id){
        Usuario user= usuarioRepository.findById(id).orElse(null);
        if (user == null){
            return "INEXISTENTE";
        }
        return user.getEstadoCuenta().name();
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
        auditar(idUsuario, "Rol cambiado a: " + rol.getNombre(), "usuario");
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
            auditar(id, "Usuario eliminado", "usuario");
            return true;
        }
        return false;
    }
    public Usuario cambiarEstado(long id, Usuario.EstadoCuenta nuevoEstado){
        Usuario user= usuarioRepository.findById(id).orElse(null);
        if (user != null){
            user.setEstadoCuenta(nuevoEstado);
            Usuario actualizado = usuarioRepository.save(user);
            auditar(id, "Cuenta " + (nuevoEstado == Usuario.EstadoCuenta.ACTIVO ? "activada" : "desactivada"), "usuario");
            return actualizado;
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
        if (usuario.getEstadoCuenta() == null) {
            usuario.setEstadoCuenta(Usuario.EstadoCuenta.ACTIVO);
        }
        if (existeUsuario(usuario.getUsername())) {
            return "Error: El nombre de usuario ocupado";
        }
        if (existeEmail(usuario.getEmail())) {
            return "Error: Correo electrónico ocupado";
        }
        usuario.setPassword_hash(passwordEncoder.encode(usuario.getPassword_hash()));
        usuarioRepository.save(usuario);
        auditar(usuario.getId(), "Usuario registrado: " + usuario.getUsername(), "usuario");
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
            usuarioExistente.setPassword_hash(passwordEncoder.encode(usuarioActualizado.getPassword_hash()));
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
    public Optional<UsuarioResponse> obtenerUsuarioInfo(Long id) {
        return usuarioRepository.encontrarUsuarioDTO(id);
    }

    public Usuario obtenerPorUsernameOEmail(String identificador) {
        Usuario usuario = usuarioRepository.findByUsername(identificador);
        if (usuario == null) {
            usuario = usuarioRepository.findByEmail(identificador);
        }
        return usuario;
    }

    public String actualizaPassword(Long id, String nuevaPassword){
        Usuario usuario= usuarioRepository.findById(id).orElse(null);
        if (usuario == null){
            return "Usuario no encontrado";
        }
        usuario.setPassword_hash(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
        return "Contraseña actualizada exitosamente";
    }
  
}
