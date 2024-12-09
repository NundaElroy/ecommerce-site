package com.ecommerce.nunda.serviceImp;


import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.repository.ProductRepo;
import com.ecommerce.nunda.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {
    private final ProductRepo productRepo;

    public ProductServiceImp(ProductRepo productRepo){
        this.productRepo = productRepo;
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
}
