package com.example.transportesys.infrastructure.config;

import com.example.transportesys.application.usecase.conductor.*;
import com.example.transportesys.application.usecase.pedido.*;
import com.example.transportesys.application.usecase.usuario.*;
import com.example.transportesys.application.usecase.vehiculo.*;
import com.example.transportesys.domain.repository.ConductorRepository;
import com.example.transportesys.domain.repository.PedidoRepository;
import com.example.transportesys.domain.repository.UsuarioRepository;
import com.example.transportesys.domain.repository.VehiculoRepository;
import com.example.transportesys.domain.specification.ConductorEstaActivoSpec;
import com.example.transportesys.domain.specification.VehiculoEstaActivoSpec;
import com.example.transportesys.domain.specification.VehiculoTieneCapacidadSuficienteSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración de beans para los Use Cases.
 * Instancia los Use Cases (POJOs del dominio) con sus dependencias.
 */
@Configuration
public class BeanConfiguration {

    // ============ USE CASES DE VEHÍCULO ============

    @Bean
    public CrearVehiculoUseCase crearVehiculoUseCase(VehiculoRepository vehiculoRepository) {
        return new CrearVehiculoUseCase(vehiculoRepository);
    }

    @Bean
    public ActualizarVehiculoUseCase actualizarVehiculoUseCase(VehiculoRepository vehiculoRepository) {
        return new ActualizarVehiculoUseCase(vehiculoRepository);
    }

    @Bean
    public EliminarVehiculoUseCase eliminarVehiculoUseCase(VehiculoRepository vehiculoRepository) {
        return new EliminarVehiculoUseCase(vehiculoRepository);
    }

    @Bean
    public ObtenerVehiculoUseCase obtenerVehiculoUseCase(VehiculoRepository vehiculoRepository) {
        return new ObtenerVehiculoUseCase(vehiculoRepository);
    }

    @Bean
    public ListarVehiculosUseCase listarVehiculosUseCase(VehiculoRepository vehiculoRepository) {
        return new ListarVehiculosUseCase(vehiculoRepository);
    }

    @Bean
    public ObtenerVehiculosLibresUseCase obtenerVehiculosLibresUseCase(VehiculoRepository vehiculoRepository) {
        return new ObtenerVehiculosLibresUseCase(vehiculoRepository);
    }

    @Bean
    public AsignarConductorAVehiculoUseCase asignarConductorAVehiculoUseCase(
            VehiculoRepository vehiculoRepository,
            ConductorRepository conductorRepository) {
        return new AsignarConductorAVehiculoUseCase(vehiculoRepository, conductorRepository);
    }

    // ============ USE CASES DE CONDUCTOR ============

    @Bean
    public CrearConductorUseCase crearConductorUseCase(ConductorRepository conductorRepository) {
        return new CrearConductorUseCase(conductorRepository);
    }

    @Bean
    public ActualizarConductorUseCase actualizarConductorUseCase(ConductorRepository conductorRepository) {
        return new ActualizarConductorUseCase(conductorRepository);
    }

    @Bean
    public EliminarConductorUseCase eliminarConductorUseCase(ConductorRepository conductorRepository) {
        return new EliminarConductorUseCase(conductorRepository);
    }

    @Bean
    public ObtenerConductorUseCase obtenerConductorUseCase(ConductorRepository conductorRepository) {
        return new ObtenerConductorUseCase(conductorRepository);
    }

    @Bean
    public ListarConductoresUseCase listarConductoresUseCase(ConductorRepository conductorRepository) {
        return new ListarConductoresUseCase(conductorRepository);
    }

    @Bean
    public ListarConductoresSinVehiculosUseCase listarConductoresSinVehiculosUseCase(
            ConductorRepository conductorRepository) {
        return new ListarConductoresSinVehiculosUseCase(conductorRepository);
    }

    @Bean
    public ObtenerConteoVehiculosPorConductorUseCase obtenerConteoVehiculosPorConductorUseCase(
            ConductorRepository conductorRepository) {
        return new ObtenerConteoVehiculosPorConductorUseCase(conductorRepository);
    }

    // ============ USE CASES DE PEDIDO ============

    @Bean
    public CrearPedidoUseCase crearPedidoUseCase(
            PedidoRepository pedidoRepository,
            VehiculoRepository vehiculoRepository,
            ConductorRepository conductorRepository,
            VehiculoTieneCapacidadSuficienteSpec capacidadSpec,
            VehiculoEstaActivoSpec vehiculoActivoSpec,
            ConductorEstaActivoSpec conductorActivoSpec) {
        return new CrearPedidoUseCase(pedidoRepository, vehiculoRepository, conductorRepository,
                                      capacidadSpec, vehiculoActivoSpec, conductorActivoSpec);
    }

    @Bean
    public CambiarEstadoPedidoUseCase cambiarEstadoPedidoUseCase(PedidoRepository pedidoRepository) {
        return new CambiarEstadoPedidoUseCase(pedidoRepository);
    }

    @Bean
    public ActualizarEstadoPedidoUseCase actualizarEstadoPedidoUseCase(PedidoRepository pedidoRepository) {
        return new ActualizarEstadoPedidoUseCase(pedidoRepository);
    }

    @Bean
    public IniciarPedidoUseCase iniciarPedidoUseCase(PedidoRepository pedidoRepository) {
        return new IniciarPedidoUseCase(pedidoRepository);
    }

    @Bean
    public CompletarPedidoUseCase completarPedidoUseCase(PedidoRepository pedidoRepository) {
        return new CompletarPedidoUseCase(pedidoRepository);
    }

    @Bean
    public CancelarPedidoUseCase cancelarPedidoUseCase(PedidoRepository pedidoRepository) {
        return new CancelarPedidoUseCase(pedidoRepository);
    }

    @Bean
    public ListarPedidosUseCase listarPedidosUseCase(PedidoRepository pedidoRepository) {
        return new ListarPedidosUseCase(pedidoRepository);
    }

    @Bean
    public ObtenerPedidoUseCase obtenerPedidoUseCase(PedidoRepository pedidoRepository) {
        return new ObtenerPedidoUseCase(pedidoRepository);
    }

    // ============ USE CASES DE USUARIO ============

    @Bean
    public RegisterUserUseCase registerUserUseCase(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        return new RegisterUserUseCase(usuarioRepository, passwordEncoder);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        return new ChangePasswordUseCase(usuarioRepository, passwordEncoder);
    }

    @Bean
    public UpdateUsuarioUseCase updateUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new UpdateUsuarioUseCase(usuarioRepository);
    }

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioRepository usuarioRepository) {
        return new ListarUsuariosUseCase(usuarioRepository);
    }

    @Bean
    public ObtenerUsuarioUseCase obtenerUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new ObtenerUsuarioUseCase(usuarioRepository);
    }

    @Bean
    public EliminarUsuarioUseCase eliminarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        return new EliminarUsuarioUseCase(usuarioRepository);
    }
}
