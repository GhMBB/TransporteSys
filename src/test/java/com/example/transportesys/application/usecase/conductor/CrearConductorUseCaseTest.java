package com.example.transportesys.application.usecase.conductor;

import com.example.transportesys.domain.exception.ConductorDuplicadoException;
import com.example.transportesys.domain.exception.LicenciaInvalidaException;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.repository.ConductorRepository;
import com.example.transportesys.domain.valueobject.LicenciaConducir;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para CrearConductorUseCase")
class CrearConductorUseCaseTest {

    @Mock
    private ConductorRepository conductorRepository;

    @InjectMocks
    private CrearConductorUseCase crearConductorUseCase;

    @Test
    @DisplayName("Debe crear un conductor exitosamente con datos válidos")
    void debeCrearConductorExitosamente() {
        // Arrange
        String nombre = "Juan Pérez";
        String licenciaStr = "LIC123456";

        when(conductorRepository.existsByLicencia(licenciaStr)).thenReturn(false);
        when(conductorRepository.save(any(Conductor.class))).thenAnswer(invocation -> {
            Conductor c = invocation.getArgument(0);
            return new Conductor(1L, c.getNombre(), c.getLicencia(), true, new HashSet<>());
        });

        // Act
        Conductor resultado = crearConductorUseCase.execute(nombre, licenciaStr);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(nombre, resultado.getNombre());
        assertEquals(licenciaStr, resultado.getLicencia().getNumero());
        assertTrue(resultado.isActivo());
        assertTrue(resultado.getVehiculosIds().isEmpty());

        verify(conductorRepository, times(1)).existsByLicencia(licenciaStr);
        verify(conductorRepository, times(1)).save(any(Conductor.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la licencia está duplicada")
    void debeLanzarExcepcionCuandoLicenciaDuplicada() {
        // Arrange
        String nombre = "Juan Pérez";
        String licenciaStr = "LIC123456";

        when(conductorRepository.existsByLicencia(licenciaStr)).thenReturn(true);

        // Act & Assert
        assertThrows(ConductorDuplicadoException.class, () -> {
            crearConductorUseCase.execute(nombre, licenciaStr);
        });

        verify(conductorRepository, times(1)).existsByLicencia(licenciaStr);
        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la licencia está vacía")
    void debeLanzarExcepcionCuandoLicenciaVacia() {
        // Arrange
        String nombre = "Juan Pérez";
        String licenciaVacia = "";

        // Act & Assert
        assertThrows(LicenciaInvalidaException.class, () -> {
            crearConductorUseCase.execute(nombre, licenciaVacia);
        });

        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la licencia es null")
    void debeLanzarExcepcionCuandoLicenciaNull() {
        // Arrange
        String nombre = "Juan Pérez";

        // Act & Assert
        assertThrows(LicenciaInvalidaException.class, () -> {
            crearConductorUseCase.execute(nombre, null);
        });

        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    @Test
    @DisplayName("Debe mantener el nombre tal como se proporciona")
    void debeCrearConductorConNombreOriginal() {
        // Arrange
        String nombreConEspacios = "  Juan   Pérez  ";
        String licenciaStr = "LIC123456";

        when(conductorRepository.existsByLicencia(licenciaStr)).thenReturn(false);
        when(conductorRepository.save(any(Conductor.class))).thenAnswer(invocation -> {
            Conductor c = invocation.getArgument(0);
            return new Conductor(1L, c.getNombre(), c.getLicencia(), true, new HashSet<>());
        });

        // Act
        Conductor resultado = crearConductorUseCase.execute(nombreConEspacios, licenciaStr);

        // Assert
        assertEquals(nombreConEspacios, resultado.getNombre());
    }
}
