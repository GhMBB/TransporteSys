package com.example.transportesys.infrastructure.adapter.out.persistence.repository;

import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.infrastructure.adapter.out.persistence.entity.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA Repository para PedidoEntity.
 */
@Repository
public interface PedidoJpaRepository extends JpaRepository<PedidoEntity, Long> {

    List<PedidoEntity> findByEstado(EstadoPedido estado);

    List<PedidoEntity> findByVehiculoId(Long vehiculoId);

    List<PedidoEntity> findByConductorId(Long conductorId);

    List<PedidoEntity> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    long countByEstado(EstadoPedido estado);

    Page<PedidoEntity> findAll(Pageable pageable);
}
