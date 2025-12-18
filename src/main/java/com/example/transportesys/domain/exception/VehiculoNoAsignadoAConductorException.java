package com.example.transportesys.domain.exception;

/**
 * Excepción lanzada cuando se intenta cambiar el vehículo de un pedido a uno que no está asignado al conductor del pedido.
 */
public class VehiculoNoAsignadoAConductorException extends DomainException {

    public VehiculoNoAsignadoAConductorException(String message) {
        super(message);
    }

    public VehiculoNoAsignadoAConductorException(Long vehiculoId, Long conductorId) {
        super(String.format("El vehículo ID %d no está asignado al conductor ID %d", vehiculoId, conductorId));
    }
}
