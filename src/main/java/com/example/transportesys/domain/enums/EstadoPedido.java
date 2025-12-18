package com.example.transportesys.domain.enums;

/**
 * Estados posibles de un pedido en el sistema de transporte.
 */
public enum EstadoPedido {
    PENDIENTE("Pendiente"),
    EN_PROGRESO("En Progreso"),
    COMPLETADO("Completado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean esFinal() {
        return this == COMPLETADO || this == CANCELADO;
    }

    public boolean puedeTransicionarA(EstadoPedido nuevoEstado) {
        return switch (this) {
            case PENDIENTE -> nuevoEstado == EN_PROGRESO || nuevoEstado == CANCELADO;
            case EN_PROGRESO -> nuevoEstado == COMPLETADO || nuevoEstado == CANCELADO;
            case COMPLETADO, CANCELADO -> false;
        };
    }
}
