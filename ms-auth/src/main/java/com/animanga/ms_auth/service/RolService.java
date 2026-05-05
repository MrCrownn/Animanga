package com.animanga.ms_auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public String guardar(Rol rol) {
       
        if (rolRepository.existsByNombre(rol.getNombre())) {
            return "El Rol '" + rol.getNombre() + "' ya existe";
        }
        
        // 2. Si no existe, lo guardamos y retornamos éxito
        rolRepository.save(rol);
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
            rolRepository.deleteById(id);
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
        return "Permiso Removido";
    }
}
