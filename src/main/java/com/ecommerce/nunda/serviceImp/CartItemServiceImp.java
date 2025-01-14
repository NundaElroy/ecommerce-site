package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.entity.Cart;
import com.ecommerce.nunda.entity.CartItem;
import com.ecommerce.nunda.entity.Product;
import com.ecommerce.nunda.repository.CartItemRepo;
import com.ecommerce.nunda.service.CartItemService;
import com.ecommerce.nunda.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImp implements CartItemService {

    private final CartItemRepo cartItemRepo;
    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(CartItemServiceImp.class);

    public CartItemServiceImp(CartItemRepo cartItemRepo, ProductService productService) {
        this.cartItemRepo = cartItemRepo;
        this.productService = productService;
    }

    @Override
    public  void addItemToCart(Cart cart, Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItemRepo.save(cartItem);
        logger.info("Product added to cart: {}", product.getName());

    }

    @Override
    public boolean isProductInCart(Cart cart, Product product) {
        if (cart == null || cart.getCartItemList() == null || product == null) {
            return false;
        }

        return cart.getCartItemList().stream()
                .anyMatch(cartItem -> cartItem.getProduct().equals(product));

    }

    public CartItem createCartItem(Long productid){
          CartItem cartItem = new CartItem();
          cartItem.setProduct(productService.getProductById(productid));
          return  cartItem;
    }

    @Override
    public void removeItemFromCart(Cart cart, Product product) {
        if (cart == null || cart.getCartItemList() == null || product == null) {
            //Todo: throw exception that will be handled using an error page
            return;
        }

        cart.getCartItemList().removeIf(cartItem -> cartItem.getProduct().equals(product));
        //save the wishlist
        cartItemRepo.saveAll(cart.getCartItemList());
        logger.info("Product removed from wishlist: {}", product.getName());
    }
}
