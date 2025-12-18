package com.example.transportesys.application.usecase.vehiculo;

import com.example.transportesys.domain.exception.ConductorLimiteVehiculosException;
import com.example.transportesys.domain.exception.DomainException;
import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.ConductorRepository;
import com.example.transportesys.domain.repository.VehiculoRepository;
import com.example.transportesys.domain.specification.ConductorPuedeAsignarVehiculoSpec;
import com.example.transportesys.domain.valueobject.Capacidad;
import com.example.transportesys.domain.valueobject.LicenciaConducir;
import com.example.transportesys.domain.valueobject.Placa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para AsignarConductorAVehiculoUseCase")
class AsignarConductorAVehiculoUseCaseTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private ConductorRepository conductorRepository;

    @Mock
    private ConductorPuedeAsignarVehiculoSpec conductorPuedeAsignarVehiculoSpec;

    @InjectMocks
    private AsignarConductorAVehiculoUseCase asignarConductorAVehiculoUseCase;

    @Test
    @DisplayName("Debe asignar conductor a vehículo exitosamente")
    void debeAsignarConductorExitosamente() {
        // Arrange
        Long vehiculoId = 1L;
        Long conductorId = 1L;

        Vehiculo vehiculo = new Vehiculo(
            vehiculoId,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            null
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
        lenient().when(conductorPuedeAsignarVehiculoSpec.isSatisfiedBy(conductor)).thenReturn(true);
        when(vehiculoRepository.save(any(Vehiculo.class))).thenReturn(vehiculo);
        when(conductorRepository.save(any(Conductor.class))).thenReturn(conductor);

        // Act
        Vehiculo resultado = asignarConductorAVehiculoUseCase.execute(vehiculoId, conductorId);

        // Assert
        assertNotNull(resultado);
        assertEquals(conductorId, resultado.getConductorId());
        verify(vehiculoRepository).save(any(Vehiculo.class));
        verify(conductorRepository).save(any(Conductor.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el vehículo no existe")
    void debeLanzarExcepcionCuandoVehiculoNoExiste() {
        // Arrange
        Long vehiculoId = 999L;
        Long conductorId = 1L;

        when(vehiculoRepository.findById(vehiculoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            asignarConductorAVehiculoUseCase.execute(vehiculoId, conductorId);
        });

        verify(conductorRepository, never()).findById(any());
        verify(vehiculoRepository, never()).save(any());
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
            asignarConductorAVehiculoUseCase.execute(vehiculoId, conductorId);
        });

        verify(vehiculoRepository, never()).save(any());
        verify(conductorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el vehículo está inactivo")
    void debeLanzarExcepcionCuandoVehiculoInactivo() {
        // Arrange
        Long vehiculoId = 1L;
        Long conductorId = 1L;

        Vehiculo vehiculo = new Vehiculo(
            vehiculoId,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            false, // Inactivo
            null
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

        // Act & Assert
        assertThrows(DomainException.class, () -> {
            asignarConductorAVehiculoUseCase.execute(vehiculoId, conductorId);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el conductor alcanzó el límite de vehículos")
    void debeLanzarExcepcionCuandoConductorAlcanzoLimite() {
        // Arrange
        Long vehiculoId = 1L;
        Long conductorId = 1L;

        Vehiculo vehiculo = new Vehiculo(
            vehiculoId,
            new Placa("ABC-123"),
            new Capacidad(1000.0),
            true,
            null
        );

        Set<Long> vehiculosIds = new HashSet<>();
        vehiculosIds.add(2L);
        vehiculosIds.add(3L);
        vehiculosIds.add(4L);

        Conductor conductor = new Conductor(
            conductorId,
            "Juan Pérez",
            new LicenciaConducir("LIC123456"),
            true,
            vehiculosIds
        );

        when(vehiculoRepository.findById(vehiculoId)).thenReturn(Optional.of(vehiculo));
        when(conductorRepository.findById(conductorId)).thenReturn(Optional.of(conductor));
        lenient().when(conductorPuedeAsignarVehiculoSpec.isSatisfiedBy(conductor)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            asignarConductorAVehiculoUseCase.execute(vehiculoId, conductorId);
        });

        verify(vehiculoRepository, never()).save(any());
        verify(conductorRepository, never()).save(any());
    }
}
