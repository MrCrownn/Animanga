package com.animanga.ms_production.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animanga.ms_production.model.Nacionalidad;

@Repository
public interface NacionalidadRepository extends JpaRepository<Nacionalidad, Integer> {
    boolean existsByPais(String pais);
}