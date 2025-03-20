package com.ecommerce.nunda.dto;

import com.ecommerce.nunda.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class OrderDetailsDTO {
    private  Double totalPrice  = 5000.0;
    private  Double deliveryFee =  5000.0;
    private List<Product> products = new ArrayList<>();


    @Getter
    @Setter
    class  Product{
        private Long id;
        private String name;
        private Double price;
        private Integer quantity;
    }


    public void addCartItems(List<CartItem> cartItemList){
        for (CartItem cartItem : cartItemList) {
            Product product = new Product();
            product.setId(cartItem.getProduct().getProduct_id());
            product.setName(cartItem.getProduct().getName());
            product.setQuantity(cartItem.getQuantity());

            Double price;

            //check if product has discount and is still valid
            Double price1 = cartItem.getProduct().getPrice();
            if(cartItem.getProduct().getDiscountEndTime() != null && LocalDateTime.now().isBefore(cartItem.getProduct().getDiscountEndTime())){
                price =   (price1 - ( price1 * cartItem.getProduct().getDiscountPercentage()/100) ) * cartItem.getQuantity();
            }else{
                price = price1 * cartItem.getQuantity();
            }

            totalPrice += price;
            product.setPrice(price);
            products.add(product);
        }
    }


}
