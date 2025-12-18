package com.example.transportesys.domain.exception;

/**
 * Excepción lanzada cuando se intenta devolver un vehículo que está siendo usado en pedidos activos.
 */
public class VehiculoEnUsoException extends DomainException {

    public VehiculoEnUsoException(String message) {
        super(message);
    }

    public VehiculoEnUsoException(Long vehiculoId, int pedidosActivos) {
        super(String.format("El vehículo ID %d no puede ser devuelto porque tiene %d pedido(s) activo(s)",
                          vehiculoId, pedidosActivos));
    }
}
