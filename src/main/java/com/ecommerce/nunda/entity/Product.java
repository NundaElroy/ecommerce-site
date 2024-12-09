package com.ecommerce.nunda.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_id;

    private String name;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private Double price;
    private Integer stockQuantity;

    // New ratings field
    private Double ratings;
    private String productImage;

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItemList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    // Constructors, getters, and setters
    public Product() {}

    public Double getRatings() {
        return ratings;
    }

    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

    public void addCartItem(CartItem cartItem) {
        cartItemList.add(cartItem);
        cartItem.setProduct(this);
    }

    public void removeCartItem(CartItem cartItem) {
        cartItemList.remove(cartItem);
        cartItem.setProduct(null);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {

        if (category != null) {
            this.category = category;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }
}
