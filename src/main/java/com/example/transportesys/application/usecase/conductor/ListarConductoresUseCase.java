package com.example.transportesys.application.usecase.conductor;

import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.domain.repository.ConductorRepository;

import java.util.List;

/**
 * Caso de uso para listar conductores con paginación.
 */
public class ListarConductoresUseCase {

    private final ConductorRepository conductorRepository;

    public ListarConductoresUseCase(ConductorRepository conductorRepository) {
        this.conductorRepository = conductorRepository;
    }

    public List<Conductor> execute() {
        return conductorRepository.findAll();
    }

    public List<Conductor> execute(int page, int size) {
        return conductorRepository.findAll(page, size);
    }

    public PageResult<Conductor> executePaged(int page, int size) {
        return conductorRepository.findAllPaged(page, size);
    }

    public List<Conductor> executeActivos() {
        return conductorRepository.findByActivo(true);
    }
}
