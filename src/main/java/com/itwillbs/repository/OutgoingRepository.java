package com.itwillbs.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.inventory.OutgoingDTO;
import com.itwillbs.domain.inventory.OutgoingInsertDTO;
import com.itwillbs.entity.Outgoing;

import jakarta.transaction.Transactional;

@Repository
public interface OutgoingRepository extends JpaRepository<Outgoing, String> {
	/**
	 * 출고 페이지 진입할 때 출고테이블 조회(페이징 처리)
	 */
	@Query("SELECT DISTINCT new com.itwillbs.domain.inventory.OutgoingDTO(og.outgoingId, og.outgoingStartDate, og.outgoingEndDate, og.managerId, m.name, og.status, og.productionId, og.sale.saleId) "
			+ "FROM Outgoing og LEFT JOIN fetch Manager m ON og.managerId = m.managerId "
			+ "ORDER BY og.outgoingId DESC")
	Page<OutgoingDTO> getOutgoingLists(Pageable pageable);

	/**
	 * 출고 검색 조회
	 */
	@Query("SELECT DISTINCT new com.itwillbs.domain.inventory.OutgoingDTO(og.outgoingId, og.outgoingStartDate, og.outgoingEndDate, og.managerId, m.name, og.status, og.productionId, og.sale.saleId) "
			+ "FROM Outgoing og " + "LEFT JOIN og.manager m " + "LEFT JOIN og.outgoingItems oi "
			+ "LEFT JOIN oi.item i " + "WHERE " + "(:reasonOfOutgoing = '' OR "
			+ " (:reasonOfOutgoing = '생산 요청' AND og.productionId IS NOT NULL) OR "
			+ " (:reasonOfOutgoing = '수주 완료' AND og.sale.saleId IS NOT NULL)) "
			+ "AND (:outgoingStartDate_start IS NULL OR og.outgoingStartDate >= :outgoingStartDate_start) "
			+ "AND (:outgoingStartDate_end IS NULL OR og.outgoingStartDate <= :outgoingStartDate_end) "
			+ "AND (:outgoingId = '' OR og.outgoingId LIKE CONCAT('%', :outgoingId, '%')) "
			+ "AND (:prodOrQualId = '' OR og.productionId LIKE CONCAT('%', :prodOrQualId, '%') OR og.sale.saleId LIKE CONCAT('%', :prodOrQualId, '%')) "
			+ "AND (:status = '' OR og.status = :status) "
			+ "AND (:managerCodeOrName = '' OR m.managerId LIKE CONCAT('%', :managerCodeOrName, '%') OR m.name LIKE CONCAT('%', :managerCodeOrName, '%')) "
			+ "AND (:itemCodeOrName = '' OR i.itemCode LIKE CONCAT('%', :itemCodeOrName, '%') OR i.itemName LIKE CONCAT('%', :itemCodeOrName, '%')) "
			+ "ORDER BY og.outgoingId DESC")
	Page<OutgoingDTO> findOutgoingLists(@Param("reasonOfOutgoing") String reasonOfOutgoing,
			@Param("outgoingStartDate_start") Timestamp outgoingStartDate_start,
			@Param("outgoingStartDate_end") Timestamp outgoingStartDate_end, @Param("outgoingId") String outgoingId,
			@Param("prodOrQualId") String prodOrQualId, @Param("status") String status,
			@Param("managerCodeOrName") String managerCodeOrName, @Param("itemCodeOrName") String itemCodeOrName,
			Pageable pageable);

	/**
	 * 출고 상세 정보 모달에서 출고 완료로 업데이트
	 */
	@Transactional
	@Modifying
	@Query("UPDATE Outgoing og SET og.status = '출고 완료', og.outgoingEndDate = :outgoingEndDate WHERE og.outgoingId = :outgoingId AND og.status = '출고 진행중'")
	int updateOutgoingStatus(@Param("outgoingId") String outgoingId, @Param("outgoingEndDate") Timestamp outgoingEndDate);
	
	
	/**
	 * 생산 지시되었지만 출고등록되지 않은 데이터 조회
	 */
	@Query("SELECT new com.itwillbs.domain.inventory.OutgoingInsertDTO(mfo.orderId, mfo.orderState, mfo.orderDate, mfo.item.itemName, mfo.orderAmount) " +
			"FROM MFOrder mfo LEFT JOIN Outgoing i ON mfo.orderId = i.productionId " +
			"WHERE mfo.orderState = '작업 완료' " +
			"AND i.productionId IS NULL")
	List<OutgoingInsertDTO> findAllEndOfProduction();
	
	
	/**
	 * 출고검품 완료되었지만 출고등록되지 않은 데이터 조회
	 */
	@Query("SELECT new com.itwillbs.domain.inventory.OutgoingInsertDTO(qs.quality_sale_id, qs.status, qs.order_date) " +
			"FROM QualitySale qs LEFT JOIN Sale s ON qs.quality_sale_id = s.saleId " +
			"WHERE qs.status = '출고검품 완료' " +
			"AND s.saleId IS NULL")
	List<OutgoingInsertDTO> findAllEndOfQuality();
	
}
