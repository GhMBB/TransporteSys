package com.example.transportesys.application.usecase.vehiculo;

import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.VehiculoRepository;

import java.util.List;

/**
 * Caso de uso para listar vehículos con paginación.
 */
public class ListarVehiculosUseCase {

    private final VehiculoRepository vehiculoRepository;

    public ListarVehiculosUseCase(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<Vehiculo> execute() {
        return vehiculoRepository.findAll();
    }

    public List<Vehiculo> execute(int page, int size) {
        return vehiculoRepository.findAll(page, size);
    }

    public PageResult<Vehiculo> executePaged(int page, int size) {
        return vehiculoRepository.findAllPaged(page, size);
    }

    public List<Vehiculo> executeActivos() {
        return vehiculoRepository.findByActivo(true);
    }
}
