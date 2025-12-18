package com.example.transportesys.application.usecase.vehiculo;

import com.example.transportesys.domain.exception.VehiculoDuplicadoException;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.VehiculoRepository;
import com.example.transportesys.domain.valueobject.Capacidad;
import com.example.transportesys.domain.valueobject.Placa;

/**
 * Caso de uso para crear un nuevo vehículo.
 */
public class CrearVehiculoUseCase {

    private final VehiculoRepository vehiculoRepository;

    public CrearVehiculoUseCase(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public Vehiculo execute(String placaStr, Double capacidadKg) {
        // Validar que la placa no exista
        Placa placa = new Placa(placaStr);
        if (vehiculoRepository.existsByPlaca(placa.getValor())) {
            throw new VehiculoDuplicadoException("Ya existe un vehículo con la placa: " + placa.getValor());
        }

        // Crear el vehículo
        Capacidad capacidad = new Capacidad(capacidadKg);
        Vehiculo vehiculo = new Vehiculo(null, placa, capacidad);

        // Guardar y retornar
        return vehiculoRepository.save(vehiculo);
    }
}
