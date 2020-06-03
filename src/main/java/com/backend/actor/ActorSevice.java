package com.backend.actor;

import com.backend.Error;
import com.backend.actor.dto.CreateActorDTO;
import com.backend.movie.MovieEntity;
import com.backend.root.CRUD;
import com.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorSevice implements CRUD<ActorEntity, CreateActorDTO,CreateActorDTO>{
    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<ActorEntity> getAll(){ return actorRepository.findAll(); }

    @Override
    public ActorEntity create(CreateActorDTO input){
        ActorEntity existedActor = actorRepository.findByName(input.getName());

        if(existedActor != null){
            throw Error.DuplicatedError("Actor");
        }
        ActorEntity ActorToCreate = new ActorEntity(input.getName());

        return actorRepository.save(ActorToCreate);

    }

    @Override
    public ActorEntity update(String id, CreateActorDTO input){
        ActorEntity existedActor = actorRepository.findById(id).orElse(null);

        if(existedActor == null){
            throw Error.NotFoundError("Actor");
        }

        if(input.getName() != null){
            ActorEntity existedActorName = actorRepository.findByName(input.getName());
            if(existedActorName != null && !existedActorName.getId().equals(existedActor.getId())){
                throw Error.DuplicatedError("Actor");
            }
            existedActor.setName(input.getName());
        }

        return actorRepository.save(existedActor);
    }

    @Override
    public  ActorEntity delete(String id){
        ActorEntity existedActor = actorRepository.findById(id).orElse(null);

        if(existedActor == null){
            throw Error.NotFoundError("Actor");
        }

        Query query = new Query(Criteria.where("actors").is(existedActor.getId()));
        List<MovieEntity> moviesOfCate = mongoTemplate.find(query, MovieEntity.class);
        if(moviesOfCate.size() > 0)
            throw Error.UsedError("Actor");

        actorRepository.deleteById(id);

        return existedActor;
    }
}