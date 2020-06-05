package com.backend.category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {
    CategoryEntity findByName( String name);
}
