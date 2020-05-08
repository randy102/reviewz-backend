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

    //2.1
    @GetMapping()
    public List<CategoryEntity> allCategory() throws Exception{
        return categoryService.getAllCategory();
    }

    //2.2
    @PostMapping()
    @Secured("ROLE_ADMIN")
    public CategoryEntity createCategory(@RequestBody() CreateCategoryDTO input) throws Exception{
        return categoryService.createCategory(input);
    }

    //2.3
    @PutMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public CategoryEntity updateCategory(@PathVariable("id") String id, @RequestBody() CreateCategoryDTO input) throws Exception{
        return categoryService.updateCategory(id, input);
    }

    //2.4
    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public CategoryEntity deleteCategory(@PathVariable("id") String id) throws Exception{
        return categoryService.deleteCategory(id);
    }


}