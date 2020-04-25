package com.backend.review;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<ReviewEntity,String> {
    List<ReviewEntity> findByIdUser(String id);
    List<ReviewEntity> findByIdMovie(String id);
}
