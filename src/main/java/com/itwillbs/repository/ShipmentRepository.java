package com.itwillbs.repository;

import com.itwillbs.entity.Sale;
import com.itwillbs.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {

    // 발주번호 생성을 위한 현재 최고번호 가져오기
    @Query("SELECT MAX(sm.shipmentId) FROM Shipment sm")
    String findMaxShipmentId();

//    @Query("SELECT si.item.itemName FROM SaleItems si WHERE si.sale = :sale ORDER BY si.item.itemCode ASC")
//    List<String> findFirstItemNameBySale(@Param("sale") Sale sale);
//
//    @Query("SELECT COUNT(si.item.itemCode) FROM SaleItems si WHERE si.sale = :sale")
//    int findSaleItemCountBySale(@Param("sale") Sale sale);
//
//    @Query("SELECT s FROM Sale s " +
//            "JOIN s.franchise f " +
//            "LEFT JOIN s.saleItems si " +
//            "LEFT JOIN si.item i " +
//            "WHERE (:status IS NULL OR s.status = :status) AND " +
//            "(:franchiseName IS NULL OR f.franchiseName LIKE :franchiseName) AND " +
//            "(:orderDateStart IS NULL OR s.orderDate >= :orderDateStart) AND " +
//            "(:orderDateEnd IS NULL OR s.orderDate <= :orderDateEnd) AND " +
//            "(:itemName IS NULL OR i.itemName LIKE :itemName) AND " +
//            "(:dueDateStart IS NULL OR s.dueDate >= :dueDateStart) AND " +
//            "(:dueDateEnd IS NULL OR s.dueDate <= :dueDateEnd)")
//    List<Sale> findSalesByConditions(
//            @Param("status") String status,
//            @Param("franchiseName") String franchiseName,
//            @Param("orderDateStart") Timestamp orderDateStart,
//            @Param("orderDateEnd") Timestamp orderDateEnd,
//            @Param("itemName") String itemName,
//            @Param("dueDateStart") Timestamp dueDateStart,
//            @Param("dueDateEnd") Timestamp dueDateEnd
//    );
//
//    @Transactional
//    @Modifying
//    @Query("UPDATE Sale s SET s.status = :status WHERE s.saleId = :saleId ")
//    void updateSaleStatusById(@Param("status") String status, @Param("saleId") String saleId);

}
