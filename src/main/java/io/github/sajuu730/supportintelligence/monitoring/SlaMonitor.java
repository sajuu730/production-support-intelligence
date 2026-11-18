package io.github.sajuu730.supportintelligence.monitoring;
import io.github.sajuu730.supportintelligence.incident.*;
import io.micrometer.core.instrument.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.Clock;
import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicLong;
@Component
public class SlaMonitor{
 private static final EnumSet<IncidentStatus> TERMINAL=EnumSet.of(IncidentStatus.RESOLVED,IncidentStatus.CLOSED);
 private final IncidentRepository repository;private final Clock clock;
 private final AtomicLong responseBreaches=new AtomicLong(),resolutionBreaches=new AtomicLong();
 public SlaMonitor(IncidentRepository repository,MeterRegistry registry,Clock clock){
  this.repository=repository;this.clock=clock;
  Gauge.builder("support_incidents_sla_response_breached",responseBreaches,AtomicLong::get).description("Non-terminal incidents past response target").register(registry);
  Gauge.builder("support_incidents_sla_resolution_breached",resolutionBreaches,AtomicLong::get).description("Non-terminal incidents past resolution target").register(registry);
 }
 @Scheduled(fixedDelayString="${incident.sla-monitor-interval:30000}") public void refresh(){
  var now=clock.instant();
  responseBreaches.set(repository.countByStatusNotInAndResponseDueAtBefore(TERMINAL,now));
  resolutionBreaches.set(repository.countByStatusNotInAndResolutionDueAtBefore(TERMINAL,now));
 }
}