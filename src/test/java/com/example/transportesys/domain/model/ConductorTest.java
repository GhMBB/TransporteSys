package com.example.transportesys.domain.model;

import com.example.transportesys.domain.exception.ConductorLimiteVehiculosException;
import com.example.transportesys.domain.valueobject.LicenciaConducir;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para entidad Conductor")
class ConductorTest {

    @Test
    @DisplayName("Debe crear un conductor activo por defecto")
    void debeCrearConductorActivoPorDefecto() {
        // Arrange
        String nombre = "Juan Pérez";
        LicenciaConducir licencia = new LicenciaConducir("LIC123456");

        // Act
        Conductor conductor = new Conductor(null, nombre, licencia, true, new HashSet<>());

        // Assert
        assertTrue(conductor.isActivo());
        assertTrue(conductor.getVehiculosIds().isEmpty());
    }

    @Test
    @DisplayName("Debe asignar primer vehículo exitosamente")
    void debeAsignarPrimerVehiculo() {
        // Arrange
        Conductor conductor = new Conductor(
            1L,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            new HashSet<>()
        );
        Long vehiculoId = 1L;

        // Act
        conductor.asignarVehiculo(vehiculoId);

        // Assert
        assertEquals(1, conductor.getVehiculosIds().size());
        assertTrue(conductor.getVehiculosIds().contains(vehiculoId));
    }

    @Test
    @DisplayName("Debe asignar segundo vehículo exitosamente")
    void debeAsignarSegundoVehiculo() {
        // Arrange
        Set<Long> vehiculosIds = new HashSet<>();
        vehiculosIds.add(1L);

        Conductor conductor = new Conductor(
            1L,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            vehiculosIds
        );

        // Act
        conductor.asignarVehiculo(2L);

        // Assert
        assertEquals(2, conductor.getVehiculosIds().size());
        assertTrue(conductor.getVehiculosIds().contains(2L));
    }

    @Test
    @DisplayName("Debe asignar tercer vehículo exitosamente")
    void debeAsignarTercerVehiculo() {
        // Arrange
        Set<Long> vehiculosIds = new HashSet<>();
        vehiculosIds.add(1L);
        vehiculosIds.add(2L);

        Conductor conductor = new Conductor(
            1L,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            vehiculosIds
        );

        // Act
        conductor.asignarVehiculo(3L);

        // Assert
        assertEquals(3, conductor.getVehiculosIds().size());
        assertTrue(conductor.getVehiculosIds().contains(3L));
    }

    @Test
    @DisplayName("Debe lanzar excepción al asignar cuarto vehículo (límite de 3)")
    void debeLanzarExcepcionAlAsignarCuartoVehiculo() {
        // Arrange
        Set<Long> vehiculosIds = new HashSet<>();
        vehiculosIds.add(1L);
        vehiculosIds.add(2L);
        vehiculosIds.add(3L);

        Conductor conductor = new Conductor(
            1L,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            vehiculosIds
        );

        // Act & Assert
        assertThrows(ConductorLimiteVehiculosException.class, () -> {
            conductor.asignarVehiculo(4L);
        });
    }

    @Test
    @DisplayName("Debe desasignar vehículo correctamente")
    void debeDesasignarVehiculo() {
        // Arrange
        Set<Long> vehiculosIds = new HashSet<>();
        vehiculosIds.add(1L);
        vehiculosIds.add(2L);

        Conductor conductor = new Conductor(
            1L,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            vehiculosIds
        );

        // Act
        conductor.desasignarVehiculo(1L);

        // Assert
        assertEquals(1, conductor.getVehiculosIds().size());
        assertFalse(conductor.getVehiculosIds().contains(1L));
        assertTrue(conductor.getVehiculosIds().contains(2L));
    }

    @Test
    @DisplayName("Debe verificar si puede asignar más vehículos (tiene menos de 3)")
    void debeVerificarSiPuedeAsignarMasVehiculos() {
        // Arrange
        Set<Long> vehiculosIds = new HashSet<>();
        vehiculosIds.add(1L);

        Conductor conductor = new Conductor(
            1L,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            vehiculosIds
        );

        // Act
        boolean resultado = conductor.puedeAsignarMasVehiculos();

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Debe verificar si no puede asignar más vehículos (tiene 3)")
    void debeVerificarSiNoPuedeAsignarMasVehiculos() {
        // Arrange
        Set<Long> vehiculosIds = new HashSet<>();
        vehiculosIds.add(1L);
        vehiculosIds.add(2L);
        vehiculosIds.add(3L);

        Conductor conductor = new Conductor(
            1L,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            vehiculosIds
        );

        // Act
        boolean resultado = conductor.puedeAsignarMasVehiculos();

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Debe obtener cantidad de vehículos asignados")
    void debeObtenerCantidadVehiculos() {
        // Arrange
        Set<Long> vehiculosIds = new HashSet<>();
        vehiculosIds.add(1L);
        vehiculosIds.add(2L);

        Conductor conductor = new Conductor(
            1L,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            vehiculosIds
        );

        // Act
        int cantidad = conductor.getCantidadVehiculos();

        // Assert
        assertEquals(2, cantidad);
    }

    @Test
    @DisplayName("Debe marcar conductor como inactivo")
    void debeMarcarComoInactivo() {
        // Arrange
        Conductor conductor = new Conductor(
            1L,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            new HashSet<>()
        );

        // Act
        conductor.marcarComoInactivo();

        // Assert
        assertFalse(conductor.isActivo());
    }
}
