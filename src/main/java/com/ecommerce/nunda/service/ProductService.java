package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.formvalidators.ProductForm;

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
}
