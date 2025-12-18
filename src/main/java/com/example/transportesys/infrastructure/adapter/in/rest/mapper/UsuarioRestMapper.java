package com.example.transportesys.infrastructure.adapter.in.rest.mapper;

import com.example.transportesys.domain.model.Usuario;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.UsuarioResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre Usuario (dominio) y UsuarioResponse (REST DTO).
 */
@Component
public class UsuarioRestMapper {

    public UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return new UsuarioResponse(
            usuario.getId(),
            usuario.getUsername(),
            usuario.getEmail(),
            usuario.isActivo(),
            usuario.getRoles(),
            usuario.getFechaCreacion(),
            usuario.getUltimoAcceso()
        );
    }
}
