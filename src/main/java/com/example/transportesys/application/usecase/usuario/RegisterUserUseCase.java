package com.example.transportesys.application.usecase.usuario;

import com.example.transportesys.domain.enums.RolUsuario;
import com.example.transportesys.domain.exception.DomainException;
import com.example.transportesys.domain.model.Usuario;
import com.example.transportesys.domain.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

/**
 * Caso de uso para registrar un nuevo usuario.
 */
public class RegisterUserUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario execute(String username, String password, String email, Set<RolUsuario> roles) {
        // Validar que el username no exista
        if (usuarioRepository.existsByUsername(username)) {
            throw new DomainException("El username ya está en uso");
        }

        // Validar que el email no exista
        if (usuarioRepository.existsByEmail(email)) {
            throw new DomainException("El email ya está en uso");
        }

        // Crear usuario
        Usuario usuario = new Usuario(username, passwordEncoder.encode(password), email);

        // Agregar roles
        if (roles != null && !roles.isEmpty()) {
            usuario.setRoles(roles);
        } else {
            // Por defecto, asignar rol CLIENTE
            usuario.agregarRol(RolUsuario.CLIENTE);
        }

        return usuarioRepository.save(usuario);
    }
}
