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

import com.animanga.ms_production.model.TipoEntidad;
import com.animanga.ms_production.repository.TipoEntidadRepository;

@ExtendWith(MockitoExtension.class)
class TipoEntidadServiceTest {

    @Mock
    private TipoEntidadRepository tipoEntidadRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TipoEntidadService service;

    private TipoEntidad tipo;

    @BeforeEach
    void setUp() {
        tipo = new TipoEntidad();
        tipo.setIdTipo(1);
        tipo.setNombre("ESTUDIO");
    }

    @Test
    void guardar_exito() {
        when(tipoEntidadRepository.existsByNombre("ESTUDIO")).thenReturn(false);
        when(tipoEntidadRepository.save(any(TipoEntidad.class))).thenReturn(tipo);

        String resultado = service.guardar(tipo, 1L);

        assertEquals("Tipo de entidad guardado exitosamente", resultado);
        verify(tipoEntidadRepository).save(tipo);
    }

    @Test
    void guardar_duplicado() {
        when(tipoEntidadRepository.existsByNombre("ESTUDIO")).thenReturn(true);

        String resultado = service.guardar(tipo, 1L);

        assertEquals("El tipo de entidad 'ESTUDIO' ya existe", resultado);
        verify(tipoEntidadRepository, never()).save(any());
    }

    @Test
    void guardar_sinNombre() {
        tipo.setNombre(null);

        String resultado = service.guardar(tipo, 1L);

        assertEquals("El nombre del tipo de entidad es obligatorio", resultado);
        verify(tipoEntidadRepository, never()).save(any());
    }

    @Test
    void actualizar_duplicado() {
        TipoEntidad existente = new TipoEntidad();
        existente.setIdTipo(1);
        existente.setNombre("ORIGINAL");

        TipoEntidad actualizado = new TipoEntidad();
        actualizado.setNombre("ESTUDIO");

        when(tipoEntidadRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoEntidadRepository.existsByNombre("ESTUDIO")).thenReturn(true);

        String resultado = service.actualizar(1, actualizado, 1L);

        assertEquals("Error: El nombre del tipo de entidad ya está en uso", resultado);
    }
}
