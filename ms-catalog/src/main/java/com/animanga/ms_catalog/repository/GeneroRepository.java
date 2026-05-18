package com.animanga.ms_catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.animanga.ms_catalog.model.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Integer> {
    boolean existsByNombre(String nombre);
}
