package com.example.transportesys.application.usecase.vehiculo;

import com.example.transportesys.domain.exception.PlacaInvalidaException;
import com.example.transportesys.domain.exception.VehiculoDuplicadoException;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.VehiculoRepository;
import com.example.transportesys.domain.valueobject.Capacidad;
import com.example.transportesys.domain.valueobject.Placa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para CrearVehiculoUseCase")
class CrearVehiculoUseCaseTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private CrearVehiculoUseCase crearVehiculoUseCase;

    @Test
    @DisplayName("Debe crear un vehículo exitosamente con placa y capacidad válidas")
    void debeCrearVehiculoExitosamente() {
        // Arrange
        String placaStr = "ABC-123";
        Double capacidadKg = 1000.0;

        when(vehiculoRepository.existsByPlaca(placaStr)).thenReturn(false);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(invocation -> {
            Vehiculo v = invocation.getArgument(0);
            return new Vehiculo(1L, v.getPlaca(), v.getCapacidad(), true, null);
        });

        // Act
        Vehiculo resultado = crearVehiculoUseCase.execute(placaStr, capacidadKg);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(placaStr, resultado.getPlaca().getValor());
        assertEquals(capacidadKg, resultado.getCapacidad().getValorEnKg().doubleValue());
        assertTrue(resultado.isActivo());

        verify(vehiculoRepository, times(1)).existsByPlaca(placaStr);
        verify(vehiculoRepository, times(1)).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la placa está duplicada")
    void debeLanzarExcepcionCuandoPlacaDuplicada() {
        // Arrange
        String placaStr = "ABC-123";
        Double capacidadKg = 1000.0;

        when(vehiculoRepository.existsByPlaca(placaStr)).thenReturn(true);

        // Act & Assert
        assertThrows(VehiculoDuplicadoException.class, () -> {
            crearVehiculoUseCase.execute(placaStr, capacidadKg);
        });

        verify(vehiculoRepository, times(1)).existsByPlaca(placaStr);
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la placa tiene formato inválido")
    void debeLanzarExcepcionCuandoPlacaInvalida() {
        // Arrange
        String placaInvalida = "INVALID";
        Double capacidadKg = 1000.0;

        // Act & Assert
        assertThrows(PlacaInvalidaException.class, () -> {
            crearVehiculoUseCase.execute(placaInvalida, capacidadKg);
        });

        verify(vehiculoRepository, never()).existsByPlaca(anyString());
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la capacidad es negativa")
    void debeLanzarExcepcionCuandoCapacidadNegativa() {
        // Arrange
        String placaStr = "ABC-123";
        Double capacidadNegativa = -100.0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            crearVehiculoUseCase.execute(placaStr, capacidadNegativa);
        });

        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la capacidad es cero")
    void debeLanzarExcepcionCuandoCapacidadCero() {
        // Arrange
        String placaStr = "ABC-123";
        Double capacidadCero = 0.0;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            crearVehiculoUseCase.execute(placaStr, capacidadCero);
        });

        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    @Test
    @DisplayName("Debe normalizar placa a mayúsculas")
    void debeNormalizarPlacaAMayusculas() {
        // Arrange
        String placaMinusculas = "abc-123";
        Double capacidadKg = 1000.0;

        when(vehiculoRepository.existsByPlaca("ABC-123")).thenReturn(false);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenAnswer(invocation -> {
            Vehiculo v = invocation.getArgument(0);
            return new Vehiculo(1L, v.getPlaca(), v.getCapacidad(), true, null);
        });

        // Act
        Vehiculo resultado = crearVehiculoUseCase.execute(placaMinusculas, capacidadKg);

        // Assert
        assertEquals("ABC-123", resultado.getPlaca().getValor());
        verify(vehiculoRepository, times(1)).existsByPlaca("ABC-123");
    }
}
