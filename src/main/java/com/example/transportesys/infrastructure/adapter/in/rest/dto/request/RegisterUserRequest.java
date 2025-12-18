package com.example.transportesys.infrastructure.adapter.in.rest.dto.request;

import com.example.transportesys.domain.enums.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO para solicitud de registro de usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud de registro de nuevo usuario")
public class RegisterUserRequest {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    @Schema(description = "Nombre de usuario único", example = "juanperez")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email debe ser válido")
    @Schema(description = "Correo electrónico del usuario", example = "juan@example.com")
    private String email;

    @Schema(description = "Roles del usuario", example = "[\"CLIENTE\"]")
    private Set<RolUsuario> roles;
}
