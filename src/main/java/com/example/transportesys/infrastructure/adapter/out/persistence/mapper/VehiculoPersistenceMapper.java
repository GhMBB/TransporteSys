package com.example.transportesys.infrastructure.adapter.out.persistence.mapper;

import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.valueobject.Capacidad;
import com.example.transportesys.domain.valueobject.Placa;
import com.example.transportesys.infrastructure.adapter.out.persistence.entity.VehiculoEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre Vehiculo (dominio) y VehiculoEntity (JPA).
 */
@Component
public class VehiculoPersistenceMapper {

    public VehiculoEntity toEntity(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return null;
        }

        VehiculoEntity entity = new VehiculoEntity();
        entity.setId(vehiculo.getId());
        entity.setPlaca(vehiculo.getPlaca().getValor());
        entity.setCapacidadKg(vehiculo.getCapacidad().getValorEnKg());
        entity.setActivo(vehiculo.isActivo());
        entity.setConductorId(vehiculo.getConductorId());
        entity.setCreadoPor(vehiculo.getCreadoPor());
        entity.setFechaCreacion(vehiculo.getFechaCreacion());
        entity.setModificadoPor(vehiculo.getModificadoPor());
        entity.setFechaModificacion(vehiculo.getFechaModificacion());

        return entity;
    }

    public Vehiculo toDomain(VehiculoEntity entity) {
        if (entity == null) {
            return null;
        }

        Placa placa = new Placa(entity.getPlaca());
        Capacidad capacidad = new Capacidad(entity.getCapacidadKg());

        Vehiculo vehiculo = new Vehiculo(entity.getId(), placa, capacidad);

        if (entity.getConductorId() != null) {
            vehiculo.asignarConductor(entity.getConductorId());
        }

        if (!entity.isActivo()) {
            vehiculo.desactivar();
        }

        vehiculo.setCreadoPor(entity.getCreadoPor());
        vehiculo.setFechaCreacion(entity.getFechaCreacion());
        vehiculo.setModificadoPor(entity.getModificadoPor());
        vehiculo.setFechaModificacion(entity.getFechaModificacion());

        return vehiculo;
    }
}
