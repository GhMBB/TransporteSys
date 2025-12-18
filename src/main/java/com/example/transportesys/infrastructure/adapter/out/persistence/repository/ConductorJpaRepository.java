package com.example.transportesys.infrastructure.adapter.out.persistence.repository;

import com.example.transportesys.infrastructure.adapter.out.persistence.entity.ConductorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository para ConductorEntity.
 */
@Repository
public interface ConductorJpaRepository extends JpaRepository<ConductorEntity, Long> {

    Optional<ConductorEntity> findByLicencia(String licencia);

    List<ConductorEntity> findByActivo(boolean activo);

    @Query("SELECT c FROM ConductorEntity c WHERE SIZE(c.vehiculosIds) = 0 AND c.activo = true")
    List<ConductorEntity> findConductoresSinVehiculos();

    boolean existsByLicencia(String licencia);

    Page<ConductorEntity> findAll(Pageable pageable);
}
