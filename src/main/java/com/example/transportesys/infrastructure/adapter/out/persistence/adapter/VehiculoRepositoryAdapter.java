package com.example.transportesys.infrastructure.adapter.out.persistence.adapter;

import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.repository.VehiculoRepository;
import com.example.transportesys.infrastructure.adapter.out.persistence.entity.VehiculoEntity;
import com.example.transportesys.infrastructure.adapter.out.persistence.mapper.VehiculoPersistenceMapper;
import com.example.transportesys.infrastructure.adapter.out.persistence.repository.VehiculoJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter que implementa VehiculoRepository (dominio) usando JPA.
 * Traduce entre el dominio y la infraestructura.
 */
@Component
public class VehiculoRepositoryAdapter implements VehiculoRepository {

    private final VehiculoJpaRepository jpaRepository;
    private final VehiculoPersistenceMapper mapper;

    public VehiculoRepositoryAdapter(VehiculoJpaRepository jpaRepository,
                                    VehiculoPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Vehiculo save(Vehiculo vehiculo) {
        VehiculoEntity entity = mapper.toEntity(vehiculo);
        VehiculoEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Vehiculo> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<Vehiculo> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Vehiculo> findAll(int page, int size) {
        return jpaRepository.findAll(PageRequest.of(page, size))
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public PageResult<Vehiculo> findAllPaged(int page, int size) {
        Page<VehiculoEntity> pageResult = jpaRepository.findAll(PageRequest.of(page, size));

        List<Vehiculo> content = pageResult.getContent().stream()
            .map(mapper::toDomain)
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
    public List<Vehiculo> findByActivo(boolean activo) {
        return jpaRepository.findByActivo(activo).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Vehiculo> findVehiculosLibres() {
        return jpaRepository.findVehiculosLibres().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Vehiculo> findByConductorId(Long conductorId) {
        return jpaRepository.findByConductorId(conductorId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Vehiculo> findByPlaca(String placa) {
        return jpaRepository.findByPlaca(placa)
            .map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByPlaca(String placa) {
        return jpaRepository.existsByPlaca(placa);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }
}
