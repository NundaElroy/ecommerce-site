package com.ecommerce.nunda.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String address;
    private String city ;
    private String country;
    private String phoneNumber;


    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    public DeliveryAddress() {
    }

}
