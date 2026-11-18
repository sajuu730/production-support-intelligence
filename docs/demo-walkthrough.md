# Production Support Incident Walkthrough

This walkthrough demonstrates how a support engineer can investigate, track and close a synthetic production incident using the Production Support Intelligence API.

> **Demo only:** All names, services and incident details below are synthetic. The diagnostic endpoint uses deterministic keyword rules; it does not perform AI-based diagnosis or prove the root cause.

## Scenario

The checkout API begins returning HTTP 503 responses. A support engineer records the incident, assigns an owner, acknowledges the alert, investigates the suspected database connectivity issue, records recovery and closes the incident after verification.

## 1. Start the application

From the repository root:

```powershell
docker compose up --build -d
docker compose ps
```

Wait for PostgreSQL to become healthy and the application to finish starting.

The default application URL is `http://localhost:8080`. If that host port is already occupied, use the configured host mapping (for example, `http://localhost:8081`).

## 2. Authenticate and create the incident

The local demo account defaults to `support / change-me-demo`. These credentials are for local demonstration only.

```powershell
$baseUrl = "http://localhost:8081" # Use http://localhost:8080 if that is your mapped port
$pair = "support:change-me-demo"
$bytes = [System.Text.Encoding]::ASCII.GetBytes($pair)
$token = [Convert]::ToBase64String($bytes)
$headers = @{ Authorization = "Basic $token" }

$incidentBody = @{
    title = "Checkout API returning 503"
    description = "Synthetic test incident. Checkout service is returning HTTP 503 errors."
    service = "checkout-api"
    severity = "SEV2"
    category = "APPLICATION"
    reporter = "demo-user"
} | ConvertTo-Json

$incident = Invoke-RestMethod -Method Post `
    -Uri "$baseUrl/api/v1/incidents" `
    -Headers $headers `
    -ContentType "application/json" `
    -Body $incidentBody

$incident
$incidentId = $incident.id
```

The API returns the incident identifier, current status and severity-based response and resolution deadlines. For SEV2, the example policy sets a 15-minute response target and an 8-hour resolution target.

## 3. Assign and acknowledge ownership

Assign the incident to the on-call support engineer:

```powershell
$assignBody = @{
    assignee = "support-engineer-1"
    actor = "support-lead"
} | ConvertTo-Json

Invoke-RestMethod -Method Put `
    -Uri "$baseUrl/api/v1/incidents/$incidentId/assignee" `
    -Headers $headers -ContentType "application/json" -Body $assignBody
```

Acknowledge the incident and record the initial investigation:

```powershell
$ackBody = @{
    status = "ACKNOWLEDGED"
    actor = "support-engineer-1"
    detail = "Incident reviewed; investigating checkout API failures."
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
    -Uri "$baseUrl/api/v1/incidents/$incidentId/transitions" `
    -Headers $headers -ContentType "application/json" -Body $ackBody
```

## 4. Run diagnostic triage

Submit a representative log excerpt:

```powershell
$diagnosticBody = @{
    service = "checkout-api"
    logExcerpt = "ERROR Connection timed out while connecting to PostgreSQL database"
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
    -Uri "$baseUrl/api/v1/diagnostics/analyze" `
    -Headers $headers -ContentType "application/json" -Body $diagnosticBody
```

The example text matches a network-connectivity rule and returns a category, diagnostic code, rationale and suggested checks. Treat this as a triage hint: validate DNS, routing, firewall rules and upstream reachability using the relevant operational tools before concluding the cause.

## 5. Record investigation and recovery

Move the incident to `IN_PROGRESS`:

```powershell
$progressBody = @{
    status = "IN_PROGRESS"
    actor = "support-engineer-1"
    detail = "Investigating database connectivity and restoring service."
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
    -Uri "$baseUrl/api/v1/incidents/$incidentId/transitions" `
    -Headers $headers -ContentType "application/json" -Body $progressBody
```

After the synthetic recovery has been verified, resolve the incident:

```powershell
$resolveBody = @{
    status = "RESOLVED"
    actor = "support-engineer-1"
    detail = "Database connectivity restored; checkout API responding normally."
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
    -Uri "$baseUrl/api/v1/incidents/$incidentId/transitions" `
    -Headers $headers -ContentType "application/json" -Body $resolveBody
```

The lifecycle enforces valid transitions. A direct transition from `ACKNOWLEDGED` to `RESOLVED` is rejected; record the investigation state first.

## 6. Verify and close

After the resolution has been checked, the support lead can close the incident:

```powershell
$closeBody = @{
    status = "CLOSED"
    actor = "support-lead"
    detail = "Resolution verified; incident formally closed."
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
    -Uri "$baseUrl/api/v1/incidents/$incidentId/transitions" `
    -Headers $headers -ContentType "application/json" -Body $closeBody
```

Retrieve the final incident and its activity history:

```powershell
Invoke-RestMethod -Uri "$baseUrl/api/v1/incidents/$incidentId" -Headers $headers
Invoke-RestMethod -Uri "$baseUrl/api/v1/incidents/$incidentId/history" -Headers $headers
```

Confirm the final status is `CLOSED` and review the recorded actors, timestamps and transition details.

## 7. Show operational observability

Open the following local endpoints:

- Health: `/actuator/health`
- Prometheus metrics: `/actuator/prometheus`
- SLA response-breach gauge: `support_incidents_sla_response_breached`
- SLA resolution-breach gauge: `support_incidents_sla_resolution_breached`

Use the host port configured in Docker Compose. The SLA monitor evaluates elapsed wall-clock targets; this demo does not implement business calendars or pause accounting.

## Suggested interview narrative

1. **Detect and record:** explain the synthetic 503 incident, severity selection and SLA deadlines.
2. **Establish ownership:** show assignment and acknowledgement with an auditable activity trail.
3. **Triage:** demonstrate the explainable diagnostic rule and distinguish a suggested check from a confirmed root cause.
4. **Investigate and recover:** record progress and the evidence-backed recovery update.
5. **Verify and close:** show the enforced lifecycle and final activity history.
6. **Operate:** show health and Prometheus SLA metrics, then discuss the documented limitations.

## Safety and scope

- Change demo credentials and database passwords before exposing the application beyond a trusted local environment.
- Do not use real customer data, employer logs, internal hostnames or production credentials.
- The activity history is application-managed, not tamper-proof against direct database changes.
- Authentication is a single in-memory demo account; RBAC and SSO are not implemented.
- Diagnostic classification is deterministic keyword matching, not AI or definitive root-cause analysis.
- See [known limitations](limitations.md) for additional boundaries.
