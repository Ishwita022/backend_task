# Backend Task - Spring Boot REST API

## Tech Stack
- Java 17 + Spring Boot
- Spring Security + JWT Authentication
- MySQL/PostgreSQL
- Vanilla JS Frontend

## Setup Instructions

### Prerequisites
- Java 17+
- Maven
- MySQL running locally

### Database Setup
```sql
CREATE DATABASE backend_task;
```

### Configure `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/backend_task
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### Run the Project
```bash
mvn spring-boot:run
```
App runs at: `http://localhost:8080`

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/register | Register user |
| POST | /api/login | Login & get JWT |

### Tasks (JWT Required)
| Method | Endpoint | Role |
|--------|----------|------|
| GET | /api/tasks | USER/ADMIN |
| POST | /api/tasks | USER/ADMIN |
| PUT | /api/tasks/{id} | USER/ADMIN |
| DELETE | /api/tasks/{id} | USER/ADMIN |

## Default URLs
- Frontend: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html