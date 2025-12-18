package com.example.transportesys.infrastructure.adapter.in.rest.mapper;

import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.ConductorResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre Conductor (dominio) y ConductorResponse (REST DTO).
 */
@Component
public class ConductorRestMapper {

    public ConductorResponse toResponse(Conductor conductor) {
        if (conductor == null) {
            return null;
        }

        return new ConductorResponse(
            conductor.getId(),
            conductor.getNombre(),
            conductor.getLicencia().getNumero(),
            conductor.isActivo(),
            conductor.getVehiculosIds(),
            conductor.cantidadVehiculos()
        );
    }
}
