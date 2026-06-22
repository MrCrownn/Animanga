package com.animanga.ms_social.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.animanga.ms_social.model.VotoResena;

@Repository
public interface VotoResenaRepository extends JpaRepository<VotoResena, Long> {
    List<VotoResena> findByResena_IdResena(Long idResena);
    List<VotoResena> findByIdUsuarioVota(Long idUsuarioVota);
    Optional<VotoResena> findByResena_IdResenaAndIdUsuarioVota(Long idResena, Long idUsuarioVota);
}
