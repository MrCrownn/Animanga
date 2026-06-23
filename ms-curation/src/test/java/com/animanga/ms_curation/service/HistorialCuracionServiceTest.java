package com.animanga.ms_curation.service;

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

import com.animanga.ms_curation.model.HistorialCuracion;
import com.animanga.ms_curation.model.PropuestaImportacion;
import com.animanga.ms_curation.repository.HistorialCuracionRepository;
import com.animanga.ms_curation.repository.PropuestaImportacionRepository;

@ExtendWith(MockitoExtension.class)
class HistorialCuracionServiceTest {

    @Mock
    private HistorialCuracionRepository historialRepository;

    @Mock
    private PropuestaImportacionRepository propuestaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HistorialCuracionService service;

    private HistorialCuracion historial;
    private PropuestaImportacion propuesta;

    @BeforeEach
    void setUp() {
        propuesta = new PropuestaImportacion();
        propuesta.setIdPropuesta(1L);

        historial = new HistorialCuracion();
        historial.setIdCuracion(1L);
        historial.setPropuesta(propuesta);
        historial.setDecision("APROBADO");
    }

    @Test
    void guardar_exito() {
        when(propuestaRepository.findById(1L)).thenReturn(Optional.of(propuesta));
        when(historialRepository.save(any(HistorialCuracion.class))).thenReturn(historial);

        String resultado = service.guardar(historial, 1L);

        assertEquals("Historial guardado exitosamente", resultado);
        assertEquals(1L, historial.getIdModerador());
        verify(historialRepository).save(historial);
    }

    @Test
    void guardar_sinPropuesta() {
        historial.setPropuesta(null);

        String resultado = service.guardar(historial, 1L);

        assertEquals("La propuesta es obligatoria", resultado);
        verify(historialRepository, never()).save(any());
    }

    @Test
    void guardar_sinDecision() {
        historial.setDecision(null);

        String resultado = service.guardar(historial, 1L);

        assertEquals("La decision es obligatoria", resultado);
        verify(historialRepository, never()).save(any());
    }

    @Test
    void guardar_propuestaNoExiste() {
        PropuestaImportacion propInexistente = new PropuestaImportacion();
        propInexistente.setIdPropuesta(999L);
        historial.setPropuesta(propInexistente);

        when(propuestaRepository.findById(999L)).thenReturn(Optional.empty());

        String resultado = service.guardar(historial, 1L);

        assertEquals("La propuesta con id 999 no existe", resultado);
        verify(historialRepository, never()).save(any());
    }
}
