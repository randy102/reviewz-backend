package com.backend.category;
import com.backend.RouteConfig;

import com.backend.category.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RouteConfig.CATEGORY_BASE )
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public List<CategoryEntity> allCategory() throws Exception{
        return categoryService.getAll();
    }

    @PostMapping()
    @Secured("ROLE_ADMIN")
    public CategoryEntity createCategory(@RequestBody() CreateCategoryDTO input) throws Exception{
        return categoryService.create(input);
    }

    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public CategoryEntity updateCategory(@PathVariable("id") String id, @RequestBody() CreateCategoryDTO input) throws Exception{
        return categoryService.update(id, input);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public CategoryEntity deleteCategory(@PathVariable("id") String id) throws Exception{
        return categoryService.delete(id);
    }


}