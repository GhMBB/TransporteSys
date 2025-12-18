package com.example.transportesys.domain.repository;

import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.domain.model.Pedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Port (interfaz) del repositorio de Pedido en el dominio.
 * No depende de frameworks (sin Spring Data).
 */
public interface PedidoRepository {

    Pedido save(Pedido pedido);

    Optional<Pedido> findById(Long id);

    List<Pedido> findAll();

    List<Pedido> findAll(int page, int size);

    PageResult<Pedido> findAllPaged(int page, int size);

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByVehiculoId(Long vehiculoId);

    List<Pedido> findByConductorId(Long conductorId);

    List<Pedido> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    void deleteById(Long id);

    long count();

    long countByEstado(EstadoPedido estado);
}
