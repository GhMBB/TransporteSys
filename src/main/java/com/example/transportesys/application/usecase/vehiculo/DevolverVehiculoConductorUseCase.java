package com.example.transportesys.application.usecase.vehiculo;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.exception.VehiculoEnUsoException;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.ConductorRepository;
import com.example.transportesys.domain.repository.PedidoRepository;
import com.example.transportesys.domain.repository.VehiculoRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para devolver un vehículo de un conductor.
 * Valida que el vehículo no esté siendo usado en pedidos activos antes de permitir la devolución.
 */
public class DevolverVehiculoConductorUseCase {

    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;
    private final PedidoRepository pedidoRepository;

    public DevolverVehiculoConductorUseCase(
            VehiculoRepository vehiculoRepository,
            ConductorRepository conductorRepository,
            PedidoRepository pedidoRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.conductorRepository = conductorRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public Vehiculo execute(Long vehiculoId) {
        // 1. Buscar vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con ID: " + vehiculoId));

        // 2. Verificar que el vehículo esté asignado
        if (!vehiculo.estaAsignado()) {
            throw new IllegalStateException("El vehículo no está asignado a ningún conductor");
        }

        Long conductorId = vehiculo.getConductorId();

        // 3. Buscar conductor
        Conductor conductor = conductorRepository.findById(conductorId)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con ID: " + conductorId));

        // 4. Verificar que el vehículo no esté siendo usado en pedidos activos
        long pedidosActivos = pedidoRepository.countActivosByVehiculoId(vehiculoId);
        if (pedidosActivos > 0) {
            throw new VehiculoEnUsoException(vehiculoId, (int) pedidosActivos);
        }

        // 5. Desasignar vehículo del conductor
        conductor.desasignarVehiculo(vehiculoId);

        // 6. Desasignar conductor del vehículo
        vehiculo.desasignarConductor();

        // 7. Guardar ambos
        conductorRepository.save(conductor);
        vehiculoRepository.save(vehiculo);

        return vehiculo;
    }
}
