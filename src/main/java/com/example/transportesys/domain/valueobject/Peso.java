package com.example.transportesys.domain.valueobject;

import com.example.transportesys.domain.exception.PesoInvalidoException;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object que representa el peso de un pedido en kilogramos.
 * Inmutable y con validación de valores no negativos.
 */
public class Peso {

    private final BigDecimal valorEnKg;

    public Peso(BigDecimal valorEnKg) {
        if (valorEnKg == null) {
            throw new PesoInvalidoException("El peso no puede ser nulo");
        }

        if (valorEnKg.compareTo(BigDecimal.ZERO) < 0) {
            throw new PesoInvalidoException(
                String.format("El peso no puede ser negativo. Valor recibido: %s", valorEnKg)
            );
        }

        this.valorEnKg = valorEnKg;
    }

    public Peso(double valorEnKg) {
        this(BigDecimal.valueOf(valorEnKg));
    }

    public BigDecimal getValorEnKg() {
        return valorEnKg;
    }

    /**
     * Suma dos pesos.
     */
    public Peso sumar(Peso otroPeso) {
        return new Peso(this.valorEnKg.add(otroPeso.valorEnKg));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Peso peso = (Peso) o;
        return Objects.equals(valorEnKg, peso.valorEnKg);
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
