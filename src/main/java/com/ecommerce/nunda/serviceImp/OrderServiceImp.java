package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.customexceptions.InsufficientProductException;
import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.entity.*;
import com.ecommerce.nunda.repository.OrderRepo;
import com.ecommerce.nunda.service.OrderService;
import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    private final OrderRepo orderRepo;
    private final UserService userService;
    private  final ProductService productService;


    public OrderServiceImp(OrderRepo orderRepo, UserService userService, ProductService productService) {
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.productService = productService;
    }


    @Override
    public List<Orders> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    @Transactional
    public Orders createOrder( String email , BillingDetailsDTO billingDetailsDTO) {
        User user = userService.getUserByEmail(email).orElseThrow(()->
                    new UserNotFoundException("user not found"));

        List<CartItem> cartItemList = user.getCart().getCartItemList();

        //check for stock level
        checkProductStockLevels(cartItemList);

        return saveCreatedOrder(cartItemList, user,billingDetailsDTO);

    }


    private void checkProductStockLevels(List<CartItem> cartItemList){
        cartItemList.forEach((item) -> {
                    Product product = item.getProduct();
                    if(item.getQuantity() > product.getStockQuantity() ){
                        throw new InsufficientProductException("Requested quantity for "
                                + product.getName() + " exceeds stock. Available: " + product.getStockQuantity());

                    }

                }

        );

    }


    private Orders saveCreatedOrder(List<CartItem> cartItemList,User user,BillingDetailsDTO billingDetailsDTO){

        Orders order = new Orders();
        order.setUser(user);
        order.setDeliveryAddress(createDeliveryAddress(billingDetailsDTO));

        cartItemList.forEach((item) -> {
                    Product product = item.getProduct();
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product);
                    orderItem.setPrice(product.getProductPrice());
                    orderItem.setQuantity(item.getQuantity());
                    order.addOrderItem(orderItem);
                }
        );

        order.setTotalAmount(Orders.calculateTotalAmount(order.getOrderItems()));

        return orderRepo.save(order);


    }

    private DeliveryAddress createDeliveryAddress(BillingDetailsDTO billingDetailsDTO){

        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setCity(billingDetailsDTO.getCity());
        deliveryAddress.setCountry(billingDetailsDTO.getCountry());
        deliveryAddress.setAddress(billingDetailsDTO.getAddress());
        deliveryAddress.setFullName(billingDetailsDTO.getFullName());
        deliveryAddress.setPhoneNumber(billingDetailsDTO.getPhoneNumber());
        deliveryAddress.setEmail(billingDetailsDTO.getEmail());

        return deliveryAddress;
    }


}
