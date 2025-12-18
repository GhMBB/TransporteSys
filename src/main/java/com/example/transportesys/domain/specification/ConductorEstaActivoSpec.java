package com.example.transportesys.domain.specification;

import com.example.transportesys.domain.model.Conductor;
import org.springframework.stereotype.Component;

/**
 * Especificación que verifica si un conductor está activo.
 */
@Component
public class ConductorEstaActivoSpec implements Specification<Conductor> {

    @Override
    public boolean isSatisfiedBy(Conductor conductor) {
        return conductor != null && conductor.isActivo();
    }
}
