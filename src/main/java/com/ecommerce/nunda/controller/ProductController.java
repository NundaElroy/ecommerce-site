package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.formvalidators.ProductForm;
import com.ecommerce.nunda.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {

    private final CategoryService categoryService;

    public ProductController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/products")
    public String getProducts(){
        return "product/products";
    }

    @GetMapping("/admin/addproduct")
    public String  addProduct(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("productForm", new ProductForm());
        return "product/addproduct";
    }

    @PostMapping("/admin/addproduct")
    public String addProduct(String mmmm){
        return mmmm;
    }

}
