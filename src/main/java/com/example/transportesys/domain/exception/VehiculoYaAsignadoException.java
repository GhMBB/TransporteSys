package com.example.transportesys.domain.exception;

/**
 * Excepción lanzada cuando se intenta asignar un vehículo que ya está asignado a otro conductor.
 */
public class VehiculoYaAsignadoException extends DomainException {

    public VehiculoYaAsignadoException(String message) {
        super(message);
    }

    public VehiculoYaAsignadoException(Long vehiculoId, Long conductorId) {
        super(String.format("El vehículo ID %d ya está asignado al conductor ID %d", vehiculoId, conductorId));
    }
}
