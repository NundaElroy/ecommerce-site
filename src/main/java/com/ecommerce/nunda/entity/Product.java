package com.ecommerce.nunda.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;



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
    private Double ratings = 0.0;
    private String productImage;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;




    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItemList;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews  =  new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> wishlistItemList;



    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    //section dealing with promotions and discounts
    @Column
    private Double discountPercentage;


    @Column
    private LocalDateTime discountStartTime;

    @Column
    private LocalDateTime discountEndTime;



    // Constructors, getters, and setters
    public Product() {}

    public Product(String name, String description, double price, int stockQuantity, Category category, String mainImage) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.productImage = mainImage;
    }

    //get the images associated with the product
    public List<ProductImage> getProductImages() {
        return productImages;
    }

    //add image to the list of products associated with it
    public void addProductImages(ProductImage productImage) {
        productImage.setProduct(this);
        productImages.add(productImage);

    }
    public  void removeProductImage(ProductImage productImage){
        productImage.setProduct(null);
        productImages.remove(productImage);
    }


    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Double getRatings() {
        return ratings;
    }

    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

    public void addCartItem(CartItem cartItem) {
        cartItemList.add(cartItem);

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

    public LocalDateTime getDiscountStartTime() {
        return discountStartTime;
    }

    public void setDiscountStartTime(LocalDateTime discountStartTime) {
        this.discountStartTime = discountStartTime;
    }

    public LocalDateTime getDiscountEndTime() {
        return discountEndTime;
    }

    public void setDiscountEndTime(LocalDateTime discountEndTime) {
        this.discountEndTime = discountEndTime;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReviews(Review review) {
        review.setProduct(this);
        reviews.add(review);
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Double getProductPrice(){
        //check if product has discount and is still valid
        if(discountEndTime != null && LocalDateTime.now().isBefore(discountEndTime)){
            return   price - ( price * discountPercentage/100);
        }

        return price;
    }


}
