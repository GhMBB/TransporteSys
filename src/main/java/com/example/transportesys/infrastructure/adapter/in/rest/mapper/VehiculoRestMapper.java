package com.example.transportesys.infrastructure.adapter.in.rest.mapper;

import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.VehiculoResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre Vehiculo (dominio) y VehiculoResponse (REST DTO).
 */
@Component
public class VehiculoRestMapper {

    public VehiculoResponse toResponse(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return null;
        }

        return new VehiculoResponse(
            vehiculo.getId(),
            vehiculo.getPlaca().getValor(),
            vehiculo.getCapacidad().getValorEnKg(),
            vehiculo.isActivo(),
            vehiculo.getConductorId()
        );
    }
}
