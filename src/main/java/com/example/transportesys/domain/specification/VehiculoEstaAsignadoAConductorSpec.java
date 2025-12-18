package com.example.transportesys.domain.specification;

import com.example.transportesys.domain.model.Vehiculo;
import org.springframework.stereotype.Component;

/**
 * Especificación que verifica si un vehículo está asignado a un conductor específico.
 */
@Component
public class VehiculoEstaAsignadoAConductorSpec {

    public boolean isSatisfiedBy(Vehiculo vehiculo, Long conductorId) {
        if (vehiculo == null || conductorId == null) {
            return false;
        }
        return vehiculo.estaAsignadoA(conductorId);
    }
}
