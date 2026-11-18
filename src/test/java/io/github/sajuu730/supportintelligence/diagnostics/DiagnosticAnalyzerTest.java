package io.github.sajuu730.supportintelligence.diagnostics;
import io.github.sajuu730.supportintelligence.incident.IncidentCategory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
class DiagnosticAnalyzerTest {
 private final DiagnosticAnalyzer analyzer=new DiagnosticAnalyzer();
 @Test void detectsNetworkTimeout(){var r=analyzer.analyze("Connection timed out calling upstream");assertThat(r.category()).isEqualTo(IncidentCategory.NETWORK);assertThat(r.code()).isEqualTo("NETWORK_CONNECTIVITY");}
 @Test void detectsDatabaseContention(){assertThat(analyzer.analyze("SQLSTATE 40P01 deadlock detected").category()).isEqualTo(IncidentCategory.DATABASE);}
 @Test void unknownTextRemainsUnclassified(){assertThat(analyzer.analyze("routine startup completed").category()).isEqualTo(IncidentCategory.UNKNOWN);}
}
