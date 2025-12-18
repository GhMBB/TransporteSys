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
        Pedido pedido = new Pedido(entity.getId(), entity.getDescripcion(), peso);

        pedido.setDireccionOrigen(entity.getDireccionOrigen());
        pedido.setDireccionDestino(entity.getDireccionDestino());
        pedido.setFechaCreacion(entity.getFechaCreacion());
        pedido.setFechaActualizacion(entity.getFechaActualizacion());

        // Asignar vehículo y conductor si existen
        if (entity.getVehiculoId() != null && entity.getConductorId() != null) {
            pedido.asignarVehiculoYConductor(entity.getVehiculoId(), entity.getConductorId());
        }

        return pedido;
    }
}
