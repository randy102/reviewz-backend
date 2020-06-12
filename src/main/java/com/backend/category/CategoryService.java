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
    private CategoryRepository categoryRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<CategoryEntity> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryEntity create(CreateCategoryDTO input) {
        CategoryEntity existedCategory = categoryRepository.findByName(input.getName());

        if(existedCategory != null){
            throw Error.DuplicatedError("Category");
        }
        CategoryEntity CategoryToCreate = new CategoryEntity(input.getName());

        return categoryRepository.save(CategoryToCreate);
    }

    @Override
    public CategoryEntity update(String id, CreateCategoryDTO input) {
        CategoryEntity existedCategory = categoryRepository.findById(id).orElse(null);

        if(existedCategory == null){
            throw  Error.NotFoundError("Category");
        }

        if(input.getName() != null){
            CategoryEntity existedCategoryName = categoryRepository.findByName(input.getName());
            if(existedCategoryName != null && !existedCategoryName.getId().equals(existedCategory.getId())) {
                throw Error.DuplicatedError("Category");
            }
            existedCategory.setName(input.getName());
        }
        return categoryRepository.save(existedCategory);
    }

    @Override
    public boolean delete(String id) {
        CategoryEntity existedCategory = categoryRepository.findById(id).orElse(null);

        if(existedCategory == null){
            throw Error.NotFoundError("Category");
        }

        Query query = new Query(Criteria.where("categories").is(existedCategory.getId()));
        List<MovieEntity> moviesOfCate = mongoTemplate.find(query, MovieEntity.class);
        if(moviesOfCate.size() > 0)
            throw Error.UsedError("Category");

        categoryRepository.deleteById(id);

        return true;
    }
}
