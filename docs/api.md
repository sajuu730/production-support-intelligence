# API guide

All /api/v1 endpoints require HTTP Basic authentication. Swagger UI is available at /swagger-ui.html.

| Method | Endpoint | Purpose |
|---|---|---|
| POST | /api/v1/incidents | Create incident |
| GET | /api/v1/incidents | Paginated list; optional status or service filter |
| GET | /api/v1/incidents/{id} | Fetch incident |
| POST | /api/v1/incidents/{id}/transitions | Change lifecycle state |
| PUT | /api/v1/incidents/{id}/assignee | Assign owner |
| GET | /api/v1/incidents/{id}/history | Read activity history |
| POST | /api/v1/diagnostics/analyze | Analyze supplied log excerpt |

## Create request

~~~json
{
  "title": "Checkout requests returning HTTP 503",
  "description": "Synthetic incident description",
  "service": "checkout-api",
  "severity": "SEV2",
  "category": "APPLICATION",
  "reporter": "demo-user"
}
~~~

## Transition request

~~~json
{
  "status": "ACKNOWLEDGED",
  "actor": "support-engineer",
  "detail": "Incident accepted for investigation"
}
~~~

## Diagnostic request

~~~json
{
  "service": "checkout-api",
  "logExcerpt": "Connection timed out while calling upstream payment service"
}
~~~

Diagnostic results include category, rule code, rationale and suggested checks. The endpoint does not create incidents automatically.

## Operational endpoints
- GET /actuator/health
- GET /actuator/health/liveness
- GET /actuator/health/readiness
- GET /actuator/prometheus
