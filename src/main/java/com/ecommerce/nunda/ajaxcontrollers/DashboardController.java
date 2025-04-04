package com.ecommerce.nunda.ajaxcontrollers;

import com.ecommerce.nunda.dto.RevenueData;
import com.ecommerce.nunda.dto.SalesData;
import com.ecommerce.nunda.dto.TopSellingProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.ecommerce.nunda.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DashboardController {


    private final OrderService orderService;

    public DashboardController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/sales")
    public ResponseEntity<SalesData> getSalesData(@RequestParam(value = "period", defaultValue = "today") String period) {
        SalesData salesData = orderService.getSalesByPeriod(period);
        return ResponseEntity.ok(salesData);
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueData> getRevenueData(@RequestParam(value = "period", defaultValue = "today") String period) {
        RevenueData  revenueData = orderService.getRevenueByPeriod(period);
        return ResponseEntity.ok(revenueData);
    }

    @GetMapping("/top-selling")
    public ResponseEntity<List<TopSellingProductDTO>> getTopSellingProducts(@RequestParam(value = "period", defaultValue = "today") String period) {
        List<TopSellingProductDTO>  topSellingProductData = orderService.getTopSellingProductsByPeriod(period);
        return ResponseEntity.ok(topSellingProductData);
    }
}


