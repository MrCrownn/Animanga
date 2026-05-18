package com.animanga.ms_auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.animanga.ms_auth.dto.AuditRequest;
import com.animanga.ms_auth.model.Permiso;
import com.animanga.ms_auth.model.Rol;
import com.animanga.ms_auth.repository.PermisoRepository;
import com.animanga.ms_auth.repository.RolRepository;

@Service
public class RolService {
  
    @Autowired
    private RolRepository rolRepository;

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

    public String guardar(Rol rol) {
       
        if (rolRepository.existsByNombre(rol.getNombre())) {
            return "El Rol '" + rol.getNombre() + "' ya existe";
        }
        
        rolRepository.save(rol);
        auditar("Rol '" + rol.getNombre() + "' creado", "rol");
        return "Rol guardado exitosamente";
}

    public List<Rol> obtenerTodos(){
        return rolRepository.findAll();
    }

    public Optional<Rol> obtenerPorId(Integer id){
        return rolRepository.findById(id);
    }
    public Optional<Rol> obtenerPorNombre(String nombre){
        return rolRepository.findByNombre(nombre);
    }
    public boolean eliminar(Integer id){
        if(rolRepository.existsById(id)){
            String nombre = rolRepository.findById(id).get().getNombre();
            rolRepository.deleteById(id);
            auditar("Rol '" + nombre + "' eliminado", "rol");
            return true;
        }
        return false;
    }
    public String asignarPermiso(Integer rolId, Integer permisoId){
        Rol rol = rolRepository.findById(rolId).orElse(null);

        if (rol == null) {
            return "Rol No Encontrado"; 
        }
        Permiso permiso = permisoRepository.findById(permisoId).orElse(null);
        if (permiso == null) {
            return "Permiso No Encontrado"; 
        }
        rol.getPermisos().add(permiso);
        rolRepository.save(rol);
        auditar("Permiso '" + permiso.getAccion() + "' asignado al rol '" + rol.getNombre() + "'", "rol");
        return "Permiso Asignado";
    }
    public String removerPermiso(Integer rolId, Integer permisoId){
        Rol rol = rolRepository.findById(rolId).orElse(null);

        if (rol == null) {
            return "Rol No Encontrado"; 
        }
        Permiso permiso = permisoRepository.findById(permisoId).orElse(null);
        if (permiso == null) {
            return "Permiso No Encontrado"; 
        }
        rol.getPermisos().remove(permiso);
        rolRepository.save(rol);
        auditar("Permiso '" + permiso.getAccion() + "' removido del rol '" + rol.getNombre() + "'", "rol");
        return "Permiso Removido";
    }
}
