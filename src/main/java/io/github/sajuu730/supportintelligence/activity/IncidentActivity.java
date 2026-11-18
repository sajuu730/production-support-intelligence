package io.github.sajuu730.supportintelligence.activity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="incident_activities",indexes=@Index(name="idx_activity_incident_time",columnList="incident_id,occurred_at"))
public class IncidentActivity {
    @Id @GeneratedValue private UUID id;
    @Column(name="incident_id",nullable=false) private UUID incidentId;
    @Column(nullable=false,length=80) private String action;
    @Column(nullable=false,length=120) private String actor;
    @Column(length=1000) private String detail;
    @Column(name="occurred_at",nullable=false) private Instant occurredAt;
    protected IncidentActivity(){}
    public IncidentActivity(UUID incidentId,String action,String actor,String detail,Instant occurredAt){
        this.incidentId=incidentId;this.action=action;this.actor=actor;this.detail=detail;this.occurredAt=occurredAt;
    }
    public UUID getId(){return id;} public UUID getIncidentId(){return incidentId;}
    public String getAction(){return action;} public String getActor(){return actor;}
    public String getDetail(){return detail;} public Instant getOccurredAt(){return occurredAt;}
}
