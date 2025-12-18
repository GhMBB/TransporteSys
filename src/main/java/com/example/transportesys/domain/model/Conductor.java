package com.example.transportesys.domain.model;

import com.example.transportesys.domain.exception.ConductorLimiteVehiculosException;
import com.example.transportesys.domain.exception.DomainException;
import com.example.transportesys.domain.valueobject.LicenciaConducir;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entidad de dominio que representa un conductor en el sistema de transporte.
 * POJO puro sin dependencias de frameworks (sin anotaciones JPA).
 */
public class Conductor {

    private static final int LIMITE_VEHICULOS = 3;

    private Long id;
    private String nombre;
    private LicenciaConducir licencia;
    private boolean activo;
    private List<Long> vehiculosIds;

    // Campos de auditoría
    private String creadoPor;
    private LocalDateTime fechaCreacion;
    private String modificadoPor;
    private LocalDateTime fechaModificacion;

    public Conductor() {
        this.activo = true;
        this.vehiculosIds = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
    }

    public Conductor(Long id, String nombre, LicenciaConducir licencia) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.licencia = licencia;
    }

    public Conductor(Long id, String nombre, LicenciaConducir licencia, boolean activo, java.util.Set<Long> vehiculosIds) {
        this.id = id;
        this.nombre = nombre;
        this.licencia = licencia;
        this.activo = activo;
        this.vehiculosIds = new ArrayList<>(vehiculosIds != null ? vehiculosIds : Collections.emptySet());
        this.fechaCreacion = LocalDateTime.now();
    }

    /**
     * Asigna un vehículo al conductor.
     * Valida que no se supere el límite de 3 vehículos.
     */
    public void asignarVehiculo(Long vehiculoId) {
        if (vehiculoId == null) {
            throw new DomainException("El ID del vehículo no puede ser nulo");
        }

        if (!this.activo) {
            throw new DomainException("No se puede asignar vehículo a un conductor inactivo");
        }

        if (this.vehiculosIds.contains(vehiculoId)) {
            throw new DomainException("El vehículo ya está asignado a este conductor");
        }

        if (this.vehiculosIds.size() >= LIMITE_VEHICULOS) {
            throw new ConductorLimiteVehiculosException(
                String.format("El conductor no puede tener más de %d vehículos asignados", LIMITE_VEHICULOS)
            );
        }

        this.vehiculosIds.add(vehiculoId);
        this.marcarComoModificado();
    }

    /**
     * Desasigna un vehículo del conductor.
     */
    public void desasignarVehiculo(Long vehiculoId) {
        if (vehiculoId == null) {
            throw new DomainException("El ID del vehículo no puede ser nulo");
        }

        this.vehiculosIds.remove(vehiculoId);
        this.marcarComoModificado();
    }

    /**
     * Verifica si el conductor puede aceptar más vehículos.
     */
    public boolean puedeAsignarMasVehiculos() {
        return this.activo && this.vehiculosIds.size() < LIMITE_VEHICULOS;
    }

    /**
     * Obtiene la cantidad de vehículos asignados.
     */
    public int cantidadVehiculos() {
        return this.vehiculosIds.size();
    }

    /**
     * Alias de cantidadVehiculos() para compatibilidad con tests.
     */
    public int getCantidadVehiculos() {
        return cantidadVehiculos();
    }

    /**
     * Verifica si el conductor tiene vehículos asignados.
     */
    public boolean tieneVehiculos() {
        return !this.vehiculosIds.isEmpty();
    }

    /**
     * Desactiva el conductor (eliminación lógica).
     */
    public void desactivar() {
        this.activo = false;
        this.marcarComoModificado();
    }

    /**
     * Marca el conductor como inactivo (alias para desactivar).
     */
    public void marcarComoInactivo() {
        desactivar();
    }

    /**
     * Activa el conductor.
     */
    public void activar() {
        this.activo = true;
        this.marcarComoModificado();
    }

    private void marcarComoModificado() {
        this.fechaModificacion = LocalDateTime.now();
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new DomainException("El nombre del conductor no puede ser nulo o vacío");
        }
        this.nombre = nombre;
        this.marcarComoModificado();
    }

    public LicenciaConducir getLicencia() {
        return licencia;
    }

    public void setLicencia(LicenciaConducir licencia) {
        this.licencia = licencia;
        this.marcarComoModificado();
    }

    public boolean isActivo() {
        return activo;
    }

    public List<Long> getVehiculosIds() {
        return Collections.unmodifiableList(vehiculosIds);
    }

    public void setVehiculosIds(List<Long> vehiculosIds) {
        this.vehiculosIds = vehiculosIds != null ? new ArrayList<>(vehiculosIds) : new ArrayList<>();
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getModificadoPor() {
        return modificadoPor;
    }

    public void setModificadoPor(String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
