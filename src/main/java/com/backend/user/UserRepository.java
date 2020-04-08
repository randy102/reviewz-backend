package com.backend.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends MongoRepository<UserEntity, String> {
    UserEntity findByUsername(String username);

}
