package com.backend.image;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface ImageRepository extends MongoRepository<ImageEntity, String> {
}
