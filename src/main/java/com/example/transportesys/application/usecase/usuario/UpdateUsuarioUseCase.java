package com.example.transportesys.application.usecase.usuario;

import com.example.transportesys.domain.enums.RolUsuario;
import com.example.transportesys.domain.exception.DomainException;
import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Usuario;
import com.example.transportesys.domain.repository.UsuarioRepository;

import java.util.Set;

/**
 * Caso de uso para actualizar información de un usuario.
 */
public class UpdateUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public UpdateUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario execute(Long usuarioId, String email, Set<RolUsuario> roles) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        // Actualizar email si es diferente
        if (email != null && !email.equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(email)) {
                throw new DomainException("El email ya está en uso");
            }
            usuario.setEmail(email);
        }

        // Actualizar roles si se proporcionan
        if (roles != null && !roles.isEmpty()) {
            usuario.setRoles(roles);
        }

        return usuarioRepository.save(usuario);
    }
}
