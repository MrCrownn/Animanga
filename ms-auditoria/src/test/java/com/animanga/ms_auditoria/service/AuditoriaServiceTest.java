package com.animanga.ms_auditoria.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.animanga.ms_auditoria.model.AuditoriaSistema;
import com.animanga.ms_auditoria.repository.AuditoriaRepository;

@ExtendWith(MockitoExtension.class)
class AuditoriaServiceTest {

    @Mock
    private AuditoriaRepository auditoriaRepository;

    @InjectMocks
    private AuditoriaService service;

    @Test
    void registrarLog_asignaFecha() {
        AuditoriaSistema log = new AuditoriaSistema();
        log.setDescripcionAccion("Test");
        log.setTablaAfectada("usuario");

        when(auditoriaRepository.save(any(AuditoriaSistema.class))).thenAnswer(i -> i.getArgument(0));

        AuditoriaSistema resultado = service.registrarLog(log);

        assertNotNull(resultado.getFechaHora());
        assertEquals("Test", resultado.getDescripcionAccion());
        verify(auditoriaRepository).save(log);
    }

    @Test
    void eliminarLog_exito() {
        when(auditoriaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(auditoriaRepository).deleteById(1L);

        String resultado = service.eliminarLog(1L);

        assertEquals("Log eliminado", resultado);
        verify(auditoriaRepository).deleteById(1L);
    }

    @Test
    void eliminarLog_noExiste() {
        when(auditoriaRepository.existsById(999L)).thenReturn(false);

        String resultado = service.eliminarLog(999L);

        assertEquals("El log no existe", resultado);
        verify(auditoriaRepository, never()).deleteById(any());
    }

    @Test
    void obtenerTodos() {
        service.obtenerTodos();
        verify(auditoriaRepository).findAll();
    }
}
