# Demo scenarios

Use synthetic data only.

## 1. Incident lifecycle
1. Create a SEV2 incident through Swagger or the API.
2. Acknowledge it.
3. Assign it to a demo engineer.
4. Move it to IN_PROGRESS, then RESOLVED.
5. Fetch incident history and show the ordered activity trail.

## 2. Diagnostic classification
Submit a synthetic connection timeout to the diagnostic endpoint. Explain that this is a deterministic rule match, not a definitive root-cause statement.

## 3. SLA observability
Inspect support_incidents_sla_response_breached and support_incidents_sla_resolution_breached in the Prometheus endpoint. For a rapid demo, use a test clock or short-SLA fixture rather than waiting for the example SLA duration.

## Evidence to capture
- Swagger endpoint overview
- Incident create response
- Lifecycle and activity history
- Diagnostic analysis response
- Prometheus SLA gauges
- Docker Compose service status
- Passing GitHub Actions workflow

Never include credentials, customer data, internal hostnames or employer screenshots.
