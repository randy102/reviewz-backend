package com.backend.request;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestRepository extends MongoRepository<RequestEntity, String> {
}
