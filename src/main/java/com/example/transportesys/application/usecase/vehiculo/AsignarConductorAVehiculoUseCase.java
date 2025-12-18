package com.example.transportesys.application.usecase.vehiculo;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.exception.VehiculoYaAsignadoException;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.ConductorRepository;
import com.example.transportesys.domain.repository.VehiculoRepository;
import com.example.transportesys.domain.specification.ConductorPuedeAsignarVehiculoSpec;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para asignar un conductor a un vehículo.
 */
public class AsignarConductorAVehiculoUseCase {

    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;
    private final ConductorPuedeAsignarVehiculoSpec conductorPuedeAsignarSpec;

    public AsignarConductorAVehiculoUseCase(
            VehiculoRepository vehiculoRepository,
            ConductorRepository conductorRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.conductorRepository = conductorRepository;
        this.conductorPuedeAsignarSpec = new ConductorPuedeAsignarVehiculoSpec();
    }

    @Transactional
    public Vehiculo execute(Long vehiculoId, Long conductorId) {
        // Buscar vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con ID: " + vehiculoId));

        // Buscar conductor
        Conductor conductor = conductorRepository.findById(conductorId)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con ID: " + conductorId));

        // CRITICAL: Validar que el vehículo no esté asignado a otro conductor
        if (vehiculo.estaAsignado() && !vehiculo.estaAsignadoA(conductorId)) {
            throw new VehiculoYaAsignadoException(vehiculoId, vehiculo.getConductorId());
        }

        // Validar que el conductor pueda asignar más vehículos
        if (!conductorPuedeAsignarSpec.isSatisfiedBy(conductor)) {
            throw new IllegalStateException(
                "El conductor no puede asignar más vehículos (máximo 3) o está inactivo"
            );
        }

        // Asignar conductor al vehículo
        vehiculo.asignarConductor(conductorId);

        // Asignar vehículo al conductor (solo si no lo tiene ya)
        if (!conductor.getVehiculosIds().contains(vehiculoId)) {
            conductor.asignarVehiculo(vehiculoId);
        }

        // Guardar ambos
        vehiculoRepository.save(vehiculo);
        conductorRepository.save(conductor);

        return vehiculo;
    }
}
