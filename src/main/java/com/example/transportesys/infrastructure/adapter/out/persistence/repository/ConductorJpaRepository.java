package com.example.transportesys.infrastructure.adapter.out.persistence.repository;

import com.example.transportesys.infrastructure.adapter.out.persistence.entity.ConductorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository para ConductorEntity.
 * IMPORTANTE: Todos los métodos filtran por activo=true para implementar eliminación lógica.
 */
@Repository
public interface ConductorJpaRepository extends JpaRepository<ConductorEntity, Long> {

    /**
     * Busca un conductor por ID solo si está activo.
     * Override de findById para implementar eliminación lógica.
     */
    @Query("SELECT c FROM ConductorEntity c WHERE c.id = :id AND c.activo = true")
    Optional<ConductorEntity> findByIdAndActivoTrue(@Param("id") Long id);

    /**
     * Busca un conductor por licencia solo si está activo.
     */
    @Query("SELECT c FROM ConductorEntity c WHERE c.licencia = :licencia AND c.activo = true")
    Optional<ConductorEntity> findByLicenciaAndActivoTrue(@Param("licencia") String licencia);

    /**
     * Lista todos los conductores activos.
     * Override de findAll para implementar eliminación lógica.
     */
    @Query("SELECT c FROM ConductorEntity c WHERE c.activo = true")
    List<ConductorEntity> findAllActive();

    /**
     * Lista todos los conductores activos con paginación.
     * Override de findAll(Pageable) para implementar eliminación lógica.
     */
    @Query("SELECT c FROM ConductorEntity c WHERE c.activo = true")
    Page<ConductorEntity> findAllActive(Pageable pageable);

    /**
     * Busca conductores activos sin vehículos asignados.
     */
    @Query("SELECT c FROM ConductorEntity c WHERE SIZE(c.vehiculosIds) = 0 AND c.activo = true")
    List<ConductorEntity> findConductoresSinVehiculos();

    /**
     * Verifica si existe un conductor activo con la licencia dada.
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ConductorEntity c WHERE c.licencia = :licencia AND c.activo = true")
    boolean existsByLicenciaAndActivoTrue(@Param("licencia") String licencia);

    // ===== Métodos que NO filtran por activo (para uso interno) =====

    List<ConductorEntity> findByActivo(boolean activo);

    Optional<ConductorEntity> findByLicencia(String licencia);

    boolean existsByLicencia(String licencia);
}
