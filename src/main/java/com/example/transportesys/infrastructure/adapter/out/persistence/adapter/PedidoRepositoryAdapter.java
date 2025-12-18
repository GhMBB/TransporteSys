package com.example.transportesys.infrastructure.adapter.out.persistence.adapter;

import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.repository.PedidoRepository;
import com.example.transportesys.infrastructure.adapter.out.persistence.mapper.PedidoPersistenceMapper;
import com.example.transportesys.infrastructure.adapter.out.persistence.repository.PedidoJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter que implementa PedidoRepository (dominio) usando JPA.
 */
@Component
public class PedidoRepositoryAdapter implements PedidoRepository {

    private final PedidoJpaRepository jpaRepository;
    private final PedidoPersistenceMapper mapper;

    public PedidoRepositoryAdapter(PedidoJpaRepository jpaRepository,
                                  PedidoPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Pedido save(Pedido pedido) {
        var entity = mapper.toEntity(pedido);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public List<Pedido> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> findAll(int page, int size) {
        return jpaRepository.findAll(PageRequest.of(page, size))
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public PageResult<Pedido> findAllPaged(int page, int size) {
        Page pageResult = jpaRepository.findAll(PageRequest.of(page, size));

        List<Pedido> content = (List<Pedido>) pageResult.getContent().stream()
            .map(entity -> mapper.toDomain((com.example.transportesys.infrastructure.adapter.out.persistence.entity.PedidoEntity) entity))
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
    public List<Pedido> findByEstado(EstadoPedido estado) {
        return jpaRepository.findByEstado(estado).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> findByVehiculoId(Long vehiculoId) {
        return jpaRepository.findByVehiculoId(vehiculoId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> findByConductorId(Long conductorId) {
        return jpaRepository.findByConductorId(conductorId).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Pedido> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin) {
        return jpaRepository.findByFechaCreacionBetween(inicio, fin).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public long countByEstado(EstadoPedido estado) {
        return jpaRepository.countByEstado(estado);
    }
}
