package io.github.sajuu730.supportintelligence.api;
import io.github.sajuu730.supportintelligence.incident.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler{
 @ExceptionHandler(IncidentNotFoundException.class) ResponseEntity<?> notFound(IncidentNotFoundException ex){return response(HttpStatus.NOT_FOUND,ex.getMessage());}
 @ExceptionHandler(InvalidIncidentTransitionException.class) ResponseEntity<?> conflict(InvalidIncidentTransitionException ex){return response(HttpStatus.CONFLICT,ex.getMessage());}
 @ExceptionHandler({IllegalArgumentException.class,MethodArgumentNotValidException.class}) ResponseEntity<?> badRequest(Exception ex){return response(HttpStatus.BAD_REQUEST,ex.getMessage());}
 private ResponseEntity<Map<String,Object>> response(HttpStatus status,String message){
  return ResponseEntity.status(status).body(Map.of("timestamp",Instant.now(),"status",status.value(),"error",status.getReasonPhrase(),"message",message));
 }
}