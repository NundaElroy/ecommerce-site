package com.ecommerce.nunda.controller;


import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.service.CategoryService;
import com.ecommerce.nunda.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public UserController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    //guest page for non auth users
    @GetMapping({"/guest","","/home"})
    public String index(Model model ) {

        Page<Product> page = productService.getNewProduct(0,5);
        model.addAttribute("newProducts",page.getContent());
        return "user/guest_user" ;
    }

//    @GetMapping("/customer/home")
//    public String authenticatedUserHomePage() {
//        return "user/guest_user" ;
//    }


}
