package com.backend.actor;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface ActorRepository extends MongoRepository< ActorEntity, String> {
    ActorEntity findByName(String name);
}
