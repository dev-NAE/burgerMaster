package com.itwillbs.repository;

import com.itwillbs.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    // 발주번호 생성을 위한 현재 최고번호 가져오기
    @Query("SELECT MAX(o.orderId) FROM Order o")
    String findMaxOrderId();

    @Query("SELECT s.supplierName FROM Supplier s JOIN Order o ON " +
            "s.supplierCode = o.supplierCode WHERE o.orderId = :orderId")
    String findSupplierNameByOrderId(@Param("orderId") String orderId);

    @Query("SELECT i.itemName FROM OrderItems oi JOIN Item i ON oi.itemCode = i.itemCode " +
            "WHERE oi.order = :order ORDER BY i.itemCode ASC")
    List<String> findFirstItemNameByOrder(@Param("order") Order order);

    @Query("SELECT COUNT(oi.itemCode) FROM OrderItems oi WHERE oi.order = :order")
    int findOrderItemCountByOrder(@Param("order") Order order);

}
