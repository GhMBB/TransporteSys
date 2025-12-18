package com.example.transportesys.domain.model;

import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.exception.DomainException;
import com.example.transportesys.domain.valueobject.Peso;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests para entidad Pedido")
class PedidoTest {

    @Test
    @DisplayName("Debe crear pedido en estado PENDIENTE por defecto")
    void debeCrearPedidoEnEstadoPendiente() {
        // Arrange & Act
        Pedido pedido = new Pedido(
            null,
            "Descripción",
            new Peso(500.0),
            "Origen",
            "Destino"
        );

        // Assert
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
    }

    @Test
    @DisplayName("Debe asignar vehículo y conductor correctamente")
    void debeAsignarVehiculoYConductor() {
        // Arrange
        Pedido pedido = new Pedido(
            null,
            "Descripción",
            new Peso(500.0),
            "Origen",
            "Destino"
        );
        Long vehiculoId = 1L;
        Long conductorId = 1L;

        // Act
        pedido.asignarVehiculoYConductor(vehiculoId, conductorId);

        // Assert
        assertEquals(vehiculoId, pedido.getVehiculoId());
        assertEquals(conductorId, pedido.getConductorId());
    }

    @Test
    @DisplayName("Debe cambiar estado de PENDIENTE a EN_PROGRESO")
    void debeCambiarEstadoPendienteAEnProgreso() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.PENDIENTE,
            "Origen",
            "Destino"
        );

        // Act
        pedido.cambiarEstado(EstadoPedido.EN_PROGRESO);

        // Assert
        assertEquals(EstadoPedido.EN_PROGRESO, pedido.getEstado());
    }

    @Test
    @DisplayName("Debe cambiar estado de PENDIENTE a CANCELADO")
    void debeCambiarEstadoPendienteACancelado() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.PENDIENTE,
            "Origen",
            "Destino"
        );

        // Act
        pedido.cambiarEstado(EstadoPedido.CANCELADO);

        // Assert
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
    }

    @Test
    @DisplayName("Debe cambiar estado de EN_PROGRESO a COMPLETADO")
    void debeCambiarEstadoEnProgresoACompletado() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.EN_PROGRESO,
            "Origen",
            "Destino"
        );

        // Act
        pedido.cambiarEstado(EstadoPedido.COMPLETADO);

        // Assert
        assertEquals(EstadoPedido.COMPLETADO, pedido.getEstado());
    }

    @Test
    @DisplayName("Debe cambiar estado de EN_PROGRESO a CANCELADO")
    void debeCambiarEstadoEnProgresoACancelado() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.EN_PROGRESO,
            "Origen",
            "Destino"
        );

        // Act
        pedido.cambiarEstado(EstadoPedido.CANCELADO);

        // Assert
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
    }

    @Test
    @DisplayName("Debe lanzar excepción al cambiar de PENDIENTE a COMPLETADO")
    void debeLanzarExcepcionAlCambiarPendienteACompletado() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.PENDIENTE,
            "Origen",
            "Destino"
        );

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            pedido.cambiarEstado(EstadoPedido.COMPLETADO);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción al cambiar de COMPLETADO a cualquier estado")
    void debeLanzarExcepcionAlCambiarDesdeCompletado() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.COMPLETADO,
            "Origen",
            "Destino"
        );

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            pedido.cambiarEstado(EstadoPedido.PENDIENTE);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción al cambiar de CANCELADO a cualquier estado")
    void debeLanzarExcepcionAlCambiarDesdeCancelado() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.CANCELADO,
            "Origen",
            "Destino"
        );

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            pedido.cambiarEstado(EstadoPedido.EN_PROGRESO);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción al cambiar de EN_PROGRESO a PENDIENTE")
    void debeLanzarExcepcionAlCambiarEnProgresoAPendiente() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.EN_PROGRESO,
            "Origen",
            "Destino"
        );

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            pedido.cambiarEstado(EstadoPedido.PENDIENTE);
        });
    }

    @Test
    @DisplayName("Debe verificar si está en estado final (COMPLETADO)")
    void debeVerificarEstadoFinalCompletado() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.COMPLETADO,
            "Origen",
            "Destino"
        );

        // Act
        boolean resultado = pedido.estaEnEstadoFinal();

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Debe verificar si está en estado final (CANCELADO)")
    void debeVerificarEstadoFinalCancelado() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.CANCELADO,
            "Origen",
            "Destino"
        );

        // Act
        boolean resultado = pedido.estaEnEstadoFinal();

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Debe verificar si no está en estado final (PENDIENTE)")
    void debeVerificarNoEstadoFinal() {
        // Arrange
        Pedido pedido = new Pedido(
            1L,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.PENDIENTE,
            "Origen",
            "Destino"
        );

        // Act
        boolean resultado = pedido.estaEnEstadoFinal();

        // Assert
        assertFalse(resultado);
    }
}
