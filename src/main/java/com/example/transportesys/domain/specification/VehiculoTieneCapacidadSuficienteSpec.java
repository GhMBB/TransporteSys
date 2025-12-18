package com.example.transportesys.domain.specification;

import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.valueobject.Peso;
import org.springframework.stereotype.Component;

@Component
public class VehiculoTieneCapacidadSuficienteSpec {

    public boolean isSatisfiedBy(Vehiculo vehiculo, Peso peso) {
        if (vehiculo == null || peso == null) {
            return false;
        }
        return vehiculo.tieneCapacidadPara(peso);
    }
}
