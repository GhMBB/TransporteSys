package com.example.transportesys.application.usecase.usuario;

import com.example.transportesys.domain.exception.ResourceNotFoundException;
import com.example.transportesys.domain.model.Usuario;
import com.example.transportesys.domain.repository.UsuarioRepository;

/**
 * Caso de uso para eliminar (desactivar) un usuario.
 */
public class EliminarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public EliminarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Desactiva el usuario (eliminación lógica).
     */
    public void execute(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        usuario.desactivar();
        usuarioRepository.save(usuario);
    }
}
