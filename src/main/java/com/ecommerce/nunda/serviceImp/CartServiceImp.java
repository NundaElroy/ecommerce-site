package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.ecommerce.nunda.entity.*;
import com.ecommerce.nunda.formvalidators.CartItemsDto;
import com.ecommerce.nunda.repository.CartRepo;
import com.ecommerce.nunda.service.*;
import com.ecommerce.nunda.enums.CartStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




@Service
public class CartServiceImp implements CartService {

    private final CartRepo cartRepo;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;
    private final CookieService cookieService;
    private final JacksonService jacksonService;
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImp.class);

    public CartServiceImp(CartRepo cartRepo, CartItemService cartItemService, UserService userService, ProductService productService, CookieService cookieService, JacksonService jacksonService) {
        this.cartRepo = cartRepo;
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService = productService;
        this.cookieService = cookieService;
        this.jacksonService = jacksonService;
    }

    @Override
    public void saveCart(Cart cart) {
        cartRepo.save(cart);
    }

    @Override
    public Cart createCart() {
        logger.info("Creating a new cart for user ");
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

    @Override
    @Transactional
    public void addProductToUserCart(String userEmail, Long productId)  {
        User user = userService.getUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        logger.info("User {} is adding product {} to cart", userEmail, productId);

        Cart cart = Optional.ofNullable(user.getCart())
                .orElseGet(this::createCart);

        Product product = productService.getProductById(productId,"CartServiceImp");

        if (cartItemService.isProductInCart(cart, product)) {
            throw new IllegalArgumentException("Product already in cart");
        }

        cartItemService.addItemToCart(cart, product);
        logger.info("Product {} successfully added to cart for user {}", productId, userEmail);

    }

    @Override
    public String addProductToGuestCart(String usercart, Long productId) throws JsonProcessingException {
        Product product = productService.getProductById(productId, "CartServiceImp");

        List<String> productIds =  convertStringCookieToList(usercart);

        if (productIds.contains(productId.toString())) {
            logger.info("Product {} already in cart", productId);
            throw new IllegalArgumentException("Product already in cart");
        }

        productIds.add(productId.toString());



        return jacksonService.convertProductIdsToString(productIds);


    }

    @Override
    public List<CartItemsDto>  convertCartItemsToCartItemsDTO(List<CartItem> cartItemList){
        List<CartItemsDto>  cartItemsDtos = new ArrayList<>();

        for (CartItem cartItem : cartItemList){
            CartItemsDto cartItemsDto = new CartItemsDto();
            cartItemsDto.setProduct_id(cartItem.getProduct().getProduct_id());
            cartItemsDto.setName(cartItem.getProduct().getName());
            cartItemsDto.setPrice(cartItem.getProduct().getPrice());
            cartItemsDto.setProductImage(cartItem.getProduct().getProductImage());
            cartItemsDto.setDescription(cartItem.getProduct().getDescription());
            cartItemsDto.setDiscountPercentage(cartItem.getProduct().getDiscountPercentage());
            cartItemsDto.setStockQuantity(cartItem.getProduct().getStockQuantity());
            cartItemsDtos.add(cartItemsDto);
        }

        return cartItemsDtos;
    }

    @Override
    public void removeProductFromCart(Cart cart, Long productId) {
        Product product = productService.getProductById(productId,"CartServiceImp");
        cartItemService.removeItemFromCart(cart, product);
        logger.info("Product {} removed from cart for userid {}", productId, cart.getUser().getUser_id());
    }

    @Override
    public boolean checkIfProductsExistAndQuantityIsSufficient(List<CartItemsDto> items) {
        for(CartItemsDto item : items){
            Product product = productService.getProductById(item.getProduct_id(),"CartServiceImp");
            if (product == null || product.getStockQuantity() < item.getQuantity()){
                return false;
            }
        }
        return true;
    }

    @Transactional
    @Override
    public boolean changeCartStatus(String email, List<CartItemsDto> items) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Cart cart = user.getCart();
        if (cart == null){
            return false;
        }

        if (!updateCartItemsQuantity(items, cart)){
            return false;
        }


        cart.setStatus(CartStatus.CHECKOUT);
        cartRepo.save(cart);
        return true;
    }


    private boolean updateCartItemsQuantity(List<CartItemsDto> items, Cart cart) {

        for (CartItemsDto item : items){
            Product product = productService.getProductById(item.getProduct_id(),"CartServiceImp");
            if (product == null){
                return false;
            }
            cartItemService.updateCartItemQuantity(cart, product, item.getQuantity());
        }

        return true;
    }


    @Override
    public String removeProductFromGuestCart(String userCart, Long product_id) throws JsonProcessingException {
        List<String> productIds = convertStringCookieToList(userCart);

        if(!productIds.contains(product_id.toString()) ||  productIds.isEmpty()){
            logger.info("Product {} not in cart", product_id);
            throw new IllegalArgumentException("Product not in cart");
        }

        productIds.remove(product_id.toString());

        return jacksonService.convertProductIdsToString(productIds);
    }


    //convert cookie string to list or return empty list
    private List<String>  convertStringCookieToList(String userCart){

        return  Optional.ofNullable(userCart)
                .map(cookie -> {
                    try {
                        return jacksonService.convertStringCookieToList(cookieService.decodeCookie(cookie));
                    } catch (JsonProcessingException e) {
                        throw new IllegalArgumentException(e);
                    }
                })
                .orElse(new ArrayList<>());

    }

    @Override
    public void moveCookieCartItemsToCartForRegisteringUser(String userCart,String email) throws JsonProcessingException {

        List<CartItem> items = convertCookieListToCartItems(
                jacksonService.convertStringCookieToList(cookieService.decodeCookie(userCart)));

        items.stream().forEach( (item) ->
                addProductToUserCart(email, item.getProduct().getProduct_id())
        );

    }



}
