package com.example.transportesys.domain.repository;

import com.example.transportesys.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Port (interfaz) del repositorio de Usuario en el dominio.
 * No depende de frameworks (sin Spring Data).
 */
public interface UsuarioRepository {

    Usuario save(Usuario usuario);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAll();

    void deleteById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
