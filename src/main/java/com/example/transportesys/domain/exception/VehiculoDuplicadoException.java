package com.example.transportesys.domain.exception;

public class VehiculoDuplicadoException extends DomainException {
    public VehiculoDuplicadoException(String message) {
        super(message);
    }

    public VehiculoDuplicadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
