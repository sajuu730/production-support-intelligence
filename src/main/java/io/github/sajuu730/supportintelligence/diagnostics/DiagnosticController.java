package io.github.sajuu730.supportintelligence.diagnostics;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/diagnostics")
public class DiagnosticController{
 private final DiagnosticAnalyzer analyzer;
 public DiagnosticController(DiagnosticAnalyzer analyzer){this.analyzer=analyzer;}
 @PostMapping("/analyze") public DiagnosticAnalyzer.Analysis analyze(@Valid @RequestBody Request request){return analyzer.analyze(request.logExcerpt());}
 public record Request(@NotBlank @Size(max=20000) String logExcerpt,@NotBlank @Size(max=120) String service){}
}