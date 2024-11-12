package com.itwillbs.repository;

import com.itwillbs.domain.transaction.SaleDTO;
import com.itwillbs.domain.transaction.ShipmentDTO;
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

    // 출하 대상 수주 건 조회
    @Query("SELECT new com.itwillbs.domain.transaction.SaleDTO " +
            "(s.saleId, s.totalPrice, s.orderDate, s.dueDate, s.franchise, qs.status)" +
            "FROM Shipment sm RIGHT JOIN sm.sale s RIGHT JOIN s.qualitySale qs " +
            "WHERE qs.status = '검품완료' AND sm.status IS NULL " +
            "ORDER BY s.dueDate")
    // 출고 정보 추가해야 함
    List<SaleDTO> findAllQualified();

    // 출하등록 된 것 중 출하검품 상태 확인
    @Query("SELECT qsm.status FROM QualityShipment qsm JOIN qsm.shipment sm WHERE sm.shipmentId = :shipmentId")
    String checkShipmentQualified(@Param("shipmentId") String shipmentId);


    @Query("SELECT sm FROM Shipment sm JOIN sm.sale s " +
            "JOIN s.franchise f " +
            "LEFT JOIN s.saleItems si " +
            "LEFT JOIN si.item i " +
            "WHERE (:status IS NULL OR sm.status = :status) AND " +
            "(:franchiseName IS NULL OR f.franchiseName LIKE :franchiseName) AND " +
            "(:shipDateStart IS NULL OR sm.shipDate >= :shipDateStart) AND " +
            "(:shipDateEnd IS NULL OR sm.shipDate <= :shipDateEnd) AND " +
            "(:itemName IS NULL OR i.itemName LIKE :itemName) AND " +
            "(:dueDateStart IS NULL OR s.dueDate >= :dueDateStart) AND " +
            "(:dueDateEnd IS NULL OR s.dueDate <= :dueDateEnd)")
    List<Shipment> findShipmentByConditions(
            @Param("status") String status,
            @Param("franchiseName") String franchiseName,
            @Param("shipDateStart") Timestamp shipDateStart,
            @Param("shipDateEnd") Timestamp shipDateEnd,
            @Param("itemName") String itemName,
            @Param("dueDateStart") Timestamp dueDateStart,
            @Param("dueDateEnd") Timestamp dueDateEnd
    );

    @Query("SELECT new com.itwillbs.domain.transaction.ShipmentDTO" +
            "(sm.shipmentId, sm.shipDate, sm.status, sm.sale.saleId, sm.sale.franchise.franchiseCode, sm.sale.franchise.franchiseName," +
            "sm.manager.managerId, sm.manager.name, sm.sale.dueDate, sm.note, sm.sale.totalPrice, qsm.status) " +
            "FROM Shipment sm JOIN QualityShipment qsm ON sm.shipmentId = qsm.shipment.shipmentId " +
            "WHERE sm.shipmentId = :shipmentId")
    ShipmentDTO getShipmentDTOById(@Param("shipmentId") String shipmentId);


    @Query("SELECT new com.itwillbs.domain.transaction.ShipmentDTO" +
            "(sm.status, qsm.status) FROM Shipment sm JOIN QualityShipment qsm ON sm.shipmentId = qsm.shipment.shipmentId " +
            "WHERE sm.shipmentId = :shipmentId")
    ShipmentDTO syncByShipmentId(@Param("shipmentId") String shipmentId);

    @Transactional
    @Modifying
    @Query("UPDATE Shipment sm SET sm.status = :status WHERE sm.shipmentId = :shipmentId ")
    void updateShipStatusById(@Param("shipmentId") String shipmentId, @Param("status") String status);


}
