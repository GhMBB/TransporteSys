package com.example.transportesys.domain.exception;

public class ConductorDuplicadoException extends DomainException {
    public ConductorDuplicadoException(String message) {
        super(message);
    }

    public ConductorDuplicadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
