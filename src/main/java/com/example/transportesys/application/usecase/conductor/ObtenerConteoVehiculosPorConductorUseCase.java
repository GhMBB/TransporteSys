package com.example.transportesys.application.usecase.conductor;

import com.example.transportesys.domain.repository.ConductorRepository;

import java.util.Map;

/**
 * Caso de uso para obtener el conteo de vehículos por conductor.
 */
public class ObtenerConteoVehiculosPorConductorUseCase {

    private final ConductorRepository conductorRepository;

    public ObtenerConteoVehiculosPorConductorUseCase(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    public Map<Long, Integer> execute() {
        return conductorRepository.contarVehiculosPorConductor();
    }
}
