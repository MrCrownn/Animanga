package com.animanga.ms_media.service;

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

import com.animanga.ms_media.model.RecursoMultimedia;
import com.animanga.ms_media.repository.RecursoMultimediaRepository;

@ExtendWith(MockitoExtension.class)
class RecursoMultimediaServiceTest {

    @Mock
    private RecursoMultimediaRepository recursoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RecursoMultimediaService service;

    private RecursoMultimedia recurso;

    @BeforeEach
    void setUp() {
        recurso = new RecursoMultimedia();
        recurso.setIdRecurso(1L);
        recurso.setIdAnimanga(1L);
        recurso.setTipoRecurso("PORTADA");
        recurso.setUrlRecurso("https://example.com/image.jpg");
    }

    @Test
    void guardar_exito() {
        when(restTemplate.getForObject(anyString(), eq(Object.class))).thenReturn(new Object());
        when(recursoRepository.save(any(RecursoMultimedia.class))).thenReturn(recurso);

        String resultado = service.guardar(recurso, 1L);

        assertEquals("Recurso multimedia guardado exitosamente", resultado);
        verify(recursoRepository).save(recurso);
    }

    @Test
    void guardar_sinIdAnimanga() {
        recurso.setIdAnimanga(null);

        String resultado = service.guardar(recurso, 1L);

        assertEquals("El ID del animanga es obligatorio", resultado);
        verify(recursoRepository, never()).save(any());
    }

    @Test
    void guardar_animangaNoExiste() {
        when(restTemplate.getForObject(anyString(), eq(Object.class)))
                .thenThrow(new RuntimeException("Animanga no encontrado"));

        String resultado = service.guardar(recurso, 1L);

        assertEquals("El animanga con id 1 no existe", resultado);
        verify(recursoRepository, never()).save(any());
    }

    @Test
    void guardar_sinTipoRecurso() {
        recurso.setTipoRecurso(null);
        when(restTemplate.getForObject(anyString(), eq(Object.class))).thenReturn(new Object());

        String resultado = service.guardar(recurso, 1L);

        assertEquals("El tipo de recurso es obligatorio", resultado);
        verify(recursoRepository, never()).save(any());
    }

    @Test
    void guardar_sinUrl() {
        recurso.setUrlRecurso(null);
        when(restTemplate.getForObject(anyString(), eq(Object.class))).thenReturn(new Object());

        String resultado = service.guardar(recurso, 1L);

        assertEquals("La URL del recurso es obligatoria", resultado);
        verify(recursoRepository, never()).save(any());
    }

    @Test
    void actualizar_exito() {
        RecursoMultimedia existente = new RecursoMultimedia();
        existente.setIdRecurso(1L);
        existente.setUrlRecurso("https://old-url.jpg");

        RecursoMultimedia actualizado = new RecursoMultimedia();
        actualizado.setUrlRecurso("https://new-url.jpg");
        actualizado.setIdAnimanga(1L);

        when(recursoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(restTemplate.getForObject(anyString(), eq(Object.class))).thenReturn(new Object());
        when(recursoRepository.save(any(RecursoMultimedia.class))).thenReturn(existente);

        String resultado = service.actualizar(1L, actualizado, 1L);

        assertEquals("Recurso multimedia actualizado exitosamente", resultado);
        assertEquals("https://new-url.jpg", existente.getUrlRecurso());
    }
}
