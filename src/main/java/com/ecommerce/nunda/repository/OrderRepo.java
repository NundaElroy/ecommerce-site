package com.ecommerce.nunda.repository;



import com.ecommerce.nunda.dto.TopSellingProductDTO;
import com.ecommerce.nunda.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepo  extends JpaRepository<Orders,Long> {

    @Query("SELECT COUNT(o) FROM Orders o WHERE o.status = 'COMPLETE' AND o.timeStamp BETWEEN :start AND :end")
    Long countCompletedOrders(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Orders o JOIN o.payment p WHERE o.status = 'COMPLETE' AND o.timeStamp BETWEEN :start AND :end")
    Double getTotalRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
    SELECT new com.ecommerce.nunda.dto.TopSellingProductDTO(
        p.productImage, p.name, p.price, 
        SUM(oi.quantity), SUM(oi.quantity * oi.price)
    ) 
    FROM Orders o 
    JOIN o.orderItems oi 
    JOIN oi.product p
    WHERE o.status = 'COMPLETE' 
    AND o.timeStamp BETWEEN :start AND :end
    GROUP BY p.productImage, p.name, p.price
    ORDER BY SUM(oi.quantity) DESC
    LIMIT 5
""")
    List<TopSellingProductDTO> getBestSellingProducts(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


}
