package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.repository.PedidoRepository;

/**
 * Caso de uso para actualizar el estado de un pedido.
 * Valida las transiciones de estado permitidas.
 */
public class ActualizarEstadoPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public ActualizarEstadoPedidoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido execute(Long pedidoId, EstadoPedido nuevoEstado) {
        // Buscar el pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + pedidoId));

        // Cambiar estado (la validación de transición está en la entidad)
        pedido.cambiarEstado(nuevoEstado);

        // Guardar y retornar
        return pedidoRepository.save(pedido);
    }
}
