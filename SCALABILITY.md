# Scalability Notes

## Current Architecture
- Monolithic Spring Boot app with MySQL and JWT auth

## How to Scale

### Caching (Redis)
- Cache frequent GET /api/tasks responses using Redis
- Reduces DB load significantly under high traffic

### Microservices
- Split into: Auth Service, Task Service, API Gateway
- Each service deployable and scalable independently

### Load Balancing
- Deploy multiple instances behind Nginx or AWS ALB
- Stateless JWT auth makes horizontal scaling easy

### Docker Deployment
- Containerize with Docker + docker-compose
- Orchestrate with Kubernetes for auto-scaling

### Database
- Add read replicas for heavy read traffic
- Use connection pooling (HikariCP - already default in Spring Boot)