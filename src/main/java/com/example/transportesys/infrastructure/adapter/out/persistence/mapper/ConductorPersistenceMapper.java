package com.example.transportesys.infrastructure.adapter.out.persistence.mapper;

import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.valueobject.LicenciaConducir;
import com.example.transportesys.infrastructure.adapter.out.persistence.entity.ConductorEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Mapper que convierte entre Conductor (dominio) y ConductorEntity (JPA).
 */
@Component
public class ConductorPersistenceMapper {

    public ConductorEntity toEntity(Conductor conductor) {
        if (conductor == null) {
            return null;
        }

        ConductorEntity entity = new ConductorEntity();
        entity.setId(conductor.getId());
        entity.setNombre(conductor.getNombre());
        entity.setLicencia(conductor.getLicencia().getNumero());
        entity.setActivo(conductor.isActivo());
        entity.setVehiculosIds(new ArrayList<>(conductor.getVehiculosIds()));
        entity.setCreadoPor(conductor.getCreadoPor());
        entity.setFechaCreacion(conductor.getFechaCreacion());
        entity.setModificadoPor(conductor.getModificadoPor());
        entity.setFechaModificacion(conductor.getFechaModificacion());

        return entity;
    }

    public Conductor toDomain(ConductorEntity entity) {
        if (entity == null) {
            return null;
        }

        LicenciaConducir licencia = new LicenciaConducir(entity.getLicencia());
        Conductor conductor = new Conductor(entity.getId(), entity.getNombre(), licencia);

        if (!entity.isActivo()) {
            conductor.desactivar();
        }

        conductor.setVehiculosIds(new ArrayList<>(entity.getVehiculosIds()));
        conductor.setCreadoPor(entity.getCreadoPor());
        conductor.setFechaCreacion(entity.getFechaCreacion());
        conductor.setModificadoPor(entity.getModificadoPor());
        conductor.setFechaModificacion(entity.getFechaModificacion());

        return conductor;
    }
}
