package com.animanga.ms_social.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.animanga.ms_social.model.VotoResena;
import com.animanga.ms_social.service.VotoResenaService;

@RestController
@RequestMapping("/api/votos")
public class VotoResenaController {

    @Autowired
    private VotoResenaService votoService;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody VotoResena voto) {
        if (voto.getResena() == null || voto.getResena().getIdResena() == null) {
            return ResponseEntity.badRequest().body("La resena es obligatoria");
        }
        if (voto.getIdUsuarioVota() == null) {
            return ResponseEntity.badRequest().body("El ID del usuario que vota es obligatorio");
        }
        if (voto.getEsUtil() == null) {
            return ResponseEntity.badRequest().body("El marcador de utilidad es obligatorio");
        }

        String respuesta = votoService.guardar(voto);
        if (respuesta.equals("Voto guardado exitosamente")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }
        if (respuesta.contains("no existe")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
        if (respuesta.contains("ya ha votado")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @GetMapping
    public List<VotoResena> obtenerTodos() {
        return votoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<VotoResena> voto = votoService.obtenerPorId(id);
        if (voto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voto no encontrado");
        }
        return ResponseEntity.ok(voto.get());
    }

    @GetMapping("/resena/{idResena}")
    public List<VotoResena> obtenerPorResena(@PathVariable Long idResena) {
        return votoService.obtenerPorResena(idResena);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean eliminado = votoService.eliminar(id);
        if (eliminado) {
            return ResponseEntity.ok("Voto eliminado exitosamente");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Voto no encontrado");
    }
}
