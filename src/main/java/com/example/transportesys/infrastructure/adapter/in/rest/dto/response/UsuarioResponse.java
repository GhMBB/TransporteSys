package com.example.transportesys.infrastructure.adapter.in.rest.dto.response;

import com.example.transportesys.domain.enums.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO de respuesta para Usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de usuario")
public class UsuarioResponse {

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;

    @Schema(description = "Correo electrónico", example = "admin@example.com")
    private String email;

    @Schema(description = "Indica si el usuario está activo", example = "true")
    private boolean activo;

    @Schema(description = "Roles del usuario", example = "[\"ADMIN\"]")
    private Set<RolUsuario> roles;

    @Schema(description = "Fecha de creación del usuario")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha del último acceso del usuario")
    private LocalDateTime ultimoAcceso;
}
