<div align="center">

<img src="https://img.shields.io/badge/TeamSync-Backend-1a1a2e?style=for-the-badge&logo=spring&logoColor=white" alt="TeamSync"/>

# TeamSync Backend

**Professional cycling team management system built for real teams, built to scale.**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-26-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=flat-square&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)
[![Status](https://img.shields.io/badge/Status-In%20Development-orange?style=flat-square)]()

---

[Features](#features) · [Architecture](#architecture) · [Entities](#entities) · [Security](#security) · [API](#api-endpoints) · [Setup](#setup)

</div>

---

## What is TeamSync?

TeamSync is a REST API backend for managing cycling teams - athletes, staff, competitions, results, sponsors, and disciplines - with multi-team support and role-based access control. Built from a real need: most teams still manage everything in Excel or paper.

Designed to be the backbone of a full-stack management platform, with clean separation between teams, strict data isolation, and JWT-based authentication.

---

## Features

**Team Management**
- Full CRUD for team profile, disciplines, and organizational structure
- Multi-team architecture - one platform, isolated data per team
- Sponsor management with contract tracking and status

**Athlete & Staff Management**
- Athlete profiles with license, nationality, and discipline assignments
- Staff roles - custom per team (Coach, Mechanic, Physiotherapist, and more)
- Many-to-many discipline assignments for athletes competing in multiple categories

**Competition & Results** `[ In Development ]`
- Competition registry with discipline and location tracking
- Result recording per athlete per competition - position, time, points, DNF status
- Team performance overview across the season

**Authentication & Security**
- JWT-based stateless authentication
- Role-based access control - Manager, Staff, Athlete
- Complete data isolation per team - no cross-team data leakage
- BCrypt password hashing

---

## Architecture

```
TeamSync/
├── entity/          Entity classes mapped to PostgreSQL tables
├── repository/      Spring Data JPA repositories
├── service/         Business logic layer - all team isolation enforced here
├── controller/      REST controllers - HTTP layer only
├── dto/             Request and Response DTOs - password_hash never exposed
└── security/        JWT service, auth filter, security config, auth utils
```

**Request lifecycle:**

```
Client Request
      ↓
JwtAuthFilter        Extracts and validates Bearer token
      ↓
SecurityConfig       Checks role permissions for the endpoint
      ↓
Controller           Extracts team_id from authenticated principal via AuthUtils
      ↓
Service              Filters all queries by team_id - never returns cross-team data
      ↓
Repository           Spring Data JPA - executes filtered query against PostgreSQL
      ↓
Response             DTO returned - sensitive fields excluded
```

---

## Entities

**Team**
Central entity. Every piece of data in the system belongs to a team. Managers authenticate against their team.

| Field | Type | Description |
|---|---|---|
| team_id | UUID | Primary key |
| name | VARCHAR(200) | Full team name |
| acronym | VARCHAR(20) | Short identifier |
| license | VARCHAR(100) | Federation license number |
| founded_year | INT | Year the team was founded |
| location | VARCHAR(200) | Team base location |
| disciplines | Many-to-many | Disciplines the team competes in |

**Manager**
Authenticates as `ROLE_MANAGER`. Full access to team data.

| Field | Type | Description |
|---|---|---|
| manager_id | UUID | Primary key |
| team_id | UUID | Foreign key to Team |
| name | VARCHAR(150) | Full name |
| email | VARCHAR(100) | Login email (unique) |
| password_hash | VARCHAR(255) | BCrypt hash - never exposed in responses |
| birth_day | DATE | Date of birth |
| address_id | UUID | Foreign key to Address (lazy) |

**Athlete** `[ In Development ]`
Authenticates as `ROLE_ATHLETE`. Access limited to own data and results.

| Field | Type | Description |
|---|---|---|
| athlete_id | UUID | Primary key |
| team_id | UUID | Foreign key to Team |
| name | VARCHAR(150) | Full name |
| email | VARCHAR(100) | Login email (unique) |
| license | VARCHAR(100) | Federation license number |
| nationality | VARCHAR(100) | Default: Portugal |
| disciplines | Many-to-many | Disciplines the athlete competes in |

**Staff** `[ In Development ]`
Authenticates as `ROLE_STAFF`. Access to athletes and competitions, no financial data.

| Field | Type | Description |
|---|---|---|
| staff_id | UUID | Primary key |
| team_id | UUID | Foreign key to Team |
| staff_role_id | UUID | Foreign key to StaffRole |
| name | VARCHAR(150) | Full name |
| email | VARCHAR(100) | Login email (unique) |

**StaffRole** `[ In Development ]`
Custom roles per team - Coach, Mechanic, Physiotherapist, or any role the team defines.

**Discipline**
Global entity - XCO, XCC, Estrada, CRI, DHI. Shared across teams.

**Competition** `[ In Development ]`
Events with date, location, and discipline. Teams register athletes into competitions.

**Result** `[ In Development ]`
Links an athlete to a competition. Stores position, finish time, points, and DNF status. Unique per athlete per competition.

**Sponsor** `[ In Development ]`
Sponsor profiles with contract dates, type (Main, Official, Technical, Local), and status.

**Address**
Reusable address entity referenced by Manager, Staff, Athlete, and Sponsor.

---

## Security

**Authentication flow:**

```
POST /api/auth/login
      ↓
Credentials validated against database (BCrypt comparison)
      ↓
JWT token generated - contains email + team_id as claims
      ↓
Token returned to client
      ↓
Client sends token on every request: Authorization: Bearer <token>
      ↓
JwtAuthFilter validates signature and expiration on every request
      ↓
team_id extracted from token - never from request body
```

**Role-based access control:**

| Endpoint | MANAGER | STAFF | ATHLETE |
|---|---|---|---|
| `/api/auth/**` | Public | Public | Public |
| `/api/teams/**` | Own team only | Own team only | Own team only |
| `/api/managers/**` | Yes | No | No |
| `/api/sponsors/**` | Yes | No | No |
| `/api/staff-roles/**` | Yes | No | No |
| `/api/athletes/**` | Yes | Yes | Own only |
| `/api/staff/**` | Yes | Yes | No |
| `/api/competitions/**` | Yes | Yes | Yes |
| `/api/results/**` | Yes | Yes | Own only |

**Team isolation:**
Every service method filters by `team_id` extracted from the JWT token. It is impossible for an authenticated user to access or modify data from another team - the `team_id` is never trusted from the request itself.

---

## API Endpoints

### Auth
```
POST   /api/auth/login          Login - returns JWT token
```

### Teams
```
GET    /api/teams               Get authenticated manager's team
PUT    /api/teams               Update team profile
DELETE /api/teams               Delete team
```

### Managers
```
GET    /api/managers            List managers in team
GET    /api/managers/{id}       Get manager by id
POST   /api/managers            Create manager
PUT    /api/managers/{id}       Update manager
DELETE /api/managers/{id}       Delete manager
```

### Athletes `[ In Development ]`
```
GET    /api/athletes            List athletes in team
GET    /api/athletes/{id}       Get athlete by id
POST   /api/athletes            Create athlete
PUT    /api/athletes/{id}       Update athlete
DELETE /api/athletes/{id}       Delete athlete
```

### Staff `[ In Development ]`
```
GET    /api/staff               List staff in team
GET    /api/staff/{id}          Get staff member by id
POST   /api/staff               Create staff member
PUT    /api/staff/{id}          Update staff member
DELETE /api/staff/{id}          Delete staff member
```

### Competitions `[ In Development ]`
```
GET    /api/competitions        List competitions
GET    /api/competitions/{id}   Get competition by id
POST   /api/competitions        Create competition
PUT    /api/competitions/{id}   Update competition
DELETE /api/competitions/{id}   Delete competition
```

### Results `[ In Development ]`
```
GET    /api/results/athlete/{athleteId}       Results by athlete
GET    /api/results/competition/{competitionId} Results by competition
POST   /api/results             Record result
PUT    /api/results/{id}        Update result
DELETE /api/results/{id}        Delete result
```

### Sponsors `[ In Development ]`
```
GET    /api/sponsors            List sponsors in team
GET    /api/sponsors/{id}       Get sponsor by id
POST   /api/sponsors            Create sponsor
PUT    /api/sponsors/{id}       Update sponsor
DELETE /api/sponsors/{id}       Delete sponsor
```

### Disciplines
```
GET    /api/disciplines         List all disciplines
GET    /api/disciplines/{id}    Get discipline by id
```

---

## Setup

**Requirements:**
- Java 21+
- PostgreSQL 16+
- Maven 3.9+

**1. Clone the repository**
```bash
git clone https://github.com/0x194/TeamSync-Backend.git
cd TeamSync-Backend
```

**2. Create the database**
```sql
CREATE DATABASE teamsync;
```

**3. Run the schema**
```bash
psql -U postgres -d teamsync -f schema.sql
```

**4. Seed with sample data (optional)**
```bash
psql -U postgres -d teamsync -f seed.sql
```

**5. Configure application.properties**
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Edit `application.properties` with your database credentials and generate a JWT secret:
```bash
openssl rand -base64 64
```

**6. Run**
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

**Testing the login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "jferreira@efapel.pt", "password": "Admin1234!"}'
```

---

## Stack

| Layer | Technology |
|---|---|
| Language | Java 26 |
| Framework | Spring Boot 4.1.1 |
| Security | Spring Security + JWT (jjwt 0.12.6) |
| Persistence | Spring Data JPA + Hibernate 7 |
| Database | PostgreSQL 16 |
| Connection Pool | HikariCP |
| Build | Maven |
| Utilities | Lombok, Bean Validation |

---

## Frontend

The frontend for TeamSync is being developed separately at [TeamSync-Frontend](https://github.com/0x194/TeamSync-Frontend). `[ In Development ]`

---

<div align="center">

Built by [Afonso Oliveira](https://github.com/0x194) - Computer Engineering student at IPVC

</div>