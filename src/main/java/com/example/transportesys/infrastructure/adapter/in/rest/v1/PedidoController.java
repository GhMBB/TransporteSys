package com.example.transportesys.infrastructure.adapter.in.rest.v1;

import com.example.transportesys.application.usecase.pedido.*;
import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.request.PedidoRequest;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.PagedResponse;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.PedidoResponse;
import com.example.transportesys.infrastructure.adapter.in.rest.mapper.PedidoRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller para gestionar pedidos.
 * API versionada: /api/v1/pedidos
 */
@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "API para gestión de pedidos")
public class PedidoController {

    private final CrearPedidoUseCase crearPedidoUseCase;
    private final ActualizarEstadoPedidoUseCase actualizarEstadoUseCase;
    private final IniciarPedidoUseCase iniciarPedidoUseCase;
    private final CompletarPedidoUseCase completarPedidoUseCase;
    private final CancelarPedidoUseCase cancelarPedidoUseCase;
    private final CambiarVehiculoPedidoUseCase cambiarVehiculoUseCase;
    private final ListarPedidosUseCase listarPedidosUseCase;
    private final ObtenerPedidoUseCase obtenerPedidoUseCase;
    private final PedidoRestMapper mapper;

    public PedidoController(
            CrearPedidoUseCase crearPedidoUseCase,
            ActualizarEstadoPedidoUseCase actualizarEstadoUseCase,
            IniciarPedidoUseCase iniciarPedidoUseCase,
            CompletarPedidoUseCase completarPedidoUseCase,
            CancelarPedidoUseCase cancelarPedidoUseCase,
            CambiarVehiculoPedidoUseCase cambiarVehiculoUseCase,
            ListarPedidosUseCase listarPedidosUseCase,
            ObtenerPedidoUseCase obtenerPedidoUseCase,
            PedidoRestMapper mapper) {
        this.crearPedidoUseCase = crearPedidoUseCase;
        this.actualizarEstadoUseCase = actualizarEstadoUseCase;
        this.iniciarPedidoUseCase = iniciarPedidoUseCase;
        this.completarPedidoUseCase = completarPedidoUseCase;
        this.cancelarPedidoUseCase = cancelarPedidoUseCase;
        this.cambiarVehiculoUseCase = cambiarVehiculoUseCase;
        this.listarPedidosUseCase = listarPedidosUseCase;
        this.obtenerPedidoUseCase = obtenerPedidoUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido",
               description = "Crea un pedido validando capacidad del vehículo y estado del conductor")
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest request) {
        Pedido pedido = crearPedidoUseCase.execute(
            request.getDescripcion(),
            request.getPesoKg(),
            request.getVehiculoId(),
            request.getConductorId(),
            request.getDireccionOrigen(),
            request.getDireccionDestino()
        );
        return new ResponseEntity<>(mapper.toResponse(pedido), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pedido por ID")
    public ResponseEntity<PedidoResponse> obtener(@PathVariable Long id) {
        Pedido pedido = obtenerPedidoUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(pedido));
    }

    @GetMapping
    @Operation(summary = "Listar todos los pedidos con paginación opcional")
    public ResponseEntity<?> listar(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) EstadoPedido estado,
            @RequestParam(required = false) Long vehiculoId,
            @RequestParam(required = false) Long conductorId) {

        // Si hay filtros específicos, retornar lista simple
        if (estado != null || vehiculoId != null || conductorId != null) {
            List<Pedido> pedidos;
            if (estado != null) {
                pedidos = listarPedidosUseCase.executeByEstado(estado);
            } else if (vehiculoId != null) {
                pedidos = listarPedidosUseCase.executeByVehiculo(vehiculoId);
            } else {
                pedidos = listarPedidosUseCase.executeByConductor(conductorId);
            }

            List<PedidoResponse> response = pedidos.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        }

        // Si hay paginación, retornar con metadata
        if (page != null && size != null) {
            PageResult<Pedido> pageResult = listarPedidosUseCase.executePaged(page, size);

            List<PedidoResponse> content = pageResult.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

            PagedResponse.PageMetadata metadata = new PagedResponse.PageMetadata(
                pageResult.getPageNumber(),
                pageResult.getPageSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast(),
                pageResult.hasNext(),
                pageResult.hasPrevious(),
                pageResult.getNumberOfElements(),
                pageResult.isEmpty()
            );

            PagedResponse<PedidoResponse> response = new PagedResponse<>(content, metadata);
            return ResponseEntity.ok(response);
        }

        // Sin paginación ni filtros, retornar todos
        List<Pedido> pedidos = listarPedidosUseCase.execute();
        List<PedidoResponse> response = pedidos.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar el estado de un pedido")
    public ResponseEntity<PedidoResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPedido estado) {

        Pedido pedido = actualizarEstadoUseCase.execute(id, estado);
        return ResponseEntity.ok(mapper.toResponse(pedido));
    }

    @PostMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar un pedido (cambiar a EN_PROGRESO)")
    public ResponseEntity<PedidoResponse> iniciar(@PathVariable Long id) {
        Pedido pedido = iniciarPedidoUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(pedido));
    }

    @PostMapping("/{id}/completar")
    @Operation(summary = "Completar un pedido")
    public ResponseEntity<PedidoResponse> completar(@PathVariable Long id) {
        Pedido pedido = completarPedidoUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(pedido));
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar un pedido")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        Pedido pedido = cancelarPedidoUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(pedido));
    }

    @PatchMapping("/{pedidoId}/vehiculo")
    @Operation(summary = "Cambiar el vehículo de un pedido",
               description = "Cambia el vehículo de un pedido en estado PENDIENTE. El nuevo vehículo debe estar asignado al mismo conductor.")
    public ResponseEntity<PedidoResponse> cambiarVehiculo(
            @PathVariable Long pedidoId,
            @RequestParam Long vehiculoId) {

        Pedido pedido = cambiarVehiculoUseCase.execute(pedidoId, vehiculoId);
        return ResponseEntity.ok(mapper.toResponse(pedido));
    }
}
