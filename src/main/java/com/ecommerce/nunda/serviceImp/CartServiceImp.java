package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.CartItem;
import com.ecommerce.nunda.repository.CartRepo;
import com.ecommerce.nunda.service.CartItemService;
import com.ecommerce.nunda.service.CartService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CartServiceImp implements CartService {

    private final CartRepo cartRepo;
    private final CartItemService cartItemService;

    public CartServiceImp(CartRepo cartRepo, CartItemService cartItemService) {
        this.cartRepo = cartRepo;
        this.cartItemService = cartItemService;
    }

    @Override
    public void saveCart(Cart cart) {
        cartRepo.save(cart);
    }

    @Override
    public Cart createCart() {
        return new Cart();
    }

    @Override
    public Cart getUserCart(Long user_id) {
//        Cart cart = cartRepo.findByUserId(user_id);
//        if (cart == null) {
//            cart = createCart();
//            cartRepo.save(cart);
//        }

        return null;
    }


    //convert to cookie cart items to cartitems
    public List<CartItem> convertCookieListToCartItems(List<String> cartProductsInCookieCart){

         return cartProductsInCookieCart.stream()
                                        .map((productid) -> {
                                              return cartItemService.createCartItem(Long.valueOf(productid));
                                         })
                                        .collect(Collectors.toList()) ;

    }


}
