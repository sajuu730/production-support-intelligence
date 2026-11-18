# Operations runbook

## Start and stop

~~~bash
docker compose up --build -d
docker compose ps
docker compose logs -f app
docker compose down
~~~

Destructive local reset (removes the database volume):

~~~bash
docker compose down -v
~~~

## Health checks

~~~bash
curl -i http://localhost:8080/actuator/health
curl -u support:change-me-demo http://localhost:8080/api/v1/incidents
curl http://localhost:8080/actuator/prometheus
~~~

## Troubleshooting

| Symptom | Checks |
|---|---|
| Database connection failure | Check Compose health, PostgreSQL logs and datasource variables |
| Migration failure | Inspect startup logs; never edit an already-applied migration |
| HTTP 401 | Verify Basic Auth credentials and environment variables |
| HTTP 409 | Requested lifecycle transition is not permitted |
| SLA gauges stale | Check scheduler logs and database connectivity |
| Port conflict | Change host-side ports in Compose |

Change demo credentials before exposing the app. Use TLS, a secret manager and network restrictions for shared environments. Redact personal data, tokens and customer identifiers from log excerpts.
