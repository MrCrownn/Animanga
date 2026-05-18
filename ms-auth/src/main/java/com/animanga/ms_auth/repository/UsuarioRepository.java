package com.animanga.ms_auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.animanga.ms_auth.dto.UsuarioResponse;
import com.animanga.ms_auth.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Usuario findByUsername(String username);
    public Usuario findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query(value = """
            SELECT u.id, u.username, u.email, u.estado_cuenta, r.nombre AS rolNombre
            FROM usuario u
            LEFT JOIN rol r ON u.id_rol = r.id
            WHERE u.id = :id
            """, nativeQuery = true)
    Optional<UsuarioResponse> encontrarUsuarioDTO(@Param("id") Long id);
}
