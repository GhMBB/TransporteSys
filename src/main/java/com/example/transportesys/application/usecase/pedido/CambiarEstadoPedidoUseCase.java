package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CambiarEstadoPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    @Transactional
    public Pedido execute(Long pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + pedidoId));

        pedido.cambiarEstado(nuevoEstado);

        return pedidoRepository.save(pedido);
    }
}
