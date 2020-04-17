package com.backend.category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRespository extends MongoRepository<CategoryEntity, String> {
}
