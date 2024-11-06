package com.itwillbs.repository;

import com.itwillbs.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    // 발주번호 생성을 위한 현재 최고번호 가져오기
    @Query("SELECT MAX(o.orderId) FROM Order o")
    String findMaxOrderId();

    @Query("SELECT o.supplier.supplierName FROM Order o WHERE o.orderId = :orderId")
    String findSupplierNameByOrderId(@Param("orderId") String orderId);

    @Query("SELECT oi.item.itemName FROM OrderItems oi WHERE oi.order = :order ORDER BY oi.item.itemCode ASC")
    List<String> findFirstItemNameByOrder(@Param("order") Order order);

    @Query("SELECT COUNT(oi.item.itemCode) FROM OrderItems oi WHERE oi.order = :order")
    int findOrderItemCountByOrder(@Param("order") Order order);

    @Query("SELECT o FROM Order o " +
            "JOIN o.supplier s " +
            "LEFT JOIN o.orderItems oi " +
            "LEFT JOIN oi.item i " +
            "WHERE (:status IS NULL OR o.status = :status) AND " +
            "(:supplierName IS NULL OR s.supplierName LIKE :supplierName) AND " +
            "(:orderDateStart IS NULL OR o.orderDate >= :orderDateStart) AND " +
            "(:orderDateEnd IS NULL OR o.orderDate <= :orderDateEnd) AND " +
            "(:itemName IS NULL OR i.itemName LIKE :itemName) AND " +
            "(:dueDateStart IS NULL OR o.dueDate >= :dueDateStart) AND " +
            "(:dueDateEnd IS NULL OR o.dueDate <= :dueDateEnd)")
    List<Order> findOrdersByConditions(
            @Param("status") String status,
            @Param("supplierName") String supplierName,
            @Param("orderDateStart") Timestamp orderDateStart,
            @Param("orderDateEnd") Timestamp orderDateEnd,
            @Param("itemName") String itemName,
            @Param("dueDateStart") Timestamp dueDateStart,
            @Param("dueDateEnd") Timestamp dueDateEnd
    );

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.orderId = :orderId ")
    void updateOrderStatusById(@Param("status") String status, @Param("orderId") String orderId);
}
