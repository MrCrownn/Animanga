package com.animanga.ms_curation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_curation.model.EstadoCuracion;
import com.animanga.ms_curation.model.PropuestaImportacion;
import com.animanga.ms_curation.repository.PropuestaImportacionRepository;

@ExtendWith(MockitoExtension.class)
class PropuestaImportacionServiceTest {

    @Mock
    private PropuestaImportacionRepository propuestaRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PropuestaImportacionService service;

    private PropuestaImportacion propuesta;

    @BeforeEach
    void setUp() {
        propuesta = new PropuestaImportacion();
        propuesta.setIdPropuesta(1L);
        propuesta.setDatosJson("{\"titulo\": \"Test\"}");
        propuesta.setEstadoCuracion(EstadoCuracion.PENDIENTE);
        propuesta.setIdUsuarioPropone(2L);
    }

    @Test
    void guardar_exito() {
        when(propuestaRepository.save(any(PropuestaImportacion.class))).thenReturn(propuesta);

        String resultado = service.guardar(propuesta, 2L);

        assertEquals("Propuesta creada exitosamente", resultado);
        assertEquals(EstadoCuracion.PENDIENTE, propuesta.getEstadoCuracion());
        assertEquals(2L, propuesta.getIdUsuarioPropone());
        verify(propuestaRepository).save(propuesta);
    }

    @Test
    void guardar_datosJsonVacio() {
        propuesta.setDatosJson("");

        String resultado = service.guardar(propuesta, 2L);

        assertEquals("Los datos JSON son obligatorios", resultado);
        verify(propuestaRepository, never()).save(any());
    }

    @Test
    void guardar_datosJsonNull() {
        propuesta.setDatosJson(null);

        String resultado = service.guardar(propuesta, 2L);

        assertEquals("Los datos JSON son obligatorios", resultado);
        verify(propuestaRepository, never()).save(any());
    }

    @Test
    void actualizarEstado_aprobar_exito() {
        when(propuestaRepository.findById(1L)).thenReturn(Optional.of(propuesta));
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("ok"));
        when(propuestaRepository.save(any(PropuestaImportacion.class))).thenReturn(propuesta);

        String resultado = service.actualizarEstado(1L, EstadoCuracion.APROBADO, null, 1L);

        assertEquals("Propuesta APROBADO exitosamente", resultado);
        assertEquals(EstadoCuracion.APROBADO, propuesta.getEstadoCuracion());
        verify(restTemplate).postForEntity(eq("http://ms-catalog/api/animanga"), any(), eq(String.class));
    }

    @Test
    void actualizarEstado_aprobar_catalogError() {
        when(propuestaRepository.findById(1L)).thenReturn(Optional.of(propuesta));
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        String resultado = service.actualizarEstado(1L, EstadoCuracion.APROBADO, null, 1L);

        assertTrue(resultado.contains("Error al conectar con catalogo"));
        assertEquals(EstadoCuracion.PENDIENTE, propuesta.getEstadoCuracion());
    }

    @Test
    void actualizarEstado_rechazar_conComentario() {
        when(propuestaRepository.findById(1L)).thenReturn(Optional.of(propuesta));
        when(propuestaRepository.save(any(PropuestaImportacion.class))).thenReturn(propuesta);

        String resultado = service.actualizarEstado(1L, EstadoCuracion.RECHAZADO, "Datos incompletos", 1L);

        assertEquals("Propuesta RECHAZADO exitosamente", resultado);
        assertEquals(EstadoCuracion.RECHAZADO, propuesta.getEstadoCuracion());
        assertEquals("Datos incompletos", propuesta.getComentarioRechazo());
    }

    @Test
    void actualizarEstado_noEncontrada() {
        when(propuestaRepository.findById(999L)).thenReturn(Optional.empty());

        String resultado = service.actualizarEstado(999L, EstadoCuracion.APROBADO, null, 1L);

        assertEquals("Propuesta no encontrada", resultado);
        verify(propuestaRepository, never()).save(any());
    }

    @Test
    void actualizarEstado_estadoNoValido() {
        when(propuestaRepository.findById(1L)).thenReturn(Optional.of(propuesta));

        String resultado = service.actualizarEstado(1L, EstadoCuracion.PENDIENTE, null, 1L);

        assertEquals("Estado invalido. Use APROBADO o RECHAZADO", resultado);
        verify(propuestaRepository, never()).save(any());
    }

    @Test
    void eliminar_exito() {
        when(propuestaRepository.findById(1L)).thenReturn(Optional.of(propuesta));
        doNothing().when(propuestaRepository).delete(propuesta);

        boolean resultado = service.eliminar(1L, 1L);

        assertTrue(resultado);
        verify(propuestaRepository).delete(propuesta);
    }

    @Test
    void eliminar_noExiste() {
        when(propuestaRepository.findById(999L)).thenReturn(Optional.empty());

        boolean resultado = service.eliminar(999L, 1L);

        assertFalse(resultado);
        verify(propuestaRepository, never()).delete(any());
    }

    @Test
    void obtenerTodas() {
        when(propuestaRepository.findAll()).thenReturn(Arrays.asList(propuesta));

        List<PropuestaImportacion> resultado = service.obtenerTodas();

        assertEquals(1, resultado.size());
        verify(propuestaRepository).findAll();
    }
}
