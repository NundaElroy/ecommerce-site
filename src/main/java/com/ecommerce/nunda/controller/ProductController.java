package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.formvalidators.ProductForm;
import com.ecommerce.nunda.service.CategoryService;
import com.ecommerce.nunda.service.FileStorageHandlerService;
import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.serviceImp.FileStorageHandlerServiceImp;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import java.io.IOException;

@Controller
public class ProductController {

    private final CategoryService categoryService;
    private final FileStorageHandlerService  fileStorageHandlerService;
    private final ProductService productService;

    public ProductController(CategoryService categoryService, FileStorageHandlerService fileStorageHandlerService, ProductService productService){
        this.categoryService = categoryService;
        this.fileStorageHandlerService = fileStorageHandlerService;
        this.productService = productService;
    }

    @GetMapping("/admin/products")
    public String getProducts(Model model){
        model.addAttribute("products",productService.getAllProducts());
        return "product/products";
    }

    @GetMapping("/admin/addproduct")
    public String  addProduct(Model model){
        model.addAttribute("categories",categoryService.getAllCategories());
        model.addAttribute("productForm", new ProductForm());
        return "product/addproduct";
    }

    @PostMapping("/admin/addproduct")
    public String addProduct(@Valid  @ModelAttribute("productForm")  ProductForm productForm,BindingResult bindingResult
                             , Model model)  {

        if(bindingResult.hasErrors()){
            model.addAttribute("categories",categoryService.getAllCategories());
            return "product/addproduct";
        }

        /*
        handling the saving of the file first
        */
        MultipartFile file = productForm.getImage();
        String generatedUniqueFilename = fileStorageHandlerService.storeProductImages(file);


        Product product = new Product();
        product.setName(productForm.getName());
        product.setDescription(productForm.getDescription());
        product.setCategory(categoryService.getCategoryById(productForm.getCategory()));
        product.setPrice(productForm.getPrice());
        product.setStockQuantity(productForm.getStockQuantity());
        product.setProductImage(generatedUniqueFilename);


        // Set the product to the category's product list
        Category category = product.getCategory();
        if (category != null) {
            category.addProduct(product);
        }

        productService.saveProduct(product);




        return "redirect:/admin/products";
    }

    @GetMapping
    public String deleteProduct(@RequestParam("product_id") Long id){
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/editproduct.html")
    public String editProduct(@RequestParam("product_id") Long id, Model model){
        Product product = productService.getProductById(id);
        ProductForm productForm = new ProductForm();
        productForm.setName(product.getName());
        productForm.setDescription(product.getDescription());
        productForm.setCategory(product.getCategory().getCategory_id());
        productForm.setPrice(product.getPrice());
        productForm.setStockQuantity(product.getStockQuantity());

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/editproduct")
    public String getProductForm(@RequestParam("id") Long productId, Model model) {
        // Retrieve the product from the database using the product ID
        Product product = productService.getProductById(productId);

        if (product != null) {
            model.addAttribute("productForm", product);
        } else {
            // Redirect to an error page or show a message if the product doesn't exist
            model.addAttribute("errorMessage", "Product not found");
            return "error"; // Or any error page template
        }

        // Optionally, load categories or other required data
        List<Category> categories =  categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        return "editproduct"; // This should be the name of your Thymeleaf template
    }

}
