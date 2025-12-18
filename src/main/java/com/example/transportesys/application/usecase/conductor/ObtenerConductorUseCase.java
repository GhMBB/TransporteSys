package com.example.transportesys.application.usecase.conductor;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.repository.ConductorRepository;

/**
 * Caso de uso para obtener un conductor por ID.
 */
public class ObtenerConductorUseCase {

    private final ConductorRepository conductorRepository;

    public ObtenerConductorUseCase(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    public Conductor execute(Long id) {
        return conductorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con ID: " + id));
    }
}
