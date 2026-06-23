package com.animanga.ms_social.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_social.model.Resena;
import com.animanga.ms_social.repository.ResenaRepository;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ResenaService service;

    private Resena resena;

    @BeforeEach
    void setUp() {
        resena = new Resena();
        resena.setIdResena(1L);
        resena.setIdUsuario(1L);
        resena.setIdAnimanga(1L);
        resena.setTitulo("Excelente anime");
        resena.setPuntuacion(new BigDecimal("8.5"));
        resena.setComentario("Muy buena animación");
    }

    @Test
    void guardar_exito() {
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena);

        String resultado = service.guardar(resena, 1L);

        assertEquals("Resena guardada exitosamente", resultado);
        assertEquals(1L, resena.getIdUsuario());
        assertEquals(0, resena.getLikeCount());
        assertEquals(0, resena.getComentarioCount());
        verify(resenaRepository).save(resena);
    }

    @Test
    void guardar_sinIdUsuario() {
        resena.setIdUsuario(null);

        String resultado = service.guardar(resena, 1L);

        assertEquals("El ID del usuario es obligatorio", resultado);
        verify(resenaRepository, never()).save(any());
    }

    @Test
    void guardar_sinIdAnimanga() {
        resena.setIdAnimanga(null);

        String resultado = service.guardar(resena, 1L);

        assertEquals("El ID del animanga es obligatorio", resultado);
        verify(resenaRepository, never()).save(any());
    }

    @Test
    void guardar_sinTitulo() {
        resena.setTitulo(null);

        String resultado = service.guardar(resena, 1L);

        assertEquals("El titulo de la resena es obligatorio", resultado);
        verify(resenaRepository, never()).save(any());
    }

    @Test
    void guardar_puntuacionNegativa() {
        resena.setPuntuacion(new BigDecimal("-1.0"));

        String resultado = service.guardar(resena, 1L);

        assertEquals("La puntuacion debe estar entre 0.0 y 10.0", resultado);
        verify(resenaRepository, never()).save(any());
    }

    @Test
    void guardar_puntuacionExcedida() {
        resena.setPuntuacion(new BigDecimal("10.1"));

        String resultado = service.guardar(resena, 1L);

        assertEquals("La puntuacion debe estar entre 0.0 y 10.0", resultado);
        verify(resenaRepository, never()).save(any());
    }

    @Test
    void actualizar_puntuacionInvalida() {
        Resena existente = new Resena();
        existente.setIdResena(1L);
        existente.setPuntuacion(new BigDecimal("5.0"));

        Resena actualizado = new Resena();
        actualizado.setPuntuacion(new BigDecimal("15.0"));

        when(resenaRepository.findById(1L)).thenReturn(Optional.of(existente));

        String resultado = service.actualizar(1L, actualizado, 1L);

        assertEquals("La puntuacion debe estar entre 0.0 y 10.0", resultado);
    }
}
