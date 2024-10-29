package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.repository.CategoryRepo;
import com.ecommerce.nunda.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryServiceImp(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public void saveCategory(Category category) {
        categoryRepo.save(category);
    }
    @Override
    public void deleteCategoryById(Long id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }
}
