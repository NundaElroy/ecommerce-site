package com.ecommerce.nunda.service;

import com.ecommerce.nunda.entity.Product;

import java.util.List;

public interface ProductService {
    void saveProduct(Product product);
    List<Product> getAllProducts();

    void deleteProduct(Long id);

    Product getProductById(Long id);
}
