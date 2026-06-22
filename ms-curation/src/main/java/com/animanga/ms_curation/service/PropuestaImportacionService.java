package com.animanga.ms_curation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_curation.dto.AuditRequest;
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

    public String guardar(PropuestaImportacion propuesta) {
        if (propuesta.getIdUsuarioPropone() == null) {
            return "El ID del usuario que propone es obligatorio";
        }
        if (propuesta.getDatosJson() == null || propuesta.getDatosJson().trim().isEmpty()) {
            return "Los datos JSON son obligatorios";
        }

        propuesta.setEstadoCuracion("PENDIENTE");
        propuestaRepository.save(propuesta);
        auditar("Propuesta " + propuesta.getIdPropuesta() + " creada por usuario " + propuesta.getIdUsuarioPropone(), "propuesta_importacion");
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

    public List<PropuestaImportacion> obtenerPorEstado(String estado) {
        return propuestaRepository.findByEstadoCuracion(estado);
    }

    public String actualizarEstado(Long id, String estado, String comentarioRechazo) {
        PropuestaImportacion propuesta = propuestaRepository.findById(id).orElse(null);
        if (propuesta == null) {
            return "Propuesta no encontrada";
        }

        if (!estado.equals("APROBADO") && !estado.equals("RECHAZADO")) {
            return "Estado invalido. Use APROBADO o RECHAZADO";
        }

        propuesta.setEstadoCuracion(estado);
        if (estado.equals("RECHAZADO") && comentarioRechazo != null) {
            propuesta.setComentarioRechazo(comentarioRechazo);
        }
        propuestaRepository.save(propuesta);
        auditar("Propuesta " + id + " " + estado, "propuesta_importacion");
        return "Propuesta " + estado + " exitosamente";
    }

    public boolean eliminar(Long id) {
        PropuestaImportacion propuesta = propuestaRepository.findById(id).orElse(null);
        if (propuesta == null) {
            return false;
        }
        propuestaRepository.delete(propuesta);
        auditar("Propuesta " + id + " eliminada", "propuesta_importacion");
        return true;
    }
}
