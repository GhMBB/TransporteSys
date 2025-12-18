package com.example.transportesys.application.usecase.conductor;

import com.example.transportesys.domain.exception.ConductorDuplicadoException;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.repository.ConductorRepository;
import com.example.transportesys.domain.valueobject.LicenciaConducir;

/**
 * Caso de uso para crear un nuevo conductor.
 */
public class CrearConductorUseCase {

    private final ConductorRepository conductorRepository;

    public CrearConductorUseCase(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    public Conductor execute(String nombre, String licenciaStr) {
        // Validar que la licencia no exista
        LicenciaConducir licencia = new LicenciaConducir(licenciaStr);
        if (conductorRepository.existsByLicencia(licencia.getNumero())) {
            throw new ConductorDuplicadoException("Ya existe un conductor con la licencia: " + licencia.getNumero());
        }

        // Crear el conductor
        Conductor conductor = new Conductor(null, nombre, licencia);

        // Guardar y retornar
        return conductorRepository.save(conductor);
    }
}
