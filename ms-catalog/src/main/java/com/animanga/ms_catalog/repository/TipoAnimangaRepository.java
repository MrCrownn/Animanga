package com.animanga.ms_catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animanga.ms_catalog.model.TipoAnimanga;

@Repository
public interface TipoAnimangaRepository extends JpaRepository<TipoAnimanga, Long> {

}
