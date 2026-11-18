package io.github.sajuu730.supportintelligence.incident;

import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.UUID;

public final class IncidentDtos {
    private IncidentDtos(){}
    public record CreateRequest(@NotBlank @Size(max=180) String title,
        @NotBlank @Size(max=4000) String description,@NotBlank @Size(max=120) String service,
        @NotNull IncidentSeverity severity,@NotNull IncidentCategory category,
        @NotBlank @Size(max=120) String reporter){}
    public record TransitionRequest(@NotNull IncidentStatus status,
        @NotBlank @Size(max=120) String actor,@Size(max=1000) String detail){}
    public record AssignRequest(@NotBlank @Size(max=120) String assignee,
        @NotBlank @Size(max=120) String actor){}
    public record Response(UUID id,String title,String description,String service,IncidentSeverity severity,
        IncidentCategory category,IncidentStatus status,String reporter,String assignee,Instant createdAt,
        Instant updatedAt,Instant responseDueAt,Instant resolutionDueAt,Instant acknowledgedAt,
        Instant resolvedAt,long version) {
        public static Response from(Incident i){return new Response(i.getId(),i.getTitle(),i.getDescription(),
            i.getService(),i.getSeverity(),i.getCategory(),i.getStatus(),i.getReporter(),i.getAssignee(),
            i.getCreatedAt(),i.getUpdatedAt(),i.getResponseDueAt(),i.getResolutionDueAt(),
            i.getAcknowledgedAt(),i.getResolvedAt(),i.getVersion());}
    }
}
