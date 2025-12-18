package com.example.transportesys.application.usecase.vehiculo;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.VehiculoRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para eliminar lógicamente un vehículo.
 */
public class EliminarVehiculoUseCase {

    private final VehiculoRepository vehiculoRepository;

    public EliminarVehiculoUseCase(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    @Transactional
    public void execute(Long id) {
        // Buscar el vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con ID: " + id));

        // Desactivar (eliminación lógica)
        vehiculo.desactivar();

        // Guardar cambios
        vehiculoRepository.save(vehiculo);
    }
}
