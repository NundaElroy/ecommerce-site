package com.ecommerce.nunda.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Product> products;

    public Category(){}

    public void addProduct(Product product){
        products.add(product);
        product.setCategory(this);
    }

    public void removeProduct(Product product){
        products.remove(product);
        product.setCategory(null);
    }

    public Long getCategory_id() {
        return category_id;
    }
    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
