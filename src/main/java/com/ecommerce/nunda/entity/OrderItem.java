package com.ecommerce.nunda.entity;

import jakarta.persistence.*;


@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_item_id;

    private Integer quantity;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public OrderItem(){}




    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Long getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(Long order_item_id) {
        this.order_item_id = order_item_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

//SELECT
//product.product_id,
//product.product_image,
//product.name,
//COUNT(order_item.product_id) AS sold,
//SUM(order_item.price * order_item.quantity) AS revenue
//FROM order_item
//INNER JOIN product ON order_item.product_id = product.product_id
//INNER JOIN orders ON order_item.order_id = orders.order_id
//WHERE orders.time_stamp BETWEEN DATE_FORMAT(NOW(), '%Y-01-01') AND NOW()
//GROUP BY product.product_id, product.product_image, product.name;