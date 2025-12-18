package com.example.transportesys.domain.specification;

import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.valueobject.Peso;

/**
 * Especificación que verifica si un vehículo tiene capacidad suficiente para un peso dado.
 */
public class VehiculoTieneCapacidadSpec {

    /**
     * Verifica si el vehículo tiene capacidad suficiente para el peso especificado.
     *
     * @param vehiculo El vehículo a validar
     * @param peso El peso que se desea transportar
     * @return true si el vehículo tiene capacidad suficiente
     */
    public boolean isSatisfiedBy(Vehiculo vehiculo, Peso peso) {
        if (vehiculo == null || peso == null) {
            return false;
        }
        return vehiculo.tieneCapacidadPara(peso);
    }
}
