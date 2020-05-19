package com.backend.category;

import com.backend.Error;
import com.backend.category.dto.CreateCategoryDTO;
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
public class CategoryService implements CRUD<CategoryEntity, CreateCategoryDTO, CreateCategoryDTO> {
    @Autowired
    private  CategoryRespository categoryRespository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<CategoryEntity> getAll() {
        return categoryRespository.findAll();
    }

    @Override
    public CategoryEntity create(CreateCategoryDTO input) {
        CategoryEntity existedCategory = categoryRespository.findByName(input.getName());

        if(existedCategory != null){
            throw Error.DuplicatedError("Category");
        }
        CategoryEntity CategoryToCreate = new CategoryEntity(input.getName());

        return categoryRespository.save(CategoryToCreate);
    }

    @Override
    public CategoryEntity update(String id, CreateCategoryDTO input) {
        CategoryEntity existedCategory = categoryRespository.findById(id).orElse(null);

        if(existedCategory == null){
            throw  Error.NotFoundError("Category");
        }

        if(input.getName() != null){
            CategoryEntity existedCategoryName = categoryRespository.findByName(input.getName());
            if(existedCategoryName != null && !existedCategoryName.getId().equals(existedCategory.getId())) {
                throw Error.DuplicatedError("Category");
            }
            existedCategory.setName(input.getName());
        }
        return categoryRespository.save(existedCategory);
    }

    @Override
    public CategoryEntity delete(String id) {
        CategoryEntity existedCategory = categoryRespository.findById(id).orElse(null);

        if(existedCategory == null){
            throw Error.NotFoundError("Category");
        }

        Query query = new Query(Criteria.where("categories").is(existedCategory.getId()));
        List<MovieEntity> moviesOfCate = mongoTemplate.find(query, MovieEntity.class);
        if(moviesOfCate.size() > 0)
            throw Error.UsedError("Category");

        categoryRespository.deleteById(id);

        return existedCategory;
    }
}
