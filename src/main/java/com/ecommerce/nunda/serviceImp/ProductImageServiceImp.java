package com.ecommerce.nunda.serviceImp;


import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.entity.ProductImage;
import com.ecommerce.nunda.repository.ProductImageRepo;
import com.ecommerce.nunda.service.ProductImageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageServiceImp implements ProductImageService {
    private final ProductImageRepo productImageRepo;

    public ProductImageServiceImp(ProductImageRepo productImageRepo) {
        this.productImageRepo = productImageRepo;
    }

    @Override
    public List<ProductImage> getAllProductImageById(Long id) {
        return productImageRepo.getAllProductImagesByProductId(id);
    }

    @Override
    public void save(Product product, String filename, ProductImage productImage) {
        productImage.setImagePath(filename);
        product.addProductImages(productImage);
        productImageRepo.save(productImage);
    }

    public void delete(Product product , ProductImage productImage){
        product.removeProductImage(productImage);
        productImageRepo.delete(productImage);
    }

    @Override
    public ProductImage getProductImageById(Long id) {
        return productImageRepo.getReferenceById(id);

    }

}
