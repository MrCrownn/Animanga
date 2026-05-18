package com.animanga.ms_auditoria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_auditoria.model.AuditoriaSistema;
import com.animanga.ms_auditoria.service.AuditoriaService;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService;

    @PostMapping
    public ResponseEntity<?> registrarLog(@RequestBody AuditoriaSistema auditoria) {

        AuditoriaSistema nuevoLog = auditoriaService.registrarLog(auditoria);

        return ResponseEntity.ok(nuevoLog);
    }

    @GetMapping
    public List<AuditoriaSistema> obtenerTodos() {
        return auditoriaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public AuditoriaSistema obtenerPorId(@PathVariable Long id) {
        return auditoriaService.obtenerPorId(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<AuditoriaSistema> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return auditoriaService.obtenerPorUsuario(idUsuario);
    }

    @GetMapping("/tabla/{tabla}")
    public List<AuditoriaSistema> obtenerPorTabla(@PathVariable String tabla) {
        return auditoriaService.obtenerPorTabla(tabla);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLog(@PathVariable Long id) {

        String respuesta = auditoriaService.eliminarLog(id);

        return ResponseEntity.ok(respuesta);
    }
}
