package com.example.transportesys.infrastructure.adapter.in.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de Response para Vehículo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoResponse {
    private Long id;
    private String placa;
    private BigDecimal capacidadKg;
    private boolean activo;
    private Long conductorId;
}
