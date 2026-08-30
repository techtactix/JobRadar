#  JobRadar — RESTful Job Portal API

[![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x%20%2F%203.x-brightgreen.svg?style=flat&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-blue.svg?style=flat&logo=springsecurity)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue.svg?style=flat&logo=postgresql)](https://www.postgresql.org/)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](#)

**JobRadar** is a high-performance, secure backend REST API designed for managing job postings, recruitment workflows, and candidate searches. Built using **Spring Boot**, **Spring Security**, and **PostgreSQL**, JobRadar implements a stateless **JSON Web Token (JWT)** authentication architecture with granular **Role-Based Access Control (RBAC)**.

---

##  Key Features

*  **Stateless JWT Authentication**: Secure, token-based sessionless authentication using JJWT (`HMAC-SHA256`).
*  **Role-Based Access Control (RBAC)**: Fine-grained permissions separating standard **`USER`** capabilities (reading and keyword searching) from **`ADMIN`** capabilities (creating, updating, deleting jobs, and data loading).
*  **Password Security & Mass-Assignment Protection**: 12-round `BCryptPasswordEncoder` hashing and backend-enforced default roles to prevent privilege escalation.
*  **Job Management CRUD**: Endpoints for adding, updating, retrieving, and deleting job posts.
*  **Keyword Search**: Dynamic job filtering based on tech stack and profile keywords.
*  **Data Seeding**: Built-in endpoint for loading sample job postings.

---

##  Architecture & JWT Authentication Workflow

JobRadar uses a stateless security architecture where every protected request is validated via a custom security filter before reaching controller endpoints.

```
                     ┌─────────────────────────────────────────┐
                     │          Client (Postman / Frontend)    │
                     └────────────────────┬────────────────────┘
                                          │
                  ┌───────────────────────┴───────────────────────┐
                  │                                               │
           1. POST /register                               2. POST /login
                  │                                               │
                  ▼                                               ▼
         [UserService.saveUser]                       [AuthenticationManager]
      • Hashes password with BCrypt                   • Verifies BCrypt hash
      • Enforces USER role                            • Returns signed JWT
      • Saves to PostgreSQL                           (Subject: user, Claim: role)
                  │                                               │
                  └───────────────────────┬───────────────────────┘
                                          │
                               3. Protected Request
                     (Header: Authorization: Bearer <JWT>)
                                          │
                                          ▼
                                     [JwtFilter]
                         • Extracts token from header
                         • Validates token signature & expiration
                         • Loads UserPrincipal & authorities
                         • Sets SecurityContextHolder Authentication
                                          │
                                          ▼
                              [SecurityFilterChain]
                         • Validates endpoint RBAC rules
                         • Checks hasRole("ADMIN") / hasAnyRole(...)
                                          │
                                          ▼
                                 [JobRestController]
                               • Returns requested resource
```

### How JWT Authentication is Implemented:

1. **Token Generation (`JwtService`)**:
   Upon successful login via `AuthenticationManager`, `JwtService.getToken(username, role)` creates a digitally signed JWT token (valid for 12 hours) containing the subject (`username`) and claims (`role`).
2. **Request Interception (`JwtFilter`)**:
   A custom `OncePerRequestFilter` intercepts incoming HTTP requests, extracts the `Bearer <token>` from the `Authorization` header, parses the username/claims, and verifies the token's validity and expiration.
3. **Security Context Population**:
   Upon successful token validation, a `UsernamePasswordAuthenticationToken` with the user's granted authorities (`ROLE_USER` or `ROLE_ADMIN`) is placed in Spring's `SecurityContextHolder`.
4. **Endpoint Authorization (`SecurityConfig`)**:
   Spring Security checks the authenticated user's role against method-level and URL-level rules:
   * Public: `/register`, `/login`, `/error`
   * Read operations: Allowed for both `USER` and `ADMIN`
   * Write operations (POST, PUT, DELETE): Restricted strictly to `ADMIN`

---

## 📡 API Reference

**Base URL**: `http://localhost:8081`

###  Authentication Endpoints

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/register` | Public | Register a new user (defaults to `USER` role) |
| `POST` | `/login` | Public | Authenticate credentials and receive a JWT token |

#### Register Payload:
```json
POST /register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "mypassword123"
}
```

#### Login Payload:
```json
POST /login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "mypassword123"
}
```
**Response**: Returns the raw JWT string: `eyJhbGciOiJIUzI1NiJ9...`

---

###  Job Post Endpoints

> **Note**: All job endpoints require the header:  
> `Authorization: Bearer <your_jwt_token>`

| Method | Endpoint | Required Role | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/jobPosts` | `USER`, `ADMIN` | Fetch all available job postings |
| `GET` | `/jobPost/{postId}` | `USER`, `ADMIN` | Get specific job post details by ID |
| `GET` | `/jobPost/keyword/{keyword}` | `USER`, `ADMIN` | Search jobs matching profile or description keywords |
| `POST` | `/jobPost` | `ADMIN` | Create a new job posting |
| `PUT` | `/jobPost` | `ADMIN` | Update an existing job posting |
| `DELETE` | `/jobPost/{postId}` | `ADMIN` | Delete a job posting by ID |
| `GET` | `/load` | `ADMIN` | Seed predefined job posts into the database |

#### Create / Update Job Post Payload:
```json
POST /jobPost
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: application/json

{
  "postId": 101,
  "postProfile": "Senior Java Developer",
  "postDesc": "Looking for a seasoned backend engineer proficient in Spring Boot & Microservices.",
  "reqExperience": 5,
  "postTechStack": ["Java", "Spring Boot", "PostgreSQL", "Docker", "AWS"]
}
```

---

##  Tech Stack

* **Language**: Java 21
* **Framework**: Spring Boot 4.x / 3.x
  * Spring Security
  * Spring Data JPA
  * Spring Web
* **Database**: PostgreSQL
* **Security & Tokens**:
  * JJWT (`jjwt-api`, `jjwt-impl`, `jjwt-jackson` `0.11.5`)
  * `BCryptPasswordEncoder` (Strength: 12)
* **Libraries & Tools**:
  * Lombok
  * Apache Maven

---

##  Getting Started & Local Setup

### 1. Prerequisites
* **Java Development Kit (JDK) 21** or later
* **PostgreSQL** running locally
* **Maven** (or use the included `./mvnw` wrapper)

### 2. Database Configuration
Create a database named `techtactix` in PostgreSQL:
```sql
CREATE DATABASE techtactix;
```

Update your database credentials in `src/main/resources/application.properties`:
```properties
spring.application.name=SpringRest
spring.datasource.url=jdbc:postgresql://localhost:5432/techtactix
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8081
```

### 3. Run the Application
Run using the Maven wrapper:
```bash
# Windows
./mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

### 4. Run Automated Tests
```bash
./mvnw test
```

---

##  Testing with Postman

1. **Register**: Send `POST http://localhost:8081/register` with JSON body `{"username": "testuser", "password": "password123"}`.
2. **Login**: Send `POST http://localhost:8081/login` with the same credentials to receive your JWT token.
3. **Authorize**: In subsequent requests (e.g. `GET http://localhost:8081/jobPosts`), go to the **Authorization** tab, select **Bearer Token**, and paste your token.

---

## 📄 License
This project is open source and available under the [MIT License](LICENSE).
