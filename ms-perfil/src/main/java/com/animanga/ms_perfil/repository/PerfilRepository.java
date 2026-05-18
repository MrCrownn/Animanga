package com.animanga.ms_perfil.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.animanga.ms_perfil.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByIdUsuario(Long idUsuario);
}
