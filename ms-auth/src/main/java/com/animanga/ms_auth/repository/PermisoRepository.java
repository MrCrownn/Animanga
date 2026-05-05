package com.animanga.ms_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animanga.ms_auth.model.Permiso;

@Repository
public interface PermisoRepository extends JpaRepository<com.animanga.ms_auth.model.Permiso, Integer> {
    Permiso findByAccion(String accion);
    boolean existsByAccion(String accion);

}
