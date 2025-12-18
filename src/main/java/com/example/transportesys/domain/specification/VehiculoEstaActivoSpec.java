package com.example.transportesys.domain.specification;

import com.example.transportesys.domain.model.Vehiculo;
import org.springframework.stereotype.Component;

/**
 * Especificación que verifica si un vehículo está activo.
 */
@Component
public class VehiculoEstaActivoSpec implements Specification<Vehiculo> {

    @Override
    public boolean isSatisfiedBy(Vehiculo vehiculo) {
        return vehiculo != null && vehiculo.isActivo();
    }
}
