package com.ecommerce.nunda.serviceImp;


import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.formvalidators.ProductForm;
import com.ecommerce.nunda.repository.ProductRepo;
import com.ecommerce.nunda.service.CategoryService;
import com.ecommerce.nunda.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {
    private final ProductRepo productRepo;
    private final CategoryService categoryService;

    public ProductServiceImp(ProductRepo productRepo, CategoryService categoryService){
        this.productRepo = productRepo;
        this.categoryService = categoryService;
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
}
