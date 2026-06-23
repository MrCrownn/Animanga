package com.animanga.ms_auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_auth.model.Permiso;
import com.animanga.ms_auth.model.Rol;
import com.animanga.ms_auth.repository.PermisoRepository;
import com.animanga.ms_auth.repository.RolRepository;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PermisoRepository permisoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RolService service;

    private Rol rol;
    private Permiso permiso;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId(1);
        rol.setNombre("Admin");

        permiso = new Permiso("USUARIO_CREAR", "Crear usuarios");
        permiso.setId(1);
    }

    @Test
    void guardar_exito() {
        when(rolRepository.existsByNombre("Admin")).thenReturn(false);
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);

        String resultado = service.guardar(rol);

        assertEquals("Rol guardado exitosamente", resultado);
        verify(rolRepository).save(rol);
    }

    @Test
    void guardar_duplicado() {
        when(rolRepository.existsByNombre("Admin")).thenReturn(true);

        String resultado = service.guardar(rol);

        assertEquals("El Rol 'Admin' ya existe", resultado);
        verify(rolRepository, never()).save(any());
    }

    @Test
    void eliminar_exito() {
        when(rolRepository.existsById(1)).thenReturn(true);
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        doNothing().when(rolRepository).deleteById(1);

        boolean resultado = service.eliminar(1);

        assertTrue(resultado);
        verify(rolRepository).deleteById(1);
    }

    @Test
    void asignarPermiso_exito() {
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(permisoRepository.findById(1)).thenReturn(Optional.of(permiso));
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);

        String resultado = service.asignarPermiso(1, 1);

        assertEquals("Permiso Asignado", resultado);
        assertTrue(rol.getPermisos().contains(permiso));
    }

    @Test
    void asignarPermiso_rolNoExiste() {
        when(rolRepository.findById(999)).thenReturn(Optional.empty());

        String resultado = service.asignarPermiso(999, 1);

        assertEquals("Rol No Encontrado", resultado);
        verify(rolRepository, never()).save(any());
    }

    @Test
    void removerPermiso_exito() {
        rol.setPermisos(new HashSet<>());
        rol.getPermisos().add(permiso);

        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(permisoRepository.findById(1)).thenReturn(Optional.of(permiso));
        when(rolRepository.save(any(Rol.class))).thenReturn(rol);

        String resultado = service.removerPermiso(1, 1);

        assertEquals("Permiso Removido", resultado);
        assertFalse(rol.getPermisos().contains(permiso));
    }
}
