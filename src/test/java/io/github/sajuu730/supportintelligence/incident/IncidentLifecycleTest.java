package io.github.sajuu730.supportintelligence.incident;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.*;
class IncidentLifecycleTest {
 private final Instant now=Instant.parse("2026-01-01T00:00:00Z");
 private Incident incident(){return new Incident("API unavailable","Synthetic test","orders",IncidentSeverity.SEV2,IncidentCategory.APPLICATION,"tester",now);}
 @Test void calculatesSeverityDeadlines(){assertThat(incident().getResponseDueAt()).isEqualTo(now.plusSeconds(900));assertThat(incident().getResolutionDueAt()).isEqualTo(now.plusSeconds(28800));}
 @Test void recordsLifecycleTimestamps(){Incident i=incident();i.transitionTo(IncidentStatus.ACKNOWLEDGED,now.plusSeconds(10));i.transitionTo(IncidentStatus.IN_PROGRESS,now.plusSeconds(20));i.transitionTo(IncidentStatus.RESOLVED,now.plusSeconds(30));assertThat(i.getStatus()).isEqualTo(IncidentStatus.RESOLVED);assertThat(i.getAcknowledgedAt()).isEqualTo(now.plusSeconds(10));assertThat(i.getResolvedAt()).isEqualTo(now.plusSeconds(30));}
 @Test void rejectsInvalidTransition(){assertThatThrownBy(()->incident().transitionTo(IncidentStatus.CLOSED,now)).isInstanceOf(InvalidIncidentTransitionException.class);}
}
