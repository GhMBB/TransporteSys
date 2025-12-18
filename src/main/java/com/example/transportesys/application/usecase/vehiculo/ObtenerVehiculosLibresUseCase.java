package com.example.transportesys.application.usecase.vehiculo;

import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.VehiculoRepository;

import java.util.List;

/**
 * Caso de uso para obtener vehículos libres (sin conductor asignado).
 */
public class ObtenerVehiculosLibresUseCase {

    private final VehiculoRepository vehiculoRepository;

    public ObtenerVehiculosLibresUseCase(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<Vehiculo> execute() {
        return vehiculoRepository.findVehiculosLibres();
    }
}
