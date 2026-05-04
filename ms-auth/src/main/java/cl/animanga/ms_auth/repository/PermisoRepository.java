package cl.animanga.ms_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisoRepository extends JpaRepository<cl.animanga.ms_auth.model.Permiso, Integer> {
    cl.animanga.ms_auth.model.Permiso findByAccion(String accion);

}
