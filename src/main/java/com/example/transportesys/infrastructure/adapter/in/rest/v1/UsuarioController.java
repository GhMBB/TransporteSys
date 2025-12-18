package com.example.transportesys.infrastructure.adapter.in.rest.v1;

import com.example.transportesys.application.usecase.usuario.*;
import com.example.transportesys.domain.model.Usuario;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.request.ChangePasswordRequest;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.request.RegisterUserRequest;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.request.UpdateUsuarioRequest;
import com.example.transportesys.infrastructure.adapter.in.rest.dto.response.UsuarioResponse;
import com.example.transportesys.infrastructure.adapter.in.rest.mapper.UsuarioRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller para gestión de usuarios.
 * API versionada: /api/v1/usuarios
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
public class UsuarioController {

    private final RegisterUserUseCase registerUserUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final UpdateUsuarioUseCase updateUsuarioUseCase;
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final ObtenerUsuarioUseCase obtenerUsuarioUseCase;
    private final EliminarUsuarioUseCase eliminarUsuarioUseCase;
    private final UsuarioRestMapper mapper;

    public UsuarioController(
            RegisterUserUseCase registerUserUseCase,
            ChangePasswordUseCase changePasswordUseCase,
            UpdateUsuarioUseCase updateUsuarioUseCase,
            ListarUsuariosUseCase listarUsuariosUseCase,
            ObtenerUsuarioUseCase obtenerUsuarioUseCase,
            EliminarUsuarioUseCase eliminarUsuarioUseCase,
            UsuarioRestMapper mapper) {
        this.registerUserUseCase = registerUserUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.updateUsuarioUseCase = updateUsuarioUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.obtenerUsuarioUseCase = obtenerUsuarioUseCase;
        this.eliminarUsuarioUseCase = eliminarUsuarioUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario",
               description = "Solo ADMIN puede crear usuarios. Si no se especifican roles, se asigna CLIENTE por defecto")
    public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        Usuario usuario = registerUserUseCase.execute(
            request.getUsername(),
            request.getPassword(),
            request.getEmail(),
            request.getRoles()
        );
        return new ResponseEntity<>(mapper.toResponse(usuario), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<Usuario> usuarios = listarUsuariosUseCase.execute();
        List<UsuarioResponse> response = usuarios.stream()
            .map(mapper::toResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID")
    public ResponseEntity<UsuarioResponse> obtener(@PathVariable Long id) {
        Usuario usuario = obtenerUsuarioUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(usuario));
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener información del usuario actual")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioActual(Authentication authentication) {
        String username = authentication.getName();
        Usuario usuario = obtenerUsuarioUseCase.executeByUsername(username);
        return ResponseEntity.ok(mapper.toResponse(usuario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUsuarioRequest request) {
        Usuario usuario = updateUsuarioUseCase.execute(
            id,
            request.getEmail(),
            request.getRoles()
        );
        return ResponseEntity.ok(mapper.toResponse(usuario));
    }

    @PostMapping("/{id}/change-password")
    @Operation(summary = "Cambiar contraseña del usuario",
               description = "El usuario debe proporcionar su contraseña actual")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        changePasswordUseCase.execute(id, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/change-password")
    @Operation(summary = "Cambiar contraseña del usuario actual")
    public ResponseEntity<Void> changeMyPassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        String username = authentication.getName();
        Usuario usuario = obtenerUsuarioUseCase.executeByUsername(username);
        changePasswordUseCase.execute(usuario.getId(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reset-password")
    @Operation(summary = "Resetear contraseña de un usuario (solo ADMIN)",
               description = "Permite cambiar la contraseña sin validar la anterior")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        changePasswordUseCase.executeByAdmin(id, newPassword);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar (desactivar) un usuario")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarUsuarioUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
