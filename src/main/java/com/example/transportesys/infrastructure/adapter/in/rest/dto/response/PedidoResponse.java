package com.example.transportesys.infrastructure.adapter.in.rest.dto.response;

import com.example.transportesys.domain.enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de Response para Pedido.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private Long id;
    private String descripcion;
    private BigDecimal pesoKg;
    private EstadoPedido estado;
    private Long vehiculoId;
    private Long conductorId;
    private String direccionOrigen;
    private String direccionDestino;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
