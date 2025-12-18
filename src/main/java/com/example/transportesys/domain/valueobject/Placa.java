package com.example.transportesys.domain.valueobject;

import com.example.transportesys.domain.exception.PlacaInvalidaException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object que representa una placa de vehículo.
 * Formato válido: ABC-123 (3 letras mayúsculas, guión, 3 dígitos)
 * Inmutable y con validación incorporada.
 */
public class Placa {

    private static final Pattern PATTERN = Pattern.compile("^[A-Z]{3}-\\d{3}$");
    private final String valor;

    public Placa(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new PlacaInvalidaException("La placa no puede ser nula o vacía");
        }

        String placaNormalizada = valor.trim().toUpperCase();

        if (!PATTERN.matcher(placaNormalizada).matches()) {
            throw new PlacaInvalidaException(
                String.format("Formato de placa inválido: %s. Formato esperado: ABC-123", valor)
            );
        }

        this.valor = placaNormalizada;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placa placa = (Placa) o;
        return Objects.equals(valor, placa.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
