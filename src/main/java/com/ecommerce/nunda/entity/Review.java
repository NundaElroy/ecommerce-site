package com.ecommerce.nunda.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT") // For longer reviews
    private String review;

    private Double rating;

    private LocalDateTime createdAt;

    // Set createdAt automatically before persisting
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Relationship with Product (assuming you want to link reviews to products)
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
