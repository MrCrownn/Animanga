package cl.animanga.ms_auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.animanga.ms_auth.model.Rol;
import cl.animanga.ms_auth.repository.RolRepository;

@Service
public class RolService {
  
    @Autowired
    private RolRepository rolRepository;
    public Rol guardar (Rol rol){
        return rolRepository.save(rol);
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
}
