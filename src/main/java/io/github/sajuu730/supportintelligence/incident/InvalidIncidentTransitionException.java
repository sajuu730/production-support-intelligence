package io.github.sajuu730.supportintelligence.incident;
public class InvalidIncidentTransitionException extends RuntimeException {
    public InvalidIncidentTransitionException(IncidentStatus current,IncidentStatus requested) {
        super("Cannot transition incident from "+current+" to "+requested);
    }
}
