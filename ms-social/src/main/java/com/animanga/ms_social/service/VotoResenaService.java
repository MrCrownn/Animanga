package com.animanga.ms_social.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_social.dto.AuditRequest;
import com.animanga.ms_social.model.Resena;
import com.animanga.ms_social.model.VotoResena;
import com.animanga.ms_social.repository.ResenaRepository;
import com.animanga.ms_social.repository.VotoResenaRepository;

@Service
public class VotoResenaService {

    @Autowired
    private VotoResenaRepository votoRepository;

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private void auditar(String accion, String tabla, Long idUsuario) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(idUsuario, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String guardar(VotoResena voto, Long idUsuarioAuth) {
        if (voto.getResena() == null || voto.getResena().getIdResena() == null) {
            return "La resena es obligatoria";
        }
        if (voto.getIdUsuarioVota() == null) {
            return "El ID del usuario que vota es obligatorio";
        }
        voto.setIdUsuarioVota(idUsuarioAuth);
        if (voto.getEsUtil() == null) {
            return "El marcador de utilidad es obligatorio";
        }

        Resena resena = resenaRepository.findById(voto.getResena().getIdResena()).orElse(null);
        if (resena == null) {
            return "La resena con id " + voto.getResena().getIdResena() + " no existe";
        }

        Optional<VotoResena> votoExistente = votoRepository
                .findByResena_IdResenaAndIdUsuarioVota(voto.getResena().getIdResena(), voto.getIdUsuarioVota());
        if (votoExistente.isPresent()) {
            return "El usuario ya ha votado esta resena";
        }

        voto.setResena(resena);
        votoRepository.save(voto);

        resena.setLikeCount(resena.getLikeCount() + 1);
        resenaRepository.save(resena);

        auditar("Voto " + voto.getIdVoto() + " creado para resena " + voto.getResena().getIdResena() + " por usuario " + idUsuarioAuth, "voto_resena", idUsuarioAuth);
        return "Voto guardado exitosamente";
    }

    public List<VotoResena> obtenerTodos() {
        return votoRepository.findAll();
    }

    public Optional<VotoResena> obtenerPorId(Long id) {
        return votoRepository.findById(id);
    }

    public List<VotoResena> obtenerPorResena(Long idResena) {
        return votoRepository.findByResena_IdResena(idResena);
    }

    public boolean eliminar(Long id, Long idUsuarioAuth) {
        VotoResena voto = votoRepository.findById(id).orElse(null);
        if (voto == null) {
            return false;
        }

        Resena resena = voto.getResena();
        resena.setLikeCount(resena.getLikeCount() - 1);
        resenaRepository.save(resena);

        votoRepository.delete(voto);
        auditar("Voto " + id + " eliminado por usuario " + idUsuarioAuth, "voto_resena", idUsuarioAuth);
        return true;
    }
}
