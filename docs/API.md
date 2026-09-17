# TeamSync API Documentation

This document describes the current TeamSync backend REST API.

Base URL for local development:

```text
http://localhost:8080
```

All endpoints require authentication unless marked as public.

## Authentication

TeamSync currently uses manager-based authentication. A manager logs in with email and password and receives a JWT token. The token must be sent in the `Authorization` header for protected requests.

```http
Authorization: Bearer <token>
```

Account creation is not publicly available yet. A registration flow is already planned and under development. The expected flow is to create the first `Team` and first `Manager` together, then return a JWT for the new manager.

### Login

```http
POST /api/auth/login
```

Public endpoint.

Request body:

```json
{
  "email": "manager@example.com",
  "password": "your-password"
}
```

Successful response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## Team

Team endpoints operate on the team that belongs to the authenticated manager.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/team` | Get the authenticated manager's team. |
| `PUT` | `/api/team/update` | Update the authenticated manager's team. |
| `DELETE` | `/api/team/delete` | Delete the authenticated manager's team. |

Example update payload:

```json
{
  "name": "TeamSync Racing",
  "acronym": "TSR",
  "phone": "+351900000000",
  "foundedYear": 2026,
  "license": "TEAM-2026-001",
  "location": "Viana do Castelo, Portugal",
  "description": "Cycling team focused on road and mountain bike racing."
}
```

## Manager

Manager endpoints return data for the authenticated manager account.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/manager` | Get the authenticated manager profile. |

## Athletes

Athlete endpoints are scoped to the authenticated manager's team.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/athletes` | List athletes from the authenticated team. |
| `GET` | `/api/athletes/{id}` | Get one athlete from the authenticated team. |
| `POST` | `/api/athletes` | Create an athlete in the authenticated team. |
| `PUT` | `/api/athletes/{id}` | Update an athlete in the authenticated team. |
| `DELETE` | `/api/athletes/{id}` | Delete an athlete from the authenticated team. |

Create or update payload:

```json
{
  "name": "Joana Silva",
  "birthDay": "2001-04-12",
  "email": "joana@example.com",
  "password": "StrongPassword123!",
  "phone": "+351912345678",
  "license": "PT-12345",
  "nationality": "Portugal"
}
```

## Staff

Staff endpoints are scoped to the authenticated manager's team.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/staff` | List staff from the authenticated team. |
| `GET` | `/api/staff/{id}` | Get one staff member. |
| `POST` | `/api/staff` | Create a staff member in the authenticated team. |
| `PUT` | `/api/staff/{id}` | Update a staff member in the authenticated team. |
| `DELETE` | `/api/staff/{id}` | Delete a staff member from the authenticated team. |

Create or update payload:

```json
{
  "name": "Carlos Mendes",
  "birthDay": "1988-09-20",
  "email": "carlos@example.com",
  "password": "StrongPassword123!",
  "phone": "+351913000000",
  "staffRoleId": "00000000-0000-0000-0000-000000000000",
  "addressId": "00000000-0000-0000-0000-000000000000"
}
```

## Staff Roles

Staff role endpoints are scoped to the authenticated manager's team.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/staffroles` | List staff roles from the authenticated team. |
| `POST` | `/api/staffroles` | Create a staff role. |
| `PUT` | `/api/staffroles/{id}` | Update a staff role. |
| `DELETE` | `/api/staffroles/{id}` | Delete a staff role. |

Create or update payload:

```json
{
  "name": "Coach",
  "description": "Responsible for training planning and race preparation."
}
```

## Sponsors

Sponsor endpoints are scoped to the authenticated manager's team.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/sponsors` | List sponsors from the authenticated team. |
| `GET` | `/api/sponsors/{id}` | Get one sponsor from the authenticated team. |
| `POST` | `/api/sponsors` | Create a sponsor for the authenticated team. |
| `PUT` | `/api/sponsors/{id}` | Update a sponsor from the authenticated team. |
| `DELETE` | `/api/sponsors/{id}` | Delete a sponsor from the authenticated team. |

Create or update payload:

```json
{
  "addressId": "00000000-0000-0000-0000-000000000000",
  "name": "ACME Bikes",
  "email": "partnerships@acme.test",
  "phone": "+351900000000",
  "website": "https://example.com",
  "sponsorType": "Main",
  "status": true,
  "startDate": "2026-01-01",
  "endDate": "2026-12-31"
}
```

## Competitions

Competition endpoints currently operate as global resources.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/competitions` | List competitions. |
| `GET` | `/api/competitions/{id}` | Get one competition. |
| `POST` | `/api/competitions` | Create a competition. |
| `PUT` | `/api/competitions/{id}` | Update a competition. |
| `DELETE` | `/api/competitions/{id}` | Delete a competition. |

Create or update payload:

```json
{
  "disciplineId": "00000000-0000-0000-0000-000000000000",
  "name": "Portuguese XCO Cup - Round 1",
  "location": "Viana do Castelo",
  "competitionDate": "2026-03-15"
}
```

## Results

Result endpoints are scoped to the authenticated manager's team.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/results` | List results for the authenticated team. |
| `GET` | `/api/results/{id}` | Get one result from the authenticated team. |
| `POST` | `/api/results` | Create a result for the authenticated team. |
| `PUT` | `/api/results/{id}` | Update a result from the authenticated team. |
| `DELETE` | `/api/results/{id}` | Delete a result from the authenticated team. |

Create or update payload:

```json
{
  "athleteId": "00000000-0000-0000-0000-000000000000",
  "competitionId": "00000000-0000-0000-0000-000000000000",
  "position": 3,
  "finishTime": "01:24:31",
  "points": 75.50,
  "dnf": false
}
```

## Disciplines

Discipline endpoints currently operate as global resources.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/disciplines` | List disciplines. |
| `GET` | `/api/disciplines/{id}` | Get one discipline. |
| `POST` | `/api/disciplines` | Create a discipline. |
| `PUT` | `/api/disciplines/{id}` | Update a discipline. |
| `DELETE` | `/api/disciplines/{id}` | Delete a discipline. |

Create or update payload:

```json
{
  "name": "XCO",
  "description": "Olympic cross-country mountain bike discipline."
}
```

## Addresses

Address endpoints currently operate as global resources.

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/api/addresses` | List addresses. |
| `GET` | `/api/addresses/{id}` | Get one address. |
| `POST` | `/api/addresses` | Create an address. |
| `PUT` | `/api/addresses/{id}` | Update an address. |
| `DELETE` | `/api/addresses/{id}` | Delete an address. |

Create or update payload:

```json
{
  "street": "Rua Example 123",
  "city": "Viana do Castelo",
  "district": "Viana do Castelo",
  "postalCode": "4900-000",
  "country": "Portugal"
}
```

## Current API Notes

- Only `/api/auth/**` is public.
- Every other endpoint requires a valid JWT token.
- Authentication currently supports manager accounts only.
- Team-owned resources derive the team from the authenticated manager, not from request bodies.
- Registration is planned and under development, but not available yet.
