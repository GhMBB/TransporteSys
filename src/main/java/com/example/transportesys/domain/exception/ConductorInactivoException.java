package com.example.transportesys.domain.exception;

public class ConductorInactivoException extends DomainException {
    public ConductorInactivoException(String message) {
        super(message);
    }

    public ConductorInactivoException() {
        super("El conductor no está activo");
    }
}
