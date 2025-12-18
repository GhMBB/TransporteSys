package com.example.transportesys.domain.repository;

import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.domain.model.Vehiculo;

import java.util.List;
import java.util.Optional;

/**
 * Port (interfaz) del repositorio de Vehículo en el dominio.
 * No depende de frameworks (sin Spring Data).
 */
public interface VehiculoRepository {

    Vehiculo save(Vehiculo vehiculo);

    Optional<Vehiculo> findById(Long id);

    List<Vehiculo> findAll();

    List<Vehiculo> findAll(int page, int size);

    PageResult<Vehiculo> findAllPaged(int page, int size);

    List<Vehiculo> findByActivo(boolean activo);

    List<Vehiculo> findVehiculosLibres();

    List<Vehiculo> findByConductorId(Long conductorId);

    Optional<Vehiculo> findByPlaca(String placa);

    void deleteById(Long id);

    boolean existsByPlaca(String placa);

    long count();
}
