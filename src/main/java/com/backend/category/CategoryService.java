package com.backend.category;

import com.backend.Error;
import com.backend.category.dto.CreateCategoryDTO;
import com.backend.movie.MovieEntity;
import com.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private  CategoryRespository categoryRespository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Secured("ROLE_ADMIN")
    public List<CategoryEntity> getAllCategory() throws ArrayStoreException{
        return categoryRespository.findAll();
    }

    @Secured("ROLE_ADMIN")
    public CategoryEntity createCategory(CreateCategoryDTO input) throws Exception{
        CategoryEntity existedCategory = categoryRespository.findByName(input.getName());

        if(existedCategory != null){
            throw Error.DuplicatedError("Category");
        }
        CategoryEntity CategoryToCreate =new CategoryEntity(input.getName());

        return categoryRespository.save(CategoryToCreate);
    }

    @Secured("ROLE_ADMIN")
    public CategoryEntity updateCategory(String id, CreateCategoryDTO input) throws Exception{
        CategoryEntity existedCategory = categoryRespository.findById(id).orElse(null);

        if(existedCategory == null){
            throw  Error.NotFoundError("Category");
        }

        if(input.getName() != null){
            CategoryEntity existedCategoryname = categoryRespository.findByName(input.getName());
            if(existedCategoryname != null && !existedCategoryname.getId().equals(existedCategory.getId())) {
                throw Error.DuplicatedError("Category");
            }
            existedCategory.setName(input.getName());
        }
        return categoryRespository.save(existedCategory);
    }

    @Secured("ROLE_ADMIN")
    public CategoryEntity deleteCategory(String id) throws Exception {
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
