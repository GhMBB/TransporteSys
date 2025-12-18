package com.example.transportesys.application.usecase.conductor;

import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.repository.ConductorRepository;

import java.util.List;

/**
 * Caso de uso para listar conductores sin vehículos asignados.
 */
public class ListarConductoresSinVehiculosUseCase {

    private final ConductorRepository conductorRepository;

    public ListarConductoresSinVehiculosUseCase(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    public List<Conductor> execute() {
        return conductorRepository.findConductoresSinVehiculos();
    }
}
