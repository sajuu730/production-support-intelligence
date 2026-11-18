package io.github.sajuu730.supportintelligence.incident;

public enum IncidentSeverity {
    SEV1, SEV2, SEV3, SEV4;
    public int responseMinutes() {
        return switch (this) { case SEV1 -> 5; case SEV2 -> 15; case SEV3 -> 60; case SEV4 -> 240; };
    }
    public int resolutionMinutes() {
        return switch (this) { case SEV1 -> 240; case SEV2 -> 480; case SEV3 -> 2880; case SEV4 -> 7200; };
    }
}
