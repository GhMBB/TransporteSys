package com.example.transportesys.domain.valueobject;

import com.example.transportesys.domain.exception.CapacidadInvalidaException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object que representa la capacidad de un vehículo en kilogramos.
 * Inmutable y con validación de valores positivos.
 */
public class Capacidad {

    private final BigDecimal valorEnKg;

    public Capacidad(BigDecimal valorEnKg) {
        if (valorEnKg == null) {
            throw new CapacidadInvalidaException("La capacidad no puede ser nula");
        }

        if (valorEnKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CapacidadInvalidaException(
                String.format("La capacidad debe ser mayor a cero. Valor recibido: %s", valorEnKg)
            );
        }

        this.valorEnKg = valorEnKg;
    }

    public Capacidad(double valorEnKg) {
        this(BigDecimal.valueOf(valorEnKg));
    }

    public BigDecimal getValorEnKg() {
        return valorEnKg;
    }

    /**
     * Verifica si esta capacidad es suficiente para el peso especificado.
     */
    public boolean esSuficientePara(Peso peso) {
        return this.valorEnKg.compareTo(peso.getValorEnKg()) >= 0;
    }

    /**
     * Calcula la capacidad disponible después de restar un peso.
     */
    public Capacidad restar(Peso peso) {
        BigDecimal resultado = this.valorEnKg.subtract(peso.getValorEnKg());
        if (resultado.compareTo(BigDecimal.ZERO) < 0) {
            throw new CapacidadInvalidaException("La capacidad resultante no puede ser negativa");
        }
        return new Capacidad(resultado);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Capacidad capacidad = (Capacidad) o;
        return Objects.equals(valorEnKg, capacidad.valorEnKg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valorEnKg);
    }

    @Override
    public String toString() {
        return valorEnKg + " kg";
    }
}
