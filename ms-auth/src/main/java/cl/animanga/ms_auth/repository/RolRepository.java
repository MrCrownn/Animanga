package cl.animanga.ms_auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.animanga.ms_auth.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional <Rol> findByNombre(String nombre);
}
