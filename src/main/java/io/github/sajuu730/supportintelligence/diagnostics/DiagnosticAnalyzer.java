package io.github.sajuu730.supportintelligence.diagnostics;

import io.github.sajuu730.supportintelligence.incident.IncidentCategory;
import org.springframework.stereotype.Component;
import java.util.Locale;

@Component
public class DiagnosticAnalyzer {
    public Analysis analyze(String excerpt){
        String text=excerpt.toLowerCase(Locale.ROOT);
        if(has(text,"connection refused","connection timed out","no route to host","dns"))
            return new Analysis(IncidentCategory.NETWORK,"NETWORK_CONNECTIVITY","Network or name-resolution indicator matched","Check DNS, routing, firewall rules and upstream reachability.");
        if(has(text,"sqlstate","deadlock","too many connections","database connection","could not connect to server"))
            return new Analysis(IncidentCategory.DATABASE,"DATABASE_ACCESS_OR_CONTENTION","Database connectivity or contention indicator matched","Inspect database availability, connection pools and lock activity.");
        if(has(text,"outofmemoryerror","heap space","oomkilled","memory limit"))
            return new Analysis(IncidentCategory.INFRASTRUCTURE,"RESOURCE_EXHAUSTION","Memory exhaustion indicator matched","Inspect memory limits, heap usage and recent workload changes.");
        if(has(text,"http 5","status=5","internal server error","nullpointerexception","exception"))
            return new Analysis(IncidentCategory.APPLICATION,"APPLICATION_ERROR","Application exception or server-error indicator matched","Correlate logs with deployment, dependency and application changes.");
        return new Analysis(IncidentCategory.UNKNOWN,"UNCLASSIFIED","No configured diagnostic rule matched","Review surrounding logs and correlate with service metrics and recent changes.");
    }
    private boolean has(String value,String... terms){for(String term:terms)if(value.contains(term))return true;return false;}
    public record Analysis(IncidentCategory category,String code,String rationale,String suggestedChecks){}
}
