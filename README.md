# Tenpo Transactions Challenge

Aplicación FullStack para la gestión de transacciones de Tenpistas, desarrollada con Spring Boot (backend) y React + TypeScript (frontend), siguiendo principios SOLID y arquitectura hexagonal.

## 📋 Tabla de Contenidos
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Inicio Rápido](#-inicio-rápido-docker-compose)
- [Ejecución Local](#-ejecución-local-sin-docker)
- [Testing](#-testing)
- [API Documentation](#-api-documentation)
- [Decisiones de Diseño](#-decisiones-de-diseño)
- [Estructura del Proyecto](#-estructura-del-proyecto)

---

## 🏗 Arquitectura

### Backend (Spring Boot)
El backend implementa **arquitectura hexagonal** (Ports & Adapters), separando claramente el dominio de las implementaciones técnicas:

```
┌─────────────────────────────────────────────────┐
│         Infrastructure Layer (Adapters)         │
│  ┌──────────────────┐  ┌───────────────────┐    │
│  │   API Adapter    │  │  JPA Adapter      │    │
│  │  (Controllers)   │  │  (Repositories)   │    │
│  │  - REST endpoints│  │  - DB operations  │    │
│  │  - DTOs          │  │  - JPA entities   │    │
│  └────────┬─────────┘  └─────────┬─────────┘    │
└───────────┼─────────────────────┼───────────────┘
            │                     │
            │  Implements Port    │  Implements Port
            │                     │
┌───────────▼─────────────────────▼───────────────┐
│         Application Layer (Ports & Services)    │
│  ┌───────────────────────────────────────────┐  │
│  │  TransactionRepositoryPort (Interface)    │  │
│  │  - Abstracción de persistencia            │  │
│  └───────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────┐  │
│  │  TransactionService (Business Logic)      │  │
│  │  - Casos de uso                           │  │
│  │  - Reglas de negocio                      │  │
│  │  - Orquestación                           │  │
│  └───────────────────────────────────────────┘  │
└───────────────────────┬─────────────────────────┘
                        │
                        │  Uses
                        │
┌───────────────────────▼─────────────────────────┐
│              Domain Layer (Core)                │
│  ┌───────────────────────────────────────────┐  │
│  │  Transaction (Entity)                     │  │
│  │  - Modelo de dominio puro                 │  │
│  │  - Sin dependencias externas              │  │
│  └───────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
```

**Componentes principales:**

**🔵 Domain Layer (Núcleo)**
- `Transaction`: Entidad de dominio pura, sin anotaciones de infraestructura
- Representa el modelo de negocio central
- Sin dependencias hacia capas externas

**🟢 Application Layer (Lógica de Aplicación)**
- `TransactionRepositoryPort`: Puerto (interface) que define contrato de persistencia
- `TransactionService` / `TransactionServiceImpl`: Casos de uso y lógica de negocio
- Independiente de frameworks y detalles de implementación

**🟡 Infrastructure Layer (Adaptadores)**
- **API Adapter**:
  - `TransactionController`: Adaptador REST (entrada)
  - `TransactionRequest/Response`: DTOs para comunicación HTTP
- **JPA Adapter**:
  - `TransactionJpaEntity`: Entidad JPA (adaptación del modelo de dominio)
  - `TransactionJpaRepository`: Repositorio Spring Data
  - `TransactionRepositoryJpaAdapter`: Implementación del puerto de persistencia
- **Mappers**: Transformación entre capas (Domain ↔ JPA, Domain ↔ DTO)
- **Exception Handling**: Gestión centralizada de errores

**Ventajas de esta arquitectura:**
- ✅ **Independencia del dominio**: El núcleo no conoce Spring, JPA o HTTP
- ✅ **Testabilidad**: Fácil mockear puertos en tests unitarios
- ✅ **Flexibilidad**: Cambiar adaptadores sin afectar el dominio (ej: MongoDB en lugar de PostgreSQL)
- ✅ **Mantenibilidad**: Separación clara de responsabilidades
- ✅ **Principio de Inversión de Dependencias**: El dominio define contratos, infraestructura los implementa

### Frontend (React + TypeScript)
Arquitectura basada en **componentes funcionales** con hooks:

```
┌─────────────────────────────────────┐
│           Pages/Views               │
│  - TransactionsPage                 │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Feature Components             │
│  - TransactionList                  │
│  - TransactionForm                  │
│  - Filtros y búsqueda               │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│       Shared Components             │
│  - Buttons, Cards, Modals           │
│  - Loading states                   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Services/API Layer           │
│  - Axios client                     │
│  - API calls                        │
└─────────────────────────────────────┘
```

---

## 🛠 Tecnologías

### Backend
- **Java 21** - LTS release con mejoras de rendimiento y features modernos
- **Spring Boot 3.4** - Framework principal
- **Spring Data JPA** (Hibernate) - ORM y gestión de persistencia
- **Spring Validation** - Validación declarativa de DTOs
- **PostgreSQL 16** - Base de datos relacional
- **Flyway** - Migraciones de base de datos versionadas
- **JUnit 5** + **Mockito** - Testing unitario e integración
- **Lombok** - Reducción de boilerplate
- **Maven** - Gestión de dependencias y build

### Frontend
- **React 18** - Biblioteca UI con Hooks
- **TypeScript** - Type safety y mejor experiencia de desarrollo
- **Vite** - Build tool rápido y moderno
- **Tailwind CSS** - Utility-first CSS framework
- **Axios** - Cliente HTTP con interceptors
- **React Hook Form** - Gestión eficiente de formularios
- **Vitest** + **Testing Library** - Testing moderno para React

### DevOps
- **Docker** - Containerización
- **Docker Compose** - Orquestación multi-contenedor
- **Nginx** - Servidor web para frontend en producción

---

## 📦 Requisitos Previos

- **Docker** >= 20.10
- **Docker Compose** >= 2.0
- (Opcional) **JDK 21** + **Maven 3.8+** para desarrollo local del backend
- (Opcional) **Node.js 18+** + **npm** para desarrollo local del frontend

---

## 🚀 Inicio Rápido (Docker Compose)

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd tenpo-challenge
```

### 2. Levantar todos los servicios

**Windows (cmd/PowerShell):**
```cmd
docker compose up --build
```

**Linux/macOS:**
```bash
docker compose up --build
```

### 3. Acceder a la aplicación

| Servicio     | URL                                    | Descripción                                            |
|--------------|----------------------------------------|--------------------------------------------------------|
| Frontend     | http://localhost:3000                  | Interfaz de usuario                                    |
| Backend API  | http://localhost:8080                  | REST API                                               |
| Swagger UI   | http://localhost:8080/swagger-ui.html  | Documentación interactiva                              |
| PostgreSQL   | localhost:5432                         | Base de datos (usuario: `tenpo`, password: `tenpo123`) |

### 4. Detener los servicios
```bash
# Detener contenedores
docker compose down

# Detener y eliminar volúmenes (limpieza completa)
docker compose down -v
```

> **⚠️ Nota:** Si cambias la versión de PostgreSQL en `docker-compose.yml`, ejecuta `docker compose down -v` antes de volver a levantar los servicios para evitar incompatibilidades en el volumen de datos.

---

## 💻 Ejecución Local (sin Docker)

### Backend

1. **Iniciar PostgreSQL** (puedes usar el contenedor standalone):
```bash
docker run -d \
  --name postgres-tenpo \
  -e POSTGRES_DB=tenpo \
  -e POSTGRES_USER=tenpo \
  -e POSTGRES_PASSWORD=tenpo123 \
  -p 5432:5432 \
  postgres:16-alpine
```

2. **Configurar application.properties** (si es necesario):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tenpo
spring.datasource.username=tenpo
spring.datasource.password=tenpo123
```

3. **Ejecutar el backend**:
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

El backend estará disponible en `http://localhost:8080`

### Frontend

1. **Configurar variables de entorno**. Crea `frontend/.env`:
```env
VITE_API_BASE_URL=http://localhost:8080
```

2. **Instalar dependencias y ejecutar**:
```bash
cd frontend
npm install
npm run dev
```

El frontend estará disponible en `http://localhost:5173` (puerto por defecto de Vite)

---

## 🧪 Testing

### Backend (JUnit 5)

```bash
cd backend

# Ejecutar todos los tests
./mvnw test

# Ejecutar tests con cobertura
./mvnw verify

# Ejecutar solo tests unitarios
./mvnw test -Dtest=*UnitTest

# Ejecutar solo tests de integración
./mvnw test -Dtest=*IntegrationTest
```

**Tipos de tests implementados:**
- ✅ Tests unitarios de servicios (mockeo de puertos)
- ✅ Tests de validación de DTOs
- ✅ Tests de adaptadores con `@DataJpaTest`
- ✅ Tests de integración de controllers con `@SpringBootTest`

### Frontend (Vitest)

```bash
cd frontend

# Ejecutar todos los tests
npm test

# Modo watch (desarrollo)
npm run test:watch

# Coverage report
npm run test:coverage

# Tests específicos
npx vitest run TransactionForm.test.tsx
```

**Tipos de tests implementados:**
- ✅ Tests de componentes con Testing Library
- ✅ Tests de hooks personalizados
- ✅ Tests de utilidades y helpers
- ✅ Tests de integración de formularios

---

## 📚 API Documentation

### Swagger/OpenAPI

La documentación interactiva está disponible en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Endpoints Principales

#### `POST /transactions`
Crea una nueva transacción.

**Request Body:**
```json
{
  "amount": 12000,
  "business": "Supermercado ABC",
  "tenpistaName": "Juan Pérez",
  "transactionDate": "2024-12-01T14:30:00"
}
```

**Validaciones:**
- `amount`: Debe ser positivo (> 0)
- `business`: No puede estar vacío, máximo 255 caracteres
- `tenpistaName`: No puede estar vacío, máximo 100 caracteres
- `transactionDate`: No puede ser fecha futura

**Response:** `201 Created`
```json
{
  "id": 1,
  "amount": 12000,
  "business": "Supermercado ABC",
  "tenpistaName": "Juan Pérez",
  "transactionDate": "2024-12-01T14:30:00",
  "createdAt": "2024-12-08T10:00:00Z",
  "updatedAt": "2024-12-08T10:00:00Z"
}
```

**Headers:**
- `Location: /transactions/1`

#### `GET /transactions`
Lista todas las transacciones (sin paginación).

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "amount": 12000,
    "business": "Supermercado ABC",
    "tenpistaName": "Juan Pérez",
    "transactionDate": "2024-12-01T14:30:00",
    "createdAt": "2024-12-08T10:00:00Z",
    "updatedAt": "2024-12-08T10:00:00Z"
  }
]
```

**Ordenación:** Por defecto ordena por `createdAt` DESC (más recientes primero)

#### `GET /transactions?page={page}&size={size}`
Lista transacciones con paginación.

**Parámetros de query:**
- `page`: Número de página (default: 0)
- `size`: Tamaño de página (default: 10)

**Response:** `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "amount": 12000,
      "business": "Supermercado ABC",
      "tenpistaName": "Juan Pérez",
      "transactionDate": "2024-12-01T14:30:00",
      "createdAt": "2024-12-08T10:00:00Z",
      "updatedAt": "2024-12-08T10:00:00Z"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 42,
  "totalPages": 5
}
```

### Manejo de Errores

Todos los errores devuelven una estructura consistente:

```json
{
  "timestamp": "2024-12-08T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "El monto debe ser positivo",
  "path": "/transactions"
}
```

**Códigos de error comunes:**
- `400 Bad Request`: Validación fallida
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

---

## 🎯 Decisiones de Diseño

### Backend

#### 1. Arquitectura Hexagonal (Ports & Adapters)
- **Separación por capas conceptuales**: Domain (núcleo), Application (lógica), Infrastructure (adaptadores)
- **Puertos e interfaces**: `TransactionRepositoryPort` define contrato sin implementación
- **Inversión de dependencias**: El dominio no depende de frameworks externos
- **Adaptadores intercambiables**: JPA, REST, o cualquier otro sin afectar el núcleo
- **Testabilidad superior**: Mock de puertos para tests unitarios aislados

#### 2. Separación de Modelos
- **Domain Model**: `Transaction` - Entidad de dominio pura
- **JPA Entity**: `TransactionJpaEntity` - Modelo de persistencia con anotaciones JPA
- **DTOs**: `TransactionRequest/Response` - Contratos de API
- **Mappers**: Transformación explícita entre modelos de diferentes capas

#### 3. Auditoría Automática
- **JPA Auditing**: `@CreatedDate` y `@LastModifiedDate` para tracking automático
- **Timestamps en UTC**: `timestamptz` para evitar problemas de zonas horarias
- **Campos inmutables**: `createdAt` solo se setea en creación

#### 4. Migraciones con Flyway
- **Versionado**: Migraciones numeradas secuencialmente (`V1__`, `V2__`, etc.)
- **Idempotencia**: Scripts seguros para re-ejecución
- **Control por ambiente**: Variable `SPRING_FLYWAY_ENABLED` en Docker Compose

#### 5. Manejo de Errores Centralizado
- **GlobalExceptionHandler**: Un único punto para mapear excepciones a respuestas HTTP
- **Respuestas consistentes**: Misma estructura para todos los errores
- **Logging**: Trazabilidad de errores con stack traces en logs

#### 6. Paginación Flexible
- **Dual approach**: Endpoint sin paginación para simplicidad, con paginación para escalabilidad
- **Defaults sensatos**: Tamaño de página por defecto de 10
- **Metadatos completos**: totalElements, totalPages para implementar UI pagination

### Frontend

#### 1. TypeScript
- **Type safety**: Prevención de errores en tiempo de desarrollo
- **Interfaces claras**: Contratos explícitos entre componentes y API
- **Mejor DX**: Autocompletado y documentación inline

#### 2. Validación de Formularios
- **Doble validación**: Cliente (UX) + Servidor (seguridad)
- **Feedback inmediato**: Validación en tiempo real con React Hook Form
- **Mensajes claros**: Errores específicos por campo

#### 3. Gestión de Estado
- **React Query** (opcional/recomendado): Cache y sincronización con servidor
- **useState local**: Para estado UI simple
- **Props drilling mínimo**: Composición de componentes bien definida

#### 4. Estilos con Tailwind
- **Utility-first**: Desarrollo rápido sin context switching
- **Responsive por defecto**: Mobile-first approach
- **Consistencia**: Sistema de diseño coherente

### DevOps

#### 1. Docker Multi-Stage Builds
- **Backend**: Build de Maven en imagen builder, runtime en JRE slim
- **Frontend**: Build de Vite, servido con Nginx alpine
- **Optimización**: Imágenes finales pequeñas (<100MB frontend, ~200MB backend)

#### 2. Docker Compose
- **Desarrollo local**: Ambiente completo con un comando
- **Networking**: Red interna para comunicación entre servicios
- **Volúmenes**: Persistencia de datos de PostgreSQL

#### 3. Variables de Entorno
- **Configuración externalizada**: Fácil cambio entre ambientes
- **Secrets seguros**: No hardcodear credenciales
- **Defaults sensatos**: Valores por defecto para desarrollo local

---

## 📁 Estructura del Proyecto

```
tenpo-challenge/
├── backend/
│   ├── src/
│   │   ├── main/java/com/tenpo/transactions/
│   │   │   ├── domain/
│   │   │   │   └── model/
│   │   │   │       └── Transaction.java              # Entidad de dominio pura
│   │   │   │
│   │   │   ├── application/
│   │   │   │   ├── port/
│   │   │   │   │   └── TransactionRepositoryPort.java # Puerto (interface)
│   │   │   │   └── service/
│   │   │   │       ├── TransactionService.java
│   │   │   │       └── TransactionServiceImpl.java    # Lógica de negocio
│   │   │   │
│   │   │   └── infrastructure/
│   │   │       ├── adapters/
│   │   │       │   └── api/
│   │   │       │       └── controller/
│   │   │       │           ├── dto/
│   │   │       │           │   ├── TransactionRequest.java
│   │   │       │           │   └── TransactionResponse.java
│   │   │       │           └── TransactionController.java  # Adaptador REST
│   │   │       │
│   │   │       ├── jpa/
│   │   │       │   ├── TransactionJpaEntity.java          # Entidad JPA
│   │   │       │   ├── TransactionJpaRepository.java
│   │   │       │   └── TransactionRepositoryJpaAdapter.java # Adaptador persistencia
│   │   │       │
│   │   │       ├── mapper/
│   │   │       │   └── TransactionMapper.java             # Mapeo entre capas
│   │   │       │
│   │   │       ├── config/
│   │   │       │   └── OpenApiConfig.java
│   │   │       │
│   │   │       └── exception/
│   │   │           ├── ApiError.java
│   │   │           └── GlobalExceptionHandler.java
│   │   │
│   │   └── resources/
│   │       ├── db/migration/                              # Flyway migrations
│   │       └── application.properties
│   │
│   ├── test/                                              # Unit & Integration Tests
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── components/                                    # React Components
│   │   │   ├── features/                                  # Feature-specific
│   │   │   └── shared/                                    # Reusable
│   │   ├── services/                                      # API calls
│   │   ├── types/                                         # TypeScript types
│   │   ├── utils/                                         # Helper functions
│   │   └── App.tsx
│   ├── public/
│   ├── Dockerfile
│   ├── package.json
│   └── vite.config.ts
│
├── docker-compose.yml
└── README.md
```

---

## 🔧 Comandos Útiles

### Docker

```bash
# Rebuild completo
docker compose build --no-cache

# Ver logs en tiempo real
docker compose logs -f

# Logs de un servicio específico
docker compose logs backend -f

# Ejecutar comando en contenedor
docker compose exec backend bash
docker compose exec db psql -U tenpo -d tenpo

# Reiniciar un servicio
docker compose restart backend

# Ver estado de contenedores
docker compose ps
```

### Base de Datos

```bash
# Conectarse a PostgreSQL
docker compose exec db psql -U tenpo -d tenpo

# Backup
docker compose exec db pg_dump -U tenpo tenpo > backup.sql

# Restore
docker compose exec -T db psql -U tenpo -d tenpo < backup.sql

# Ver tablas
docker compose exec db psql -U tenpo -d tenpo -c "\dt"
```

---

## 📈 Mejoras Futuras

- [ ] Implementar autenticación y autorización (Spring Security + JWT)
- [ ] Agregar filtros y búsqueda avanzada de transacciones
- [ ] Implementar soft delete para transacciones
- [ ] Agregar métricas con Actuator y Prometheus
- [ ] Implementar rate limiting
- [ ] CI/CD pipeline (GitHub Actions, GitLab CI)
- [ ] Logging estructurado con ELK stack
- [ ] Internacionalización (i18n) en frontend
- [ ] Tests E2E con Playwright/Cypress
- [ ] Implementar más adaptadores (GraphQL, gRPC)

---

## 📄 Licencia

Este proyecto fue desarrollado como parte del Tenpo FullStack Challenge.

---

## 👥 Contacto

Si tienes preguntas sobre la implementación, no dudes en abrir un issue en el repositorio o contactarme a wdpinto@utp.edu.co.