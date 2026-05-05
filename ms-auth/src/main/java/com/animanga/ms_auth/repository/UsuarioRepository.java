package com.animanga.ms_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.animanga.ms_auth.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Usuario findByUsername(String username);
    public Usuario findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
