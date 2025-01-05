package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.CartItem;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.repository.CartItemRepo;
import com.ecommerce.nunda.service.CartItemService;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImp implements CartItemService {

    private final CartItemRepo cartItemRepo;

    public CartItemServiceImp(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    public  void addItemToCart(Cart cart, Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItemRepo.save(cartItem);
    }

    @Override
    public boolean isProductInCart(Cart cart, Product product) {
        if (cart == null || cart.getCartItemList() == null || product == null) {
            return false; // Null safety check
        }

        return cart.getCartItemList().stream()
                .anyMatch(cartItem -> cartItem.getProduct().equals(product));

    }
}
