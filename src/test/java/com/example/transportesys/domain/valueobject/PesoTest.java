package com.example.transportesys.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para Value Object Peso")
class PesoTest {

    @Test
    @DisplayName("Debe crear un peso válido")
    void debeCrearPesoValido() {
        // Arrange & Act
        Peso peso = new Peso(500.0);

        // Assert
        assertNotNull(peso);
        assertEquals(0, new BigDecimal("500.0").compareTo(peso.getValorEnKg()));
    }

    @Test
    @DisplayName("Debe aceptar pesos decimales")
    void debeAceptarPesosDecimales() {
        // Arrange & Act
        Peso peso = new Peso(123.45);

        // Assert
        assertEquals(0, new BigDecimal("123.45").compareTo(peso.getValorEnKg()));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-100.0, -1.0, -0.01})
    @DisplayName("Debe rechazar pesos negativos")
    void debeRechazarPesosNegativos(double valorNegativo) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Peso(valorNegativo));
    }

    @Test
    @DisplayName("Debe rechazar peso cero")
    void debeRechazarPesoCero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Peso(0.0));
    }

    @Test
    @DisplayName("Dos pesos con el mismo valor deben ser iguales")
    void dosPesosIgualesDabenSerIguales() {
        // Arrange
        Peso peso1 = new Peso(500.0);
        Peso peso2 = new Peso(500.0);

        // Act & Assert
        assertEquals(peso1, peso2);
        assertEquals(peso1.hashCode(), peso2.hashCode());
    }

    @Test
    @DisplayName("Dos pesos con valores diferentes no deben ser iguales")
    void dosPesosDiferentesNoDabenSerIguales() {
        // Arrange
        Peso peso1 = new Peso(500.0);
        Peso peso2 = new Peso(1000.0);

        // Act & Assert
        assertNotEquals(peso1, peso2);
    }
}
