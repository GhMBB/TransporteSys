package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.repository.PedidoRepository;

/**
 * Caso de uso para obtener un pedido por ID.
 */
public class ObtenerPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public ObtenerPedidoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido execute(Long id) {
        return pedidoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));
    }
}
