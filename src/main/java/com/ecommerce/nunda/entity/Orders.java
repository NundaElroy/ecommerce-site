package com.ecommerce.nunda.entity;

import com.ecommerce.nunda.enums.CartStatus;
import com.ecommerce.nunda.enums.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long order_id;

    @CreationTimestamp
    private LocalDateTime timeStamp;

    @Column
    private Double totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL)
    private Payment payment;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private  OrderStatus status = OrderStatus.PENDING;  // Default to PENDING

    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL)
    private DeliveryAddress deliveryAddress;

    public Orders(){}

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public  static  Double calculateTotalAmount(List<OrderItem> items){
        Double totalAmount = 0.0;
        for( OrderItem item : items){
               totalAmount += item.getPrice();
        }

        return totalAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        user.addOrder(this);
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        orderItems.add(orderItem);
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        deliveryAddress.setOrder(this);
        this.deliveryAddress = deliveryAddress;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
