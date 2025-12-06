# Tenpo Transactions Challenge

Proyecto para el challenge FullStack-Externo: backend en Java (Spring Boot) y frontend en React.

Este README resume cómo ejecutar el backend localmente y con Docker, endpoints disponibles, validaciones, y notas importantes para la entrega.

**Tecnologías principales**
- Backend: Java 21, Spring Boot 3.x, Spring Data JPA, Hibernate
- Base de datos: PostgreSQL (vía Docker)
- API docs: Springdoc OpenAPI (Swagger)
- Frontend: React + Vite (carpeta `frontend`)
- Build & run: Maven wrapper (`./mvnw`) y `docker-compose`

**Estado actual**
- Endpoints implementados:
  - `POST /transactions` - crea una transacción (devuelve `201 Created`, `Location` header y el body con la transacción creada).
  - `GET /transactions` - lista todas las transacciones.
- Validaciones y reglas de negocio:
  - `amount` no puede ser negativo (validación en DTO + validación de dominio en `Transaction`).
  - `transactionDate` no puede ser posterior a la fecha/hora actual (validación en el dominio).
- Manejo de errores centralizado en `GlobalExceptionHandler` que devuelve `ApiError` con `timestamp`, `status`, `message` y `path`.
- Tests: la suite de tests unitarios del backend pasa (`./mvnw test`) — actualmente 41 tests.

## Ejecutar con Docker (recomendado para evaluación)

El repositorio tiene un `docker-compose.yml` que arranca Postgres y el backend.

Variables usadas en `docker-compose.yml` (por defecto):
- Postgres user: `tenpo`
- Postgres password: `tenpo123`
- Postgres database: `tenpo`

Comandos:

```bash
# Levantar servicios (desde la raíz del repo)
docker-compose up --build

# Parar y remover contenedores
docker-compose down
```

Una vez levantado el backend estará disponible en `http://localhost:8080`.

Swagger / OpenAPI UI (documentación):
- `http://localhost:8080/swagger-ui/index.html` (o `http://localhost:8080/swagger-ui.html` según entorno)

## Ejecutar backend localmente (sin Docker)

Requisitos: JDK 21 y Maven.

```bash
cd backend
./mvnw spring-boot:run
```

Comandos de tests:

```bash
cd backend
./mvnw test
```

## Ejecutar frontend localmente

Requisitos: Node.js + npm/yarn.

```bash
cd frontend
npm install
npm run dev
```

El frontend usa Vite (`npm run dev`) y estará disponible en `http://localhost:5173` por defecto.

## Endpoints principales

- POST /transactions

  - Descripción: Crea una transacción.
  - Request body (ejemplo JSON):

    ```json
    {
      "amount": 12000,
      "business": "Supermercado ABC",
      "tenpistaName": "Juan Perez",
      "transactionDate": "2024-12-01T14:30:00"
    }
    ```

  - Respuesta: `201 Created` con header `Location: /transactions/{id}` y el body JSON de la transacción creada.

- GET /transactions

  - Descripción: Devuelve todas las transacciones.
  - Respuesta: `200 OK` con un array de transacciones.

## Notas técnicas y decisiones

- Arquitectura: se aplicó un patrón estilo hexagonal/port-adapter (existe `application.port`, adaptadores JPA y API). Esto facilita mantenibilidad y testing.
- Validaciones: se realizan en DTOs y en el dominio (`Transaction` es un `record` que valida invariantes). Si prefieres que campos como `business` y `tenpistaName` sean obligatorios, se pueden forzar con `@NotBlank` en el DTO.
- Persistencia: se usan anotaciones JPA básicas y `hibernate.ddl-auto=update` para simplificar el desarrollo. Para producción se recomienda usar Flyway o Liquibase para migraciones gestionadas.

## Recomendaciones para la entrega

- Incluye este README en el repositorio junto con el código.
- Indica en la descripción del PR/README los puntos que realizaste (endpoints, validaciones, Docker, tests verdes).
- Opcionales pero recomendables: agregar Flyway y tests de integración con Testcontainers.

## Cambios aplicados durante la revisión

- Mejoré `GlobalExceptionHandler` para concatenar todos los errores de validación y añadir logging.
- Añadí constraints JPA (`@Column(nullable = false)` para `amount` y `length` en strings) en `TransactionJpaEntity`.
- `POST /transactions` ahora retorna `ResponseEntity` con `201 Created` y `Location` header. Actualicé tests unitarios correspondientes.

Si quieres que añada Flyway o tests de integración antes de subir, lo hago a continuación. ¿Prefieres que genere el archivo `README.md` del `backend/` también o sólo el README raíz?

***
Archivo generado automáticamente por el asistente en base al estado actual del proyecto.
