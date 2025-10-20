# MELI Order Management System

A robust REST API solution for order management, developed as part of the "Spring and Spring Boot in Java for Web Applications" challenge by Digital NAO. This project addresses technical requirements for the fictional company MELI by implementing a flexible and scalable order management system.

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Project Architecture](#project-architecture)
- [API Robustness & Error Handling](#api-robustness--error-handling)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
    - [Development (Default)](#development-default)
    - [Production](#production)
- [API Documentation](#api-documentation)
- [Database Access](#database-access)
- [Testing with Postman](#testing-with-postman)
- [Roadmap](#roadmap)

## Overview

The MELI Order Management System is a Spring Boot application that provides a complete RESTful API for managing orders. Built with modern Java technologies and best practices, this system offers full CRUD operations with persistent data storage.

**Current Version:** Sprint 2 - Multi-Environment Configuration

## Tech Stack

- **Java 17** - Programming language
- **Spring Boot 3.x** - Application framework
- **Maven** - Dependency management and build tool
- **Spring Web** - RESTful web services
- **Spring Data JPA** - Data persistence layer
- **Spring Boot Validation** - Input validation and constraint enforcement
- **H2 Database** - In-memory database for development
- **PostgreSQL** - Relational database for production
- **YAML Configuration** - Environment profile management with `.yml` files
- **Lombok** - Code generation and boilerplate reduction

## Features

**Sprint 1 Completed:**

- ✅ Spring Boot project configuration with Maven
- ✅ Complete RESTful API with CRUD operations
- ✅ `Order` entity with JPA mapping
- ✅ H2 in-memory database integration
- ✅ Version control with Git and GitHub repository
- ✅ Startup scripts for quick deployment
- ✅ Three-layer architecture (Controller-Service-Repository)
- ✅ Input validation with Jakarta Bean Validation
- ✅ Global exception handling with standardized error responses

**Sprint 2 Completed:**

- ✅ Environment profile configuration (development and production)
- ✅ PostgreSQL database integration for production environment
- ✅ YAML-based configuration management
- ✅ Environment variable support for secure credential handling

## Project Architecture

The application follows a **three-layer architecture pattern** that separates concerns and promotes code maintainability:

```
┌─────────────────────────────────────────┐
│         Controller Layer                │
│  (Handles HTTP Requests/Responses)      │
│         OrderController                 │
└──────────────┬──────────────────────────┘
               │
               ↓
┌─────────────────────────────────────────┐
│          Service Layer                  │
│    (Business Logic & Validation)        │
│          OrderService                   │
└──────────────┬──────────────────────────┘
               │
               ↓
┌─────────────────────────────────────────┐
│        Repository Layer                 │
│      (Data Access & Persistence)        │
│        OrderRepository                  │
└─────────────────────────────────────────┘
```

### Benefits of This Architecture:

- **Separation of Concerns (SoC):** Each layer has a distinct responsibility:
  - **Controller Layer:** Manages HTTP requests and responses, delegates business operations to the service layer
  - **Service Layer:** Contains all business logic, orchestrates operations, and enforces business rules
  - **Repository Layer:** Handles data persistence and retrieval using Spring Data JPA

- **Better Maintainability:** Changes in one layer don't cascade to others, making the codebase easier to modify and extend

- **Enhanced Testability:** Business logic in the service layer can be unit tested independently without requiring HTTP context or database connections

- **Scalability:** New features can be added by extending the appropriate layer without affecting the entire application

## API Robustness & Error Handling

The API implements comprehensive validation and error handling mechanisms to ensure data integrity and provide clear feedback to clients:

### Input Validation

The application uses **Jakarta Bean Validation** (`jakarta.validation`) to automatically validate incoming request payloads:

- **`@Valid`** annotation triggers automatic validation on request bodies
- **`@NotBlank`** ensures string fields are not null or empty (e.g., `customerName`, `status`)
- **`@Positive`** validates that numeric fields contain positive values (e.g., `totalAmount`)

Invalid requests are automatically rejected with a `400 Bad Request` response before reaching the business logic layer.

### Global Exception Handling

A **`@RestControllerAdvice`** component (`GlobalExceptionHandler`) provides centralized exception handling across the entire application:

- **Catches validation errors** from `@Valid` annotations automatically
- **Returns structured JSON error responses** instead of generic HTML error pages
- **Provides field-level error details** to help clients understand exactly what went wrong
- **Prevents 500 Internal Server Errors** caused by validation issues

This approach ensures consistent, predictable error responses and improves the API's developer experience.

## Getting Started

### Prerequisites

Ensure you have the following installed on your system:

- **JDK 17** or higher
- **Git** for version control
- **Maven** (optional, wrapper included in project)
- **PostgreSQL** (only for production environment)

### Installation

1. Clone the repository:
```bash
git clone [Paste your GitHub repository URL here]
```

2. Navigate to the project directory:
```bash
cd meli-order-management
```

### Running the Application

The application supports two environment profiles: **development** (default) and **production**.

#### Development (Default)

The development profile uses an H2 in-memory database and is configured as the default profile. This is the simplest way to run the application for local development and testing.

**For Windows:**
```bash
start.bat
```

**For Linux/macOS:**
```bash
chmod +x start.sh
./start.sh
```

The application will start and be accessible at: **http://localhost:8080**

**H2 Console Access:**

You can access the H2 Console to view and query the database directly:

- **URL:** http://localhost:8080/h2-console
- **JDBC URL:** `jdbc:h2:mem:orderdb`
- **Username:** `sa`
- **Password:** `password`

**Note:** The H2 Console is only available when running in development mode.

#### Production

The production profile uses a PostgreSQL database and requires additional configuration through environment variables for secure credential management.

**Required Environment Variables:**

- `DB_URL` - PostgreSQL JDBC connection URL (e.g., `jdbc:postgresql://localhost:5432/ordersdb`)
- `DB_USER` - Database username
- `DB_PASSWORD` - Database password

**Example for Windows (CMD):**

```bash
set SPRING_PROFILES_ACTIVE=prod
set DB_URL=jdbc:postgresql://localhost:5432/ordersdb
set DB_USER=your_username
set DB_PASSWORD=your_password
start.bat
```

**Example for Linux/macOS:**

```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:postgresql://localhost:5432/ordersdb
export DB_USER=your_username
export DB_PASSWORD=your_password
./start.sh
```

**Note:** Ensure PostgreSQL is installed and running, and that the database specified in `DB_URL` exists before starting the application.

## API Documentation

### Endpoints

| HTTP Method | Endpoint | Description | Request Body |
|-------------|----------|-------------|--------------|
| `POST` | `/api/orders` | Create a new order | Required (see below) |
| `GET` | `/api/orders` | Retrieve all orders | None |
| `GET` | `/api/orders/{id}` | Retrieve a specific order by ID | None |
| `PUT` | `/api/orders/{id}` | Update an existing order | Required (see below) |
| `DELETE` | `/api/orders/{id}` | Delete an order by ID | None |

### Request Body Examples

**Create Order (POST /api/orders):**
```json
{
  "customerName": "Arturo Bandini",
  "status": "Pending",
  "totalAmount": 150.75
}
```

**Update Order (PUT /api/orders/{id}):**
```json
{
  "customerName": "Arturo Bandini",
  "status": "Shipped",
  "totalAmount": 155.00
}
```

### Response Examples

**Success Response (200 OK):**
```json
{
  "id": 1,
  "customerName": "Arturo Bandini",
  "status": "Pending",
  "totalAmount": 150.75,
  "createdAt": "2025-10-16T10:30:00"
}
```

**Get All Orders Response:**
```json
[
  {
    "id": 1,
    "customerName": "Arturo Bandini",
    "status": "Pending",
    "totalAmount": 150.75,
    "createdAt": "2025-10-16T10:30:00"
  },
  {
    "id": 2,
    "customerName": "Jane Doe",
    "status": "Shipped",
    "totalAmount": 200.00,
    "createdAt": "2025-10-16T11:45:00"
  }
]
```

**Error Response (400 Bad Request):**
```json
{
  "totalAmount": "Total amount cannot be null",
  "customerName": "Customer name cannot be blank",
  "status": "Status cannot be blank"
}
```

## Database Access

### Development Environment

The development environment uses an H2 in-memory database. You can access the H2 Console to view and query the database directly when running in development mode.

**H2 Console Access:**

- **URL:** http://localhost:8080/h2-console
- **JDBC URL:** `jdbc:h2:mem:orderdb`
- **Username:** `sa`
- **Password:** `password`

**Note:** The H2 Console is only available when the application is running with the development profile.

### Production Environment

The production environment uses PostgreSQL for persistent data storage. Connect to your PostgreSQL instance using your preferred database client with the credentials configured in your environment variables.

## Testing with Postman

A ready-to-use Postman collection is included in the project to facilitate API testing.

### Import Instructions

1. Open Postman
2. Click on **Import** button (top-left corner)
3. Select **File** tab
4. Navigate to the project root directory
5. Select `MELI Order Management API.postman_collection.json`
6. Click **Import**

The collection includes pre-configured requests for all available endpoints with sample data.

## Roadmap

### Sprint 3 (Planned)
- Swagger/OpenAPI documentation integration
- Comprehensive unit testing with JUnit
- Integration testing suite
- Enhanced error handling and validation
- Docker containerization
- CI/CD pipeline setup

---

**License:** This project is part of an educational challenge by Digital NAO.