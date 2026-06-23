package com.animanga.ms_helpdesk.service;

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
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_helpdesk.model.CategoriaIncidencia;
import com.animanga.ms_helpdesk.model.EstadoIncidencia;
import com.animanga.ms_helpdesk.model.Incidencia;
import com.animanga.ms_helpdesk.model.PrioridadIncidencia;
import com.animanga.ms_helpdesk.repository.IncidenciaRepository;

@ExtendWith(MockitoExtension.class)
class IncidenciaServiceTest {

    @Mock
    private IncidenciaRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IncidenciaService service;

    private Incidencia incidencia;

    @BeforeEach
    void setUp() {
        incidencia = new Incidencia();
        incidencia.setIdIncidencia(1L);
        incidencia.setTitulo("Error en login");
        incidencia.setDescripcion("No puedo iniciar sesión");
        incidencia.setCategoria(CategoriaIncidencia.TECNICO);
        incidencia.setPrioridad(PrioridadIncidencia.ALTA);
        incidencia.setIdUsuarioReporta(2L);
        incidencia.setEstado(EstadoIncidencia.ABIERTO);
    }

    @Test
    void crear_exito() {
        when(repository.save(any(Incidencia.class))).thenReturn(incidencia);

        String resultado = service.crear(incidencia, 2L);

        assertEquals("Incidencia creada exitosamente", resultado);
        assertEquals(2L, incidencia.getIdUsuarioReporta());
        assertEquals(EstadoIncidencia.ABIERTO, incidencia.getEstado());
        verify(repository).save(incidencia);
    }

    @Test
    void crear_sinTitulo() {
        incidencia.setTitulo(null);

        String resultado = service.crear(incidencia, 2L);

        assertEquals("El titulo es obligatorio", resultado);
        verify(repository, never()).save(any());
    }

    @Test
    void crear_sinCategoria() {
        incidencia.setCategoria(null);

        String resultado = service.crear(incidencia, 2L);

        assertEquals("La categoria es obligatoria", resultado);
        verify(repository, never()).save(any());
    }

    @Test
    void crear_sinPrioridad() {
        incidencia.setPrioridad(null);

        String resultado = service.crear(incidencia, 2L);

        assertEquals("La prioridad es obligatoria", resultado);
        verify(repository, never()).save(any());
    }

    @Test
    void actualizarEstado_aResuelto() {
        when(repository.findById(1L)).thenReturn(Optional.of(incidencia));
        when(repository.save(any(Incidencia.class))).thenReturn(incidencia);

        String resultado = service.actualizarEstado(1L, EstadoIncidencia.RESUELTO, 1L);

        assertEquals("Incidencia actualizada a RESUELTO", resultado);
        assertEquals(EstadoIncidencia.RESUELTO, incidencia.getEstado());
        assertNotNull(incidencia.getFechaResolucion());
    }

    @Test
    void actualizarEstado_noEncontrada() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        String resultado = service.actualizarEstado(999L, EstadoIncidencia.RESUELTO, 1L);

        assertEquals("Incidencia no encontrada", resultado);
        verify(repository, never()).save(any());
    }
}
