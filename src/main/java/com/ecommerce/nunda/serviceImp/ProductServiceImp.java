package com.ecommerce.nunda.serviceImp;


import com.ecommerce.nunda.controller.ProductController;
import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.ProductImage;
import com.ecommerce.nunda.formvalidators.ProductForm;
import com.ecommerce.nunda.formvalidators.PromotionsDTO;
import com.ecommerce.nunda.repository.ProductRepo;
import com.ecommerce.nunda.service.CategoryService;
import com.ecommerce.nunda.service.ProductImageService;
import com.ecommerce.nunda.service.ProductService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {
    private final ProductRepo productRepo;
    private final CategoryService categoryService;
    private final ProductImageService productImageService;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);

    public ProductServiceImp(ProductRepo productRepo, CategoryService categoryService, ProductImageService productImageService){
        this.productRepo = productRepo;
        this.categoryService = categoryService;
        this.productImageService = productImageService;
    }
    @Override
    public void saveProduct(Product product) {
     productRepo.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
       return productRepo.findAll();
    }
    @Override
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public ProductForm convertToForm(Product product) {
        ProductForm productForm = new ProductForm();
        productForm.setName(product.getName());
        productForm.setStockQuantity(product.getStockQuantity());
//        productForm.setCategory(product.getCategory().getCategory_id());
        productForm.setDescription(product.getDescription());
        productForm.setPrice(product.getPrice());

        return  productForm;

    }

    @Override
    public Product convertToEntity(ProductForm productForm, Long id) {
        Product product = productRepo.findById(id).orElse(null);
        assert product != null;
        product.setName(productForm.getName());
        product.setCategory(categoryService.getCategoryById(productForm.getCategory()));
        product.setStockQuantity(product.getStockQuantity());
        product.setPrice(productForm.getPrice());
        product.setDescription(productForm.getDescription());
        return product;
    }
    @Transactional
    public void saveProductAndImages(String name, String description, double price, int stockQuantity,
                                     Long categoryId, List<String> imagePaths) {
        logger.info("Saving product with name: {}, description: {}, price: {}, stock: {}, category ID: {}",
                name, description, price, stockQuantity, categoryId);

        // Retrieve category entity
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            logger.error("Invalid category ID: {}", categoryId);
            throw new IllegalArgumentException("Invalid category ID: " + categoryId);
        }
        logger.debug("Category retrieved successfully: {}", category.getName());

        // Save the product with the main image
        String mainImage = imagePaths.isEmpty() ? null : imagePaths.get(0);
        Product product = new Product(name, description, price, stockQuantity, category, mainImage);
        Product productAfterPersistence = productRepo.save(product);
        logger.info("Product saved successfully with ID: {}", productAfterPersistence.getProduct_id());

        // Save additional images if they exist
        if (imagePaths.size() > 1) {
            logger.debug("Saving additional images for product ID: {}", productAfterPersistence.getProduct_id());
            for (int i = 1; i < imagePaths.size(); i++) {
                ProductImage productImage = new ProductImage();
                productImageService.save(productAfterPersistence, imagePaths.get(i), productImage);
                logger.debug("Saved additional image: {}", imagePaths.get(i));
            }
        } else {
            logger.debug("No additional images to save for product ID: {}", productAfterPersistence.getProduct_id());
        }

        logger.info("Product and images saved successfully for product ID: {}", productAfterPersistence.getProduct_id());
    }

    @Override
    public void addPromotion(PromotionsDTO promotionsDTO) {
        Product product = productRepo.getReferenceById(promotionsDTO.getProductId());
        product.setDiscountPercentage(promotionsDTO.getDiscountPercentage());
        product.setDiscountStartTime(promotionsDTO.getDiscountStartTime());
        product.setDiscountEndTime(promotionsDTO.getDiscountEndTime());

        productRepo.save(product);

        logger.info("Product promotions saved successfully for product ID: {}", product.getProduct_id());
    }

    @Override
    public void deletePromotion(Long productid) {
        Product product = productRepo.getReferenceById(productid);
        product.setDiscountPercentage(null);
        product.setDiscountStartTime(null);
        product.setDiscountEndTime(null);

        productRepo.save(product);

        logger.info("Product promotions deleted successfully for product ID: {}", product.getProduct_id());
    }


}
