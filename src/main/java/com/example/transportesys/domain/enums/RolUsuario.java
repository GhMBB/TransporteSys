package com.example.transportesys.domain.enums;

/**
 * Roles de usuario en el sistema.
 */
public enum RolUsuario {
    ADMIN("Administrador"),
    CONDUCTOR("Conductor"),
    CLIENTE("Cliente");

    private final String descripcion;

    RolUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
