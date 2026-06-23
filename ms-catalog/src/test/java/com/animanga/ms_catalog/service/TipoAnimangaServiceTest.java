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

import com.animanga.ms_catalog.model.TipoAnimanga;
import com.animanga.ms_catalog.repository.TipoAnimangaRepository;

@ExtendWith(MockitoExtension.class)
class TipoAnimangaServiceTest {

    @Mock
    private TipoAnimangaRepository tipoAnimangaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TipoAnimangaService service;

    private TipoAnimanga tipo;

    @BeforeEach
    void setUp() {
        tipo = new TipoAnimanga();
        tipo.setIdTipo(1);
        tipo.setNombre("Anime");
    }

    @Test
    void guardar_exito() {
        when(tipoAnimangaRepository.existsByNombre("Anime")).thenReturn(false);
        when(tipoAnimangaRepository.save(any(TipoAnimanga.class))).thenReturn(tipo);

        String resultado = service.guardar(tipo, 1L);

        assertEquals("TipoAnimanga guardado exitosamente", resultado);
        verify(tipoAnimangaRepository).save(tipo);
    }

    @Test
    void guardar_duplicado() {
        when(tipoAnimangaRepository.existsByNombre("Anime")).thenReturn(true);

        String resultado = service.guardar(tipo, 1L);

        assertEquals("El TipoAnimanga 'Anime' ya existe", resultado);
        verify(tipoAnimangaRepository, never()).save(any());
    }

    @Test
    void guardar_sinNombre() {
        tipo.setNombre(null);

        String resultado = service.guardar(tipo, 1L);

        assertEquals("El nombre del TipoAnimanga es obligatorio", resultado);
        verify(tipoAnimangaRepository, never()).save(any());
    }

    @Test
    void eliminar_exito() {
        TipoAnimanga existente = new TipoAnimanga();
        existente.setIdTipo(1);
        existente.setNombre("Anime");
        when(tipoAnimangaRepository.existsById(1)).thenReturn(true);
        when(tipoAnimangaRepository.findById(1)).thenReturn(Optional.of(existente));
        doNothing().when(tipoAnimangaRepository).deleteById(1);

        boolean resultado = service.eliminar(1, 1L);

        assertTrue(resultado);
        verify(tipoAnimangaRepository).deleteById(1);
    }
}
