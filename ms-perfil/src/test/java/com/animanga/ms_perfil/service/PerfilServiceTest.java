package com.animanga.ms_perfil.service;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_perfil.model.Perfil;
import com.animanga.ms_perfil.repository.PerfilRepository;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PerfilService service;

    private Perfil perfil;

    @BeforeEach
    void setUp() {
        perfil = new Perfil();
        perfil.setIdPerfil(1L);
        perfil.setIdUsuario(1L);
        perfil.setBiografia("Hola mundo");
        perfil.setAvatarUrl("https://example.com/avatar.png");
    }

    @Test
    void guardar_exito() {
        when(perfilRepository.findByIdUsuario(anyLong())).thenReturn(Optional.empty());
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        String resultado = service.guardar(perfil, 1L, "ADMIN");

        assertEquals("Perfil guardado exitosamente", resultado);
        assertEquals(1L, perfil.getIdUsuario());
        verify(perfilRepository).save(perfil);
    }

    @Test
    void guardar_usuarioNoExiste() {
        when(perfilRepository.findByIdUsuario(anyLong())).thenReturn(Optional.empty());
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        String resultado = service.guardar(perfil, 1L, "ADMIN");

        assertEquals("El usuario con id 1 no existe", resultado);
        verify(perfilRepository, never()).save(any());
    }

    @Test
    void guardar_errorConexion() {
        when(perfilRepository.findByIdUsuario(anyLong())).thenReturn(Optional.empty());
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        String resultado = service.guardar(perfil, 1L, "ADMIN");

        assertTrue(resultado.contains("Error al validar usuario"));
        verify(perfilRepository, never()).save(any());
    }

    @Test
    void guardar_sinXUserId() {
        String resultado = service.guardar(perfil, null, "ADMIN");

        assertEquals("X-User-Id es requerido", resultado);
        verify(perfilRepository, never()).save(any());
    }

    @Test
    void guardar_otroUsuarioNoAdmin() {
        perfil.setIdUsuario(2L);

        String resultado = service.guardar(perfil, 1L, "USUARIO");

        assertEquals("No puedes crear un perfil para otro usuario", resultado);
        verify(perfilRepository, never()).save(any());
    }

    @Test
    void guardar_yaExiste() {
        when(perfilRepository.findByIdUsuario(1L)).thenReturn(Optional.of(perfil));

        String resultado = service.guardar(new Perfil(), 1L, "ADMIN");

        assertEquals("Ya existe un perfil para el usuario 1", resultado);
        verify(perfilRepository, never()).save(any());
    }

    @Test
    void guardar_adminCreaParaOtro() {
        Perfil nuevo = new Perfil();
        nuevo.setIdUsuario(2L);

        when(perfilRepository.findByIdUsuario(2L)).thenReturn(Optional.empty());
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(nuevo);

        String resultado = service.guardar(nuevo, 1L, "ADMIN");

        assertEquals("Perfil guardado exitosamente", resultado);
        assertEquals(2L, nuevo.getIdUsuario());
        verify(perfilRepository).save(nuevo);
    }

    @Test
    void actualizar_exito() {
        Perfil existente = new Perfil();
        existente.setIdPerfil(1L);
        existente.setBiografia("Vieja bio");

        Perfil actualizado = new Perfil();
        actualizado.setBiografia("Nueva bio");
        actualizado.setAvatarUrl("https://new-avatar.png");

        when(perfilRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(existente);

        String resultado = service.actualizar(1L, actualizado, 1L);

        assertEquals("Perfil actualizado exitosamente", resultado);
        assertEquals("Nueva bio", existente.getBiografia());
        assertEquals("https://new-avatar.png", existente.getAvatarUrl());
    }

    @Test
    void actualizar_noEncontrado() {
        when(perfilRepository.findById(999L)).thenReturn(Optional.empty());

        String resultado = service.actualizar(999L, perfil, 1L);

        assertEquals("Perfil no encontrado", resultado);
        verify(perfilRepository, never()).save(any());
    }

    @Test
    void eliminar_exito() {
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));
        doNothing().when(perfilRepository).delete(perfil);

        boolean resultado = service.eliminar(1L, 1L);

        assertTrue(resultado);
        verify(perfilRepository).delete(perfil);
    }
}
