package com.example.transportesys.infrastructure.adapter.in.rest.v1;

import com.example.transportesys.application.usecase.conductor.ListarConductoresSinVehiculosUseCase;
import com.example.transportesys.application.usecase.conductor.ObtenerConteoVehiculosPorConductorUseCase;
import com.example.transportesys.application.usecase.vehiculo.ObtenerVehiculosLibresUseCase;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.ConductorResponse;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.VehiculoResponse;
import com.example.transportesys.infrastructure.adapter.in.rest.mapper.ConductorRestMapper;
import com.example.transportesys.infrastructure.adapter.in.rest.mapper.VehiculoRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller para reportes y métricas del sistema.
 * API versionada: /api/v1/reportes
 */
@RestController
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes", description = "API para reportes y métricas del sistema")
public class ReporteController {

    private final ObtenerVehiculosLibresUseCase vehiculosLibresUseCase;
    private final ListarConductoresSinVehiculosUseCase conductoresSinVehiculosUseCase;
    private final ObtenerConteoVehiculosPorConductorUseCase conteoVehiculosUseCase;
    private final VehiculoRestMapper vehiculoMapper;
    private final ConductorRestMapper conductorMapper;

    public ReporteController(
            ObtenerVehiculosLibresUseCase vehiculosLibresUseCase,
            ListarConductoresSinVehiculosUseCase conductoresSinVehiculosUseCase,
            ObtenerConteoVehiculosPorConductorUseCase conteoVehiculosUseCase,
            VehiculoRestMapper vehiculoMapper,
            ConductorRestMapper conductorMapper) {
        this.vehiculosLibresUseCase = vehiculosLibresUseCase;
        this.conductoresSinVehiculosUseCase = conductoresSinVehiculosUseCase;
        this.conteoVehiculosUseCase = conteoVehiculosUseCase;
        this.vehiculoMapper = vehiculoMapper;
        this.conductorMapper = conductorMapper;
    }

    @GetMapping("/vehiculos-libres")
    @Operation(summary = "Obtener vehículos disponibles (sin conductor asignado)",
               description = "Lista todos los vehículos activos que no tienen conductor asignado")
    @Cacheable(value = "reporteVehiculosLibres")
    public ResponseEntity<List<VehiculoResponse>> obtenerVehiculosLibres() {
        List<Vehiculo> vehiculos = vehiculosLibresUseCase.execute();
        List<VehiculoResponse> response = vehiculos.stream()
            .map(vehiculoMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conductores-sin-vehiculos")
    @Operation(summary = "Obtener conductores sin vehículos asignados",
               description = "Lista todos los conductores activos que no tienen vehículos asignados")
    @Cacheable(value = "reporteConductoresSinVehiculos")
    public ResponseEntity<List<ConductorResponse>> obtenerConductoresSinVehiculos() {
        List<Conductor> conductores = conductoresSinVehiculosUseCase.execute();
        List<ConductorResponse> response = conductores.stream()
            .map(conductorMapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vehiculos-por-conductor")
    @Operation(summary = "Obtener número de vehículos por conductor",
               description = "Devuelve un mapa con el ID del conductor y la cantidad de vehículos asignados")
    @Cacheable(value = "reporteVehiculosPorConductor")
    public ResponseEntity<Map<Long, Integer>> obtenerVehiculosPorConductor() {
        Map<Long, Integer> conteo = conteoVehiculosUseCase.execute();
        return ResponseEntity.ok(conteo);
    }
}
