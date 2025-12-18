package com.example.transportesys.infrastructure.adapter.out.persistence.mapper;

import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.valueobject.Peso;
import com.example.transportesys.infrastructure.adapter.out.persistence.entity.PedidoEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre Pedido (dominio) y PedidoEntity (JPA).
 */
@Component
public class PedidoPersistenceMapper {

    public PedidoEntity toEntity(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        PedidoEntity entity = new PedidoEntity();
        entity.setId(pedido.getId());
        entity.setDescripcion(pedido.getDescripcion());
        entity.setPesoKg(pedido.getPeso().getValorEnKg());
        entity.setEstado(pedido.getEstado());
        entity.setVehiculoId(pedido.getVehiculoId());
        entity.setConductorId(pedido.getConductorId());
        entity.setDireccionOrigen(pedido.getDireccionOrigen());
        entity.setDireccionDestino(pedido.getDireccionDestino());
        entity.setFechaCreacion(pedido.getFechaCreacion());
        entity.setFechaActualizacion(pedido.getFechaActualizacion());

        return entity;
    }

    public Pedido toDomain(PedidoEntity entity) {
        if (entity == null) {
            return null;
        }

        Peso peso = new Peso(entity.getPesoKg());

        // Use the full constructor to restore all fields including estado
        // This avoids calling domain validation methods that only work in PENDIENTE state
        Pedido pedido = new Pedido(
            entity.getId(),
            entity.getDescripcion(),
            peso,
            entity.getVehiculoId(),
            entity.getConductorId(),
            entity.getEstado(),
            entity.getDireccionOrigen(),
            entity.getDireccionDestino()
        );

        // Restore timestamps from database
        pedido.setFechaCreacion(entity.getFechaCreacion());
        pedido.setFechaActualizacion(entity.getFechaActualizacion());

        return pedido;
    }
}
