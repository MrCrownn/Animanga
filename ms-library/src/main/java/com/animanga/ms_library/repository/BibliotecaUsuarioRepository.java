package com.animanga.ms_library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.animanga.ms_library.model.BibliotecaUsuario;
import com.animanga.ms_library.model.EstadoSeguimiento;

public interface BibliotecaUsuarioRepository extends JpaRepository<BibliotecaUsuario, Long> {

    List<BibliotecaUsuario> findByIdUsuario(Long idUsuario);

    Optional<BibliotecaUsuario> findByIdUsuarioAndIdAnimanga(Long idUsuario, Long idAnimanga);

    List<BibliotecaUsuario> findByIdUsuarioAndEstadoSeguimiento(Long idUsuario, EstadoSeguimiento estadoSeguimiento);
}
