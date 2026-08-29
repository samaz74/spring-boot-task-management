# Spring Boot Task Management System

A production-oriented task management REST API built with **Java 21** and **Spring Boot 4.1.1**, featuring JWT authentication, role-based access control, real-time notifications via WebSocket, and a complete task workflow.

> **Status:** Work in Progress (WIP) — actively being developed as part of a learning roadmap toward enterprise-level Java backend development.

---

## Overview

This project was built to practice and implement modern backend development concepts including:

- Secure JWT authentication with token blacklisting
- Role-based access control (ADMIN / MANAGER / USER)
- Real-time notifications using WebSocket + STOMP
- Clean layered architecture (Controller → Service → Repository)
- Builder pattern in DTOs
- Component-based Mapper layer
- Containerization with Docker

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1.1 |
| Security | Spring Security + JWT (jjwt 0.13.0) |
| Database | MariaDB |
| ORM | Spring Data JPA / Hibernate |
| Real-time | Spring WebSocket + STOMP |
| Build Tool | Maven |
| Utilities | Lombok |
| Containerization | Docker + Docker Compose |

---

## Architecture

```
Controller Layer
      ↓
Service Layer
      ↓
Repository Layer
      ↓
Database (MariaDB)
```

```
com.app.taskmanagement
├── config/          # SecurityConfig, WebSocketConfig, DataInitializer
├── controller/      # REST Controllers
├── dto/             # Request & Response DTOs
│   └── mapper/      # Entity ↔ DTO Mappers
├── exception/       # Custom Exceptions & GlobalExceptionHandler
├── model/           # JPA Entities
│   └── enums/       # Roles, TaskStatus, Priority, NotificationType
├── repository/      # Spring Data JPA Repositories
├── security/        # JWT Filter, UserPrincipal, UserDetailsService
└── service/         # Business Logic
```

---

## Features

### Authentication
- User registration (ADMIN only)
- JWT login with token generation
- Logout with token blacklisting — invalidated tokens cannot be reused
- Automatic cleanup of expired tokens via scheduled job
- Default admin user created on startup via `DataInitializer`

### Task Management
- Create, update, and retrieve tasks
- Assign tasks to users
- Filter tasks by status, priority, assigned user, or creator
- Task status workflow: `CREATED → IN_PROGRESS → DONE → CLOSED`

### Notifications
- Automatic notification on task assignment
- Notification on task status update
- Notification on new comment
- Mark as read / delete notifications
- Real-time push via WebSocket + STOMP

### Comments
- Add comments to tasks
- View all comments on a task
- Automatic notification to the other party on new comment

---

## Security

JWT-based stateless authentication.

```
Authorization: Bearer <token>
```

| Role | Access |
|---|---|
| ADMIN | Full access — user management, all tasks |
| MANAGER | Create and manage tasks, assign to users |
| USER | View and update assigned tasks, add comments |

**Token blacklisting:** Logged-out tokens are stored in `InvalidatedToken` table and rejected on every request.

---

## API Endpoints

### Auth — `/api/auth`
| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/createUser` | ADMIN | Create new user |
| POST | `/api/auth/login` | Public | Login and get JWT |
| POST | `/api/auth/logout` | Authenticated | Logout and invalidate token |

### Users — `/api/user`
| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/user/userId/{userId}` | ADMIN | Get user by ID |
| GET | `/api/user/email/{email}` | ADMIN | Get user by email |
| GET | `/api/user/` | ADMIN | Get all users |
| GET | `/api/user/search/email/{email}` | ADMIN | Search users by email |
| GET | `/api/user/search/firstName/{name}` | ADMIN | Search by first name |
| GET | `/api/user/search/lastName/{name}` | ADMIN | Search by last name |
| GET | `/api/user/search/role/{role}` | ADMIN | Filter by role |
| PUT | `/api/user/{userId}` | ADMIN | Update user |
| DELETE | `/api/user/{userId}` | ADMIN | Delete user |

### Tasks — `/api/task`
| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/task/{taskId}` | Authenticated | Get task by ID |
| POST | `/api/task` | MANAGER | Create task |
| PUT | `/api/task/{taskId}` | MANAGER | Update task |
| GET | `/api/task/search/assignedTo/{userId}` | Authenticated | Tasks assigned to user |
| GET | `/api/task/search/createdBy/{userId}` | Authenticated | Tasks created by user |
| GET | `/api/task/search/assignedToAndState/{state}` | Authenticated | My assigned tasks by status |
| GET | `/api/task/search/createdByAndState/{state}` | Authenticated | My created tasks by status |

### Comments — `/api/comment`
| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/comment/{taskId}` | Authenticated | Get comments for task |
| POST | `/api/comment` | Authenticated | Add comment to task |

### Notifications — `/api/notification`
| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/notification/` | Authenticated | Get user notifications |
| PATCH | `/api/notification/{id}` | Authenticated | Mark notification as read |
| DELETE | `/api/notification/{id}` | Authenticated | Delete notification |

---

## Getting Started

### Prerequisites

- Java 21
- Maven 3.9+
- Docker + Docker Compose

### Run with Docker

**1. Clone the repository**
```bash
git clone https://github.com/samaz74/spring-boot-task-management.git
cd spring-boot-task-management
```

**2. Configure environment variables**

Update the following values in `docker-compose.yml`:
```yaml
SPRING_DATASOURCE_PASSWORD: your_password
JWT_SECRET: yourSecretKeyHereMinimum32Characters
```

**3. Build and run**
```bash
docker-compose up --build
```

**4. Default admin credentials**
```
Email: admin@app.com
Password: (as configured in DataInitializer)
```

### Run Locally (without Docker)

**1. Create the database**
```sql
CREATE DATABASE taskManagement;
```

**2. Configure `application.properties`**
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/taskManagement
spring.datasource.username=your_username
spring.datasource.password=your_password

jwt.secret=yourSecretKeyHereMinimum32Characters
jwt.expiration=1200000
```

**3. Run**
```bash
mvn spring-boot:run
```

---

## WebSocket

Real-time connection via STOMP over WebSocket:

```
ws://localhost:8080/ws/websocket
```

Subscribe to notifications:
```
SUBSCRIBE
destination:/topic/notifications/{userId}
```

---

## Roadmap

- [x] JWT Authentication + Token Blacklisting
- [x] Role-Based Access Control
- [x] Task CRUD + Filtering
- [x] Comment System
- [x] Notification System
- [x] WebSocket Real-time Notifications
- [x] Docker + Docker Compose
- [ ] Camunda BPMN Workflow
- [ ] JasperReports PDF Export
- [ ] CI/CD with GitHub Actions
- [ ] OAuth2 (Google Login)
- [ ] Unit & Integration Tests

---

## Author

**Peyman Azish**  
Java Backend Developer

- GitHub: [github.com/samaz74](https://github.com/samaz74)
- LinkedIn: [linkedin.com/in/peyman-azish](https://www.linkedin.com/in/peyman-azish)
- Email: azish.pey@gmail.com
