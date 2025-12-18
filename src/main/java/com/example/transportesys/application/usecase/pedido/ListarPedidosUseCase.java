package com.example.transportesys.application.usecase.pedido;

import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.repository.PedidoRepository;

import java.util.List;

/**
 * Caso de uso para listar pedidos.
 */
public class ListarPedidosUseCase {

    private final PedidoRepository pedidoRepository;

    public ListarPedidosUseCase(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> execute() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> execute(int page, int size) {
        return pedidoRepository.findAll(page, size);
    }

    public PageResult<Pedido> executePaged(int page, int size) {
        return pedidoRepository.findAllPaged(page, size);
    }

    public List<Pedido> executeByEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public List<Pedido> executeByVehiculo(Long vehiculoId) {
        return pedidoRepository.findByVehiculoId(vehiculoId);
    }

    public List<Pedido> executeByConductor(Long conductorId) {
        return pedidoRepository.findByConductorId(conductorId);
    }
}
