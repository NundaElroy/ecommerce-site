package com.ecommerce.nunda.controller;

import com.ecommerce.nunda.customexceptions.StorageException;
import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.ProductImage;
import com.ecommerce.nunda.dto.OnAdd;
import com.ecommerce.nunda.dto.ProductForm;
import com.ecommerce.nunda.dto.PromotionsDTO;
import com.ecommerce.nunda.service.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ProductController {

    private final CategoryService categoryService;
    private final FileStorageHandlerService fileStorageHandlerService;
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ExcelFileService excelFileService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    public ProductController(CategoryService categoryService, FileStorageHandlerService fileStorageHandlerService, ProductService productService, ProductImageService productImageService, ExcelFileService excelFileService) {
        this.categoryService = categoryService;
        this.fileStorageHandlerService = fileStorageHandlerService;
        this.productService = productService;
        this.productImageService = productImageService;
        this.excelFileService = excelFileService;
    }

    //products page/template
    @GetMapping("/admin/products")
    public String getProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "product/products";
    }

    //get template for adding new product
    @GetMapping("/admin/addproduct")
    public String addProduct(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("productForm", new ProductForm());
        return "product/addproduct";
    }

    //add new product
    @PostMapping("/admin/addproduct")
    public String addProduct(@Validated(OnAdd.class) @ModelAttribute("productForm") ProductForm productForm, BindingResult bindingResult
            , Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
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

    //delete product
    @PostMapping("/admin/deleteproduct/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    //view product
    @GetMapping("/admin/viewproduct/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product/viewproduct";
    }

    //get edit product template
    @GetMapping("/admin/editproduct/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        ProductForm productForm = productService.convertToForm(product);
        model.addAttribute("productForm", productForm);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("product_id", id);

        return "product/editproduct";
    }

    //update/edited product
    @PostMapping("/admin/editproduct")
    public String updateProduct(@Valid @ModelAttribute ProductForm productForm, BindingResult result, Model model,
                                @RequestParam("productId") Long id) {
        if (result.hasErrors()) {
            model.addAttribute("product_id", id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/editproduct";
        }
        Product product = productService.convertToEntity(productForm, id);
        productService.saveProduct(product);

        //redirect
        return "redirect:/admin/products";
    }

    //get template for adding new images
    @GetMapping("/admin/changeimage/{id}")
    public String manageImages(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        List<ProductImage> imageFilenames = productImageService.getAllProductImageById(id);

        // Add data to the model
        model.addAttribute("productId", id);
        model.addAttribute("mainImage", product.getProductImage());
        model.addAttribute("images", imageFilenames);

        // Return the template name
        return "product/manageproductimages";
    }

    //post mapping for adding new images
    @PostMapping("/admin/addimage")
    public String addNewImages(@RequestParam("imageFile") MultipartFile file, @RequestParam("productId") Long id) {
        //product associated with image being added
        Product product = productService.getProductById(id);
        ProductImage productImage = new ProductImage();
        String filepath = fileStorageHandlerService.storeProductImages(file);

        //save image
        productImageService.save(product, filepath, productImage);

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

    //bulk upload using excel file
    @PostMapping("/admin/addProduct/bulkUpload")
    public String addNewProductsInBulk(@RequestParam("file") MultipartFile file) {
        logger.info("Bulk upload initiated for file: {}", file.getOriginalFilename());

        String filename = excelFileService.saveFile(file);
        logger.info("File saved successfully at path: {}", filename);

        try (FileInputStream fis = new FileInputStream(filename)) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            logger.info("Reading sheet: '{}' from workbook", sheet.getSheetName());

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    logger.debug("Skipping header row");
                    continue; // Skip header row
                }

                // Extract standard fields from the row
                String name = excelFileService.getCellValue(row.getCell(0));
                String description = excelFileService.getCellValue(row.getCell(1));
                String priceStr = excelFileService.getCellValue(row.getCell(2));
                String stockQuantityStr = excelFileService.getCellValue(row.getCell(3));
                String categoryIdStr = excelFileService.getCellValue(row.getCell(4));

                // Log extracted values
                logger.debug("Row {} - Name: {}, Description: {}, Price: {}, Stock: {}, Category: {}",
                        row.getRowNum(), name, description, priceStr, stockQuantityStr, categoryIdStr);

                try {
                    double price = Double.parseDouble(priceStr);
                    int stockQuantity = Integer.parseInt(stockQuantityStr);
                    Long categoryId = Long.parseLong(categoryIdStr);

                    // Collect image paths dynamically (start from column index 5)
                    List<String> imagePaths = new ArrayList<>();
                    for (int colIndex = 5; colIndex < row.getLastCellNum(); colIndex++) {
                        String imagePath = excelFileService.getCellValue(row.getCell(colIndex));
                        if (imagePath != null && !imagePath.isEmpty()) {
                            imagePaths.add(imagePath);
                        }
                    }

                    // Log image paths
                    logger.debug("Row {} - Image Paths: {}", row.getRowNum(), imagePaths);

                    // Process product and images
                    productService.saveProductAndImages(name, description, price, stockQuantity, categoryId, imagePaths);
                    logger.info("Row {} processed successfully", row.getRowNum());

                } catch (NumberFormatException e) {
                    logger.error("Error parsing numerical fields in row {}: {}", row.getRowNum(), e.getMessage());
                }
            }

            workbook.close();
            logger.info("Bulk upload completed successfully for file: {}", file.getOriginalFilename());
            return "redirect:/admin/products"; // Redirect or return success message

        } catch (IOException e) {
            logger.error("An error occurred while processing the file: {}", e.getMessage());
            throw new StorageException("An error occurred. Storage Failed", e);
        }
    }

    //promotions template
    @GetMapping("/admin/promotions")
    public String getPromotions(Model model){
        model.addAttribute("promotionDTO", new PromotionsDTO());
        model.addAttribute("products",productService.getAllProducts());
        return "product/promotions";

    }

    //add promotion
    @PostMapping("/admin/addpromotion")
    public String addPromotion(@Valid @ModelAttribute("promotionDTO") PromotionsDTO promotionsDTO,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            // Log detailed errors
            bindingResult.getAllErrors().forEach(error -> {
                logger.error("Validation error: {}", error.getDefaultMessage());
            });

            // Reload data for the modal
            model.addAttribute("promotionDTO", promotionsDTO);
            model.addAttribute("products", productService.getAllProducts());
            model.addAttribute("showModal", true); // Signal to reopen modal

            return "product/promotions";
        }

        productService.addPromotion(promotionsDTO);
        return "redirect:/admin/promotions";
    }

    //delete promotion
    @PostMapping("/admin/deletepromotion/{id}")
    public String deletePromotion(@PathVariable("id") Long productid){
        productService.deletePromotion(productid);
        return "redirect:/admin/promotions";
    }

}