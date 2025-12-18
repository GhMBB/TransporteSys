package com.example.transportesys.application.usecase.conductor;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.repository.ConductorRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para eliminar lógicamente un conductor.
 */
public class EliminarConductorUseCase {

    private final ConductorRepository conductorRepository;

    public EliminarConductorUseCase(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    @Transactional
    public void execute(Long id) {
        // Buscar el conductor
        Conductor conductor = conductorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con ID: " + id));

        // Desactivar (eliminación lógica)
        conductor.desactivar();

        // Guardar cambios
        conductorRepository.save(conductor);
    }
}
