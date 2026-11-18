package io.github.sajuu730.supportintelligence.incident;

import io.github.sajuu730.supportintelligence.activity.*;
import io.github.sajuu730.supportintelligence.incident.IncidentDtos.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IncidentService {
    private final IncidentRepository incidents;
    private final IncidentActivityRepository activities;
    private final Clock clock;
    public IncidentService(IncidentRepository incidents,IncidentActivityRepository activities,Clock clock){
        this.incidents=incidents;this.activities=activities;this.clock=clock;
    }
    public Response create(CreateRequest request){
        Instant now=clock.instant();
        Incident saved=incidents.save(new Incident(request.title(),request.description(),request.service(),
            request.severity(),request.category(),request.reporter(),now));
        activities.save(new IncidentActivity(saved.getId(),"CREATED",request.reporter(),
            "Created with severity "+request.severity(),now));
        return Response.from(saved);
    }
    @Transactional(readOnly=true) public Response get(UUID id){return Response.from(find(id));}
    @Transactional(readOnly=true) public Page<Response> list(IncidentStatus status,String service,Pageable pageable){
        Page<Incident> page=status!=null?incidents.findByStatus(status,pageable):
            service!=null&&!service.isBlank()?incidents.findByServiceContainingIgnoreCase(service,pageable):
            incidents.findAll(pageable);
        return page.map(Response::from);
    }
    public Response transition(UUID id,TransitionRequest request){
        Incident incident=find(id);Instant now=clock.instant();incident.transitionTo(request.status(),now);
        Incident saved=incidents.save(incident);
        activities.save(new IncidentActivity(id,"STATUS_CHANGED",request.actor(),
            request.detail()==null?"Changed status to "+request.status():request.detail(),now));
        return Response.from(saved);
    }
    public Response assign(UUID id,AssignRequest request){
        Incident incident=find(id);Instant now=clock.instant();incident.assign(request.assignee(),now);
        Incident saved=incidents.save(incident);
        activities.save(new IncidentActivity(id,"ASSIGNED",request.actor(),"Assigned to "+request.assignee(),now));
        return Response.from(saved);
    }
    @Transactional(readOnly=true) public List<IncidentActivity> history(UUID id){
        find(id);return activities.findByIncidentIdOrderByOccurredAtAsc(id);
    }
    private Incident find(UUID id){return incidents.findById(id).orElseThrow(()->new IncidentNotFoundException(id));}
}
