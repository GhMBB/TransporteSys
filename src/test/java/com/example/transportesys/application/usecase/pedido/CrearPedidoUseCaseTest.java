package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.exception.CapacidadInsuficienteException;
import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.ConductorRepository;
import com.example.transportesys.domain.repository.PedidoRepository;
import com.example.transportesys.domain.repository.VehiculoRepository;
import com.example.transportesys.domain.specification.VehiculoTieneCapacidadSuficienteSpec;
import com.example.transportesys.domain.valueobject.Capacidad;
import com.example.transportesys.domain.valueobject.LicenciaConducir;
import com.example.transportesys.domain.valueobject.Peso;
import com.example.transportesys.domain.valueobject.Placa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para CrearPedidoUseCase")
class CrearPedidoUseCaseTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private ConductorRepository conductorRepository;

    @Mock
    private VehiculoTieneCapacidadSuficienteSpec capacidadSpec;

    @InjectMocks
    private CrearPedidoUseCase crearPedidoUseCase;

    @Test
    @DisplayName("Debe crear un pedido exitosamente con todos los datos válidos")
    void debeCrearPedidoExitosamente() {
        // Arrange
        String descripcion = "Envío de paquetes";
        Double pesoKg = 500.0;
        Long vehiculoId = 1L;
        Long conductorId = 1L;
        String direccionOrigen = "Calle A #123";
        String direccionDestino = "Calle B #456";

        Vehiculo vehiculo = new Vehiculo(
            vehiculoId,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            conductorId
        );

        Conductor conductor = new Conductor(
            conductorId,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            new HashSet<>()
        );

        when(vehiculoRepository.findById(vehiculoId)).thenReturn(Optional.of(vehiculo));
        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductor));
        when(capacidadSpec.isSatisfiedBy(any(Vehiculo.class), any(Peso.class))).thenReturn(true);
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            return new Pedido(1L, p.getDescripcion(), p.getPeso(), p.getVehiculoId(),
                             p.getConductorId(), p.getEstado(), p.getDireccionOrigen(), p.getDireccionDestino());
        });

        // Act
        Pedido resultado = crearPedidoUseCase.execute(descripcion, pesoKg, vehiculoId,
                                                      conductorId, direccionOrigen, direccionDestino);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(descripcion, resultado.getDescripcion());
        assertEquals(pesoKg, resultado.getPeso().getValorEnKg().doubleValue());
        assertEquals(vehiculoId, resultado.getVehiculoId());
        assertEquals(conductorId, resultado.getConductorId());
        assertEquals(EstadoPedido.PENDIENTE, resultado.getEstado());
        assertEquals(direccionOrigen, resultado.getDireccionOrigen());
        assertEquals(direccionDestino, resultado.getDireccionDestino());

        verify(vehiculoRepository, times(1)).findById(vehiculoId);
        verify(conductorRepository, times(1)).findById(conductorId);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el vehículo no existe")
    void debeLanzarExcepcionCuandoVehiculoNoExiste() {
        // Arrange
        Long vehiculoId = 999L;

        when(vehiculoRepository.findById(vehiculoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            crearPedidoUseCase.execute("Descripción", 500.0, vehiculoId,
                                      1L, "Origen", "Destino");
        });

        verify(conductorRepository, never()).findById(any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el conductor no existe")
    void debeLanzarExcepcionCuandoConductorNoExiste() {
        // Arrange
        Long vehiculoId = 1L;
        Long conductorId = 999L;

        Vehiculo vehiculo = new Vehiculo(
            vehiculoId,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            null
        );

        when(vehiculoRepository.findById(vehiculoId)).thenReturn(Optional.of(vehiculo));
        when(conductorRepository.findById(conductorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            crearPedidoUseCase.execute("Descripción", 500.0, vehiculoId,
                                      conductorId, "Origen", "Destino");
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el vehículo no tiene capacidad suficiente")
    void debeLanzarExcepcionCuandoCapacidadInsuficiente() {
        // Arrange
        Long vehiculoId = 1L;
        Long conductorId = 1L;
        Double pesoExcesivo = 1500.0; // Mayor que la capacidad del vehículo

        Vehiculo vehiculo = new Vehiculo(
            vehiculoId,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            conductorId
        );

        Conductor conductor = new Conductor(
            conductorId,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            new HashSet<>()
        );

        when(vehiculoRepository.findById(vehiculoId)).thenReturn(Optional.of(vehiculo));
        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductor));
        when(capacidadSpec.isSatisfiedBy(any(Vehiculo.class), any(Peso.class))).thenReturn(false);

        // Act & Assert
        assertThrows(CapacidadInsuficienteException.class, () -> {
            crearPedidoUseCase.execute("Descripción", pesoExcesivo, vehiculoId,
                                      conductorId, "Origen", "Destino");
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el vehículo está inactivo")
    void debeLanzarExcepcionCuandoVehiculoInactivo() {
        // Arrange
        Long vehiculoId = 1L;
        Long conductorId = 1L;

        Vehiculo vehiculoInactivo = new Vehiculo(
            vehiculoId,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            false, // Inactivo
            conductorId
        );

        when(vehiculoRepository.findById(vehiculoId)).thenReturn(Optional.of(vehiculoInactivo));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            crearPedidoUseCase.execute("Descripción", 500.0, vehiculoId,
                                      conductorId, "Origen", "Destino");
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el conductor está inactivo")
    void debeLanzarExcepcionCuandoConductorInactivo() {
        // Arrange
        Long vehiculoId = 1L;
        Long conductorId = 1L;

        Vehiculo vehiculo = new Vehiculo(
            vehiculoId,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            conductorId
        );

        Conductor conductorInactivo = new Conductor(
            conductorId,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            false, // Inactivo
            new HashSet<>()
        );

        when(vehiculoRepository.findById(vehiculoId)).thenReturn(Optional.of(vehiculo));
        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductorInactivo));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            crearPedidoUseCase.execute("Descripción", 500.0, vehiculoId,
                                      conductorId, "Origen", "Destino");
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el peso es negativo")
    void debeLanzarExcepcionCuandoPesoNegativo() {
        // Arrange
        Double pesoNegativo = -100.0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            crearPedidoUseCase.execute("Descripción", pesoNegativo, 1L,
                                      1L, "Origen", "Destino");
        });

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el peso es cero")
    void debeLanzarExcepcionCuandoPesoCero() {
        // Arrange
        Double pesoCero = 0.0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            crearPedidoUseCase.execute("Descripción", pesoCero, 1L,
                                      1L, "Origen", "Destino");
        });

        verify(pedidoRepository, never()).save(any());
    }
}
