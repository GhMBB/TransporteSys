package com.example.transportesys.infrastructure.adapter.out.persistence.repository;

import com.example.transportesys.infrastructure.adapter.out.persistence.entity.VehiculoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository para VehiculoEntity.
 * IMPORTANTE: Todos los métodos filtran por activo=true para implementar eliminación lógica.
 */
@Repository
public interface VehiculoJpaRepository extends JpaRepository<VehiculoEntity, Long> {

    /**
     * Busca un vehículo por ID solo si está activo.
     * Override de findById para implementar eliminación lógica.
     */
    @Query("SELECT v FROM VehiculoEntity v WHERE v.id = :id AND v.activo = true")
    Optional<VehiculoEntity> findByIdAndActivoTrue(@Param("id") Long id);

    /**
     * Busca un vehículo por placa solo si está activo.
     */
    @Query("SELECT v FROM VehiculoEntity v WHERE v.placa = :placa AND v.activo = true")
    Optional<VehiculoEntity> findByPlacaAndActivoTrue(@Param("placa") String placa);

    /**
     * Lista todos los vehículos activos.
     * Override de findAll para implementar eliminación lógica.
     */
    @Query("SELECT v FROM VehiculoEntity v WHERE v.activo = true")
    List<VehiculoEntity> findAllActive();

    /**
     * Lista todos los vehículos activos con paginación.
     * Override de findAll(Pageable) para implementar eliminación lógica.
     */
    @Query("SELECT v FROM VehiculoEntity v WHERE v.activo = true")
    Page<VehiculoEntity> findAllActive(Pageable pageable);

    /**
     * Busca vehículos activos por conductor.
     */
    @Query("SELECT v FROM VehiculoEntity v WHERE v.conductorId = :conductorId AND v.activo = true")
    List<VehiculoEntity> findByConductorIdAndActivoTrue(@Param("conductorId") Long conductorId);

    /**
     * Busca vehículos libres (sin conductor asignado) y activos.
     */
    @Query("SELECT v FROM VehiculoEntity v WHERE v.conductorId IS NULL AND v.activo = true")
    List<VehiculoEntity> findVehiculosLibres();

    /**
     * Verifica si existe un vehículo activo con la placa dada.
     */
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM VehiculoEntity v WHERE v.placa = :placa AND v.activo = true")
    boolean existsByPlacaAndActivoTrue(@Param("placa") String placa);

    // ===== Métodos que NO filtran por activo (para uso interno) =====

    List<VehiculoEntity> findByActivo(boolean activo);

    Optional<VehiculoEntity> findByPlaca(String placa);

    List<VehiculoEntity> findByConductorId(Long conductorId);

    boolean existsByPlaca(String placa);
}
