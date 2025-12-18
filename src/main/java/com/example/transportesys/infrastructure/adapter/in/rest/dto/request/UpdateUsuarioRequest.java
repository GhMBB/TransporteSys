package com.example.transportesys.infrastructure.adapter.in.rest.dto.request;

import com.example.transportesys.domain.enums.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO para solicitud de actualización de usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud de actualización de usuario")
public class UpdateUsuarioRequest {

    @Email(message = "Email debe ser válido")
    @Schema(description = "Nuevo correo electrónico del usuario", example = "nuevo@example.com")
    private String email;

    @Schema(description = "Nuevos roles del usuario", example = "[\"ADMIN\"]")
    private Set<RolUsuario> roles;
}
