package com.example.transportesys.infrastructure.config;

import com.example.transportesys.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad con Spring Security + JWT.
 * Define reglas de autorización por roles y protege endpoints.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                         UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/actuator/**").permitAll()

                // Endpoints de vehículos
                .requestMatchers(HttpMethod.GET, "/api/v1/vehiculos/**").hasAnyRole("ADMIN", "CONDUCTOR", "CLIENTE")
                .requestMatchers(HttpMethod.POST, "/api/v1/vehiculos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/vehiculos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/vehiculos/**").hasRole("ADMIN")

                // Endpoints de conductores
                .requestMatchers(HttpMethod.GET, "/api/v1/conductores/**").hasAnyRole("ADMIN", "CONDUCTOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/conductores/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/conductores/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/conductores/**").hasRole("ADMIN")

                // Endpoints de pedidos
                .requestMatchers(HttpMethod.GET, "/api/v1/pedidos/**").hasAnyRole("ADMIN", "CONDUCTOR", "CLIENTE")
                .requestMatchers(HttpMethod.POST, "/api/v1/pedidos").hasAnyRole("ADMIN", "CLIENTE")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/pedidos/**").hasAnyRole("ADMIN", "CONDUCTOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/pedidos/*/iniciar").hasAnyRole("ADMIN", "CONDUCTOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/pedidos/*/completar").hasAnyRole("ADMIN", "CONDUCTOR")
                .requestMatchers(HttpMethod.POST, "/api/v1/pedidos/*/cancelar").hasAnyRole("ADMIN", "CLIENTE")

                // Endpoints de usuarios
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/register").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/me").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/me/change-password").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/*/change-password").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/*/reset-password").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/usuarios/*").hasRole("ADMIN")

                // Endpoints de reportes
                .requestMatchers("/api/v1/reportes/**").hasAnyRole("ADMIN", "CONDUCTOR")

                // Cualquier otro endpoint requiere autenticación
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
