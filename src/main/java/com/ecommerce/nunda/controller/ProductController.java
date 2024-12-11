package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.ProductImage;
import com.ecommerce.nunda.formvalidators.OnAdd;
import com.ecommerce.nunda.formvalidators.ProductForm;
import com.ecommerce.nunda.service.CategoryService;
import com.ecommerce.nunda.service.FileStorageHandlerService;
import com.ecommerce.nunda.service.ProductImageService;
import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.serviceImp.FileStorageHandlerServiceImp;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import java.io.IOException;

@Controller
public class ProductController {

    private final CategoryService categoryService;
    private final FileStorageHandlerService  fileStorageHandlerService;
    private final ProductService productService;
    private final ProductImageService productImageService;

    public ProductController(CategoryService categoryService, FileStorageHandlerService fileStorageHandlerService, ProductService productService, ProductImageService productImageService){
        this.categoryService = categoryService;
        this.fileStorageHandlerService = fileStorageHandlerService;
        this.productService = productService;
        this.productImageService = productImageService;
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
    public String addProduct(@Validated(OnAdd.class)  @ModelAttribute("productForm")  ProductForm productForm, BindingResult bindingResult
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

    @PostMapping("/admin/deleteproduct/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/viewproduct/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product/viewproduct";
    }


    @GetMapping("/admin/editproduct/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        ProductForm productForm = productService.convertToForm(product);
        model.addAttribute("productForm", productForm);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("product_id", id);

        return "product/editproduct";
    }

    @PostMapping("/admin/editproduct")
    public String updateProduct(@Valid @ModelAttribute ProductForm productForm, BindingResult result, Model model,
                                @RequestParam("productId") Long id) {
        if (result.hasErrors()) {
            model.addAttribute("product_id", id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/editproduct";
        }
        Product product = productService.convertToEntity(productForm,id);
        productService.saveProduct(product);

        //redirect
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/changeimage/{id}")
    public String manageImages(@PathVariable Long id , Model model){
        Product product = productService.getProductById(id);
        List<ProductImage> imageFilenames = productImageService.getAllProductImageById(id);

        // Add data to the model
        model.addAttribute("productId", id);
        model.addAttribute("mainImage", product.getProductImage());
        model.addAttribute("images", imageFilenames);

        // Return the template name
        return "product/manageproductimages";
    }

    @PostMapping("/admin/addimage")
    public String addNewImages(@RequestParam("imageFile")MultipartFile file , @RequestParam("productId") Long id){
       //product associated with image being added
        Product product  = productService.getProductById(id);
        ProductImage productImage = new ProductImage();
        String filepath = fileStorageHandlerService.storeProductImages(file);

        //save image
        productImageService.save(product,filepath,productImage);

        return "redirect:/admin/changeimage/" + id;


    }

    //deleteimage
    @PostMapping("/admin/deleteimage/{id}")
    public String deleteImage(@PathVariable Long id) {
        ProductImage productImage = productImageService.getProductImageById(id);
        Product product = productImage.getProduct();

        // delete image
        productImageService.delete(product, productImage);
        // id of product associated with image
        Long product_id = product.getProduct_id();

        // redirect back to change image after deletion
        return "redirect:/admin/changeimage/" + product_id;
    }







}
