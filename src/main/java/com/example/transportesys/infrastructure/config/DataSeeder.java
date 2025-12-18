package com.example.transportesys.infrastructure.config;

import com.example.transportesys.application.usecase.conductor.CrearConductorUseCase;
import com.example.transportesys.application.usecase.pedido.CrearPedidoUseCase;
import com.example.transportesys.application.usecase.vehiculo.AsignarConductorAVehiculoUseCase;
import com.example.transportesys.application.usecase.vehiculo.CrearVehiculoUseCase;
import com.example.transportesys.domain.enums.EstadoPedido;
import com.example.transportesys.domain.model.Conductor;
import com.example.transportesys.domain.model.Pedido;
import com.example.transportesys.domain.model.Vehiculo;
import com.example.transportesys.domain.enums.RolUsuario;
import com.example.transportesys.domain.repository.PedidoRepository;
import com.example.transportesys.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.example.transportesys.infrastructure.adapter.out.persistence.repository.UsuarioJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Seed de datos para inicializar la base de datos con datos de prueba.
 * Solo se ejecuta en profiles 'dev' o 'local'.
 */
@Component
@Profile({"dev", "local"})
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UsuarioJpaRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CrearVehiculoUseCase crearVehiculoUseCase;
    private final CrearConductorUseCase crearConductorUseCase;
    private final AsignarConductorAVehiculoUseCase asignarConductorAVehiculoUseCase;
    private final CrearPedidoUseCase crearPedidoUseCase;
    private final PedidoRepository pedidoRepository;

    @Override
    public void run(String... args) {
        log.info("🌱 Iniciando seed de datos...");

        // Verificar si ya existen datos
        if (usuarioRepository.count() > 0) {
            log.info("✅ Base de datos ya contiene datos. Saltando seed.");
            return;
        }

        try {
            // 1. Crear usuarios
            log.info("👤 Creando usuarios...");
            crearUsuarios();

            // 2. Crear vehículos
            log.info("🚛 Creando vehículos...");
            Long[] vehiculosIds = crearVehiculos();

            // 3. Crear conductores
            log.info("🧑‍✈️ Creando conductores...");
            Long[] conductoresIds = crearConductores();

            // 4. Asignar vehículos a conductores
            log.info("🔗 Asignando vehículos a conductores...");
            asignarVehiculosAConductores(vehiculosIds, conductoresIds);

            // 5. Crear pedidos con diferentes estados
            log.info("📦 Creando pedidos de ejemplo...");
            crearPedidosDeEjemplo(vehiculosIds, conductoresIds);

            log.info("✅ Seed de datos completado exitosamente!");
            imprimirResumen();

        } catch (Exception e) {
            log.error("❌ Error durante el seed de datos: {}", e.getMessage(), e);
        }
    }

    private void crearUsuarios() {
        // Usuario ADMIN
        UsuarioEntity admin = new UsuarioEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@transportesys.com");
        admin.setActivo(true);
        admin.setFechaCreacion(LocalDateTime.now());
        Set<RolUsuario> rolesAdmin = new HashSet<>();
        rolesAdmin.add(RolUsuario.ADMIN);
        admin.setRoles(rolesAdmin);
        usuarioRepository.save(admin);
        log.info("  ✓ Usuario ADMIN creado (username: admin, password: admin123)");

        // Usuario CONDUCTOR 1
        UsuarioEntity conductor1 = new UsuarioEntity();
        conductor1.setUsername("carlos.conductor");
        conductor1.setPassword(passwordEncoder.encode("conductor123"));
        conductor1.setEmail("carlos@transportesys.com");
        conductor1.setActivo(true);
        conductor1.setFechaCreacion(LocalDateTime.now());
        Set<RolUsuario> rolesConductor = new HashSet<>();
        rolesConductor.add(RolUsuario.CONDUCTOR);
        conductor1.setRoles(rolesConductor);
        usuarioRepository.save(conductor1);
        log.info("  ✓ Usuario CONDUCTOR creado (username: carlos.conductor, password: conductor123)");

        // Usuario CONDUCTOR 2
        UsuarioEntity conductor2 = new UsuarioEntity();
        conductor2.setUsername("maria.conductor");
        conductor2.setPassword(passwordEncoder.encode("conductor123"));
        conductor2.setEmail("maria@transportesys.com");
        conductor2.setActivo(true);
        conductor2.setFechaCreacion(LocalDateTime.now());
        conductor2.setRoles(rolesConductor);
        usuarioRepository.save(conductor2);
        log.info("  ✓ Usuario CONDUCTOR creado (username: maria.conductor, password: conductor123)");

        // Usuario CLIENTE
        UsuarioEntity cliente = new UsuarioEntity();
        cliente.setUsername("cliente");
        cliente.setPassword(passwordEncoder.encode("cliente123"));
        cliente.setEmail("cliente@transportesys.com");
        cliente.setActivo(true);
        cliente.setFechaCreacion(LocalDateTime.now());
        Set<RolUsuario> rolesCliente = new HashSet<>();
        rolesCliente.add(RolUsuario.CLIENTE);
        cliente.setRoles(rolesCliente);
        usuarioRepository.save(cliente);
        log.info("  ✓ Usuario CLIENTE creado (username: cliente, password: cliente123)");
    }

    private Long[] crearVehiculos() {
        Long[] ids = new Long[6];

        // Vehículos de carga pesada
        Vehiculo v1 = crearVehiculoUseCase.execute("ABC-101", 5000.0);
        ids[0] = v1.getId();
        log.info("  ✓ Vehículo creado: {} - {} kg", v1.getPlaca().getValor(), v1.getCapacidad().getValorEnKg());

        Vehiculo v2 = crearVehiculoUseCase.execute("ABC-102", 8000.0);
        ids[1] = v2.getId();
        log.info("  ✓ Vehículo creado: {} - {} kg", v2.getPlaca().getValor(), v2.getCapacidad().getValorEnKg());

        // Vehículos de carga media
        Vehiculo v3 = crearVehiculoUseCase.execute("XYZ-201", 3000.0);
        ids[2] = v3.getId();
        log.info("  ✓ Vehículo creado: {} - {} kg", v3.getPlaca().getValor(), v3.getCapacidad().getValorEnKg());

        Vehiculo v4 = crearVehiculoUseCase.execute("XYZ-202", 3500.0);
        ids[3] = v4.getId();
        log.info("  ✓ Vehículo creado: {} - {} kg", v4.getPlaca().getValor(), v4.getCapacidad().getValorEnKg());

        // Vehículos de carga ligera
        Vehiculo v5 = crearVehiculoUseCase.execute("DEF-301", 1500.0);
        ids[4] = v5.getId();
        log.info("  ✓ Vehículo creado: {} - {} kg", v5.getPlaca().getValor(), v5.getCapacidad().getValorEnKg());

        // Vehículo libre (sin asignar)
        Vehiculo v6 = crearVehiculoUseCase.execute("GHI-401", 2000.0);
        ids[5] = v6.getId();
        log.info("  ✓ Vehículo creado: {} - {} kg (LIBRE)", v6.getPlaca().getValor(), v6.getCapacidad().getValorEnKg());

        return ids;
    }

    private Long[] crearConductores() {
        Long[] ids = new Long[3];

        // Conductor 1 - Carlos Rodríguez
        Conductor c1 = crearConductorUseCase.execute("Carlos Rodríguez", "LIC001234");
        ids[0] = c1.getId();
        log.info("  ✓ Conductor creado: {} - Licencia: {}", c1.getNombre(), c1.getLicencia().getNumero());

        // Conductor 2 - María González
        Conductor c2 = crearConductorUseCase.execute("María González", "LIC002345");
        ids[1] = c2.getId();
        log.info("  ✓ Conductor creado: {} - Licencia: {}", c2.getNombre(), c2.getLicencia().getNumero());

        // Conductor 3 - Juan Pérez (sin vehículos)
        Conductor c3 = crearConductorUseCase.execute("Juan Pérez", "LIC003456");
        ids[2] = c3.getId();
        log.info("  ✓ Conductor creado: {} - Licencia: {} (SIN VEHÍCULOS)", c3.getNombre(), c3.getLicencia().getNumero());

        return ids;
    }

    private void asignarVehiculosAConductores(Long[] vehiculosIds, Long[] conductoresIds) {
        // Carlos Rodríguez - 3 vehículos (límite máximo)
        asignarConductorAVehiculoUseCase.execute(vehiculosIds[0], conductoresIds[0]); // ABC-101
        log.info("  ✓ Vehículo ABC-101 asignado a Carlos Rodríguez");

        asignarConductorAVehiculoUseCase.execute(vehiculosIds[1], conductoresIds[0]); // ABC-102
        log.info("  ✓ Vehículo ABC-102 asignado a Carlos Rodríguez");

        asignarConductorAVehiculoUseCase.execute(vehiculosIds[2], conductoresIds[0]); // XYZ-201
        log.info("  ✓ Vehículo XYZ-201 asignado a Carlos Rodríguez");

        // María González - 2 vehículos
        asignarConductorAVehiculoUseCase.execute(vehiculosIds[3], conductoresIds[1]); // XYZ-202
        log.info("  ✓ Vehículo XYZ-202 asignado a María González");

        asignarConductorAVehiculoUseCase.execute(vehiculosIds[4], conductoresIds[1]); // DEF-301
        log.info("  ✓ Vehículo DEF-301 asignado a María González");

        // Juan Pérez - sin vehículos (para demostrar consulta de conductores sin vehículos)
        // vehiculosIds[5] (GHI-401) queda libre
    }

    @Transactional
    private void crearPedidosDeEjemplo(Long[] vehiculosIds, Long[] conductoresIds) {
        // PEDIDO 1 - COMPLETADO
        Pedido p1 = crearPedidoUseCase.execute(
            "Entrega de mercancía a zona industrial",
            2500.0,
            vehiculosIds[0], // ABC-101
            conductoresIds[0], // Carlos
            "Av. Principal 123, Zona Centro",
            "Parque Industrial Norte, Bodega 5"
        );
        p1.iniciar();
        p1.completar();
        pedidoRepository.save(p1);
        log.info("  ✓ Pedido #{} creado - COMPLETADO (2500 kg)", p1.getId());

        // PEDIDO 2 - EN_PROGRESO
        Pedido p2 = crearPedidoUseCase.execute(
            "Transporte de materiales de construcción",
            4000.0,
            vehiculosIds[1], // ABC-102
            conductoresIds[0], // Carlos
            "Depósito Central, Calle 45 #678",
            "Obra en construcción, Av. Libertador km 12"
        );
        p2.iniciar();
        pedidoRepository.save(p2);
        log.info("  ✓ Pedido #{} creado - EN_PROGRESO (4000 kg)", p2.getId());

        // PEDIDO 3 - PENDIENTE
        Pedido p3 = crearPedidoUseCase.execute(
            "Distribución de productos alimenticios",
            1800.0,
            vehiculosIds[2], // XYZ-201
            conductoresIds[0], // Carlos
            "Centro de Distribución, Zona Este",
            "Supermercado El Ahorro, Av. Central 890"
        );
        log.info("  ✓ Pedido #{} creado - PENDIENTE (1800 kg)", p3.getId());

        // PEDIDO 4 - COMPLETADO (María)
        Pedido p4 = crearPedidoUseCase.execute(
            "Mudanza de oficina corporativa",
            3200.0,
            vehiculosIds[3], // XYZ-202
            conductoresIds[1], // María
            "Torre Empresarial, Piso 8",
            "Nuevo Edificio Corporativo, Zona Norte"
        );
        p4.iniciar();
        p4.completar();
        pedidoRepository.save(p4);
        log.info("  ✓ Pedido #{} creado - COMPLETADO (3200 kg)", p4.getId());

        // PEDIDO 5 - CANCELADO
        Pedido p5 = crearPedidoUseCase.execute(
            "Envío urgente de paquetería",
            800.0,
            vehiculosIds[4], // DEF-301
            conductoresIds[1], // María
            "Almacén de paquetería, Calle 12",
            "Centro comercial Plaza Mayor"
        );
        p5.cancelar();
        pedidoRepository.save(p5);
        log.info("  ✓ Pedido #{} creado - CANCELADO (800 kg)", p5.getId());

        // PEDIDO 6 - PENDIENTE (María)
        Pedido p6 = crearPedidoUseCase.execute(
            "Transporte de equipos tecnológicos",
            1200.0,
            vehiculosIds[3], // XYZ-202
            conductoresIds[1], // María
            "Proveedor de tecnología, Zona Comercial",
            "Oficinas centrales, Torre B"
        );
        log.info("  ✓ Pedido #{} creado - PENDIENTE (1200 kg)", p6.getId());

        // PEDIDO 7 - EN_PROGRESO
        Pedido p7 = crearPedidoUseCase.execute(
            "Distribución de electrodomésticos",
            2800.0,
            vehiculosIds[0], // ABC-101
            conductoresIds[0], // Carlos
            "Fábrica de electrodomésticos, Zona Industrial",
            "Tienda departamental, Centro Ciudad"
        );
        p7.iniciar();
        pedidoRepository.save(p7);
        log.info("  ✓ Pedido #{} creado - EN_PROGRESO (2800 kg)", p7.getId());

        // PEDIDO 8 - PENDIENTE (peso alto para demostrar capacidad)
        Pedido p8 = crearPedidoUseCase.execute(
            "Carga pesada de maquinaria industrial",
            7500.0,
            vehiculosIds[1], // ABC-102 (8000 kg capacidad)
            conductoresIds[0], // Carlos
            "Puerto marítimo, Muelle 3",
            "Planta industrial, Km 45 Carretera Sur"
        );
        log.info("  ✓ Pedido #{} creado - PENDIENTE (7500 kg - carga pesada)", p8.getId());
    }

    private void imprimirResumen() {
        log.info("\n");
        log.info("═══════════════════════════════════════════════════════════");
        log.info("              📊 RESUMEN DE DATOS INICIALIZADOS            ");
        log.info("═══════════════════════════════════════════════════════════");
        log.info("");
        log.info("👤 USUARIOS:");
        log.info("  • admin / admin123 (ROL: ADMIN)");
        log.info("  • carlos.conductor / conductor123 (ROL: CONDUCTOR)");
        log.info("  • maria.conductor / conductor123 (ROL: CONDUCTOR)");
        log.info("  • cliente / cliente123 (ROL: CLIENTE)");
        log.info("");
        log.info("🚛 VEHÍCULOS:");
        log.info("  • ABC-101 (5000 kg) → Carlos Rodríguez");
        log.info("  • ABC-102 (8000 kg) → Carlos Rodríguez");
        log.info("  • XYZ-201 (3000 kg) → Carlos Rodríguez");
        log.info("  • XYZ-202 (3500 kg) → María González");
        log.info("  • DEF-301 (1500 kg) → María González");
        log.info("  • GHI-401 (2000 kg) → LIBRE (sin conductor)");
        log.info("");
        log.info("🧑‍✈️ CONDUCTORES:");
        log.info("  • Carlos Rodríguez (LIC001234) - 3 vehículos");
        log.info("  • María González (LIC002345) - 2 vehículos");
        log.info("  • Juan Pérez (LIC003456) - 0 vehículos");
        log.info("");
        log.info("📦 PEDIDOS:");
        log.info("  • 3 pedidos COMPLETADOS");
        log.info("  • 3 pedidos EN_PROGRESO");
        log.info("  • 3 pedidos PENDIENTES");
        log.info("  • 1 pedido CANCELADO");
        log.info("");
        log.info("🔗 ENDPOINTS PARA PROBAR:");
        log.info("  POST /api/v1/auth/login");
        log.info("  GET  /api/v1/vehiculos");
        log.info("  GET  /api/v1/vehiculos/libres");
        log.info("  GET  /api/v1/conductores");
        log.info("  GET  /api/v1/conductores/sin-vehiculos");
        log.info("  GET  /api/v1/pedidos");
        log.info("  GET  /api/v1/reportes/vehiculos-por-conductor");
        log.info("");
        log.info("📚 Swagger UI: http://localhost:8080/swagger-ui.html");
        log.info("═══════════════════════════════════════════════════════════");
        log.info("\n");
    }
}
