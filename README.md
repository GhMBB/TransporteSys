# TransporteSys - Sistema de Transporte Urbano

Sistema de gestión de transporte urbano desarrollado con **Clean Architecture** y Spring Boot 4.0.

## 🏗️ Arquitectura

- **Clean Architecture (Hexagonal)**: Separación total entre dominio, aplicación e infraestructura
- **Domain Layer**: Entidades, Value Objects, Specifications y Repository Ports (POJOs puros)
- **Application Layer**: Use Cases que orquestan la lógica de negocio
- **Infrastructure Layer**: Adapters REST, JPA, Security, etc.

## 🚀 Tecnologías

- Java 25
- Spring Boot 4.0.0
- PostgreSQL 16
- Spring Security + JWT
- Swagger/OpenAPI
- JPA/Hibernate
- Lombok
- JaCoCo (cobertura 80%)
- Caffeine Cache
- Docker + Docker Compose
- Prometheus + Grafana (métricas)

## 📋 Funcionalidades

### Vehículos
- CRUD completo con eliminación lógica
- Validación de placa (formato ABC-123)
- Capacidad en kilogramos (BigDecimal)
- Asignación a conductores
- Consulta de vehículos libres
- Auditoría de cambios

### Conductores
- CRUD completo
- Límite de 3 vehículos por conductor
- Validación de licencia
- Consulta de conductores sin vehículos
- Conteo de vehículos por conductor
- Auditoría de cambios

### Pedidos
- Creación con validaciones:
  - Vehículo activo
  - Conductor activo
  - Capacidad suficiente del vehículo
- Estados: PENDIENTE → EN_PROGRESO → COMPLETADO/CANCELADO
- Transiciones de estado validadas
- Filtros por estado, vehículo, conductor

### Seguridad
- Autenticación JWT
- Roles: ADMIN, CONDUCTOR, CLIENTE
- Endpoints protegidos por rol
- Tokens válidos por 24 horas

## 🛠️ Instalación y Uso

### Opción 1: Docker Compose (Recomendado)

```bash
# Levantar todo el stack
docker-compose up -d

# Con monitoring (Prometheus + Grafana)
docker-compose --profile monitoring up -d

# Ver logs
docker-compose logs -f app
```

Servicios disponibles:
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- Prometheus: http://localhost:9090 (con --profile monitoring)
- Grafana: http://localhost:3000 (con --profile monitoring)

### Opción 2: Ejecución Local

**Requisitos:**
- Java 25
- PostgreSQL 16
- Maven 3.9+

```bash
# 1. Crear la base de datos
psql -U postgres
CREATE DATABASE transportesys;
\q

# 2. Compilar
./mvnw clean install

# 3. Ejecutar
./mvnw spring-boot:run

# O ejecutar el JAR
java -jar target/TransporteSys-0.0.1-SNAPSHOT.jar
```

## 📚 Documentación API

### Swagger UI
Accede a la documentación interactiva en: http://localhost:8080/swagger-ui.html

### Endpoints Principales

**Autenticación:**
```
POST /api/v1/auth/login
```

**Vehículos:**
```
GET    /api/v1/vehiculos
POST   /api/v1/vehiculos
GET    /api/v1/vehiculos/{id}
PUT    /api/v1/vehiculos/{id}
DELETE /api/v1/vehiculos/{id}
GET    /api/v1/vehiculos/libres
POST   /api/v1/vehiculos/{vehiculoId}/asignar-conductor/{conductorId}
```

**Conductores:**
```
GET    /api/v1/conductores
POST   /api/v1/conductores
GET    /api/v1/conductores/{id}
PUT    /api/v1/conductores/{id}
DELETE /api/v1/conductores/{id}
GET    /api/v1/conductores/sin-vehiculos
GET    /api/v1/conductores/conteo-vehiculos
```

**Pedidos:**
```
GET    /api/v1/pedidos
POST   /api/v1/pedidos
GET    /api/v1/pedidos/{id}
POST   /api/v1/pedidos/{id}/iniciar
POST   /api/v1/pedidos/{id}/completar
POST   /api/v1/pedidos/{id}/cancelar
PATCH  /api/v1/pedidos/{id}/estado?estado=EN_PROGRESO
```

**Reportes:**
```
GET /api/v1/reportes/vehiculos-libres
GET /api/v1/reportes/conductores-sin-vehiculos
GET /api/v1/reportes/vehiculos-por-conductor
```

## 🌱 Datos de Prueba (Seed)

El sistema incluye un **seed automático** que carga datos de prueba al iniciar la aplicación en modo `dev` o `local`.

### Datos Pre-cargados

**👤 Usuarios:**
| Username | Password | Rol | Email |
|----------|----------|-----|-------|
| admin | admin123 | ADMIN | admin@transportesys.com |
| carlos.conductor | conductor123 | CONDUCTOR | carlos@transportesys.com |
| maria.conductor | conductor123 | CONDUCTOR | maria@transportesys.com |
| cliente | cliente123 | CLIENTE | cliente@transportesys.com |

**🚛 Vehículos:**
| Placa | Capacidad | Conductor Asignado |
|-------|-----------|-------------------|
| ABC-101 | 5000 kg | Carlos Rodríguez |
| ABC-102 | 8000 kg | Carlos Rodríguez |
| XYZ-201 | 3000 kg | Carlos Rodríguez |
| XYZ-202 | 3500 kg | María González |
| DEF-301 | 1500 kg | María González |
| GHI-401 | 2000 kg | **LIBRE** (sin conductor) |

**🧑‍✈️ Conductores:**
| Nombre | Licencia | Vehículos Asignados |
|--------|----------|---------------------|
| Carlos Rodríguez | LIC001234 | 3 (límite máximo) |
| María González | LIC002345 | 2 |
| Juan Pérez | LIC003456 | 0 (sin vehículos) |

**📦 Pedidos de Ejemplo:**
- 3 pedidos **COMPLETADOS**
- 3 pedidos **EN_PROGRESO**
- 3 pedidos **PENDIENTES**
- 1 pedido **CANCELADO**

### Configuración del Seed

El seed se ejecuta automáticamente cuando:
- El perfil activo es `dev` o `local`
- La base de datos está vacía (no hay usuarios)

**Activar/Desactivar:**
```properties
# En application.properties
spring.profiles.active=dev    # Activa seed
spring.profiles.active=prod   # Desactiva seed
```

**Reiniciar datos:**
```bash
# Opción 1: Borrar y recrear base de datos
psql -U postgres
DROP DATABASE transportesys;
CREATE DATABASE transportesys;
\q

# Opción 2: Cambiar ddl-auto a create (¡cuidado en producción!)
spring.jpa.hibernate.ddl-auto=create
```

## 🔐 Autenticación

### 1. Login con usuario pre-cargado (Recomendado):
Ya no necesitas crear usuarios manualmente. Usa cualquiera de los usuarios del seed.

### 2. Obtener token:
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

### 3. Usar token en requests:
```bash
curl -X GET http://localhost:8080/api/v1/vehiculos \
  -H "Authorization: Bearer {TOKEN}"
```

## 🧪 Tests

```bash
# Ejecutar todos los tests
./mvnw test

# Con cobertura JaCoCo
./mvnw test jacoco:report

# Ver reporte de cobertura
open target/site/jacoco/index.html
```

## 📊 Métricas y Monitoring

### Actuator Endpoints
- Health: http://localhost:8080/actuator/health
- Metrics: http://localhost:8080/actua`tor/metrics
- Prometheus: http://localhost:8080/actuator/prometheus

### Prometheus + Grafana`
```bash
docker-compose --profile monitoring up -d
```
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)

## 🏛️ Reglas de Negocio

1. **Placa de vehículo**: Formato ABC-123 (3 letras + guión + 3 números)
2. **Capacidad**: Mayor a 0, expresada en kilogramos
3. **Conductor**: Máximo 3 vehículos asignados
4. **Pedido**: Solo se crea si:
   - El vehículo tiene capacidad suficiente
   - El conductor está activo
   - El vehículo está activo
5. **Estados de pedido**: Transiciones permitidas:
   - PENDIENTE → EN_PROGRESO o CANCELADO
   - EN_PROGRESO → COMPLETADO o CANCELADO
   - COMPLETADO/CANCELADO → Sin cambios (estados finales)

## 📂 Estructura del Proyecto

```
src/main/java/com/example/transportesys/
├── domain/                      # Capa de Dominio (POJOs puros)
│   ├── model/                   # Entidades
│   ├── valueobject/             # Value Objects
│   ├── enums/                   # Enumeraciones
│   ├── exception/               # Excepciones de dominio
│   ├── repository/              # Ports (interfaces)
│   ├── service/                 # Domain Services
│   └── specification/           # Business Rules
│
├── application/                 # Capa de Aplicación
│   ├── usecase/                 # Use Cases
│   ├── port/                    # Input/Output Ports
│   └── dto/                     # DTOs de aplicación
│
└── infrastructure/              # Capa de Infraestructura
    ├── adapter/
    │   ├── in/rest/             # REST Controllers (Input Adapter)
    │   └── out/persistence/     # JPA Repositories (Output Adapter)
    ├── config/                  # Configuraciones Spring
    ├── security/                # JWT, Security
    └── exception/               # Exception Handlers
```

## 🐳 Docker Commands

```bash
# Build de la imagen
docker build -t transportesys:latest .

# Run standalone
docker run -p 8080:8080 transportesys:latest

# Docker Compose
docker-compose up -d          # Levantar
docker-compose down           # Bajar
docker-compose logs -f app    # Ver logs
docker-compose restart app    # Reiniciar
```


## 👨‍💻 Autor

Mauricio Bordon