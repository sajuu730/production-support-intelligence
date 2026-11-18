# Architecture

## Components
- Incident API: validated, paginated incident operations.
- Incident service: lifecycle rules, SLA deadlines and transactional activity records.
- Diagnostic analyzer: deterministic log-text rules with visible rationale and suggested checks.
- SLA monitor: periodic database queries exposed as Micrometer gauges.
- PostgreSQL: incidents and activity events; Flyway manages schema changes.
- Security: HTTP Basic demo account; health and Prometheus endpoints are public for local scraping.

## Lifecycle

~~~mermaid
stateDiagram-v2
 [*] --> OPEN
 OPEN --> ACKNOWLEDGED
 OPEN --> IN_PROGRESS
 ACKNOWLEDGED --> IN_PROGRESS
 ACKNOWLEDGED --> PENDING
 IN_PROGRESS --> PENDING
 IN_PROGRESS --> RESOLVED
 PENDING --> IN_PROGRESS
 PENDING --> RESOLVED
 RESOLVED --> CLOSED
 RESOLVED --> IN_PROGRESS
 CLOSED --> [*]
~~~

Invalid transitions return HTTP 409. JPA optimistic locking uses a version column to prevent silent concurrent overwrites.

## Design choices
- UTC Instant timestamps and UUID identifiers.
- SLA windows are elapsed-time targets, not business-calendar calculations.
- Diagnostic results include rule code and rationale; they are advisory.
- Metrics contain aggregate counts, not incident IDs or unbounded labels.
- Activity history is append-only through application workflows; database-level tamper-proof auditing is not configured.
