package com.ecommerce.nunda.service;


import com.ecommerce.nunda.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void saveCategory(Category category);
    void deleteCategoryById(Long id);
    Category getCategoryById(Long id);
}
