package com.example.transportesys.infrastructure.adapter.out.persistence.repository;

import com.example.transportesys.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository para UsuarioEntity.
 * IMPORTANTE: Todos los métodos filtran por activo=true para implementar eliminación lógica.
 */
@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

    /**
     * Busca un usuario por ID solo si está activo.
     * Override de findById para implementar eliminación lógica.
     */
    @Query("SELECT u FROM UsuarioEntity u WHERE u.id = :id AND u.activo = true")
    Optional<UsuarioEntity> findByIdAndActivoTrue(@Param("id") Long id);

    /**
     * Busca un usuario por username solo si está activo.
     */
    @Query("SELECT u FROM UsuarioEntity u WHERE u.username = :username AND u.activo = true")
    Optional<UsuarioEntity> findByUsernameAndActivoTrue(@Param("username") String username);

    /**
     * Busca un usuario por email solo si está activo.
     */
    @Query("SELECT u FROM UsuarioEntity u WHERE u.email = :email AND u.activo = true")
    Optional<UsuarioEntity> findByEmailAndActivoTrue(@Param("email") String email);

    /**
     * Lista todos los usuarios activos.
     * Override de findAll para implementar eliminación lógica.
     */
    @Query("SELECT u FROM UsuarioEntity u WHERE u.activo = true")
    List<UsuarioEntity> findAllActive();

    /**
     * Verifica si existe un usuario activo con el username dado.
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UsuarioEntity u WHERE u.username = :username AND u.activo = true")
    boolean existsByUsernameAndActivoTrue(@Param("username") String username);

    /**
     * Verifica si existe un usuario activo con el email dado.
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UsuarioEntity u WHERE u.email = :email AND u.activo = true")
    boolean existsByEmailAndActivoTrue(@Param("email") String email);

    // ===== Métodos que NO filtran por activo (para uso interno) =====

    Optional<UsuarioEntity> findByUsername(String username);

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
