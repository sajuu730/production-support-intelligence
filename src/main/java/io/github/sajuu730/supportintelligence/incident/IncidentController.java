package io.github.sajuu730.supportintelligence.incident;

import io.github.sajuu730.supportintelligence.activity.IncidentActivity;
import io.github.sajuu730.supportintelligence.incident.IncidentDtos.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentController {
    private final IncidentService service;
    public IncidentController(IncidentService service){this.service=service;}
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Response create(@Valid @RequestBody CreateRequest request){return service.create(request);}
    @GetMapping public Page<Response> list(@RequestParam(required=false) IncidentStatus status,
        @RequestParam(required=false) String service,@RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="20") int size){
        if(page<0||size<1||size>100)throw new IllegalArgumentException("page must be >= 0 and size must be 1..100");
        return this.service.list(status,service,PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdAt")));
    }
    @GetMapping("/{id}") public Response get(@PathVariable UUID id){return service.get(id);}
    @PostMapping("/{id}/transitions") public Response transition(@PathVariable UUID id,@Valid @RequestBody TransitionRequest request){return service.transition(id,request);}
    @PutMapping("/{id}/assignee") public Response assign(@PathVariable UUID id,@Valid @RequestBody AssignRequest request){return service.assign(id,request);}
    @GetMapping("/{id}/history") public List<IncidentActivity> history(@PathVariable UUID id){return service.history(id);}
}
