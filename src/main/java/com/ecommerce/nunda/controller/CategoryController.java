package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CategoryController {
     private  final CategoryService categoryService;

     public CategoryController(CategoryService categoryService){
            this.categoryService = categoryService;
        }

    //fetching all the categories
    @GetMapping("/admin/category")
    public String getCategory(Model model){
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/category";
    }

    // Adding a new category
    @PostMapping("/admin/category/add")
    public String addCategory(@RequestParam("name") String name) {
        Category newCategory = new Category();
        newCategory.setName(name);
        categoryService.saveCategory(newCategory);
        return "redirect:/admin/category";
    }
    // Editing an existing category
    @PostMapping("/admin/category/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, @RequestParam("name") String name) {
        Category category = categoryService.getCategoryById(id);
        if (category != null) {
            category.setName(name);
            categoryService.saveCategory(category);
        }
        return "redirect:/admin/category";
    }

    // Deleting a category
    @PostMapping("/admin/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/category";
    }
}
