package com.example.transportesys.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para Value Object Capacidad")
class CapacidadTest {

    @Test
    @DisplayName("Debe crear una capacidad válida")
    void debeCrearCapacidadValida() {
        // Arrange & Act
        Capacidad capacidad = new Capacidad(1000.0);

        // Assert
        assertNotNull(capacidad);
        assertEquals(0, new BigDecimal("1000.0").compareTo(capacidad.getValorEnKg()));
    }

    @Test
    @DisplayName("Debe aceptar capacidades decimales")
    void debeAceptarCapacidadesDecimales() {
        // Arrange & Act
        Capacidad capacidad = new Capacidad(1500.75);

        // Assert
        assertEquals(0, new BigDecimal("1500.75").compareTo(capacidad.getValorEnKg()));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-100.0, -1.0, -0.01})
    @DisplayName("Debe rechazar capacidades negativas")
    void debeRechazarCapacidadesNegativas(double valorNegativo) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Capacidad(valorNegativo));
    }

    @Test
    @DisplayName("Debe rechazar capacidad cero")
    void debeRechazarCapacidadCero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Capacidad(0.0));
    }

    @Test
    @DisplayName("Debe verificar si es suficiente para un peso menor")
    void debeSerSuficienteParaPesoMenor() {
        // Arrange
        Capacidad capacidad = new Capacidad(1000.0);
        Peso peso = new Peso(500.0);

        // Act
        boolean resultado = capacidad.esSuficientePara(peso);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Debe verificar si es suficiente para un peso igual")
    void debeSerSuficienteParaPesoIgual() {
        // Arrange
        Capacidad capacidad = new Capacidad(1000.0);
        Peso peso = new Peso(1000.0);

        // Act
        boolean resultado = capacidad.esSuficientePara(peso);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("No debe ser suficiente para un peso mayor")
    void noDebeSerSuficienteParaPesoMayor() {
        // Arrange
        Capacidad capacidad = new Capacidad(1000.0);
        Peso peso = new Peso(1500.0);

        // Act
        boolean resultado = capacidad.esSuficientePara(peso);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Dos capacidades con el mismo valor deben ser iguales")
    void dosCapacidadesIgualesDabenSerIguales() {
        // Arrange
        Capacidad capacidad1 = new Capacidad(1000.0);
        Capacidad capacidad2 = new Capacidad(1000.0);

        // Act & Assert
        assertEquals(capacidad1, capacidad2);
        assertEquals(capacidad1.hashCode(), capacidad2.hashCode());
    }

    @Test
    @DisplayName("Dos capacidades con valores diferentes no deben ser iguales")
    void dosCapacidadesDiferentesNoDabenSerIguales() {
        // Arrange
        Capacidad capacidad1 = new Capacidad(1000.0);
        Capacidad capacidad2 = new Capacidad(2000.0);

        // Act & Assert
        assertNotEquals(capacidad1, capacidad2);
    }
}
