package com.animanga.ms_library.service;

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

import com.animanga.ms_library.model.BibliotecaUsuario;
import com.animanga.ms_library.model.EstadoSeguimiento;
import com.animanga.ms_library.model.ProgresoAnime;
import com.animanga.ms_library.repository.BibliotecaUsuarioRepository;
import com.animanga.ms_library.repository.ProgresoAnimeRepository;

@ExtendWith(MockitoExtension.class)
class BibliotecaServiceTest {

    @Mock
    private BibliotecaUsuarioRepository bibliotecaRepository;

    @Mock
    private ProgresoAnimeRepository progresoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BibliotecaService service;

    private BibliotecaUsuario entrada;
    private ProgresoAnime progreso;

    @BeforeEach
    void setUp() {
        entrada = new BibliotecaUsuario();
        entrada.setIdBiblioteca(1L);
        entrada.setIdUsuario(2L);
        entrada.setIdAnimanga(1L);
        entrada.setEstadoSeguimiento(EstadoSeguimiento.LEYENDO);

        progreso = new ProgresoAnime();
        progreso.setIdProgreso(1L);
        progreso.setIdBiblioteca(1L);
        progreso.setCapituloActual(5);
    }

    @Test
    void agregar_exito() {
        when(bibliotecaRepository.findByIdUsuarioAndIdAnimanga(2L, 1L)).thenReturn(Optional.empty());
        when(bibliotecaRepository.save(any(BibliotecaUsuario.class))).thenReturn(entrada);

        String resultado = service.agregarABiblioteca(entrada, 2L);

        assertEquals("Animanga agregado a la biblioteca exitosamente", resultado);
        assertEquals(2L, entrada.getIdUsuario());
        verify(bibliotecaRepository).save(entrada);
    }

    @Test
    void agregar_sinIdAnimanga() {
        entrada.setIdAnimanga(null);

        String resultado = service.agregarABiblioteca(entrada, 2L);

        assertEquals("El ID del animanga es obligatorio", resultado);
        verify(bibliotecaRepository, never()).save(any());
    }

    @Test
    void agregar_sinEstado() {
        entrada.setEstadoSeguimiento(null);

        String resultado = service.agregarABiblioteca(entrada, 2L);

        assertEquals("El estado de seguimiento es obligatorio", resultado);
        verify(bibliotecaRepository, never()).save(any());
    }

    @Test
    void agregar_duplicado() {
        when(bibliotecaRepository.findByIdUsuarioAndIdAnimanga(2L, 1L)).thenReturn(Optional.of(entrada));

        String resultado = service.agregarABiblioteca(entrada, 2L);

        assertEquals("El animanga ya esta en la biblioteca del usuario", resultado);
        verify(bibliotecaRepository, never()).save(any());
    }

    @Test
    void actualizarEstado_exito() {
        when(bibliotecaRepository.findById(1L)).thenReturn(Optional.of(entrada));
        when(bibliotecaRepository.save(any(BibliotecaUsuario.class))).thenReturn(entrada);

        String resultado = service.actualizarEstadoSeguimiento(1L, EstadoSeguimiento.COMPLETADO, 2L);

        assertEquals("Estado de seguimiento actualizado exitosamente", resultado);
        assertEquals(EstadoSeguimiento.COMPLETADO, entrada.getEstadoSeguimiento());
    }

    @Test
    void actualizarProgreso_exito() {
        when(bibliotecaRepository.findById(1L)).thenReturn(Optional.of(entrada));
        when(progresoRepository.findTopByIdBibliotecaOrderByFechaActualizacionDesc(1L)).thenReturn(Optional.of(progreso));
        when(progresoRepository.save(any(ProgresoAnime.class))).thenReturn(progreso);

        String resultado = service.actualizarProgreso(1L, 10, 2L);

        assertEquals("Progreso actualizado exitosamente", resultado);
        assertEquals(10, progreso.getCapituloActual());
    }

    @Test
    void actualizarProgreso_capituloInvalido() {
        when(bibliotecaRepository.findById(1L)).thenReturn(Optional.of(entrada));

        String resultado = service.actualizarProgreso(1L, -1, 2L);

        assertEquals("El capitulo actual debe ser un numero valido", resultado);
        verify(progresoRepository, never()).save(any());
    }
}
