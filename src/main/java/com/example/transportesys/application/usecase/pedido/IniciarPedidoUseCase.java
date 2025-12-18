package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.repository.PedidoRepository;

/**
 * Caso de uso para iniciar un pedido (cambiar a EN_PROGRESO).
 */
public class IniciarPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public IniciarPedidoUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido execute(Long pedidoId) {
        // Buscar el pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + pedidoId));

        // Iniciar (la validación está en la entidad)
        pedido.iniciar();

        // Guardar y retornar
        return pedidoRepository.save(pedido);
    }
}
