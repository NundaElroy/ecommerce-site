package com.ecommerce.nunda.serviceImp;

import com.ecommerce.nunda.entity.Orders;
import com.ecommerce.nunda.repository.OrderRepo;
import com.ecommerce.nunda.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImp implements OrderService {

    private final OrderRepo orderRepo;

    public OrderServiceImp(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }


    @Override
    public List<Orders> getAllOrders() {
        return orderRepo.findAll();
    }
}
