package com.backend.actor;

import com.backend.RouteConfig;
import com.backend.actor.dto.CreateActorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RouteConfig.ACTOR_BASE )
public class ActorController {

    @Autowired
    private ActorSevice actorService;

    @GetMapping()
    public List<ActorEntity> allActor() throws Exception{
        return actorService.getAll();
    }

    @PostMapping()
    @Secured("ROLE_ADMIN")
    public ActorEntity createActor(@RequestBody() CreateActorDTO input) throws Exception{
        return actorService.create(input);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ActorEntity updateActor(@PathVariable("id") String id, @RequestBody() CreateActorDTO  input) throws Exception{
        return actorService.update(id, input);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public ActorEntity deleteActor(@PathVariable("id") String id) throws Exception{
        return actorService.delete(id);
    }


}
