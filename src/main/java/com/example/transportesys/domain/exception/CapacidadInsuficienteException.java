package com.example.transportesys.domain.exception;

public class CapacidadInsuficienteException extends DomainException {
    public CapacidadInsuficienteException(String message) {
        super(message);
    }

    public CapacidadInsuficienteException() {
        super("El vehículo no tiene capacidad suficiente para este pedido");
    }
}
