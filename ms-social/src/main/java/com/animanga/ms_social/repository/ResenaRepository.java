package com.animanga.ms_social.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animanga.ms_social.model.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByIdUsuario(Long idUsuario);
    List<Resena> findByIdAnimanga(Long idAnimanga);
}
