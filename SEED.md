# 🌱 Guía de Seed de Datos - TransporteSys

## Descripción

El sistema incluye un **seed automático** (`DataSeeder`) que carga datos de prueba completos al iniciar la aplicación. Esto permite probar todas las funcionalidades del sistema sin necesidad de crear datos manualmente.

## 📊 Datos Pre-cargados

### 👤 4 Usuarios con Diferentes Roles

| Username | Password | Rol | Email | Descripción |
|----------|----------|-----|-------|-------------|
| `admin` | `admin123` | ADMIN | admin@transportesys.com | Acceso total al sistema |
| `carlos.conductor` | `conductor123` | CONDUCTOR | carlos@transportesys.com | Conductor con 3 vehículos (máximo) |
| `maria.conductor` | `conductor123` | CONDUCTOR | maria@transportesys.com | Conductor con 2 vehículos |
| `cliente` | `cliente123` | CLIENTE | cliente@transportesys.com | Cliente del sistema |

### 🚛 6 Vehículos de Diferentes Capacidades

| Placa | Capacidad | Estado | Conductor Asignado | Observaciones |
|-------|-----------|--------|-------------------|---------------|
| ABC-101 | 5000 kg | Activo | Carlos Rodríguez | Carga pesada |
| ABC-102 | 8000 kg | Activo | Carlos Rodríguez | Carga muy pesada |
| XYZ-201 | 3000 kg | Activo | Carlos Rodríguez | Carga media |
| XYZ-202 | 3500 kg | Activo | María González | Carga media |
| DEF-301 | 1500 kg | Activo | María González | Carga ligera |
| **GHI-401** | **2000 kg** | **Activo** | **LIBRE** | **Sin conductor** |

### 🧑‍✈️ 3 Conductores con Diferentes Asignaciones

| Nombre | Licencia | Vehículos | Observaciones |
|--------|----------|-----------|---------------|
| Carlos Rodríguez | LIC001234 | 3 | **Límite máximo alcanzado** |
| María González | LIC002345 | 2 | Puede asignar 1 vehículo más |
| Juan Pérez | LIC003456 | 0 | **Sin vehículos asignados** |

### 📦 8 Pedidos con Diferentes Estados

| # | Descripción | Peso | Vehículo | Conductor | Estado | Observaciones |
|---|-------------|------|----------|-----------|--------|---------------|
| 1 | Entrega a zona industrial | 2500 kg | ABC-101 | Carlos | ✅ COMPLETADO | Flujo completo |
| 2 | Materiales de construcción | 4000 kg | ABC-102 | Carlos | 🚚 EN_PROGRESO | En tránsito |
| 3 | Productos alimenticios | 1800 kg | XYZ-201 | Carlos | ⏳ PENDIENTE | Sin iniciar |
| 4 | Mudanza de oficina | 3200 kg | XYZ-202 | María | ✅ COMPLETADO | Flujo completo |
| 5 | Envío urgente | 800 kg | DEF-301 | María | ❌ CANCELADO | Cancelado |
| 6 | Equipos tecnológicos | 1200 kg | XYZ-202 | María | ⏳ PENDIENTE | Sin iniciar |
| 7 | Electrodomésticos | 2800 kg | ABC-101 | Carlos | 🚚 EN_PROGRESO | En tránsito |
| 8 | Maquinaria industrial | 7500 kg | ABC-102 | Carlos | ⏳ PENDIENTE | Carga pesada |

## 🎯 Flujos Completos Incluidos

### 1. Flujo de Conductor con Límite Máximo
- **Carlos Rodríguez** tiene 3 vehículos asignados (límite máximo)
- No puede asignar más vehículos
- Perfecto para probar la validación de límite

### 2. Flujo de Vehículo Libre
- **GHI-401** está disponible sin conductor
- Puede usarse para probar asignación de conductores
- Útil para endpoint `/api/v1/vehiculos/libres`

### 3. Flujo de Conductor Sin Vehículos
- **Juan Pérez** no tiene vehículos asignados
- Perfecto para probar `/api/v1/conductores/sin-vehiculos`

### 4. Flujos de Estado de Pedidos
- **PENDIENTE → EN_PROGRESO**: Pedidos #3, #6, #8
- **EN_PROGRESO → COMPLETADO**: Pedidos #1, #4
- **EN_PROGRESO → CANCELADO**: Pedido #5
- **Estados finales**: Pedidos #1, #4, #5

### 5. Flujo de Capacidad vs Peso
- Pedido #8: 7500 kg en vehículo de 8000 kg (cerca del límite)
- Demuestra validación de capacidad

## ⚙️ Configuración

### Activar/Desactivar Seed

El seed se activa mediante perfiles de Spring:

```properties
# application.properties

# Activar seed (desarrollo)
spring.profiles.active=dev

# Desactivar seed (producción)
spring.profiles.active=prod
```

### Variables de Entorno (Docker)

```yaml
# docker-compose.yml
environment:
  SPRING_PROFILES_ACTIVE: dev    # Activa seed
  # O
  SPRING_PROFILES_ACTIVE: prod   # Desactiva seed
```

### Comando de Ejecución

```bash
# Con seed
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

# Sin seed
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## 🔄 Reiniciar Datos

### Opción 1: Borrar Base de Datos (Recomendado)

```bash
# Conectar a PostgreSQL
psql -U postgres

# Borrar y recrear
DROP DATABASE transportesys;
CREATE DATABASE transportesys;
\q

# Reiniciar aplicación (seed se ejecuta automáticamente)
./mvnw spring-boot:run
```

### Opción 2: Cambiar DDL Auto (⚠️ Cuidado)

```properties
# Cambia en application.properties
spring.jpa.hibernate.ddl-auto=create  # Recrea tablas cada vez

# Después de ejecutar, volver a:
spring.jpa.hibernate.ddl-auto=update
```

## 🧪 Probando el Seed

### 1. Verificar que los datos se cargaron

```bash
# Ver logs al iniciar
[INFO] 🌱 Iniciando seed de datos...
[INFO] 👤 Creando usuarios...
[INFO]   ✓ Usuario ADMIN creado
[INFO]   ✓ Usuario CONDUCTOR creado
[INFO] 🚛 Creando vehículos...
[INFO]   ✓ Vehículo creado: ABC-101
...
[INFO] ✅ Seed de datos completado exitosamente!
```

### 2. Login con usuarios pre-cargados

```bash
# Login como ADMIN
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Login como CONDUCTOR
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"carlos.conductor","password":"conductor123"}'
```

### 3. Consultar datos

```bash
# Obtener token (ejemplo)
TOKEN="eyJhbGc..."

# Listar vehículos
curl http://localhost:8080/api/v1/vehiculos \
  -H "Authorization: Bearer $TOKEN"

# Ver conductores sin vehículos
curl http://localhost:8080/api/v1/conductores/sin-vehiculos \
  -H "Authorization: Bearer $TOKEN"

# Ver vehículos libres
curl http://localhost:8080/api/v1/vehiculos/libres \
  -H "Authorization: Bearer $TOKEN"

# Ver todos los pedidos
curl http://localhost:8080/api/v1/pedidos \
  -H "Authorization: Bearer $TOKEN"

# Ver reporte de vehículos por conductor
curl http://localhost:8080/api/v1/reportes/vehiculos-por-conductor \
  -H "Authorization: Bearer $TOKEN"
```

## 📝 Notas Importantes

### Condiciones para Ejecución

El seed **SOLO se ejecuta** cuando:
1. El perfil activo es `dev` o `local`
2. La base de datos está **vacía** (no hay usuarios)

Si la base de datos ya tiene datos, verás:
```
[INFO] ✅ Base de datos ya contiene datos. Saltando seed.
```

### Seguridad

- Las contraseñas están hasheadas con **BCrypt**
- Los usuarios de prueba **NO deben usarse en producción**
- En producción, usar `spring.profiles.active=prod`

### Orden de Creación

1. **Usuarios** (primero, para autenticación)
2. **Vehículos** (sin asignar)
3. **Conductores** (sin vehículos)
4. **Asignaciones** (vehículos ← conductores)
5. **Pedidos** (con diferentes estados)

## 🎓 Casos de Uso para Probar

### ✅ Escenarios Exitosos

- Login con diferentes roles
- Listar vehículos libres (debería mostrar GHI-401)
- Listar conductores sin vehículos (debería mostrar Juan Pérez)
- Ver pedidos completados, en progreso, pendientes
- Asignar vehículo GHI-401 a Juan Pérez

### ❌ Escenarios de Error

- Intentar asignar un 4to vehículo a Carlos (debe fallar por límite)
- Crear pedido con peso > capacidad del vehículo
- Cambiar estado inválido (ej: COMPLETADO → PENDIENTE)
- Asignar conductor inactivo a vehículo

## 🔗 Referencias

- Ver implementación: `src/main/java/com/example/transportesys/infrastructure/config/DataSeeder.java`
- Configuración: `src/main/resources/application.properties`
- Docker: `docker-compose.yml`
- Documentación API: http://localhost:8080/swagger-ui.html
