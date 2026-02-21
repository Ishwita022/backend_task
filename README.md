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
spring.datasource.password=root
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
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- <img width="1920" height="1080" alt="Screenshot 2026-02-21 224317" src="https://github.com/user-attachments/assets/24b8a89a-52b9-4a5a-9f59-b3577c3c94c7" />


- Frontend: http://localhost:8080
- 
## Index
<img width="1920" height="1080" alt="Screenshot 2026-02-21 235913" src="https://github.com/user-attachments/assets/f601ed1f-3886-451a-8b84-55ecc0370448" />


## Register
<img width="1920" height="1080" alt="Screenshot 2026-02-21 235952" src="https://github.com/user-attachments/assets/408dd968-30dd-4b5b-bce3-8056253d551f" />


## Login
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/2256018d-3194-417e-9a89-19625a32b778" />


## Dashboard 
<img width="1920" height="1080" alt="Screenshot 2026-02-22 000009" src="https://github.com/user-attachments/assets/8e4d3c78-22eb-4524-a125-71d71b5b2654" />

## All user entry
<img width="1920" height="1080" alt="Screenshot 2026-02-22 000026" src="https://github.com/user-attachments/assets/e9e58ac9-b397-4188-8052-35d272e3ddbb" />
