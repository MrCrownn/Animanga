package com.animanga.ms_production.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animanga.ms_production.model.TipoEntidad;

@Repository
public interface TipoEntidadRepository extends JpaRepository<TipoEntidad, Integer> {
    boolean existsByNombre(String nombre);
}