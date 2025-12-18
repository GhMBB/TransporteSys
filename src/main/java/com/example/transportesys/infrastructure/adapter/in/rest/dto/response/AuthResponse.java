package com.example.transportesys.infrastructure.adapter.in.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO de Response para autenticación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private Date expiresAt;

    public AuthResponse(String token, String username, Date expiresAt) {
        this.token = token;
        this.username = username;
        this.expiresAt = expiresAt;
    }
}
