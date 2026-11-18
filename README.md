# Production Support Intelligence Platform

A portfolio-grade production support workflow application built with Java 21, Spring Boot, PostgreSQL and Prometheus.

It provides structured incident tracking, severity-based SLA deadlines, controlled lifecycle transitions, diagnostic log classification, activity history and operational metrics.

> Portfolio demonstration using synthetic data only. This project is not affiliated with or connected to any former employer, customer environment or proprietary support system.

## Capabilities
- Incident creation, search, filtering and pagination
- Explicit incident state machine and assignment workflow
- Severity-based response and resolution targets
- Append-only activity history for application-managed changes
- Explainable, deterministic diagnostic log classification
- PostgreSQL persistence and Flyway schema migrations
- Actuator health and Prometheus SLA metrics
- HTTP Basic demo authentication
- Docker Compose deployment and GitHub Actions CI

## Architecture

~~~mermaid
flowchart LR
  Client[Support engineer / API client] --> API[Spring Boot REST API]
  API --> Service[Incident services]
  Service --> DB[(PostgreSQL)]
  Service --> Analyzer[Diagnostic analyzer]
  Scheduler[SLA monitor] --> DB
  API --> Metrics[Actuator / Prometheus]
  Metrics --> Prom[Prometheus]
~~~

See architecture, API, operations, demo and limitations documents in docs/.

## Stack

| Area | Technology |
|---|---|
| Runtime | Java 21 |
| Framework | Spring Boot 3.5 |
| API | Spring MVC, Bean Validation, OpenAPI |
| Persistence | Spring Data JPA, PostgreSQL |
| Migrations | Flyway |
| Observability | Actuator, Micrometer, Prometheus |
| Tests | JUnit 5, Mockito |
| Delivery | Docker Compose, GitHub Actions |

## Quick start

Requirements: Docker Engine and Docker Compose.

~~~bash
git clone https://github.com/sajuu730/production-support-intelligence.git
cd production-support-intelligence
docker compose up --build -d
docker compose ps
~~~

API: http://localhost:8080  
Swagger: http://localhost:8080/swagger-ui.html  
Health: http://localhost:8080/actuator/health  
Metrics: http://localhost:8080/actuator/prometheus

Demo credentials: support / change-me-demo. Override APP_SECURITY_USERNAME and APP_SECURITY_PASSWORD before exposing the service beyond localhost. Never use demo credentials in a real environment.

## Example: create an incident

~~~bash
curl -u support:change-me-demo -X POST http://localhost:8080/api/v1/incidents \
  -H 'Content-Type: application/json' \
  -d '{"title":"Checkout requests returning HTTP 503","description":"Synthetic demo incident","service":"checkout-api","severity":"SEV2","category":"APPLICATION","reporter":"demo-user"}'
~~~

## SLA examples

| Severity | Response target | Resolution target |
|---|---:|---:|
| SEV1 | 5 minutes | 4 hours |
| SEV2 | 15 minutes | 8 hours |
| SEV3 | 1 hour | 2 days |
| SEV4 | 4 hours | 5 days |

These are configurable demonstration targets, not contractual SLAs. The initial implementation uses elapsed wall-clock time and does not implement business-calendar exclusions or pause accounting.

## Local development

~~~bash
./mvnw test
./mvnw verify
~~~

On Windows PowerShell use .\mvnw.cmd test and .\mvnw.cmd verify. A Maven wrapper will be added in a follow-up if not present.

## Honest project boundaries

Diagnostic analysis is rule-based and advisory, not AI or definitive root-cause analysis. No external ticketing, paging, email, chat, CMDB or customer systems are integrated. Authentication is a single demo account. See docs/limitations.md.

## License

MIT.
