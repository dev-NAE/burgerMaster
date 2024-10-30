package com.itwillbs.repository;

import com.itwillbs.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT MAX(orderId) FROM Order")
    String findMaxOrderId();

}
