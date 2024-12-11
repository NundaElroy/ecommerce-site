package com.ecommerce.nunda.service;


import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.ProductImage;

import java.util.List;

public interface ProductImageService {
    List<ProductImage> getAllProductImageById(Long id);
    void save(Product product , String filename, ProductImage productImage);
    void delete(Product product , ProductImage productImage);
    ProductImage getProductImageById(Long id);
}
