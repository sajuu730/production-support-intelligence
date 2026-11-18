CREATE TABLE incidents (
 id UUID PRIMARY KEY,title VARCHAR(180) NOT NULL,description VARCHAR(4000) NOT NULL,
 service_name VARCHAR(120) NOT NULL,severity VARCHAR(12) NOT NULL,category VARCHAR(24) NOT NULL,
 status VARCHAR(24) NOT NULL,reporter VARCHAR(120) NOT NULL,assignee VARCHAR(120),
 created_at TIMESTAMPTZ NOT NULL,updated_at TIMESTAMPTZ NOT NULL,response_due_at TIMESTAMPTZ NOT NULL,
 resolution_due_at TIMESTAMPTZ NOT NULL,acknowledged_at TIMESTAMPTZ,resolved_at TIMESTAMPTZ,
 version BIGINT NOT NULL DEFAULT 0
);
CREATE INDEX idx_incident_status_severity ON incidents(status,severity);
CREATE INDEX idx_incident_service_created ON incidents(service_name,created_at DESC);
CREATE TABLE incident_activities (
 id UUID PRIMARY KEY,incident_id UUID NOT NULL REFERENCES incidents(id),action VARCHAR(80) NOT NULL,
 actor VARCHAR(120) NOT NULL,detail VARCHAR(1000),occurred_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_activity_incident_time ON incident_activities(incident_id,occurred_at);
