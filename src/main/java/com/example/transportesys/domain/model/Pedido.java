package com.example.transportesys.domain.model;

import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.exception.DomainException;
import com.example.transportesys.domain.valueobject.Peso;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un pedido en el sistema de transporte.
 * POJO puro sin dependencias de frameworks (sin anotaciones JPA).
 */
public class Pedido {

    private Long id;
    private String descripcion;
    private Peso peso;
    private EstadoPedido estado;
    private Long vehiculoId;
    private Long conductorId;
    private String direccionOrigen;
    private String direccionDestino;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Pedido() {
        this.estado = EstadoPedido.PENDIENTE;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Pedido(Long id, String descripcion, Peso peso) {
        this();
        this.id = id;
        this.descripcion = descripcion;
        this.peso = peso;
    }

    public Pedido(Long id, String descripcion, Peso peso, String direccionOrigen, String direccionDestino) {
        this(id, descripcion, peso);
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
    }

    public Pedido(Long id, String descripcion, Peso peso, Long vehiculoId, Long conductorId,
                  EstadoPedido estado, String direccionOrigen, String direccionDestino) {
        this.id = id;
        this.descripcion = descripcion;
        this.peso = peso;
        this.vehiculoId = vehiculoId;
        this.conductorId = conductorId;
        this.estado = estado != null ? estado : EstadoPedido.PENDIENTE;
        this.direccionOrigen = direccionOrigen;
        this.direccionDestino = direccionDestino;
        this.fechaCreacion = LocalDateTime.now();
    }

    /**
     * Asigna un vehículo y conductor al pedido.
     */
    public void asignarVehiculoYConductor(Long vehiculoId, Long conductorId) {
        if (vehiculoId == null || conductorId == null) {
            throw new DomainException("El vehículo y conductor no pueden ser nulos");
        }

        if (this.estado != EstadoPedido.PENDIENTE) {
            throw new DomainException(
                String.format("No se puede asignar vehículo a un pedido en estado %s", this.estado)
            );
        }

        this.vehiculoId = vehiculoId;
        this.conductorId = conductorId;
        this.marcarComoModificado();
    }

    /**
     * Cambia el estado del pedido con validación de transiciones permitidas.
     */
    public void cambiarEstado(EstadoPedido nuevoEstado) {
        if (nuevoEstado == null) {
            throw new DomainException("El nuevo estado no puede ser nulo");
        }

        if (!this.estado.puedeTransicionarA(nuevoEstado)) {
            throw new DomainException(
                String.format("No se puede cambiar de estado %s a %s", this.estado, nuevoEstado)
            );
        }

        this.estado = nuevoEstado;
        this.marcarComoModificado();
    }

    /**
     * Inicia el pedido (cambia a EN_PROGRESO).
     */
    public void iniciar() {
        if (this.vehiculoId == null || this.conductorId == null) {
            throw new DomainException("El pedido debe tener vehículo y conductor asignados para iniciarse");
        }
        this.cambiarEstado(EstadoPedido.EN_PROGRESO);
    }

    /**
     * Completa el pedido.
     */
    public void completar() {
        this.cambiarEstado(EstadoPedido.COMPLETADO);
    }

    /**
     * Cancela el pedido.
     */
    public void cancelar() {
        this.cambiarEstado(EstadoPedido.CANCELADO);
    }

    /**
     * Verifica si el pedido está en un estado final.
     */
    public boolean estaFinalizado() {
        return this.estado.esFinal();
    }

    /**
     * Alias de estaFinalizado() para compatibilidad con tests.
     */
    public boolean estaEnEstadoFinal() {
        return estaFinalizado();
    }

    /**
     * Verifica si el pedido tiene vehículo asignado.
     */
    public boolean tieneVehiculoAsignado() {
        return this.vehiculoId != null;
    }

    private void marcarComoModificado() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new DomainException("La descripción del pedido no puede ser nula o vacía");
        }
        this.descripcion = descripcion;
        this.marcarComoModificado();
    }

    public Peso getPeso() {
        return peso;
    }

    public void setPeso(Peso peso) {
        this.peso = peso;
        this.marcarComoModificado();
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public Long getConductorId() {
        return conductorId;
    }

    public String getDireccionOrigen() {
        return direccionOrigen;
    }

    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
        this.marcarComoModificado();
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
        this.marcarComoModificado();
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
