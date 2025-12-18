package com.example.transportesys.domain.repository;

import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.PageResult;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port (interfaz) del repositorio de Conductor en el dominio.
 * No depende de frameworks (sin Spring Data).
 */
public interface ConductorRepository {

    Conductor save(Conductor conductor);

    Optional<Conductor> findById(Long id);

    List<Conductor> findAll();

    List<Conductor> findAll(int page, int size);

    PageResult<Conductor> findAllPaged(int page, int size);

    List<Conductor> findByActivo(boolean activo);

    List<Conductor> findConductoresSinVehiculos();

    Optional<Conductor> findByLicencia(String licencia);

    void deleteById(Long id);

    boolean existsByLicencia(String licencia);

    long count();

    /**
     * Obtiene un mapa con el conteo de vehículos por conductor.
     * @return Map donde la clave es el ID del conductor y el valor es la cantidad de vehículos
     */
    Map<Long, Integer> contarVehiculosPorConductor();
}
