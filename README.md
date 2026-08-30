# Spring Boot Task Management System

A task management REST API built with Java 21 and Spring Boot. The project is being developed as a practical learning project for Java/Spring backend development, code review, testing, security, database design, and performance optimization.

> **Status:** Work in Progress — features and architecture are being improved incrementally as part of a backend learning roadmap.

---

## Overview

This project implements a task management backend with:

- JWT authentication
- Role-based access control
- User management
- Task creation and assignment
- Controlled task-status transitions
- Comments
- Notifications
- Real-time notifications using WebSocket and STOMP
- Docker support
- Continuous integration with GitHub Actions

The project follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

DTOs are used as API contracts, and mapper components convert between DTOs and entities.

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot |
| Web | Spring MVC |
| Security | Spring Security + JWT |
| Database | MariaDB |
| Persistence | Spring Data JPA / Hibernate |
| Validation | Jakarta Bean Validation |
| Real-time messaging | WebSocket + STOMP |
| Build tool | Maven |
| Utilities | Lombok |
| Containerization | Docker + Docker Compose |
| CI | GitHub Actions |
| Testing | JUnit 5 + Mockito |

---

## Project Structure

```text
com.app.taskmanagement
├── config
│   ├── DataInitializer
│   ├── SecurityConfig
│   └── WebSocketConfig
├── controller
│   ├── AuthController
│   ├── CommentController
│   ├── NotificationController
│   ├── TaskController
│   └── UserController
├── dto
│   └── mapper
├── exception
├── model
│   └── enums
├── repository
├── security
└── service
```

---

## Features

### Authentication

- Login using email and password
- JWT generation after successful authentication
- Stateless request authentication
- Logout using token invalidation
- Rejection of invalidated tokens
- Password hashing using BCrypt
- Default administrator initialization for local development

### User Management

- Create users
- Retrieve users
- Search users by email, name, or role
- Update users
- Delete users
- Global roles:
    - `ADMIN`
    - `MANAGER`
    - `USER`

### Task Management

- Create tasks
- Retrieve tasks by ID
- Update editable task information
- Assign tasks to users
- Search tasks by creator
- Search tasks by assigned user
- Filter tasks by status or priority
- Preserve server-owned fields during updates
- Update managed JPA entities using transaction-based dirty checking

A new task always starts with the `CREATED` status. The client cannot choose an arbitrary initial status.

### Task Workflow

Task status changes are action-based. The client sends an action, and the backend determines whether the transition is valid.

```text
CREATED --START--> IN_PROGRESS
IN_PROGRESS --COMPLETE--> DONE
CREATED / IN_PROGRESS --CANCEL--> CANCELED
CANCELED --REOPEN--> IN_PROGRESS
```

Supported actions:

- `START`
- `COMPLETE`
- `REOPEN`
- `CANCEL`

Invalid transitions are rejected. For example, `COMPLETE` can only be executed when a task is currently `IN_PROGRESS`.

### Comments

- Add a comment to a task
- Retrieve comments for a task
- Store the comment author and creation time
- Notify the other participant after a comment is added

### Notifications

- Notify a user when a task is assigned
- Notify participants when a task status changes
- Notify participants when a comment is added
- Retrieve user notifications
- Mark notifications as read
- Delete notifications
- Push notifications through WebSocket/STOMP

---

## Task Update Design

Task creation and general task updates use a request DTO containing only client-editable fields:

```text
title
description
priority
assignedToId
dueDate
```

Server-owned fields are not controlled by the client:

```text
id
status
createdBy
createdAt
updatedAt
```

During an update, the existing task is loaded from the database and modified inside a transaction. Hibernate dirty checking persists the changes when the transaction completes.

This prevents server-owned information such as the original creator and creation date from being overwritten.

---

## Security

The API uses stateless JWT authentication.

Authenticated requests must include:

```http
Authorization: Bearer <token>
```

The JWT filter:

1. Reads the bearer token.
2. Checks whether it has been invalidated.
3. Validates the token signature and expiration.
4. Loads the authenticated user.
5. places the authentication in the Spring Security context.

Method-level authorization is used for administrative operations.

> Security and authorization rules are still being reviewed and improved as part of the project roadmap.

---

## API Endpoints

### Authentication — `/api/auth`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/auth/login` | Public | Authenticate and receive a JWT |
| POST | `/api/auth/createUser` | ADMIN | Create a user |
| POST | `/api/auth/logout` | Authenticated | Invalidate the current token |

### Users — `/api/user`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/user/userId/{userId}` | ADMIN | Get a user by ID |
| GET | `/api/user/email/{email}` | ADMIN | Get a user by email |
| GET | `/api/user/` | ADMIN | Get all users |
| GET | `/api/user/search/email/{email}` | ADMIN | Search users by email |
| GET | `/api/user/search/firstName/{name}` | ADMIN | Search users by first name |
| GET | `/api/user/search/lastName/{name}` | ADMIN | Search users by last name |
| GET | `/api/user/search/role/{role}` | ADMIN | Filter users by role |
| PUT | `/api/user/{userId}` | ADMIN | Update a user |
| DELETE | `/api/user/{userId}` | ADMIN | Delete a user |

### Tasks — `/api/task`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/task/{taskId}` | Authenticated | Get a task by ID |
| POST | `/api/task` | Authenticated | Create a task |
| PUT | `/api/task/{taskId}` | Authenticated | Update editable task information |
| PATCH | `/api/task/update/status` | Authenticated | Execute a task-status transition |
| GET | `/api/task/search/assignedTo/{userId}` | Authenticated | Get tasks assigned to a user |
| GET | `/api/task/search/createdBy/{userId}` | Authenticated | Get tasks created by a user |
| GET | `/api/task/search/assignedToAndState/{state}` | Authenticated | Get the current user's assigned tasks by status |
| GET | `/api/task/search/createdByAndState/{state}` | Authenticated | Get the current user's created tasks by status |

### Comments — `/api/comment`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/comment/{taskId}` | Authenticated | Get comments for a task |
| POST | `/api/comment` | Authenticated | Add a comment to a task |

### Notifications — `/api/notification`

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/notification/` | Authenticated | Get the authenticated user's notifications |
| PATCH | `/api/notification/{notificationId}` | Authenticated | Mark a notification as read |
| DELETE | `/api/notification/{notificationId}` | Authenticated | Delete a notification |

---

## Task API Examples

### Create a Task

```http
POST /api/task
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "title": "Implement task workflow",
  "description": "Add controlled status transitions",
  "priority": "HIGH",
  "assignedToId": 2,
  "dueDate": "2026-09-10T18:00:00"
}
```

The backend creates the task with:

```text
status = CREATED
```

### Update a Task

```http
PUT /api/task/10
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "title": "Implement and test task workflow",
  "description": "Add controlled status transitions and tests",
  "priority": "CRITICAL",
  "assignedToId": 2,
  "dueDate": "2026-09-12T18:00:00"
}
```

The general update endpoint does not change the task status.

### Execute a Status Transition

```http
PATCH /api/task/update/status
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "taskId": 10,
  "taskActions": "START"
}
```

A successful `START` action changes the task from:

```text
CREATED → IN_PROGRESS
```

An action that is not valid for the current status is rejected.

---

## Error Handling

The project uses a global exception handler and custom exceptions.

| Condition | HTTP status |
|---|---:|
| Invalid request or workflow transition | 400 Bad Request |
| Invalid credentials | 401 Unauthorized |
| Authenticated user without permission | 403 Forbidden |
| Resource not found | 404 Not Found |

Error responses contain:

```json
{
  "timestamp": "2026-08-30T10:00:00",
  "status": 400,
  "message": "Invalid operation",
  "path": "uri=/api/task/update/status"
}
```

---

## WebSocket

The WebSocket/STOMP endpoint is:

```text
ws://localhost:8080/ws/websocket
```

Clients can subscribe to:

```text
/topic/notifications/{userId}
```

The application currently uses Spring's simple in-memory message broker.

---

## Running the Project

### Prerequisites

- Java 21
- Maven 3.9+
- MariaDB
- Docker and Docker Compose, if using containers

### Run Locally

Create the database:

```sql
CREATE DATABASE taskManagement;
```

Configure the required environment or application properties:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/taskManagement
spring.datasource.username=your_username
spring.datasource.password=your_password

jwt.secret=your_secret_key_with_sufficient_length
jwt.expiration=1200000
```

Run the application:

```bash
mvn spring-boot:run
```

### Run with Docker

Build and start the services:

```bash
docker compose up --build
```

The API will be available at:

```text
http://localhost:8080
```

---

## Testing

Run the tests with:

```bash
mvn test
```

The current test suite is being expanded. Planned test coverage includes:

- Authentication
- Task creation
- Dirty-checking-based task updates
- Valid status transitions
- Invalid status transitions
- Authorization
- Repository queries
- Controller and integration tests

---

## Current Learning Roadmap

The project is being improved incrementally in the following order:

1. JPA entity lifecycle and dirty checking
2. Transactions
3. SQL, indexes, and query optimization
4. N+1 query detection and resolution
5. Spring AOP
6. Unit and integration testing
7. API design
8. Security and authorization
9. Redis
10. Asynchronous processing and RabbitMQ
11. Java concurrency
12. Basic system design
13. Logging and monitoring

Technologies are added only when the project has a real use case for them.

The following topics are intentionally postponed:

- Kubernetes
- Kafka
- CQRS
- Deep DDD
- Spring Batch
- Complex microservice architecture
- Workflow engines

---

## Planned Improvements

- Add unit tests for task transitions
- Add integration tests for secured endpoints
- Improve project-level authorization
- Add project and project-membership concepts
- Add pagination and sorting
- Review database indexes
- Detect and fix N+1 queries
- Improve secret management
- Add database migrations
- Improve logging and monitoring
- Standardize API responses and validation errors

---

## Development Principles

This project follows several learning and design principles:

- Understand a concept before introducing a new technology.
- Prefer simple solutions until complexity is justified.
- Keep business rules in the backend.
- Do not trust client-controlled ownership or status fields.
- Use tests to protect important behavior.
- Refactor only after understanding the current design.
- Every technology used in the project should be explainable in a technical interview.

---

## Author

**Peyman Azish**

Java Backend Developer

- GitHub: [github.com/samaz74](https://github.com/samaz74)
- LinkedIn: [linkedin.com/in/peyman-azish](https://www.linkedin.com/in/peyman-azish)