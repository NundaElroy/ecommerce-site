package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.service.CategoryService;
import com.ecommerce.nunda.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductUserController {
    private final ProductService productService;

    public ProductUserController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/hotdeals")
    public String hotDeals(Model model) {
        // Get all active promotions
        model.addAttribute("products", productService.getActivePromotions());
        return "product/hotdeals";
    }

    @GetMapping("/view_product/{id}")
    public String viewProduct(@PathVariable("id")Long id,Model model){
        model.addAttribute("product", productService.getProductById(id));
        return "product/viewindividualproduct";
    }
}
