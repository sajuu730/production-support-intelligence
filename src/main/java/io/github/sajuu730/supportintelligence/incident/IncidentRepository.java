package io.github.sajuu730.supportintelligence.incident;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident,UUID> {
    Page<Incident> findByStatus(IncidentStatus status,Pageable pageable);
    Page<Incident> findByServiceContainingIgnoreCase(String service,Pageable pageable);
    long countByStatusNotInAndResponseDueAtBefore(Collection<IncidentStatus> terminal,Instant now);
    long countByStatusNotInAndResolutionDueAtBefore(Collection<IncidentStatus> terminal,Instant now);
}
