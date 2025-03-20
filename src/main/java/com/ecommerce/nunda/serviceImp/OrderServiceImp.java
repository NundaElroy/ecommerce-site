package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.customexceptions.InsufficientProductException;
import com.ecommerce.nunda.customexceptions.UserNotFoundException;
import com.ecommerce.nunda.dto.BillingDetailsDTO;
import com.ecommerce.nunda.dto.RevenueData;
import com.ecommerce.nunda.dto.SalesData;
import com.ecommerce.nunda.dto.TopSellingProductDTO;
import com.ecommerce.nunda.entity.*;
import com.ecommerce.nunda.repository.OrderRepo;
import com.ecommerce.nunda.service.OrderService;
import com.ecommerce.nunda.service.ProductService;
import com.ecommerce.nunda.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
//        deliveryAddress.setCity(billingDetailsDTO.getCity());
//        deliveryAddress.setCountry(billingDetailsDTO.getCountry());
        deliveryAddress.setAddress(billingDetailsDTO.getAddress());
        deliveryAddress.setFullName(billingDetailsDTO.getFullName());
        deliveryAddress.setPhoneNumber(billingDetailsDTO.getPhoneNumber());
        deliveryAddress.setEmail(billingDetailsDTO.getEmail());

        return deliveryAddress;
    }


    @Override
    public SalesData getSalesByPeriod(String period) {
        DateRange dateRange = getDateRange(period);
        long totalOrders = orderRepo.countCompletedOrders(dateRange.getStartDateTime(), dateRange.getEndDateTime());
        return new SalesData(totalOrders);
    }

    @Override
    public RevenueData getRevenueByPeriod(String period) {
        DateRange dateRange = getDateRange(period);
        Double totalRevenue = orderRepo.getTotalRevenue(dateRange.getStartDateTime(), dateRange.getEndDateTime());
        return new RevenueData(totalRevenue);
    }

    @Override
    public List<TopSellingProductDTO> getTopSellingProductsByPeriod(String period) {
        DateRange dateRange = getDateRange(period);
        return orderRepo.getBestSellingProducts(dateRange.getStartDateTime(), dateRange.getEndDateTime());
    }

    private DateRange getDateRange(String period) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (period.toLowerCase()) {
            case "month":
                startDate = endDate.withDayOfMonth(1);
                break;
            case "year":
                startDate = endDate.withDayOfYear(1);
                break;
            default: // "today"
                startDate = endDate;
        }

        return new DateRange(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
    }

    // Helper inner
    // class to hold date range
    private static class DateRange {
        private final LocalDateTime startDateTime;
        private final LocalDateTime endDateTime;

        public DateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
        }

        public LocalDateTime getStartDateTime() {
            return startDateTime;
        }

        public LocalDateTime getEndDateTime() {
            return endDateTime;
        }
    }

}
