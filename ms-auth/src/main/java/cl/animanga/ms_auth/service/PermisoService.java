package cl.animanga.ms_auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.animanga.ms_auth.model.Permiso;
import cl.animanga.ms_auth.repository.PermisoRepository;

@Service
public class PermisoService {
    @Autowired
    private PermisoRepository permisoRepository;
    
    public Permiso guardarPermiso(Permiso permiso) {
        return permisoRepository.save(permiso);
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
            permisoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
