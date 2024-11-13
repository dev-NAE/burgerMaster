package com.itwillbs.repository;

import com.itwillbs.domain.transaction.QualityShipmentDTO;
import com.itwillbs.domain.transaction.ShipmentDTO;
import com.itwillbs.entity.QualityShipment;
import com.itwillbs.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

public interface QualityShipmentRepository extends JpaRepository<QualityShipment, String>  {

    @Query("SELECT qs FROM QualityShipment qs JOIN qs.sale s " +
            "JOIN s.franchise f JOIN qs.shipment sm " +
            "LEFT JOIN s.saleItems si " +
            "LEFT JOIN si.item i " +
            "WHERE (:status IS NULL OR qs.status = :status) AND " +
            "(:franchiseName IS NULL OR f.franchiseName LIKE :franchiseName) AND " +
            "(:shipDateStart IS NULL OR sm.shipDate >= :shipDateStart) AND " +
            "(:shipDateEnd IS NULL OR sm.shipDate <= :shipDateEnd) AND " +
            "(:itemName IS NULL OR i.itemName LIKE :itemName) AND " +
            "(:dueDateStart IS NULL OR s.dueDate >= :dueDateStart) AND " +
            "(:dueDateEnd IS NULL OR s.dueDate <= :dueDateEnd)")
    List<QualityShipment> findQualityShipmentByConditions(
            @Param("status") String status,
            @Param("franchiseName") String franchiseName,
            @Param("shipDateStart") Timestamp shipDateStart,
            @Param("shipDateEnd") Timestamp shipDateEnd,
            @Param("itemName") String itemName,
            @Param("dueDateStart") Timestamp dueDateStart,
            @Param("dueDateEnd") Timestamp dueDateEnd
    );

    @Query("SELECT new com.itwillbs.domain.transaction.QualityShipmentDTO" +
            "(qsm.qualityShipmentId, sm.shipmentId, sm.shipDate, sm.status, sm.sale.saleId, " +
            "sm.sale.franchise.franchiseCode, sm.sale.franchise.franchiseName, " +
            "m.managerId, m.name, sm.sale.orderDate, sm.sale.dueDate, qsm.note, sm.sale.totalPrice, qsm.status) " +
            "FROM QualityShipment qsm " +
            "JOIN qsm.shipment sm " +
            "LEFT JOIN qsm.manager m " +
            "WHERE qsm.qualityShipmentId = :qsId")
    QualityShipmentDTO getShipmentDTOById(@Param("qsId") String qsId);

    @Transactional
    @Modifying
    @Query("UPDATE QualityShipment qs SET qs.status = '검품완료', qs.manager.managerId = :manager, " +
            "qs.note = :note WHERE qs.qualityShipmentId = :qsId ")
    void updateQsStatus(@Param("qsId") String qsId, @Param("manager") String manager, @Param("note") String note);
}
