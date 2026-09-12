# Spring Boot Task Management

A Java 21 / Spring Boot REST API for assigning tasks, managing their lifecycle, and exchanging comments and notifications. A practical backend learning project using JWT authentication, JPA persistence, and a layered architecture.

> **Work in progress.** Core API flows are implemented. Caching, schema initialization, and authorization have limitations documented below. This repository contains the backend only.

## Implemented functionality

- Email/password login, BCrypt password hashing, JWT validation, and database-backed token invalidation on logout.
- Administrator-only user creation, retrieval, updates, deletion, and searches by email, first name, last name, or role.
- Task creation with an assignee, retrieval, editable-field updates, and searches by creator, assignee, or current user's task status.
- Paginated task listing with configurable sorting.
- Action-based status transitions restricted to the creator, assignee, or administrator.
- Task comments and persisted notifications on task creation and comment submission.
- Listing the current user's notifications and owner-only read/delete operations.
- WebSocket/STOMP notification publishing, request validation, and a shared exception handler.

General task updates modify managed entities inside a transaction. Requests expose `title`, `description`, `priority`, `assignedToId`, and `dueDate`; status and ownership metadata are controlled separately by the server.

## Technology and architecture

| Area | Implementation |
|---|---|
| Runtime | Java 21, Spring Boot 4.1.1 |
| HTTP and security | Spring MVC, Spring Security, JJWT 0.13.0 |
| Persistence | Spring Data JPA / Hibernate, MariaDB |
| Messaging | WebSocket, STOMP, SockJS, Spring simple broker |
| Cache | Spring Cache and Redis; incomplete integration |
| Build | Maven, Maven Wrapper, Lombok |
| Tests | JUnit, Mockito, standalone MockMvc, Spring context test |
| Delivery | Dockerfile, Docker Compose, GitHub Actions |

```text
src/main/java/com/app/taskmanagement/
├── controller     HTTP endpoints
├── service        Business operations
├── repository     JPA repositories
├── model          Entities and enums
├── dto            Request/response DTOs and mappers
├── security       JWT filter and authenticated user support
├── config         Security, WebSocket, cache, initial administrator
├── exception      Exceptions and shared error handler
├── annotation     Execution-time annotation
└── aspect         Slow-method logging aspect
```

The source also includes a fetch-join query for priority lookup, SQL for an index on `(ASSIGNED_BY, status)`, and an execution-time aspect applied to login with a 500 ms threshold. These do not establish complete query optimization, migration coverage, or production monitoring.

## Task workflow

New tasks start in `CREATED`.

| Action | Allowed current status | Result |
|---|---|---|
| `START` | `CREATED` | `IN_PROGRESS` |
| `COMPLETE` | `IN_PROGRESS` | `DONE` |
| `CANCEL` | `CREATED`, `IN_PROGRESS` | `CANCELED` |
| `REOPEN` | `CANCELED` | `IN_PROGRESS` |

Invalid transitions are rejected. Only the creator, assignee, or an `ADMIN` can execute a status action. There is no transition out of `DONE`.

## Local setup

Prerequisites: Java 21, Maven 3.9+ or the included wrapper, MariaDB, and Redis. Compose uses MariaDB 10.11 and Redis 7.

```bash
git clone https://github.com/samaz74/spring-boot-task-management.git
cd spring-boot-task-management
```

Create a development database and give your database user access:

```sql
CREATE DATABASE taskManagement;
```

Set these environment variables in the terminal that runs the application. PowerShell example:

```powershell
$env:SPRING_PROFILES_ACTIVE = "dev"
$env:DB_USERNAME = "your_database_user"
$env:DB_PASSWORD = "your_database_password"
$env:JWT_SECRET = "replace-with-a-random-secret-at-least-32-ASCII-characters"
$env:SPRING_DATA_REDIS_HOST = "localhost"
$env:SPRING_DATA_REDIS_PORT = "6379"
```

Supply an actual random JWT secret of at least 32 bytes. The checked-in JWT default is too short for the HMAC key API used by `JwtUtil`. Token lifetime defaults to `1200000` milliseconds (20 minutes), configurable with `JWT_EXPIRATION`.

### Database initialization

The default and `dev` profiles use `ddl-auto=validate`, so an empty database alone is insufficient. The repository includes only `V2__add_task_assignee_status_index.sql`, which assumes the `task` table exists; there is no initial schema migration. The POM includes `flyway-mysql`, but a complete automatic migration setup is not established.

For a new, disposable development database, explicitly enable Hibernate schema update and disable Flyway for that run:

```powershell
$env:SPRING_JPA_HIBERNATE_DDL_AUTO = "update"
$env:SPRING_FLYWAY_ENABLED = "false"
.\mvnw.cmd spring-boot:run
```

This creates tables from entities; it does not apply the SQL index migration. On subsequent runs against the initialized schema, set `SPRING_JPA_HIBERNATE_DDL_AUTO=validate`. On macOS/Linux, export the same variables and use `./mvnw spring-boot:run`.

The API listens at `http://localhost:8080`. [DataInitializer.java](src/main/java/com/app/taskmanagement/config/DataInitializer.java) contains the initial development administrator login and creates that account if its email is absent. It runs without a profile restriction, so administrator provisioning needs attention before deployment.

### Docker Compose

Create an untracked `.env` beside `docker-compose.yml`:

```dotenv
DB_USERNAME=root
DB_PASSWORD=replace-with-a-local-database-password
JWT_SECRET=replace-with-a-random-secret-at-least-32-ASCII-characters
```

The database service initializes the root password and database only; it does not create a separate account for `DB_USERNAME`.

Compose defines `db`, `redis`, and `app`, but the application inherits Redis host `localhost`. Use an explicit hostname override for a development run:

```bash
docker compose up -d db redis
docker compose build app
docker compose run --rm --service-ports -e SPRING_DATA_REDIS_HOST=redis -e SPRING_FLYWAY_ENABLED=false app
```

Wait until MariaDB accepts connections before starting the application; the file has no readiness health checks. The `docker` profile uses `ddl-auto=update`. Ports are 8080 for the API, 3306 for MariaDB, and 6379 for Redis. Database data is stored in `db-data`. The Docker build skips tests.

These instructions describe development configuration, not a verified production deployment. Runtime limitations below still apply.

## REST API

Login is public. Other REST routes require `Authorization: Bearer <token>`. User administration additionally requires `ADMIN`. Authentication does not imply task ownership enforcement on every route.

| Method | Path | Operation |
|---|---|---|
| POST | `/api/auth/login` | Login with `email` and `password` |
| POST | `/api/auth/createUser` | Create user (`ADMIN`) |
| POST | `/api/auth/logout` | Invalidate current bearer token |
| GET | `/api/user/` | List users (`ADMIN`) |
| GET | `/api/user/userId/{userId}` | Find user by ID (`ADMIN`) |
| GET | `/api/user/email/{userEmail}` | Find user by email (`ADMIN`) |
| GET | `/api/user/search/email/{email}` | Search email (`ADMIN`) |
| GET | `/api/user/search/firstName/{firstName}` | Search first name (`ADMIN`) |
| GET | `/api/user/search/lastName/{lastName}` | Search last name (`ADMIN`) |
| GET | `/api/user/search/role/{role}` | Filter role (`ADMIN`) |
| PUT / DELETE | `/api/user/{userId}` | Update / delete user (`ADMIN`) |
| GET | `/api/task` | Paginated task list |
| POST | `/api/task` | Create task |
| GET / PUT | `/api/task/{taskId}` | Retrieve / update task |
| PATCH | `/api/task/update/status` | Execute status action |
| GET | `/api/task/search/assignedTo/{userId}` | Tasks assigned to user |
| GET | `/api/task/search/createdBy/{userId}` | Tasks created by user |
| GET | `/api/task/search/assignedToAndState/{state}` | Current assignee's tasks by status |
| GET | `/api/task/search/createdByAndState/{state}` | Current creator's tasks by status |
| GET | `/api/task/search/assignedToOrderedWithCratedBy/{userId}` | Assigned tasks ordered by creator descending |
| GET | `/api/comment/{taskId}` | Task comments |
| POST | `/api/comment` | Add comment with `taskId` and `content` |
| GET | `/api/notification/` | Current user's notifications |
| PATCH / DELETE | `/api/notification/{notificationId}` | Mark read / delete own notification |

The `CratedBy` spelling matches the existing route. Priority lookup exists in the service/repository but has no REST endpoint. There is no task deletion endpoint. User roles are `ADMIN`, `MANAGER`, and `USER`; user creation expects `fName`, `lName`, `email`, `password` (at least eight characters), and `role`.

### Request examples

Create a task with `POST /api/task` and `Content-Type: application/json`:

```json
{
  "title": "Review API validation",
  "description": "Check task request validation and error responses.",
  "priority": "HIGH",
  "assignedToId": 2,
  "dueDate": "2026-12-01T17:00:00"
}
```

Use an existing assignee ID. Priorities are `LOW`, `MEDIUM`, `HIGH`, and `CRITICAL`. Dates use `LocalDateTime` without a timezone offset. All five fields are required; title and description must be nonblank. The DTO does not enforce a future due date.

Change status with `PATCH /api/task/update/status`:

```json
{
  "taskId": 10,
  "taskActions": "START"
}
```

Pagination example: `GET /api/task?page=0&size=10&sort=id,desc`. Defaults are page 0, size 10, and descending ID. Other search routes return lists without pagination.

Handled application errors include 400 for validation/invalid operations/duplicates, 401 for bad credentials, 403 for custom access denial, and 404 for missing resources. Their response fields are `timestamp`, `status`, `message`, and `path`; not all security-layer failures use this handler.

## WebSocket notifications

Connect a SockJS/STOMP client to `http://localhost:8080/ws` and subscribe to `/topic/notifications/{userId}`. The configured broker is Spring's in-memory simple broker, with `/topic` destinations and `/app` application prefixes.

The handshake is public, all origin patterns are allowed, and no per-user subscription authorization is configured. A user ID in a topic name does not make it private.

## Tests and CI

```bash
./mvnw test
```

On Windows, use `.\mvnw.cmd test`. To run only the existing mocked service/controller suites:

```bash
./mvnw -Dtest=AuthServiceTest,TasksServiceTest,TaskControllerTest test
```

| Test class | Current scope |
|---|---|
| `AuthServiceTest` | Successful user creation |
| `TasksServiceTest` | Status transitions, invalid operation, unauthorized actor |
| `TaskControllerTest` | Standalone MockMvc retrieval, assignment lookup, validation, not-found response |
| `TaskmanagementApplicationTests` | Full application context loading |

Standalone MockMvc tests do not exercise the Spring Security filter chain. The context test uses application configuration and needs a compatible database/schema; there is no isolated test database profile. Test presence is not a claim that the current suite passes.

[GitHub Actions](.github/workflows/ci.yml) runs `mvn clean package` on pushes to `master` with Java 21 and MariaDB. It currently has no Redis service or explicit fresh-schema bootstrap override.

## Current limitations and unfinished integration

- **Redis caching:** task-by-ID lookup uses `@Cacheable` with a two-minute TTL. The configured default value serializer expects Java-serializable values, while `TaskResponse` does not implement `Serializable`. Task updates and status transitions have no cache eviction. Caching is not yet a completed, reliable feature.
- **Status notifications:** `NotificationService.updateTaskStatus` exists, but the status operation does not invoke it. Reassignment through a general task update also sends no assignment notification.
- **Authorization:** status changes and notification ownership have checks; general task reads/updates and comment access do not enforce task participation. `MANAGER` has no distinct management workflow.
- **Lazy loading:** task user relations are lazy and Open Session in View is disabled. Several read paths map user data without an explicit service transaction or fetch joins, so they can encounter lazy-loading errors.
- **Schema management:** the index SQL is not a complete migration history. Fresh database initialization needs the explicit development setup above.
- **API documentation:** Swagger paths are permitted by security configuration, but no OpenAPI/Swagger dependency is declared.

Project membership, RabbitMQ/asynchronous messaging, comprehensive integration tests, and production deployment hardening are not implemented. These are remaining work, not completed features.

## Author

[Peyman Azish](https://github.com/samaz74)
