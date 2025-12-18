package com.example.transportesys.infrastructure.adapter.out.persistence.adapter;

import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.domain.repository.ConductorRepository;
import com.example.transportesys.infrastructure.adapter.out.persistence.mapper.ConductorPersistenceMapper;
import com.example.transportesys.infrastructure.adapter.out.persistence.repository.ConductorJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter que implementa ConductorRepository (dominio) usando JPA.
 */
@Component
public class ConductorRepositoryAdapter implements ConductorRepository {

    private final ConductorJpaRepository jpaRepository;
    private final ConductorPersistenceMapper mapper;

    public ConductorRepositoryAdapter(ConductorJpaRepository jpaRepository,
                                     ConductorPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Conductor save(Conductor conductor) {
        var entity = mapper.toEntity(conductor);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Conductor> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<Conductor> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Conductor> findAll(int page, int size) {
        return jpaRepository.findAll(PageRequest.of(page, size))
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public PageResult<Conductor> findAllPaged(int page, int size) {
        Page pageResult = jpaRepository.findAll(PageRequest.of(page, size));

        List<Conductor> content = (List<Conductor>) pageResult.getContent().stream()
            .map(entity -> mapper.toDomain((com.example.transportesys.infrastructure.adapter.out.persistence.entity.ConductorEntity) entity))
            .collect(Collectors.toList());

        return new PageResult<>(
            content,
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.getTotalPages(),
            pageResult.isFirst(),
            pageResult.isLast(),
            pageResult.hasNext(),
            pageResult.hasPrevious(),
            pageResult.getNumberOfElements(),
            pageResult.isEmpty()
        );
    }

    @Override
    public List<Conductor> findByActivo(boolean activo) {
        return jpaRepository.findByActivo(activo).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Conductor> findConductoresSinVehiculos() {
        return jpaRepository.findConductoresSinVehiculos().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Conductor> findByLicencia(String licencia) {
        return jpaRepository.findByLicencia(licencia)
            .map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByLicencia(String licencia) {
        return jpaRepository.existsByLicencia(licencia);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public Map<Long, Integer> contarVehiculosPorConductor() {
        Map<Long, Integer> resultado = new HashMap<>();
        List<Conductor> conductores = findAll();

        for (Conductor conductor : conductores) {
            resultado.put(conductor.getId(), conductor.cantidadVehiculos());
        }

        return resultado;
    }
}
