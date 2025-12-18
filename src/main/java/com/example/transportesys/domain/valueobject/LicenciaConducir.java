package com.example.transportesys.domain.valueobject;

import com.example.transportesys.domain.exception.LicenciaInvalidaException;

import java.util.Objects;

/**
 * Value Object que representa una licencia de conducir.
 * Inmutable y con validación básica.
 */
public class LicenciaConducir {

    private final String numero;

    public LicenciaConducir(String numero) {
        if (numero == null || numero.isBlank()) {
            throw new LicenciaInvalidaException("El número de licencia no puede ser nulo o vacío");
        }

        String licenciaNormalizada = numero.trim().toUpperCase();

        if (licenciaNormalizada.length() < 5) {
            throw new LicenciaInvalidaException(
                String.format("El número de licencia debe tener al menos 5 caracteres: %s", numero)
            );
        }

        this.numero = licenciaNormalizada;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LicenciaConducir that = (LicenciaConducir) o;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero);
    }

    @Override
    public String toString() {
        return numero;
    }
}
