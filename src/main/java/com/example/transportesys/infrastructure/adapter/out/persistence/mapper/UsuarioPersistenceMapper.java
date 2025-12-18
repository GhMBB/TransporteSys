package com.example.transportesys.infrastructure.adapter.out.persistence.mapper;

import com.example.transportesys.domain.model.Usuario;
import com.example.transportesys.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * Mapper que convierte entre Usuario (dominio) y UsuarioEntity (JPA).
 */
@Component
public class UsuarioPersistenceMapper {

    public UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setUsername(usuario.getUsername());
        entity.setPassword(usuario.getPassword());
        entity.setEmail(usuario.getEmail());
        entity.setActivo(usuario.isActivo());
        entity.setRoles(new HashSet<>(usuario.getRoles()));
        entity.setFechaCreacion(usuario.getFechaCreacion());
        entity.setUltimoAcceso(usuario.getUltimoAcceso());

        return entity;
    }

    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        Usuario usuario = new Usuario(entity.getUsername(), entity.getPassword(), entity.getEmail());
        usuario.setId(entity.getId());

        if (!entity.isActivo()) {
            usuario.desactivar();
        }

        usuario.setRoles(new HashSet<>(entity.getRoles()));
        usuario.setFechaCreacion(entity.getFechaCreacion());
        usuario.setUltimoAcceso(entity.getUltimoAcceso());

        return usuario;
    }
}
