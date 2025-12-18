package com.example.transportesys.domain.valueobject;

import com.example.transportesys.domain.exception.PlacaInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para Value Object Placa")
class PlacaTest {

    @Test
    @DisplayName("Debe crear una placa válida con formato correcto")
    void debeCrearPlacaValida() {
        // Arrange & Act
        Placa placa = new Placa("ABC-123");

        // Assert
        assertNotNull(placa);
        assertEquals("ABC-123", placa.getValor());
    }

    @Test
    @DisplayName("Debe normalizar placa a mayúsculas")
    void debeNormalizarPlacaAMayusculas() {
        // Arrange & Act
        Placa placa = new Placa("abc-123");

        // Assert
        assertEquals("ABC-123", placa.getValor());
    }

    @Test
    @DisplayName("Debe eliminar espacios en blanco de la placa")
    void debeEliminarEspaciosEnBlanco() {
        // Arrange & Act
        Placa placa = new Placa("  ABC-123  ");

        // Assert
        assertEquals("ABC-123", placa.getValor());
    }

    @ParameterizedTest
    @ValueSource(strings = {"XYZ-789", "AAA-000", "ZZZ-999"})
    @DisplayName("Debe aceptar múltiples placas válidas")
    void debeAceptarPlacasValidas(String placaStr) {
        // Act & Assert
        assertDoesNotThrow(() -> new Placa(placaStr));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "INVALID",           // Sin guión ni números
        "AB-123",            // Solo 2 letras
        "ABCD-123",          // 4 letras
        "ABC-12",            // Solo 2 números
        "ABC-1234",          // 4 números
        "123-ABC",           // Orden invertido
        "ABC123",            // Sin guión
        "",                  // Vacío
        "   ",               // Solo espacios
        "ABC_123",           // Guión bajo en lugar de guión
        "AB C-123",          // Espacio en letras
        "ABC-1 23"           // Espacio en números
    })
    @DisplayName("Debe rechazar placas con formato inválido")
    void debeRechazarPlacasInvalidas(String placaInvalida) {
        // Act & Assert
        assertThrows(PlacaInvalidaException.class, () -> new Placa(placaInvalida));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la placa es null")
    void debeLanzarExcepcionCuandoPlacaNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new Placa(null));
    }

    @Test
    @DisplayName("Dos placas con el mismo valor deben ser iguales")
    void dosPlacasIgualesDabenSerIguales() {
        // Arrange
        Placa placa1 = new Placa("ABC-123");
        Placa placa2 = new Placa("ABC-123");

        // Act & Assert
        assertEquals(placa1, placa2);
        assertEquals(placa1.hashCode(), placa2.hashCode());
    }

    @Test
    @DisplayName("Dos placas con valores diferentes no deben ser iguales")
    void dosPlacasDiferentesNoDabenSerIguales() {
        // Arrange
        Placa placa1 = new Placa("ABC-123");
        Placa placa2 = new Placa("XYZ-789");

        // Act & Assert
        assertNotEquals(placa1, placa2);
    }

    @Test
    @DisplayName("Placa debe tener un toString legible")
    void placaDebeTeberToStringLegible() {
        // Arrange
        Placa placa = new Placa("ABC-123");

        // Act
        String resultado = placa.toString();

        // Assert
        assertTrue(resultado.contains("ABC-123"));
    }
}
