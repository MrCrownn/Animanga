package com.animanga.ms_auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_auth.dto.AuditRequest;
import com.animanga.ms_auth.model.Permiso;
import com.animanga.ms_auth.repository.PermisoRepository;

@Service
public class PermisoService {
    @Autowired
    private PermisoRepository permisoRepository;

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

    public Permiso guardarPermiso(Permiso permiso) {
        Permiso nuevo = permisoRepository.save(permiso);
        auditar("Permiso '" + nuevo.getAccion() + "' creado", "permiso");
        return nuevo;
    }
    public List<Permiso> listarTodos() {
        return permisoRepository.findAll();
    }
    public Permiso buscarPorId(Integer id) {
        return permisoRepository.findById(id).orElse(null);
    }
    public Permiso buscarPorAccion(String accion) {
        return permisoRepository.findByAccion(accion);
    }
    public boolean eliminarPermiso(Integer id) {
        if (permisoRepository.existsById(id)) {
            String accion = permisoRepository.findById(id).get().getAccion();
            permisoRepository.deleteById(id);
            auditar("Permiso '" + accion + "' eliminado", "permiso");
            return true;
        }
        return false;
    }
}
