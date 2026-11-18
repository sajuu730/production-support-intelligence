package io.github.sajuu730.supportintelligence.incident;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="incidents", indexes={
    @Index(name="idx_incident_status_severity",columnList="status,severity"),
    @Index(name="idx_incident_service_created",columnList="service_name,created_at")
})
public class Incident {
    @Id @GeneratedValue private UUID id;
    @Column(nullable=false,length=180) private String title;
    @Column(nullable=false,length=4000) private String description;
    @Column(name="service_name",nullable=false,length=120) private String service;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=12) private IncidentSeverity severity;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=24) private IncidentCategory category;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=24) private IncidentStatus status;
    @Column(nullable=false,length=120) private String reporter;
    @Column(length=120) private String assignee;
    @Column(name="created_at",nullable=false) private Instant createdAt;
    @Column(name="updated_at",nullable=false) private Instant updatedAt;
    @Column(name="response_due_at",nullable=false) private Instant responseDueAt;
    @Column(name="resolution_due_at",nullable=false) private Instant resolutionDueAt;
    private Instant acknowledgedAt;
    private Instant resolvedAt;
    @Version private long version;

    protected Incident() {}
    public Incident(String title,String description,String service,IncidentSeverity severity,
            IncidentCategory category,String reporter,Instant now) {
        this.title=title; this.description=description; this.service=service; this.severity=severity;
        this.category=category; this.reporter=reporter; this.status=IncidentStatus.OPEN;
        this.createdAt=now; this.updatedAt=now;
        this.responseDueAt=now.plusSeconds(severity.responseMinutes()*60L);
        this.resolutionDueAt=now.plusSeconds(severity.resolutionMinutes()*60L);
    }
    public void transitionTo(IncidentStatus next,Instant now) {
        boolean allowed=switch(status) {
            case OPEN -> next==IncidentStatus.ACKNOWLEDGED || next==IncidentStatus.IN_PROGRESS;
            case ACKNOWLEDGED -> next==IncidentStatus.IN_PROGRESS || next==IncidentStatus.PENDING;
            case IN_PROGRESS -> next==IncidentStatus.PENDING || next==IncidentStatus.RESOLVED;
            case PENDING -> next==IncidentStatus.IN_PROGRESS || next==IncidentStatus.RESOLVED;
            case RESOLVED -> next==IncidentStatus.CLOSED || next==IncidentStatus.IN_PROGRESS;
            case CLOSED -> false;
        };
        if(!allowed) throw new InvalidIncidentTransitionException(status,next);
        status=next; updatedAt=now;
        if(next==IncidentStatus.ACKNOWLEDGED && acknowledgedAt==null) acknowledgedAt=now;
        if(next==IncidentStatus.RESOLVED) resolvedAt=now;
        if(next==IncidentStatus.IN_PROGRESS) resolvedAt=null;
    }
    public void assign(String value,Instant now) { assignee=value; updatedAt=now; }
    public UUID getId(){return id;} public String getTitle(){return title;}
    public String getDescription(){return description;} public String getService(){return service;}
    public IncidentSeverity getSeverity(){return severity;} public IncidentCategory getCategory(){return category;}
    public IncidentStatus getStatus(){return status;} public String getReporter(){return reporter;}
    public String getAssignee(){return assignee;} public Instant getCreatedAt(){return createdAt;}
    public Instant getUpdatedAt(){return updatedAt;} public Instant getResponseDueAt(){return responseDueAt;}
    public Instant getResolutionDueAt(){return resolutionDueAt;} public Instant getAcknowledgedAt(){return acknowledgedAt;}
    public Instant getResolvedAt(){return resolvedAt;} public long getVersion(){return version;}
}
