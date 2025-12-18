package com.example.transportesys.infrastructure.adapter.in.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de Response para Conductor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConductorResponse {
    private Long id;
    private String nombre;
    private String licencia;
    private boolean activo;
    private List<Long> vehiculosIds;
    private int cantidadVehiculos;
}
