# Tenpo Transactions Challenge

Repositorio con la solución al challenge FullStack-Externo: backend en Java (Spring Boot) y frontend en React (Vite).

Este README describe la arquitectura, tecnologías, pasos para ejecutar la aplicación (local y con Docker), cómo ejecutar las pruebas y verificaciones útiles para quien revisará la prueba.

**Tecnologías principales**
- Backend: Java 21, Spring Boot 3.x, Spring Data JPA, Hibernate
- Base de datos: PostgreSQL (contenedor Docker)
- API docs: Springdoc OpenAPI (Swagger)
- Frontend: React + TypeScript + Vite
- Styling: Tailwind CSS
- Tests frontend: Vitest + Testing Library
- Tests backend: JUnit (Maven)

---

## Requisitos previos
- Docker & Docker Compose (recomendado para evaluación)
- JDK 21 + Maven (solo si quieres ejecutar backend fuera de Docker)
- Node.js + npm (solo si quieres ejecutar frontend fuera de Docker)

---

## Quickstart (recomendado) — ejecutar todo con Docker Compose

Desde la raíz del repositorio ejecuta:

Windows (cmd.exe):
```cmd
docker compose up --build
```

PowerShell note: si copias comandos con `&` o caracteres especiales usa comillas.

Linux / macOS:
```bash
docker compose up --build
```

Esto iniciará los siguientes servicios:
- `db` (Postgres) — credenciales por defecto en `docker-compose.yml`.
- `backend` (Spring Boot) — expuesto en `http://localhost:8080`.
- `frontend` (builder + nginx) — expuesto en `http://localhost:3000`.

Parar y remover contenedores:
```bash
docker compose down
```

Notas:
- El servicio `frontend` en `docker-compose.yml` usa una `VITE_API_BASE_URL` apuntando a `http://backend:8080` para que la build estática sepa la URL interna del backend en el entorno Docker.

---

## Ejecutar localmente (sin Docker)

Estas instrucciones sirven si prefieres ejecutar servicios por separado.

Backend (JDK 21 + Maven):
```bash
cd backend
./mvnw spring-boot:run
```

Frontend (Node.js + npm):
```bash
cd frontend
npm install
# para desarrollo
npm run dev
# para construir producción localmente
npm run build
```

Si ejecutas el frontend en modo `dev`, setea `VITE_API_BASE_URL` en `frontend/.env`:
```
VITE_API_BASE_URL=http://localhost:8080
```

---

## Tests

Backend (Maven / JUnit):
```bash
cd backend
./mvnw test
```

Frontend (Vitest):
```bash
cd frontend
npm install
npx vitest run
# para cobertura
npx vitest run --coverage
```

Se añadieron pruebas unitarias a `frontend/src/components/__tests__` y se configuró `src/setupTests.ts`.

---

## Endpoints principales

- POST /transactions
  - Crea una transacción.
  - Request example:

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
  - Devuelve todas las transacciones (ordenadas por creación, más recientes primero).

- GET /transactions?page={page}&size={size}
  - Devuelve una respuesta paginada con la estructura:

```json
{
  "content": [ /* array de transacciones */ ],
  "page": 0,
  "size": 5,
  "totalElements": 42,
  "totalPages": 9
}
```

---

## Ordenación de transacciones (nota importante)

Por diseño, las transacciones ahora se devuelven ordenadas por el orden de creación (más recientes primero). Esto se implementó ordenando por la columna `id` en la base de datos (`ORDER BY id DESC`) tanto en la consulta paginada como en el listado completo.

Motivo: la entidad actual no tenía un campo `createdAt`; usar `id` (auto-incremental) es una forma segura de representar el orden de creación sin cambios de esquema. Si prefieres un campo explícito `createdAt`, puedo añadirlo con `@PrePersist` y una migración SQL, y luego ordenar por `createdAt`.

---

## Verificar manualmente (ejemplos)

Obtener página 0, size 5 (PowerShell):
```powershell
Invoke-RestMethod -Uri 'http://localhost:8080/transactions?page=0&size=5' -Headers @{Accept='application/json'} | ConvertTo-Json -Depth 5
```

Obtener todos (curl on Linux/macOS):
```bash
curl -sS "http://localhost:8080/transactions" | jq .
```

Crear una transacción (curl):
```bash
curl -sS -X POST "http://localhost:8080/transactions" -H "Content-Type: application/json" -d '{"amount":100,"business":"Tienda","tenpistaName":"Ana","transactionDate":"2025-12-01T10:00:00"}' -i
```

---

## Buenas prácticas y consideraciones de entrega

- Para producción se recomienda:
  - Añadir migraciones gestionadas (Flyway / Liquibase).
  - Auditar y versionar la API (OpenAPI + versioning).
  - Añadir tests de integración (Testcontainers) para el flujo completo con Postgres.

- Para la evaluación, incluye en la descripción del PR/entrega:
  - Resumen de endpoints y reglas de negocio implementadas.
  - Cómo ejecutar con Docker (comandos que usarías localmente).
  - Cualquier decisión técnica relevante (por ejemplo: ordenación por `id` vs `createdAt`).

---

Si quieres que:
- haga el cambio para introducir `createdAt` en la entidad y la migración SQL,
- o que cree un pequeño `backend/README.md` con detalle técnico adicional,

dímelo y lo preparo.
