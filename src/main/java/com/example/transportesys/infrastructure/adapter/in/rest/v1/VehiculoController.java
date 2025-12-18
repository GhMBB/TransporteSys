package com.example.transportesys.infrastructure.adapter.in.rest.v1;

import com.example.transportesys.application.usecase.vehiculo.*;
import com.example.transportesys.domain.model.PageResult;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.request.VehiculoRequest;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.PagedResponse;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.VehiculoResponse;
import com.example.transportesys.infrastructure.adapter.in.rest.mapper.VehiculoRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller para gestionar vehículos.
 * API versionada: /api/v1/vehiculos
 */
@RestController
@RequestMapping("/api/v1/vehiculos")
@Tag(name = "Vehículos", description = "API para gestión de vehículos")
public class VehiculoController {

    private final CrearVehiculoUseCase crearVehiculoUseCase;
    private final ActualizarVehiculoUseCase actualizarVehiculoUseCase;
    private final EliminarVehiculoUseCase eliminarVehiculoUseCase;
    private final ObtenerVehiculoUseCase obtenerVehiculoUseCase;
    private final ListarVehiculosUseCase listarVehiculosUseCase;
    private final ObtenerVehiculosLibresUseCase obtenerVehiculosLibresUseCase;
    private final AsignarConductorAVehiculoUseCase asignarConductorUseCase;
    private final VehiculoRestMapper mapper;

    public VehiculoController(
            CrearVehiculoUseCase crearVehiculoUseCase,
            ActualizarVehiculoUseCase actualizarVehiculoUseCase,
            EliminarVehiculoUseCase eliminarVehiculoUseCase,
            ObtenerVehiculoUseCase obtenerVehiculoUseCase,
            ListarVehiculosUseCase listarVehiculosUseCase,
            ObtenerVehiculosLibresUseCase obtenerVehiculosLibresUseCase,
            AsignarConductorAVehiculoUseCase asignarConductorUseCase,
            VehiculoRestMapper mapper) {
        this.crearVehiculoUseCase = crearVehiculoUseCase;
        this.actualizarVehiculoUseCase = actualizarVehiculoUseCase;
        this.eliminarVehiculoUseCase = eliminarVehiculoUseCase;
        this.obtenerVehiculoUseCase = obtenerVehiculoUseCase;
        this.listarVehiculosUseCase = listarVehiculosUseCase;
        this.obtenerVehiculosLibresUseCase = obtenerVehiculosLibresUseCase;
        this.asignarConductorUseCase = asignarConductorUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo vehículo")
    @CacheEvict(value = "vehiculos", allEntries = true)
    public ResponseEntity<VehiculoResponse> crear(@Valid @RequestBody VehiculoRequest request) {
        Vehiculo vehiculo = crearVehiculoUseCase.execute(request.getPlaca(), request.getCapacidadKg());
        return new ResponseEntity<>(mapper.toResponse(vehiculo), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un vehículo por ID")
    public ResponseEntity<VehiculoResponse> obtener(@PathVariable Long id) {
        Vehiculo vehiculo = obtenerVehiculoUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(vehiculo));
    }

    @GetMapping
    @Operation(summary = "Listar todos los vehículos con paginación opcional")
    @Cacheable(value = "vehiculos")
    public ResponseEntity<?> listar(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page != null && size != null) {
            PageResult<Vehiculo> pageResult = listarVehiculosUseCase.executePaged(page, size);

            List<VehiculoResponse> content = pageResult.getContent().stream()
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

            PagedResponse<VehiculoResponse> response = new PagedResponse<>(content, metadata);
            return ResponseEntity.ok(response);
        } else {
            List<Vehiculo> vehiculos = listarVehiculosUseCase.execute();
            List<VehiculoResponse> response = vehiculos.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar vehículos activos")
    @Cacheable(value = "vehiculosActivos")
    public ResponseEntity<List<VehiculoResponse>> listarActivos() {
        List<Vehiculo> vehiculos = listarVehiculosUseCase.executeActivos();
        List<VehiculoResponse> response = vehiculos.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/libres")
    @Operation(summary = "Obtener vehículos libres (sin conductor asignado)")
    @Cacheable(value = "vehiculosLibres")
    public ResponseEntity<List<VehiculoResponse>> obtenerLibres() {
        List<Vehiculo> vehiculos = obtenerVehiculosLibresUseCase.execute();
        List<VehiculoResponse> response = vehiculos.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un vehículo")
    @CacheEvict(value = {"vehiculos", "vehiculosActivos", "vehiculosLibres"}, allEntries = true)
    public ResponseEntity<VehiculoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequest request) {

        Vehiculo vehiculo = actualizarVehiculoUseCase.execute(
            id,
            request.getPlaca(),
            request.getCapacidadKg()
        );

        return ResponseEntity.ok(mapper.toResponse(vehiculo));
    }

    @PostMapping("/{vehiculoId}/asignar-conductor/{conductorId}")
    @Operation(summary = "Asignar un conductor a un vehículo")
    @CacheEvict(value = {"vehiculos", "vehiculosLibres"}, allEntries = true)
    public ResponseEntity<VehiculoResponse> asignarConductor(
            @PathVariable Long vehiculoId,
            @PathVariable Long conductorId) {

        Vehiculo vehiculo = asignarConductorUseCase.execute(vehiculoId, conductorId);
        return ResponseEntity.ok(mapper.toResponse(vehiculo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar (lógicamente) un vehículo")
    @CacheEvict(value = {"vehiculos", "vehiculosActivos", "vehiculosLibres"}, allEntries = true)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarVehiculoUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
