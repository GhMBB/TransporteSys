package com.example.transportesys.domain.specification;

import com.example.transportesys.domain.model.Conductor;

/**
 * Especificación que verifica si un conductor puede asignar más vehículos.
 * Un conductor puede asignar más vehículos si:
 * - Está activo
 * - No ha alcanzado el límite de 3 vehículos
 */
public class ConductorPuedeAsignarVehiculoSpec implements Specification<Conductor> {

    @Override
    public boolean isSatisfiedBy(Conductor conductor) {
        if (conductor == null) {
            return false;
        }
        return conductor.isActivo() && conductor.puedeAsignarMasVehiculos();
    }
}
