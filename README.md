# Garage Booking API

A backend REST API for managing garages, services, bookings, and users with role-based access control. This project is designed as a real-world, production-style Spring Boot application suitable for learning, interviews, and extension into a full system.

---

## 1. Setup Instructions

### 1.1 Prerequisites

Before running the project, make sure you have the following installed:

* **Java 17** (mandatory – project uses modern Spring Boot)
* **Maven 3.8+**
* **Docker & Docker Compose** (for PostgreSQL and containerized runs)
* **Git**
* **IDE**: IntelliJ IDEA / VS Code (with Java & Spring extensions)

If Java or Maven versions mismatch, the app *will fail*. Don’t ignore this.

---

### 1.2 Clone the Repository

```bash
git clone <your-repo-url>
cd garage-booking-api
```

---

### 1.3 Database Setup (PostgreSQL via Docker)

The project expects PostgreSQL. The easiest way is Docker.

```bash
docker-compose up -d
```

This will start:

* PostgreSQL database
* Exposed on port `5432`

Default credentials (as per `application.yml`):

* DB Name: `garage_db`
* Username: `postgres`
* Password: `postgres`

If Docker is not running, **the app will crash at startup**.

---

### 1.4 Application Configuration

Check `src/main/resources/application.yml`:

Key properties:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/garage_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
```

Important notes:

* `ddl-auto: validate` means **schema must already exist**
* If entity & DB mismatch → startup failure (intentional, for safety)

---

### 1.5 Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

If tests fail, **do not skip them blindly**. Tests are part of the design.

Application runs on:

```
http://localhost:8080
```

---

### 1.6 Running Tests

```bash
mvn test
```

Tests include:

* Controller integration tests
* Security & role validation
* HTTP status verification

Failing tests indicate **logic or authorization errors**, not cosmetic issues.

---

## 2. Tech Stack Explanation

### 2.1 Backend Framework

**Spring Boot 3.x**

Why:

* Industry standard for Java backend
* Opinionated configuration (less boilerplate)
* Built-in support for REST, security, JPA, validation

Tradeoff:

* Heavier than Node.js
* Slower startup compared to lightweight frameworks

---

### 2.2 Language

**Java 17**

Why:

* LTS version
* Records, better performance, modern APIs
* Required by Spring Boot 3

Tradeoff:

* Verbose compared to Kotlin
* Steeper learning curve for beginners

---

### 2.3 Persistence Layer

**Spring Data JPA + Hibernate**

Why:

* Object–Relational Mapping
* Eliminates manual SQL for most cases
* Clean repository abstraction

Tradeoff:

* Hidden queries if misused
* Requires understanding of entity lifecycle

---

### 2.4 Database

**PostgreSQL**

Why:

* Production-grade relational database
* Strong consistency & constraints
* Excellent support for transactions

Tradeoff:

* More setup than H2
* Requires schema discipline

---

### 2.5 Security

**Spring Security + JWT**

Features:

* Authentication via JWT
* Role-based authorization
* Custom `UserPrincipal`

Roles:

* `CUSTOMER`
* `GARAGE_OWNER`
* `ADMIN`

Tradeoff:

* Complex configuration
* Easy to misconfigure if not careful

---

### 2.6 Testing

**JUnit 5 + MockMvc**

Why:

* Integration-level confidence
* Verifies real HTTP behavior
* Prevents silent security bugs

Tradeoff:

* Slower than unit tests
* Requires proper test data setup

---

### 2.7 Build Tool

**Maven**

Why:

* Widely used in Java ecosystem
* Clear dependency management

Tradeoff:

* Verbose XML
* Slower than Gradle for large builds

---

## 3. Assumptions & Tradeoffs

### 3.1 Assumptions

1. **Single Garage Ownership**

   * A garage belongs to one garage owner
   * Ownership checks are enforced at service/controller level

2. **Role-Based Access Is Mandatory**

   * Customers cannot add services
   * Garage owners cannot manage other garages
   * Admin logic is minimal by design

3. **Strict Status Codes Matter**

   * `403` for forbidden actions
   * `404` for missing resources
   * `500` only for real server faults

4. **Schema Is Source of Truth**

   * Entities must match DB schema exactly
   * Hibernate will not auto-fix mistakes

---

### 3.2 Tradeoffs Made (Intentionally)

#### a) Validation Location

Validation is partly in:

* Controller (role checks)
* Service layer (business rules)

Tradeoff:

* Slight duplication
* Clearer intent & testability

---

#### b) No DTO Mapper Library

Manual mapping instead of MapStruct

Why:

* Explicit control
* Easier debugging

Tradeoff:

* More boilerplate
* Slower to scale for very large DTOs

---

#### c) Integration Tests Over Pure Unit Tests

Why:

* Catch real bugs (auth, filters, serialization)
* Reflect production behavior

Tradeoff:

* Slower test execution
* More setup required

---

#### d) No Soft Deletes

Why:

* Simplicity
* Avoid hidden logic

Tradeoff:

* No historical recovery
* Hard deletes only

---

## 4. What This Project Demonstrates

* Clean layered architecture (Controller → Service → Repository)
* Real authorization bugs caught by tests
* Production-style error handling
* REST API best practices
* Practical Spring Security usage

This is **not a toy project**. The strictness is intentional.

---

## 5. Possible Future Improvements

* Admin dashboards
* Pagination & filtering
* Global exception handler (`@ControllerAdvice`)
* OpenAPI / Swagger docs
* Caching (Redis)
* CI/CD pipeline

