package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso para completar un pedido.
 */
@RequiredArgsConstructor
public class CompletarPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    @Transactional
    public Pedido execute(Long pedidoId) {
        // Buscar el pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + pedidoId));

        // Completar
        pedido.completar();

        // Guardar y retornar
        return pedidoRepository.save(pedido);
    }
}
