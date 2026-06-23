package com.animanga.ms_catalog.service;

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

import com.animanga.ms_catalog.model.Genero;
import com.animanga.ms_catalog.repository.GeneroRepository;

@ExtendWith(MockitoExtension.class)
class GeneroServiceTest {

    @Mock
    private GeneroRepository generoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeneroService service;

    private Genero genero;

    @BeforeEach
    void setUp() {
        genero = new Genero();
        genero.setIdGenero(1);
        genero.setNombre("Shonen");
    }

    @Test
    void guardar_exito() {
        when(generoRepository.existsByNombre("Shonen")).thenReturn(false);
        when(generoRepository.save(any(Genero.class))).thenReturn(genero);

        String resultado = service.guardar(genero, 1L);

        assertEquals("Genero guardado exitosamente", resultado);
        verify(generoRepository).save(genero);
    }

    @Test
    void guardar_duplicado() {
        when(generoRepository.existsByNombre("Shonen")).thenReturn(true);

        String resultado = service.guardar(genero, 1L);

        assertEquals("El genero 'Shonen' ya existe", resultado);
        verify(generoRepository, never()).save(any());
    }

    @Test
    void guardar_sinNombre() {
        genero.setNombre(null);

        String resultado = service.guardar(genero, 1L);

        assertEquals("El nombre del genero es obligatorio", resultado);
        verify(generoRepository, never()).save(any());
    }

    @Test
    void eliminar_exito() {
        Genero existente = new Genero();
        existente.setIdGenero(1);
        existente.setNombre("Shonen");
        when(generoRepository.findById(1)).thenReturn(Optional.of(existente));
        doNothing().when(generoRepository).delete(existente);

        boolean resultado = service.eliminar(1, 1L);

        assertTrue(resultado);
        verify(generoRepository).delete(existente);
    }
}
