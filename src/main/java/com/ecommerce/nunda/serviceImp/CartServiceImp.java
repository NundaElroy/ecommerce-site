package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.repository.CartRepo;
import com.ecommerce.nunda.service.CartService;
import org.springframework.stereotype.Service;


@Service
public class CartServiceImp implements CartService {

    private final CartRepo cartRepo;

    public CartServiceImp(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    @Override
    public void saveCart(Cart cart) {
        cartRepo.save(cart);
    }

    @Override
    public Cart createCart() {
        return new Cart();
    }


}
