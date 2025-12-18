package com.example.transportesys.infrastructure.adapter.out.persistence.adapter;

import com.example.transportesys.domain.model.Usuario;
import com.example.transportesys.domain.repository.UsuarioRepository;
import com.example.transportesys.infrastructure.adapter.out.persistence.mapper.UsuarioPersistenceMapper;
import com.example.transportesys.infrastructure.adapter.out.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter que implementa UsuarioRepository (dominio) usando JPA.
 */
@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;
    private final UsuarioPersistenceMapper mapper;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository,
                                   UsuarioPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Usuario save(Usuario usuario) {
        var entity = mapper.toEntity(usuario);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        // Solo retorna usuarios activos (eliminación lógica)
        return jpaRepository.findByIdAndActivoTrue(id)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        // Solo retorna usuarios activos (eliminación lógica)
        return jpaRepository.findByUsernameAndActivoTrue(username)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        // Solo retorna usuarios activos (eliminación lógica)
        return jpaRepository.findByEmailAndActivoTrue(email)
            .map(mapper::toDomain);
    }

    @Override
    public List<Usuario> findAll() {
        // Solo retorna usuarios activos (eliminación lógica)
        return jpaRepository.findAllActive().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        // Solo verifica existencia de usuarios activos (eliminación lógica)
        return jpaRepository.existsByUsernameAndActivoTrue(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        // Solo verifica existencia de usuarios activos (eliminación lógica)
        return jpaRepository.existsByEmailAndActivoTrue(email);
    }
}
