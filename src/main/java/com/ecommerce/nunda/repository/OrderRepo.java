package com.ecommerce.nunda.repository;



import com.ecommerce.nunda.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderRepo  extends JpaRepository<Orders,Long> {

    @Query("SELECT COUNT(o) FROM Orders o WHERE o.status = 'COMPLETE' AND o.timeStamp BETWEEN :start AND :end")
    Long countCompletedOrders(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Orders o JOIN o.payment p WHERE o.status = 'COMPLETE' AND o.timeStamp BETWEEN :start AND :end")
    Double getTotalRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


}
