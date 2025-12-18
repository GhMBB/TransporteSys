package com.example.transportesys.infrastructure.adapter.in.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de Request para crear/actualizar un Conductor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConductorRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La licencia es obligatoria")
    private String licencia;
}
