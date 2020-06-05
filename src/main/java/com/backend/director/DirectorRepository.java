package com.backend.director;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface DirectorRepository extends MongoRepository<DirectorEntity, String> {
    DirectorEntity findByName(String name);
}
