package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Category;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.formvalidators.ProductForm;
import com.ecommerce.nunda.formvalidators.PromotionsDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    void saveProduct(Product product);
    List<Product> getAllProducts();

    void deleteProduct(Long id);

    Product getProductById(Long id);

    ProductForm convertToForm(Product product);

    Product convertToEntity(ProductForm productForm, Long id);

    void saveProductAndImages(String name, String description, double price, int stockQuantity,
                         Long categoryId, List<String> imagePaths);

    void addPromotion(PromotionsDTO promotionsDTO);

    void deletePromotion(Long productid);

    List<Product>  getActivePromotions();

    List<Product> getRelatedProducts(Product product);

    Page<Product> getProductsByCategory(int page, int size,Long id);

    Page<Product> getNewProduct(int page , int size);
}
