package com.animanga.ms_production.service;

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

import com.animanga.ms_production.model.Entidad;
import com.animanga.ms_production.model.Nacionalidad;
import com.animanga.ms_production.model.TipoEntidad;
import com.animanga.ms_production.repository.EntidadRepository;
import com.animanga.ms_production.repository.NacionalidadRepository;
import com.animanga.ms_production.repository.TipoEntidadRepository;

@ExtendWith(MockitoExtension.class)
class EntidadServiceTest {

    @Mock
    private EntidadRepository entidadRepository;

    @Mock
    private TipoEntidadRepository tipoEntidadRepository;

    @Mock
    private NacionalidadRepository nacionalidadRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EntidadService service;

    private Entidad entidad;
    private TipoEntidad tipoEstudio;
    private Nacionalidad nacionalidad;

    @BeforeEach
    void setUp() {
        tipoEstudio = new TipoEntidad();
        tipoEstudio.setIdTipo(1);
        tipoEstudio.setNombre("ESTUDIO");

        nacionalidad = new Nacionalidad();
        nacionalidad.setIdNacionalidad(1);
        nacionalidad.setPais("Japón");

        entidad = new Entidad();
        entidad.setIdEntidad(1);
        entidad.setNombre("Studio Ghibli");
        entidad.setTipoEntidad(tipoEstudio);
        entidad.setNacionalidad(nacionalidad);
    }

    @Test
    void guardar_exito() {
        when(tipoEntidadRepository.findById(1)).thenReturn(Optional.of(tipoEstudio));
        when(nacionalidadRepository.findById(1)).thenReturn(Optional.of(nacionalidad));
        when(entidadRepository.existsByNombreAndTipoEntidad_IdTipo("Studio Ghibli", 1)).thenReturn(false);
        when(entidadRepository.save(any(Entidad.class))).thenReturn(entidad);

        String resultado = service.guardar(entidad, 1L);

        assertEquals("Entidad guardada exitosamente", resultado);
        verify(entidadRepository).save(entidad);
    }

    @Test
    void guardar_sinNombre() {
        entidad.setNombre(null);

        String resultado = service.guardar(entidad, 1L);

        assertEquals("El nombre de la entidad es obligatorio", resultado);
        verify(entidadRepository, never()).save(any());
    }

    @Test
    void guardar_tipoNoExiste() {
        when(tipoEntidadRepository.findById(999)).thenReturn(Optional.empty());
        entidad.getTipoEntidad().setIdTipo(999);

        String resultado = service.guardar(entidad, 1L);

        assertEquals("El tipo de entidad con id 999 no existe", resultado);
    }

    @Test
    void guardar_nacionalidadNoExiste() {
        Nacionalidad nacInvalida = new Nacionalidad();
        nacInvalida.setIdNacionalidad(999);
        nacInvalida.setPais("Invalido");
        entidad.setNacionalidad(nacInvalida);

        when(tipoEntidadRepository.findById(1)).thenReturn(Optional.of(tipoEstudio));
        when(nacionalidadRepository.findById(999)).thenReturn(Optional.empty());

        String resultado = service.guardar(entidad, 1L);

        assertEquals("La nacionalidad con id 999 no existe", resultado);
    }

    @Test
    void guardar_duplicado() {
        when(tipoEntidadRepository.findById(1)).thenReturn(Optional.of(tipoEstudio));
        when(nacionalidadRepository.findById(1)).thenReturn(Optional.of(nacionalidad));
        when(entidadRepository.existsByNombreAndTipoEntidad_IdTipo("Studio Ghibli", 1)).thenReturn(true);

        String resultado = service.guardar(entidad, 1L);

        assertEquals("La entidad 'Studio Ghibli' ya existe con ese tipo", resultado);
    }

    @Test
    void actualizar_exito() {
        Entidad existente = new Entidad();
        existente.setIdEntidad(1);
        existente.setNombre("Original");
        existente.setTipoEntidad(tipoEstudio);

        Entidad actualizado = new Entidad();
        actualizado.setNombre("Nuevo Nombre");

        when(entidadRepository.findById(1)).thenReturn(Optional.of(existente));
        when(entidadRepository.existsByNombreAndTipoEntidad_IdTipo("Nuevo Nombre", 1)).thenReturn(false);
        when(entidadRepository.save(any(Entidad.class))).thenReturn(existente);

        String resultado = service.actualizar(1, actualizado, 1L);

        assertEquals("Entidad actualizada exitosamente", resultado);
        assertEquals("Nuevo Nombre", existente.getNombre());
    }

    @Test
    void existePorId() {
        when(entidadRepository.existsById(1)).thenReturn(true);

        assertTrue(service.existePorId(1));
    }
}
