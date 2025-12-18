package com.example.transportesys.infrastructure.adapter.out.persistence.repository;

import com.example.transportesys.infrastructure.adapter.out.persistence.entity.VehiculoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository para VehiculoEntity.
 */
@Repository
public interface VehiculoJpaRepository extends JpaRepository<VehiculoEntity, Long> {

    Optional<VehiculoEntity> findByPlaca(String placa);

    List<VehiculoEntity> findByActivo(boolean activo);

    @Query("SELECT v FROM VehiculoEntity v WHERE v.conductorId IS NULL AND v.activo = true")
    List<VehiculoEntity> findVehiculosLibres();

    List<VehiculoEntity> findByConductorId(Long conductorId);

    boolean existsByPlaca(String placa);

    Page<VehiculoEntity> findAll(Pageable pageable);
}
