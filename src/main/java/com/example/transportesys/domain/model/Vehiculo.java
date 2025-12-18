package com.example.transportesys.domain.model;

import com.example.transportesys.domain.exception.DomainException;
import com.example.transportesys.domain.valueobject.Capacidad;
import com.example.transportesys.domain.valueobject.Peso;
import com.example.transportesys.domain.valueobject.Placa;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un vehículo en el sistema de transporte.
 * POJO puro sin dependencias de frameworks (sin anotaciones JPA).
 */
public class Vehiculo {

    private Long id;
    private Placa placa;
    private Capacidad capacidad;
    private boolean activo;
    private Long conductorId;

    // Campos de auditoría
    private String creadoPor;
    private LocalDateTime fechaCreacion;
    private String modificadoPor;
    private LocalDateTime fechaModificacion;

    public Vehiculo() {
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Vehiculo(Long id, Placa placa, Capacidad capacidad) {
        this();
        this.id = id;
        this.placa = placa;
        this.capacidad = capacidad;
    }

    public Vehiculo(Long id, Placa placa, Capacidad capacidad, boolean activo, Long conductorId) {
        this();
        this.id = id;
        this.placa = placa;
        this.capacidad = capacidad;
        this.activo = activo;
        this.conductorId = conductorId;
    }

    /**
     * Asigna un conductor al vehículo.
     */
    public void asignarConductor(Long conductorId) {
        if (conductorId == null) {
            throw new DomainException("El ID del conductor no puede ser nulo");
        }
        if (!this.activo) {
            throw new DomainException("No se puede asignar conductor a un vehículo inactivo");
        }
        this.conductorId = conductorId;
        this.marcarComoModificado();
    }

    /**
     * Desasigna el conductor del vehículo.
     */
    public void desasignarConductor() {
        this.conductorId = null;
        this.marcarComoModificado();
    }

    /**
     * Verifica si el vehículo tiene capacidad suficiente para un peso dado.
     */
    public boolean tieneCapacidadPara(Peso peso) {
        return this.capacidad.esSuficientePara(peso);
    }

    /**
     * Verifica si el vehículo está libre (sin conductor asignado).
     */
    public boolean estaLibre() {
        return this.conductorId == null && this.activo;
    }

    /**
     * Verifica si el vehículo está asignado a un conductor.
     */
    public boolean estaAsignado() {
        return this.conductorId != null;
    }

    /**
     * Desactiva el vehículo (eliminación lógica).
     */
    public void desactivar() {
        this.activo = false;
        this.marcarComoModificado();
    }

    /**
     * Activa el vehículo.
     */
    public void activar() {
        this.activo = true;
        this.marcarComoModificado();
    }

    /**
     * Marca el vehículo como inactivo (alias para desactivar).
     */
    public void marcarComoInactivo() {
        desactivar();
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

    public Placa getPlaca() {
        return placa;
    }

    public void setPlaca(Placa placa) {
        this.placa = placa;
        this.marcarComoModificado();
    }

    public Capacidad getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Capacidad capacidad) {
        this.capacidad = capacidad;
        this.marcarComoModificado();
    }

    public boolean isActivo() {
        return activo;
    }

    public Long getConductorId() {
        return conductorId;
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
