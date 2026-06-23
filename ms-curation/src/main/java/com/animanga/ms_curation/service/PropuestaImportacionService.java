package com.animanga.ms_curation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_curation.dto.AuditRequest;
import com.animanga.ms_curation.model.EstadoCuracion;
import com.animanga.ms_curation.model.PropuestaImportacion;
import com.animanga.ms_curation.repository.PropuestaImportacionRepository;

@Service
public class PropuestaImportacionService {

    @Autowired
    private PropuestaImportacionRepository propuestaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private void auditar(String accion, String tabla) {
        try {
            String url = "http://ms-auditoria/api/auditoria";
            AuditRequest request = new AuditRequest(null, accion, tabla);
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            System.err.println("Error al auditar: " + e.getMessage());
        }
    }

    public String guardar(PropuestaImportacion propuesta, Long idUsuarioAuth) {
        if (propuesta.getDatosJson() == null || propuesta.getDatosJson().trim().isEmpty()) {
            return "Los datos JSON son obligatorios";
        }

        propuesta.setIdUsuarioPropone(idUsuarioAuth);
        propuesta.setEstadoCuracion(EstadoCuracion.PENDIENTE);
        propuestaRepository.save(propuesta);
        auditar("Propuesta " + propuesta.getIdPropuesta() + " creada por usuario " + idUsuarioAuth, "propuesta_importacion");
        return "Propuesta creada exitosamente";
    }

    public List<PropuestaImportacion> obtenerTodas() {
        return propuestaRepository.findAll();
    }

    public Optional<PropuestaImportacion> obtenerPorId(Long id) {
        return propuestaRepository.findById(id);
    }

    public List<PropuestaImportacion> obtenerPorIdUsuario(Long idUsuario) {
        return propuestaRepository.findByIdUsuarioPropone(idUsuario);
    }

    public List<PropuestaImportacion> obtenerPorEstado(EstadoCuracion estado) {
        return propuestaRepository.findByEstadoCuracion(estado);
    }

    public String actualizarEstado(Long id, EstadoCuracion estado, String comentarioRechazo, Long idUsuarioAuth) {
        PropuestaImportacion propuesta = propuestaRepository.findById(id).orElse(null);
        if (propuesta == null) {
            return "Propuesta no encontrada";
        }

        if (estado != EstadoCuracion.APROBADO && estado != EstadoCuracion.RECHAZADO) {
            return "Estado invalido. Use APROBADO o RECHAZADO";
        }

        if (estado == EstadoCuracion.APROBADO) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("X-User-Id", String.valueOf(idUsuarioAuth));

                HttpEntity<String> request = new HttpEntity<>(propuesta.getDatosJson(), headers);
                ResponseEntity<String> response = restTemplate.postForEntity(
                        "http://ms-catalog/api/animanga", request, String.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                    return "Error al crear animanga en catalogo: " + response.getBody();
                }
            } catch (Exception e) {
                return "Error al conectar con catalogo: " + e.getMessage();
            }
        }

        propuesta.setEstadoCuracion(estado);
        if (estado == EstadoCuracion.RECHAZADO && comentarioRechazo != null) {
            propuesta.setComentarioRechazo(comentarioRechazo);
        }
        propuestaRepository.save(propuesta);
        auditar("Propuesta " + id + " " + estado.name() + " por usuario " + idUsuarioAuth, "propuesta_importacion");
        return "Propuesta " + estado.name() + " exitosamente";
    }

    public boolean eliminar(Long id, Long idUsuarioAuth) {
        PropuestaImportacion propuesta = propuestaRepository.findById(id).orElse(null);
        if (propuesta == null) {
            return false;
        }
        propuestaRepository.delete(propuesta);
        auditar("Propuesta " + id + " eliminada por usuario " + idUsuarioAuth, "propuesta_importacion");
        return true;
    }
}
