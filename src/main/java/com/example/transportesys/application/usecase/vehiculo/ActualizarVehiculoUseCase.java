package com.example.transportesys.application.usecase.vehiculo;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.VehiculoRepository;
import com.example.transportesys.domain.valueobject.Capacidad;
import com.example.transportesys.domain.valueobject.Placa;

/**
 * Caso de uso para actualizar un vehículo existente.
 */
public class ActualizarVehiculoUseCase {

    private final VehiculoRepository vehiculoRepository;

    public ActualizarVehiculoUseCase(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public Vehiculo execute(Long id, String placaStr, Double capacidadKg) {
        // Buscar el vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con ID: " + id));

        // Actualizar campos
        if (placaStr != null && !placaStr.isBlank()) {
            Placa nuevaPlaca = new Placa(placaStr);
            vehiculo.setPlaca(nuevaPlaca);
        }

        if (capacidadKg != null) {
            Capacidad nuevaCapacidad = new Capacidad(capacidadKg);
            vehiculo.setCapacidad(nuevaCapacidad);
        }

        // Guardar y retornar
        return vehiculoRepository.save(vehiculo);
    }
}
