package com.itwillbs.repository;

import com.itwillbs.entity.Order;
import com.itwillbs.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, String> {

    List<OrderItems> findByOrder(Order order);

    void deleteByOrder(Order order);

}