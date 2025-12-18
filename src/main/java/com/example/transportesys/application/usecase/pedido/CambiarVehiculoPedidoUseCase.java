package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.exception.CapacidadInsuficienteException;
import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.exception.VehiculoNoAsignadoAConductorException;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.PedidoRepository;
import com.example.transportesys.domain.repository.VehiculoRepository;
import com.example.transportesys.domain.specification.VehiculoEstaActivoSpec;
import com.example.transportesys.domain.specification.VehiculoEstaAsignadoAConductorSpec;
import com.example.transportesys.domain.specification.VehiculoTieneCapacidadSuficienteSpec;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para cambiar el vehículo de un pedido.
 * Solo permite el cambio si:
 * - El pedido está en estado PENDIENTE
 * - El nuevo vehículo existe y está activo
 * - El nuevo vehículo está asignado al mismo conductor del pedido
 * - El nuevo vehículo tiene capacidad suficiente
 */
public class CambiarVehiculoPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final VehiculoEstaActivoSpec vehiculoActivoSpec;
    private final VehiculoEstaAsignadoAConductorSpec vehiculoAsignadoConductorSpec;
    private final VehiculoTieneCapacidadSuficienteSpec capacidadSpec;

    public CambiarVehiculoPedidoUseCase(
            PedidoRepository pedidoRepository,
            VehiculoRepository vehiculoRepository,
            VehiculoEstaActivoSpec vehiculoActivoSpec,
            VehiculoEstaAsignadoAConductorSpec vehiculoAsignadoConductorSpec,
            VehiculoTieneCapacidadSuficienteSpec capacidadSpec) {
        this.pedidoRepository = pedidoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.vehiculoActivoSpec = vehiculoActivoSpec;
        this.vehiculoAsignadoConductorSpec = vehiculoAsignadoConductorSpec;
        this.capacidadSpec = capacidadSpec;
    }

    @Transactional
    public Pedido execute(Long pedidoId, Long nuevoVehiculoId) {
        // 1. Buscar pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + pedidoId));

        // 2. Verificar que el pedido tenga un conductor asignado
        if (pedido.getConductorId() == null) {
            throw new IllegalStateException("El pedido no tiene conductor asignado");
        }

        // 3. Buscar nuevo vehículo
        Vehiculo nuevoVehiculo = vehiculoRepository.findById(nuevoVehiculoId)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con ID: " + nuevoVehiculoId));

        // 4. Validar que el vehículo esté activo
        if (!vehiculoActivoSpec.isSatisfiedBy(nuevoVehiculo)) {
            throw new IllegalStateException("El vehículo no está activo");
        }

        // 5. Validar que el vehículo esté asignado al mismo conductor del pedido
        if (!vehiculoAsignadoConductorSpec.isSatisfiedBy(nuevoVehiculo, pedido.getConductorId())) {
            throw new VehiculoNoAsignadoAConductorException(nuevoVehiculoId, pedido.getConductorId());
        }

        // 6. Validar capacidad del vehículo
        if (!capacidadSpec.isSatisfiedBy(nuevoVehiculo, pedido.getPeso())) {
            throw new CapacidadInsuficienteException(
                String.format("El vehículo no tiene capacidad suficiente. Capacidad: %s kg, Peso del pedido: %s kg",
                    nuevoVehiculo.getCapacidad().getValorEnKg(), pedido.getPeso().getValorEnKg())
            );
        }

        // 7. Cambiar vehículo (la validación de estado PENDIENTE está en el dominio)
        pedido.cambiarVehiculo(nuevoVehiculoId);

        // 8. Guardar y retornar
        return pedidoRepository.save(pedido);
    }
}
