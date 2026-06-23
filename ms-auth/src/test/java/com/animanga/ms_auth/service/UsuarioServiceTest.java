package com.animanga.ms_auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_auth.model.Rol;
import com.animanga.ms_auth.model.Usuario;
import com.animanga.ms_auth.model.Usuario.EstadoCuenta;
import com.animanga.ms_auth.repository.RolRepository;
import com.animanga.ms_auth.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    private Rol rolAdmin;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        rolAdmin = new Rol();
        rolAdmin.setId(1);
        rolAdmin.setNombre("Admin");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setEmail("test@test.cl");
        usuario.setPassword_hash("plain123");
        usuario.setRol(rolAdmin);
        usuario.setEstadoCuenta(EstadoCuenta.ACTIVO);
    }

    @Test
    void registrar_exito() {
        when(rolRepository.findById(1)).thenReturn(Optional.of(rolAdmin));
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@test.cl")).thenReturn(false);
        when(passwordEncoder.encode("plain123")).thenReturn("encoded123");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String resultado = service.registrarUsuario(usuario);

        assertEquals("Usuario registrado exitosamente", resultado);
        assertEquals("encoded123", usuario.getPassword_hash());
        assertEquals(EstadoCuenta.ACTIVO, usuario.getEstadoCuenta());
    }

    @Test
    void registrar_sinRol() {
        usuario.setRol(null);

        String resultado = service.registrarUsuario(usuario);

        assertEquals("Error: El rol es requerido", resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrar_rolNoExiste() {
        when(rolRepository.findById(999)).thenReturn(Optional.empty());
        Rol rolInexistente = new Rol();
        rolInexistente.setId(999);
        usuario.setRol(rolInexistente);

        String resultado = service.registrarUsuario(usuario);

        assertEquals("Rol no existe", resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrar_usernameDuplicado() {
        when(rolRepository.findById(1)).thenReturn(Optional.of(rolAdmin));
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(true);

        String resultado = service.registrarUsuario(usuario);

        assertEquals("Error: El nombre de usuario ocupado", resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registrar_emailDuplicado() {
        when(rolRepository.findById(1)).thenReturn(Optional.of(rolAdmin));
        when(usuarioRepository.existsByUsername("testuser")).thenReturn(false);
        when(usuarioRepository.existsByEmail("test@test.cl")).thenReturn(true);

        String resultado = service.registrarUsuario(usuario);

        assertEquals("Error: Correo electrónico ocupado", resultado);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizar_cambioUsername() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setUsername("olduser");
        existente.setEmail("old@test.cl");

        Usuario actualizado = new Usuario();
        actualizado.setUsername("newuser");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByUsername("newuser")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(existente);

        String resultado = service.actualizaUsuario(1L, actualizado);

        assertEquals("Usuario actualizado exitosamente", resultado);
        assertEquals("newuser", existente.getUsername());
    }

    @Test
    void actualizar_usernameOcupado() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setUsername("olduser");

        Usuario actualizado = new Usuario();
        actualizado.setUsername("takenuser");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.existsByUsername("takenuser")).thenReturn(true);

        String resultado = service.actualizaUsuario(1L, actualizado);

        assertEquals("Error: El nombre de usuario ocupado", resultado);
        assertEquals("olduser", existente.getUsername());
    }

    @Test
    void actualizar_password() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setPassword_hash("oldhash");

        Usuario actualizado = new Usuario();
        actualizado.setPassword_hash("newpass");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(passwordEncoder.encode("newpass")).thenReturn("newhash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(existente);

        service.actualizaUsuario(1L, actualizado);

        assertEquals("newhash", existente.getPassword_hash());
    }

    @Test
    void cambiarRol_exito() {
        Rol nuevoRol = new Rol();
        nuevoRol.setId(2);
        nuevoRol.setNombre("User");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(rolRepository.findById(2)).thenReturn(Optional.of(nuevoRol));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String resultado = service.cambiarRol(1L, 2);

        assertEquals("ok", resultado);
        assertEquals(nuevoRol, usuario.getRol());
    }

    @Test
    void eliminar_exito() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        boolean resultado = service.eliminarUsuario(1L);

        assertTrue(resultado);
        verify(usuarioRepository).deleteById(1L);
    }
}
