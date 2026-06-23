package com.animanga.ms_production.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_production.model.Nacionalidad;
import com.animanga.ms_production.repository.NacionalidadRepository;

@ExtendWith(MockitoExtension.class)
class NacionalidadServiceTest {

    @Mock
    private NacionalidadRepository nacionalidadRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NacionalidadService service;

    private Nacionalidad nacionalidad;

    @BeforeEach
    void setUp() {
        nacionalidad = new Nacionalidad();
        nacionalidad.setIdNacionalidad(1);
        nacionalidad.setPais("Japón");
    }

    @Test
    void guardar_exito() {
        when(nacionalidadRepository.existsByPais("Japón")).thenReturn(false);
        when(nacionalidadRepository.save(any(Nacionalidad.class))).thenReturn(nacionalidad);

        String resultado = service.guardar(nacionalidad, 1L);

        assertEquals("Nacionalidad guardada exitosamente", resultado);
        verify(nacionalidadRepository).save(nacionalidad);
    }

    @Test
    void guardar_duplicado() {
        when(nacionalidadRepository.existsByPais("Japón")).thenReturn(true);

        String resultado = service.guardar(nacionalidad, 1L);

        assertEquals("La nacionalidad 'Japón' ya existe", resultado);
        verify(nacionalidadRepository, never()).save(any());
    }

    @Test
    void guardar_sinPais() {
        nacionalidad.setPais(null);

        String resultado = service.guardar(nacionalidad, 1L);

        assertEquals("El país es obligatorio", resultado);
        verify(nacionalidadRepository, never()).save(any());
    }

    @Test
    void actualizar_exito() {
        Nacionalidad existente = new Nacionalidad();
        existente.setIdNacionalidad(1);
        existente.setPais("Original");

        Nacionalidad actualizado = new Nacionalidad();
        actualizado.setPais("Nuevo");

        when(nacionalidadRepository.findById(1)).thenReturn(Optional.of(existente));
        when(nacionalidadRepository.existsByPais("Nuevo")).thenReturn(false);
        when(nacionalidadRepository.save(any(Nacionalidad.class))).thenReturn(existente);

        String resultado = service.actualizar(1, actualizado, 1L);

        assertEquals("Nacionalidad actualizada exitosamente", resultado);
        assertEquals("Nuevo", existente.getPais());
    }
}
