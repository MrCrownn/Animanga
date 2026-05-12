package com.animanga.ms_production.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animanga.ms_production.model.Entidad;

@Repository
public interface EntidadRepository extends JpaRepository<Entidad, Integer> {
    List<Entidad> findByTipoEntidad_Nombre(String nombre);
    boolean existsByNombreAndTipoEntidad_IdTipo(String nombre, Integer idTipo);
    List <Entidad> findByTipoEntidad_Pais(String pais);
}