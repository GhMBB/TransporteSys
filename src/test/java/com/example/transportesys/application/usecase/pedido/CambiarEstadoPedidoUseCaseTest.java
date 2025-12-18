package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.exception.DomainException;
import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.repository.PedidoRepository;
import com.example.transportesys.domain.valueobject.Peso;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para CambiarEstadoPedidoUseCase")
class CambiarEstadoPedidoUseCaseTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private CambiarEstadoPedidoUseCase cambiarEstadoPedidoUseCase;

    @Test
    @DisplayName("Debe cambiar estado de PENDIENTE a EN_PROGRESO exitosamente")
    void debeCambiarEstadoPendienteAEnProgreso() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = new Pedido(
            pedidoId,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.PENDIENTE,
            "Origen",
            "Destino"
        );

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pedido resultado = cambiarEstadoPedidoUseCase.execute(pedidoId, EstadoPedido.EN_PROGRESO);

        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoPedido.EN_PROGRESO, resultado.getEstado());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    @DisplayName("Debe cambiar estado de PENDIENTE a CANCELADO exitosamente")
    void debeCambiarEstadoPendienteACancelado() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = new Pedido(
            pedidoId,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.PENDIENTE,
            "Origen",
            "Destino"
        );

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pedido resultado = cambiarEstadoPedidoUseCase.execute(pedidoId, EstadoPedido.CANCELADO);

        // Assert
        assertEquals(EstadoPedido.CANCELADO, resultado.getEstado());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    @DisplayName("Debe cambiar estado de EN_PROGRESO a COMPLETADO exitosamente")
    void debeCambiarEstadoEnProgresoACompletado() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = new Pedido(
            pedidoId,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.EN_PROGRESO,
            "Origen",
            "Destino"
        );

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pedido resultado = cambiarEstadoPedidoUseCase.execute(pedidoId, EstadoPedido.COMPLETADO);

        // Assert
        assertEquals(EstadoPedido.COMPLETADO, resultado.getEstado());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el pedido no existe")
    void debeLanzarExcepcionCuandoPedidoNoExiste() {
        // Arrange
        Long pedidoId = 999L;
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            cambiarEstadoPedidoUseCase.execute(pedidoId, EstadoPedido.EN_PROGRESO);
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar cambiar de PENDIENTE a COMPLETADO")
    void debeLanzarExcepcionTransicionInvalidaPendienteACompletado() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = new Pedido(
            pedidoId,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.PENDIENTE,
            "Origen",
            "Destino"
        );

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            cambiarEstadoPedidoUseCase.execute(pedidoId, EstadoPedido.COMPLETADO);
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar cambiar de COMPLETADO a cualquier otro estado")
    void debeLanzarExcepcionCuandoCambiarDesdeCompletado() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = new Pedido(
            pedidoId,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.COMPLETADO,
            "Origen",
            "Destino"
        );

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            cambiarEstadoPedidoUseCase.execute(pedidoId, EstadoPedido.PENDIENTE);
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar cambiar de CANCELADO a cualquier otro estado")
    void debeLanzarExcepcionCuandoCambiarDesdeCancelado() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = new Pedido(
            pedidoId,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.CANCELADO,
            "Origen",
            "Destino"
        );

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            cambiarEstadoPedidoUseCase.execute(pedidoId, EstadoPedido.EN_PROGRESO);
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar cambiar de EN_PROGRESO a PENDIENTE")
    void debeLanzarExcepcionTransicionInvalidaEnProgresoAPendiente() {
        // Arrange
        Long pedidoId = 1L;
        Pedido pedido = new Pedido(
            pedidoId,
            "Descripción",
            new Peso(500.0),
            1L,
            1L,
            EstadoPedido.EN_PROGRESO,
            "Origen",
            "Destino"
        );

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            cambiarEstadoPedidoUseCase.execute(pedidoId, EstadoPedido.PENDIENTE);
        });

        verify(pedidoRepository, never()).save(any());
    }
}
