package com.example.transportesys.domain.specification;

import com.example.transportesys.domain.model.Vehiculo;

/**
 * Especificación que verifica si un vehículo está libre (sin conductor asignado y activo).
 */
public class VehiculoEstaLibreSpec implements Specification<Vehiculo> {

    @Override
    public boolean isSatisfiedBy(Vehiculo vehiculo) {
        return vehiculo != null && vehiculo.estaLibre();
    }
}
