package com.ecommerce.nunda.controller;



import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.service.CategoryService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalAdviceAttributes {

    private final CategoryService categoryService;

    public GlobalAdviceAttributes(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Add global attributes to all views
    @ModelAttribute("categories")
    public List<Category> populateCategories() {
        return categoryService.getAllCategories();
    }

}
