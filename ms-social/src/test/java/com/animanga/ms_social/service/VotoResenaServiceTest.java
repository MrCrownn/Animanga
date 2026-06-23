package com.animanga.ms_social.service;

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

import com.animanga.ms_social.model.Resena;
import com.animanga.ms_social.model.VotoResena;
import com.animanga.ms_social.repository.ResenaRepository;
import com.animanga.ms_social.repository.VotoResenaRepository;

@ExtendWith(MockitoExtension.class)
class VotoResenaServiceTest {

    @Mock
    private VotoResenaRepository votoRepository;

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private VotoResenaService service;

    private Resena resena;
    private VotoResena voto;

    @BeforeEach
    void setUp() {
        resena = new Resena();
        resena.setIdResena(1L);
        resena.setLikeCount(5);

        voto = new VotoResena();
        voto.setIdVoto(1L);
        voto.setResena(resena);
        voto.setIdUsuarioVota(2L);
        voto.setEsUtil(true);
    }

    @Test
    void guardar_exito() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));
        when(votoRepository.findByResena_IdResenaAndIdUsuarioVota(1L, 2L)).thenReturn(Optional.empty());
        when(votoRepository.save(any(VotoResena.class))).thenReturn(voto);
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena);

        String resultado = service.guardar(voto, 2L);

        assertEquals("Voto guardado exitosamente", resultado);
        assertEquals(2L, voto.getIdUsuarioVota());
        assertEquals(6, resena.getLikeCount());
    }

    @Test
    void guardar_sinResena() {
        voto.setResena(null);

        String resultado = service.guardar(voto, 2L);

        assertEquals("La resena es obligatoria", resultado);
        verify(votoRepository, never()).save(any());
    }

    @Test
    void guardar_resenaNoExiste() {
        when(resenaRepository.findById(999L)).thenReturn(Optional.empty());
        Resena resenaInexistente = new Resena();
        resenaInexistente.setIdResena(999L);
        voto.setResena(resenaInexistente);

        String resultado = service.guardar(voto, 2L);

        assertEquals("La resena con id 999 no existe", resultado);
        verify(votoRepository, never()).save(any());
    }

    @Test
    void guardar_votoDuplicado() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));
        when(votoRepository.findByResena_IdResenaAndIdUsuarioVota(1L, 2L)).thenReturn(Optional.of(voto));

        String resultado = service.guardar(voto, 2L);

        assertEquals("El usuario ya ha votado esta resena", resultado);
        verify(votoRepository, never()).save(voto);
    }

    @Test
    void guardar_sinEsUtil() {
        voto.setEsUtil(null);

        String resultado = service.guardar(voto, 2L);

        assertEquals("El marcador de utilidad es obligatorio", resultado);
        verify(votoRepository, never()).save(any());
    }

    @Test
    void eliminar_decrementaContador() {
        when(votoRepository.findById(1L)).thenReturn(Optional.of(voto));
        doNothing().when(votoRepository).delete(voto);
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena);

        boolean resultado = service.eliminar(1L, 2L);

        assertTrue(resultado);
        assertEquals(4, resena.getLikeCount());
        verify(votoRepository).delete(voto);
    }
}
