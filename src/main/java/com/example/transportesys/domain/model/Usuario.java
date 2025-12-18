package com.example.transportesys.domain.model;

import com.example.transportesys.domain.enums.RolUsuario;
import com.example.transportesys.domain.exception.DomainException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad de dominio que representa un usuario en el sistema.
 * POJO puro sin dependencias de frameworks (sin anotaciones JPA).
 */
public class Usuario {

    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean activo;
    private Set<RolUsuario> roles;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimoAcceso;

    public Usuario() {
        this.activo = true;
        this.roles = new HashSet<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    public Usuario(String username, String password, String email) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Agrega un rol al usuario.
     */
    public void agregarRol(RolUsuario rol) {
        if (rol == null) {
            throw new DomainException("El rol no puede ser nulo");
        }
        this.roles.add(rol);
    }

    /**
     * Verifica si el usuario tiene un rol específico.
     */
    public boolean tieneRol(RolUsuario rol) {
        return this.roles.contains(rol);
    }

    /**
     * Registra el último acceso del usuario.
     */
    public void registrarAcceso() {
        this.ultimoAcceso = LocalDateTime.now();
    }

    /**
     * Desactiva el usuario.
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Activa el usuario.
     */
    public void activar() {
        this.activo = true;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new DomainException("El username no puede ser nulo o vacío");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public Set<RolUsuario> getRoles() {
        return new HashSet<>(roles);
    }

    public void setRoles(Set<RolUsuario> roles) {
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
}
