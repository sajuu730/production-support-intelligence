# Scope and known limitations

This is a portfolio demonstration, not a certified ITSM product.

- Diagnostic analysis is a small deterministic keyword rule set, not AI or definitive root-cause analysis.
- SLA targets use elapsed wall-clock time; business calendars, pauses and maintenance windows are not implemented.
- Authentication is one in-memory demo user; no RBAC matrix, SSO or identity-provider integration exists.
- Activity history is append-only through application workflows, but database-level tamper-proof auditing is not implemented.
- No external ticketing, paging, email, chat, CMDB or log-platform integrations are included.
- SLA monitoring uses process-local scheduling; distributed scheduler coordination is not implemented.
- Prometheus is open for local scraping; apply network controls outside localhost.
- High availability, disaster recovery and load testing have not been validated.

## Next increments
1. PostgreSQL Testcontainers integration coverage.
2. Role-based access and external identity integration.
3. Business-calendar SLA policy and pause/resume.
4. Secure webhook notifications with retries and idempotency.
5. Structured log ingestion, redaction and retention.
6. Grafana dashboard and alert rules.
7. Kubernetes deployment and backup/restore drill.
