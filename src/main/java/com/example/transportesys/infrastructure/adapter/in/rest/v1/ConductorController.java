package com.example.transportesys.infrastructure.adapter.in.rest.v1;

import com.example.transportesys.application.usecase.conductor.*;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.request.ConductorRequest;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.ConductorResponse;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.PagedResponse;
import com.example.transportesys.infrastructure.adapter.in.rest.mapper.ConductorRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller para gestionar conductores.
 * API versionada: /api/v1/conductores
 */
@RestController
@RequestMapping("/api/v1/conductores")
@Tag(name = "Conductores", description = "API para gestión de conductores")
public class ConductorController {

    private final CrearConductorUseCase crearConductorUseCase;
    private final ActualizarConductorUseCase actualizarConductorUseCase;
    private final EliminarConductorUseCase eliminarConductorUseCase;
    private final ObtenerConductorUseCase obtenerConductorUseCase;
    private final ListarConductoresUseCase listarConductoresUseCase;
    private final ListarConductoresSinVehiculosUseCase listarSinVehiculosUseCase;
    private final ObtenerConteoVehiculosPorConductorUseCase conteoVehiculosUseCase;
    private final ConductorRestMapper mapper;

    public ConductorController(
            CrearConductorUseCase crearConductorUseCase,
            ActualizarConductorUseCase actualizarConductorUseCase,
            EliminarConductorUseCase eliminarConductorUseCase,
            ObtenerConductorUseCase obtenerConductorUseCase,
            ListarConductoresUseCase listarConductoresUseCase,
            ListarConductoresSinVehiculosUseCase listarSinVehiculosUseCase,
            ObtenerConteoVehiculosPorConductorUseCase conteoVehiculosUseCase,
            ConductorRestMapper mapper) {
        this.crearConductorUseCase = crearConductorUseCase;
        this.actualizarConductorUseCase = actualizarConductorUseCase;
        this.eliminarConductorUseCase = eliminarConductorUseCase;
        this.obtenerConductorUseCase = obtenerConductorUseCase;
        this.listarConductoresUseCase = listarConductoresUseCase;
        this.listarSinVehiculosUseCase = listarSinVehiculosUseCase;
        this.conteoVehiculosUseCase = conteoVehiculosUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo conductor")
    @CacheEvict(value = "conductores", allEntries = true)
    public ResponseEntity<ConductorResponse> crear(@Valid @RequestBody ConductorRequest request) {
        Conductor conductor = crearConductorUseCase.execute(request.getNombre(), request.getLicencia());
        return new ResponseEntity<>(mapper.toResponse(conductor), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un conductor por ID")
    public ResponseEntity<ConductorResponse> obtener(@PathVariable Long id) {
        Conductor conductor = obtenerConductorUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(conductor));
    }

    @GetMapping
    @Operation(summary = "Listar todos los conductores con paginación opcional")
    @Cacheable(value = "conductores")
    public ResponseEntity<?> listar(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page != null && size != null) {
            PageResult<Conductor> pageResult = listarConductoresUseCase.executePaged(page, size);

            List<ConductorResponse> content = pageResult.getContent().stream()
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

            PagedResponse<ConductorResponse> response = new PagedResponse<>(content, metadata);
            return ResponseEntity.ok(response);
        } else {
            List<Conductor> conductores = listarConductoresUseCase.execute();
            List<ConductorResponse> response = conductores.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar conductores activos")
    @Cacheable(value = "conductoresActivos")
    public ResponseEntity<List<ConductorResponse>> listarActivos() {
        List<Conductor> conductores = listarConductoresUseCase.executeActivos();
        List<ConductorResponse> response = conductores.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sin-vehiculos")
    @Operation(summary = "Listar conductores sin vehículos asignados")
    @Cacheable(value = "conductoresSinVehiculos")
    public ResponseEntity<List<ConductorResponse>> listarSinVehiculos() {
        List<Conductor> conductores = listarSinVehiculosUseCase.execute();
        List<ConductorResponse> response = conductores.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conteo-vehiculos")
    @Operation(summary = "Obtener conteo de vehículos por conductor")
    @Cacheable(value = "conteoVehiculos")
    public ResponseEntity<Map<Long, Integer>> obtenerConteoVehiculos() {
        Map<Long, Integer> conteo = conteoVehiculosUseCase.execute();
        return ResponseEntity.ok(conteo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un conductor")
    @CacheEvict(value = {"conductores", "conductoresActivos"}, allEntries = true)
    public ResponseEntity<ConductorResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ConductorRequest request) {

        Conductor conductor = actualizarConductorUseCase.execute(
            id,
            request.getNombre(),
            request.getLicencia()
        );

        return ResponseEntity.ok(mapper.toResponse(conductor));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar (lógicamente) un conductor")
    @CacheEvict(value = {"conductores", "conductoresActivos", "conductoresSinVehiculos"}, allEntries = true)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarConductorUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
