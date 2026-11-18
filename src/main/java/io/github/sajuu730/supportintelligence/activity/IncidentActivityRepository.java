package io.github.sajuu730.supportintelligence.activity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
public interface IncidentActivityRepository extends JpaRepository<IncidentActivity,UUID> {
    List<IncidentActivity> findByIncidentIdOrderByOccurredAtAsc(UUID incidentId);
}
