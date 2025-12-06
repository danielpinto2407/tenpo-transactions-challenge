# 📘 Tenpo Transactions – FullStack Challenge 2025

Aplicación FullStack desarrollada para el Challenge Externo 2025.
Permite registrar y visualizar transacciones de un Tenpista mediante un backend en Spring Boot, frontend en React y base de datos PostgreSQL, todo orquestado con Docker.

## 🚀 Tecnologías utilizadas
Backend
Java 21
Spring Boot 4.0.0
Spring Web
Spring Data JPA
Spring Validation
PostgreSQL
SpringDoc OpenAPI (Swagger)

Frontend
React 18
Vite
TypeScript
Axios
React Hook Form + Yup
TailwindCSS (o Material UI opcional)
Infraestructura
Docker
Docker Compose

## 🏛️ Arquitectura

Backend – Clean Architecture ligera
Controller → Service → Repository → Database


Controller: Maneja requests y validaciones.

Service: Lógica de negocio (reglas de montos, fechas, etc).

Repository: Acceso a datos mediante JPA.

DTOs: Separan la API de las entidades internas.

GlobalExceptionHandler: Manejo uniforme de errores.

Swagger: Documentación automática.

## 🗂️ Estructura de carpetas
Backend
/backend
 ├── src/main/java/com/tenpo/transactions
 │     ├── controller
 │     ├── dto
 │     ├── service
 │     │     └── impl
 │     ├── repository
 │     ├── entity
 │     ├── exception
 │     └── mapper
 ├── src/main/resources
 │     └── application.yml
 ├── Dockerfile
 └── pom.xml

Frontend
/frontend
 ├── src
 │    ├── api
 │    ├── components
 │    ├── hooks
 │    ├── pages
 │    ├── types
 │    └── App.tsx
 ├── public
 ├── Dockerfile
 └── vite.config.js

## 🔧 Base de Datos

Tabla transaction:

Campo	Tipo	Descripción
id	SERIAL PK	Identificador
amount	int	Monto (no negativo)
business	varchar	Giro o comercio
tenpista_name	varchar	Nombre del Tenpista
transaction_date	timestamp	Fecha de transacción (<= ahora)

## 🧪 Reglas de Negocio

El monto no puede ser negativo.
La fecha no puede ser mayor a la actual.
Validado en frontend y backend.

## 📡 Endpoints

GET /api/v1/transactions
Obtiene todas las transacciones.

POST /api/v1/transactions
Crea una transacción.

Body ejemplo:

{
  "amount": 15000,
  "business": "Supermercado",
  "tenpistaName": "Juan Pérez",
  "transactionDate": "2025-01-20T14:00:00"
}

## 🐳 Docker & Ejecución
1. Clonar repositorio
git clone https://github.com/tu-repo.git
cd tu-repo

2. Ejecutar con Docker Compose
docker-compose up --build

Servicios disponibles:

Servicio	URL
Backend	http://localhost:8080

Swagger	http://localhost:8080/swagger-ui.html

Frontend	http://localhost:5173

PostgreSQL	localhost:5432

## 🧩 Ejecutar backend sin Docker (opcional)

Requiere Java 21 y PostgreSQL activo.

cd backend
mvn spring-boot:run

## 🧩 Ejecutar frontend sin Docker (opcional)

cd frontend
npm install
npm run dev

## 📄 Swagger / API Docs

Al levantar el backend:

👉 http://localhost:8080/swagger-ui.html

## 🔥 Escalabilidad

Este proyecto está diseñado para escalar fácilmente:

Capas desacopladas (Controller/Service/Repository)

DTOs para independencia de modelos

Documentación automática OpenAPI

Contenedores independientes para backend, frontend y BD

Preparado para despliegue en Docker, ECS, Kubernetes o similar

## 👤 Autor

Wilson Daniel Pinto Rios
FullStack Developer

## 🏁 Estado

✔ Desafío completado según requerimientos técnicos y funcionales.