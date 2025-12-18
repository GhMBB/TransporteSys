package com.example.transportesys.application.usecase.usuario;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Usuario;
import com.example.transportesys.domain.repository.UsuarioRepository;

/**
 * Caso de uso para obtener un usuario por ID.
 */
public class ObtenerUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public ObtenerUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario execute(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));
    }

    public Usuario executeByUsername(String username) {
        return usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    }
}
