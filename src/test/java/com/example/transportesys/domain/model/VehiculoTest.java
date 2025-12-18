package com.example.transportesys.domain.model;

import com.example.transportesys.domain.exception.DomainException;
import com.example.transportesys.domain.valueobject.Capacidad;
import com.example.transportesys.domain.valueobject.Peso;
import com.example.transportesys.domain.valueobject.Placa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para entidad Vehiculo")
class VehiculoTest {

    @Test
    @DisplayName("Debe crear un vehículo activo por defecto")
    void debeCrearVehiculoActivoPorDefecto() {
        // Arrange
        Placa placa = new Placa("ABC-123");
        Capacidad capacidad = new Capacidad(1000.0);

        // Act
        Vehiculo vehiculo = new Vehiculo(null, placa, capacidad, true, null);

        // Assert
        assertTrue(vehiculo.isActivo());
        assertNull(vehiculo.getConductorId());
    }

    @Test
    @DisplayName("Debe asignar conductor a vehículo activo")
    void debeAsignarConductorAVehiculoActivo() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo(
            1L,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            null
        );
        Long conductorId = 1L;

        // Act
        vehiculo.asignarConductor(conductorId);

        // Assert
        assertEquals(conductorId, vehiculo.getConductorId());
    }

    @Test
    @DisplayName("Debe lanzar excepción al asignar conductor a vehículo inactivo")
    void debeLanzarExcepcionAlAsignarConductorAVehiculoInactivo() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo(
            1L,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            false, // Inactivo
            null
        );

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            vehiculo.asignarConductor(1L);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción al asignar conductor null")
    void debeLanzarExcepcionAlAsignarConductorNull() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo(
            1L,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            null
        );

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            vehiculo.asignarConductor(null);
        });
    }

    @Test
    @DisplayName("Debe desasignar conductor correctamente")
    void debeDesasignarConductor() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo(
            1L,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            1L
        );

        // Act
        vehiculo.desasignarConductor();

        // Assert
        assertNull(vehiculo.getConductorId());
    }

    @Test
    @DisplayName("Debe verificar si tiene capacidad suficiente para un peso")
    void debeVerificarCapacidadSuficiente() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo(
            1L,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            null
        );
        Peso peso = new Peso(500.0);

        // Act
        boolean resultado = vehiculo.tieneCapacidadPara(peso);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Debe verificar si no tiene capacidad suficiente para un peso mayor")
    void debeVerificarCapacidadInsuficiente() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo(
            1L,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            null
        );
        Peso peso = new Peso(1500.0);

        // Act
        boolean resultado = vehiculo.tieneCapacidadPara(peso);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Debe verificar si está libre (sin conductor asignado)")
    void debeVerificarSiEstaLibre() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo(
            1L,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            null
        );

        // Act
        boolean resultado = vehiculo.estaLibre();

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Debe verificar si no está libre (con conductor asignado)")
    void debeVerificarSiNoEstaLibre() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo(
            1L,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            1L
        );

        // Act
        boolean resultado = vehiculo.estaLibre();

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Debe marcar vehículo como inactivo")
    void debeMarcarComoInactivo() {
        // Arrange
        Vehiculo vehiculo = new Vehiculo(
            1L,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            null
        );

        // Act
        vehiculo.marcarComoInactivo();

        // Assert
        assertFalse(vehiculo.isActivo());
    }
}
