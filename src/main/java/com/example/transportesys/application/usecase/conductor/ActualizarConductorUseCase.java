package com.example.transportesys.application.usecase.conductor;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.repository.ConductorRepository;
import com.example.transportesys.domain.valueobject.LicenciaConducir;

/**
 * Caso de uso para actualizar un conductor existente.
 */
public class ActualizarConductorUseCase {

    private final ConductorRepository conductorRepository;

    public ActualizarConductorUseCase(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    public Conductor execute(Long id, String nombre, String licenciaStr) {
        // Buscar el conductor
        Conductor conductor = conductorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con ID: " + id));

        // Actualizar campos
        if (nombre != null && !nombre.isBlank()) {
            conductor.setNombre(nombre);
        }

        if (licenciaStr != null && !licenciaStr.isBlank()) {
            LicenciaConducir nuevaLicencia = new LicenciaConducir(licenciaStr);
            conductor.setLicencia(nuevaLicencia);
        }

        // Guardar y retornar
        return conductorRepository.save(conductor);
    }
}
