package com.example.transportesys.application.usecase.vehiculo;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.VehiculoRepository;

/**
 * Caso de uso para obtener un vehículo por ID.
 */
public class ObtenerVehiculoUseCase {

    private final VehiculoRepository vehiculoRepository;

    public ObtenerVehiculoUseCase(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public Vehiculo execute(Long id) {
        return vehiculoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con ID: " + id));
    }
}
