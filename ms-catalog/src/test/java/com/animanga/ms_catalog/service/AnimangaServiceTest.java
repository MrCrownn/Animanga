package com.animanga.ms_catalog.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_catalog.model.Animanga;
import com.animanga.ms_catalog.model.Animanga.EstadoEmision;
import com.animanga.ms_catalog.model.Genero;
import com.animanga.ms_catalog.model.TipoAnimanga;
import com.animanga.ms_catalog.repository.AnimangaRepository;
import com.animanga.ms_catalog.repository.TipoAnimangaRepository;

@ExtendWith(MockitoExtension.class)
class AnimangaServiceTest {

    @Mock
    private AnimangaRepository animangaRepository;

    @Mock
    private TipoAnimangaRepository tipoAnimangaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AnimangaService service;

    private Animanga animanga;
    private TipoAnimanga tipoAnime;
    private TipoAnimanga tipoManga;

    @BeforeEach
    void setUp() {
        tipoAnime = new TipoAnimanga();
        tipoAnime.setIdTipo(1);
        tipoAnime.setNombre("Anime");

        tipoManga = new TipoAnimanga();
        tipoManga.setIdTipo(2);
        tipoManga.setNombre("Manga");

        animanga = new Animanga();
        animanga.setIdAnimanga(1L);
        animanga.setTitulo("Test Anime");
        animanga.setDescripcion("Descripción");
        animanga.setFechaEstreno(LocalDate.of(2024, 1, 1));
        animanga.setTipoAnimanga(tipoAnime);
        animanga.setEstadoEmision(EstadoEmision.EN_CURSO);
        animanga.setIdEstudio(1L);
        animanga.setIdAutor(2L);
    }

    @Test
    void guardar_exito() {
        when(tipoAnimangaRepository.findById(1)).thenReturn(Optional.of(tipoAnime));
        when(animangaRepository.existsByTitulo("Test Anime")).thenReturn(false);
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);
        when(animangaRepository.save(any(Animanga.class))).thenReturn(animanga);

        String resultado = service.guardar(animanga, 1L);

        assertEquals("Animanga guardado exitosamente", resultado);
        verify(animangaRepository).save(animanga);
    }

    @Test
    void guardar_sinTitulo() {
        animanga.setTitulo(null);

        String resultado = service.guardar(animanga, 1L);

        assertEquals("El título del Animanga es obligatorio", resultado);
        verify(animangaRepository, never()).save(any());
    }

    @Test
    void guardar_sinFechaEstreno() {
        animanga.setFechaEstreno(null);

        String resultado = service.guardar(animanga, 1L);

        assertEquals("La fecha de estreno es obligatoria", resultado);
        verify(animangaRepository, never()).save(any());
    }

    @Test
    void guardar_sinTipo() {
        animanga.setTipoAnimanga(null);

        String resultado = service.guardar(animanga, 1L);

        assertEquals("El tipo de Animanga es obligatorio", resultado);
        verify(animangaRepository, never()).save(any());
    }

    @Test
    void guardar_tipoNoExiste() {
        when(tipoAnimangaRepository.findById(999)).thenReturn(Optional.empty());
        animanga.getTipoAnimanga().setIdTipo(999);

        String resultado = service.guardar(animanga, 1L);

        assertEquals("El tipo de Animanga con id 999 no existe", resultado);
    }

    @Test
    void guardar_animeConEnPausa() {
        when(tipoAnimangaRepository.findById(1)).thenReturn(Optional.of(tipoAnime));
        animanga.setEstadoEmision(EstadoEmision.EN_PAUSA);

        String resultado = service.guardar(animanga, 1L);

        assertTrue(resultado.contains("EN_PAUSA"));
        assertTrue(resultado.contains("no es válido para Anime"));
    }

    @Test
    void guardar_tituloDuplicado() {
        when(tipoAnimangaRepository.findById(1)).thenReturn(Optional.of(tipoAnime));
        when(animangaRepository.existsByTitulo("Test Anime")).thenReturn(true);

        String resultado = service.guardar(animanga, 1L);

        assertEquals("El Animanga 'Test Anime' ya existe", resultado);
    }

    @Test
    void guardar_estudioNoExiste() {
        when(tipoAnimangaRepository.findById(1)).thenReturn(Optional.of(tipoAnime));
        when(animangaRepository.existsByTitulo("Test Anime")).thenReturn(false);
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);

        String resultado = service.guardar(animanga, 1L);

        assertEquals("El estudio con id 1 no existe", resultado);
    }

    @Test
    void actualizar_exito() {
        Animanga existente = new Animanga();
        existente.setIdAnimanga(1L);
        existente.setTitulo("Original");
        existente.setDescripcion("Original desc");

        Animanga actualizado = new Animanga();
        actualizado.setTitulo("Nuevo Titulo");
        actualizado.setDescripcion("Nueva desc");

        when(animangaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(animangaRepository.existsByTitulo("Nuevo Titulo")).thenReturn(false);
        when(animangaRepository.save(any(Animanga.class))).thenReturn(existente);

        String resultado = service.actualizar(1L, actualizado, 1L);

        assertEquals("Animanga actualizado exitosamente", resultado);
        assertEquals("Nuevo Titulo", existente.getTitulo());
        assertEquals("Nueva desc", existente.getDescripcion());
    }

    @Test
    void listarResumen() {
        Genero genero1 = new Genero();
        genero1.setIdGenero(1);
        genero1.setNombre("Shonen");

        animanga.setGeneros(new HashSet<>(Arrays.asList(genero1)));

        when(animangaRepository.findAll()).thenReturn(Arrays.asList(animanga));

        var resumenes = service.listarResumen();

        assertEquals(1, resumenes.size());
        assertEquals("Test Anime", resumenes.get(0).getTitulo());
        assertEquals(1, resumenes.get(0).getGeneros().size());
    }
}
