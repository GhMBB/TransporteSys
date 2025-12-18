package com.example.transportesys.infrastructure.exception;

import com.example.transportesys.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Manejador global de excepciones para la API REST.
 * Captura excepciones y las convierte en respuestas HTTP apropiadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        PlacaInvalidaException.class,
        CapacidadInvalidaException.class,
        PesoInvalidoException.class,
        LicenciaInvalidaException.class
    })
    public ResponseEntity<ApiError> handleValidationException(
            DomainException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            ex.getMessage(),
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConductorLimiteVehiculosException.class)
    public ResponseEntity<ApiError> handleConductorLimiteVehiculosException(
            ConductorLimiteVehiculosException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.CONFLICT.value(),
            "Business Rule Violation",
            ex.getMessage(),
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CapacidadInsuficienteException.class)
    public ResponseEntity<ApiError> handleCapacidadInsuficienteException(
            CapacidadInsuficienteException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.CONFLICT.value(),
            "Insufficient Capacity",
            ex.getMessage(),
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConductorInactivoException.class)
    public ResponseEntity<ApiError> handleConductorInactivoException(
            ConductorInactivoException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.CONFLICT.value(),
            "Inactive Driver",
            ex.getMessage(),
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(
            DomainException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Business Rule Violation",
            ex.getMessage(),
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(
            IllegalStateException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.CONFLICT.value(),
            "Invalid State",
            ex.getMessage(),
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }

        ApiError error = new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            "Los datos de entrada no son válidos",
            request.getRequestURI(),
            details
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // ============ EXCEPCIONES DE AUTENTICACIÓN ============

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            "Usuario o contraseña incorrectos",
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            "Usuario o contraseña incorrectos",
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleDisabledException(
            DisabledException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Account Disabled",
            "La cuenta de usuario está desactivada",
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiError> handleLockedException(
            LockedException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Account Locked",
            "La cuenta de usuario está bloqueada",
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(
            AuthenticationException ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            "Error de autenticación: " + ex.getMessage(),
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ApiError error = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Ha ocurrido un error inesperado. Por favor contacte al administrador.",
            request.getRequestURI()
        );

        // Log the exception for debugging
        ex.printStackTrace();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
