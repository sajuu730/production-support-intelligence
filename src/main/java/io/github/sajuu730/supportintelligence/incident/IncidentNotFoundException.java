package io.github.sajuu730.supportintelligence.incident;
import java.util.UUID;
public class IncidentNotFoundException extends RuntimeException {
    public IncidentNotFoundException(UUID id){super("Incident not found: "+id);}
}
