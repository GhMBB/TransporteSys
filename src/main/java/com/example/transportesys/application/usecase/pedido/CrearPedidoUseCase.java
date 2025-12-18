package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.exception.CapacidadInsuficienteException;
import com.example.transportesys.domain.exception.ConductorInactivoException;
import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.ConductorRepository;
import com.example.transportesys.domain.repository.PedidoRepository;
import com.example.transportesys.domain.repository.VehiculoRepository;
import com.example.transportesys.domain.specification.ConductorEstaActivoSpec;
import com.example.transportesys.domain.specification.VehiculoEstaActivoSpec;
import com.example.transportesys.domain.specification.VehiculoTieneCapacidadSuficienteSpec;
import com.example.transportesys.domain.valueobject.Peso;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para crear un pedido con todas las validaciones de negocio.
 * Valida:
 * - Vehículo existe y está activo
 * - Conductor existe y está activo
 * - Vehículo tiene capacidad suficiente
 * - Conductor no supera el límite de vehículos (ya validado en asignación)
 */
public class CrearPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;
    private final VehiculoTieneCapacidadSuficienteSpec capacidadSpec;
    private final VehiculoEstaActivoSpec vehiculoActivoSpec;
    private final ConductorEstaActivoSpec conductorActivoSpec;

    public CrearPedidoUseCase(
            PedidoRepository pedidoRepository,
            VehiculoRepository vehiculoRepository,
            ConductorRepository conductorRepository,
            VehiculoTieneCapacidadSuficienteSpec capacidadSpec,
            VehiculoEstaActivoSpec vehiculoActivoSpec,
            ConductorEstaActivoSpec conductorActivoSpec) {
        this.pedidoRepository = pedidoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.conductorRepository = conductorRepository;
        this.capacidadSpec = capacidadSpec;
        this.vehiculoActivoSpec = vehiculoActivoSpec;
        this.conductorActivoSpec = conductorActivoSpec;
    }

    @Transactional
    public Pedido execute(
            String descripcion,
            Double pesoKg,
            Long vehiculoId,
            Long conductorId,
            String direccionOrigen,
            String direccionDestino) {

        // 1. Obtener y validar vehículo
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado con ID: " + vehiculoId));

        if (!vehiculoActivoSpec.isSatisfiedBy(vehiculo)) {
            throw new IllegalStateException("El vehículo no está activo");
        }

        // 2. Obtener y validar conductor
        Conductor conductor = conductorRepository.findById(conductorId)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con ID: " + conductorId));

        if (!conductorActivoSpec.isSatisfiedBy(conductor)) {
            throw new ConductorInactivoException();
        }

        // 3. Validar capacidad del vehículo
        Peso peso = new Peso(pesoKg);
        if (!capacidadSpec.isSatisfiedBy(vehiculo, peso)) {
            throw new CapacidadInsuficienteException(
                String.format("El vehículo no tiene capacidad suficiente. Capacidad: %s kg, Peso del pedido: %s kg",
                    vehiculo.getCapacidad().getValorEnKg(), peso.getValorEnKg())
            );
        }

        // 4. Crear el pedido
        Pedido pedido = new Pedido(null, descripcion, peso);
        pedido.setDireccionOrigen(direccionOrigen);
        pedido.setDireccionDestino(direccionDestino);

        // 5. Asignar vehículo y conductor
        pedido.asignarVehiculoYConductor(vehiculoId, conductorId);

        // 6. Guardar y retornar
        return pedidoRepository.save(pedido);
    }
}
