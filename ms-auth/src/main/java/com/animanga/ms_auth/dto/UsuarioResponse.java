package com.animanga.ms_auth.dto;

public class UsuarioResponse {

    private Long id;
    private String username;
    private String email;
    private String estadoCuenta;
    private String rolNombre;

    public UsuarioResponse(Long id, String username, String email, String estadoCuenta, String rolNombre) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.estadoCuenta = estadoCuenta;
        this.rolNombre = rolNombre;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public String getRolNombre() {
        return rolNombre;
    }
}
