package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.repository.PedidoRepository;

/**
 * Caso de uso para cancelar un pedido.
 */
public class CancelarPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public CancelarPedidoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido execute(Long pedidoId) {
        // Buscar el pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + pedidoId));

        // Cancelar
        pedido.cancelar();

        // Guardar y retornar
        return pedidoRepository.save(pedido);
    }
}
