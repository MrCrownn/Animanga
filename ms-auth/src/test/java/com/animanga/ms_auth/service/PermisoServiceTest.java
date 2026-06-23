package com.animanga.ms_auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_auth.model.Permiso;
import com.animanga.ms_auth.repository.PermisoRepository;

@ExtendWith(MockitoExtension.class)
class PermisoServiceTest {

    @Mock
    private PermisoRepository permisoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PermisoService service;

    private Permiso permiso;

    @BeforeEach
    void setUp() {
        permiso = new Permiso("USUARIO_CREAR", "Crear usuarios");
        permiso.setId(1);
    }

    @Test
    void guardar_exito() {
        when(permisoRepository.save(any(Permiso.class))).thenReturn(permiso);

        Permiso resultado = service.guardarPermiso(permiso);

        assertEquals("USUARIO_CREAR", resultado.getAccion());
        verify(permisoRepository).save(permiso);
    }

    @Test
    void buscarPorAccion() {
        when(permisoRepository.findByAccion("USUARIO_CREAR")).thenReturn(permiso);

        Permiso resultado = service.buscarPorAccion("USUARIO_CREAR");

        assertEquals("USUARIO_CREAR", resultado.getAccion());
    }

    @Test
    void eliminar_exito() {
        when(permisoRepository.existsById(1)).thenReturn(true);
        when(permisoRepository.findById(1)).thenReturn(java.util.Optional.of(permiso));
        doNothing().when(permisoRepository).deleteById(1);

        boolean resultado = service.eliminarPermiso(1);

        assertTrue(resultado);
        verify(permisoRepository).deleteById(1);
    }

    @Test
    void eliminar_noExiste() {
        when(permisoRepository.existsById(999)).thenReturn(false);

        boolean resultado = service.eliminarPermiso(999);

        assertFalse(resultado);
        verify(permisoRepository, never()).deleteById(any());
    }
}
