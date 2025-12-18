package com.example.transportesys.infrastructure.adapter.in.rest.mapper;

import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.PedidoResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre Pedido (dominio) y PedidoResponse (REST DTO).
 */
@Component
public class PedidoRestMapper {

    public PedidoResponse toResponse(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        return new PedidoResponse(
            pedido.getId(),
            pedido.getDescripcion(),
            pedido.getPeso().getValorEnKg(),
            pedido.getEstado(),
            pedido.getVehiculoId(),
            pedido.getConductorId(),
            pedido.getDireccionOrigen(),
            pedido.getDireccionDestino(),
            pedido.getFechaCreacion(),
            pedido.getFechaActualizacion()
        );
    }
}
