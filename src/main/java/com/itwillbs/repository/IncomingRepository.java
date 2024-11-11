package com.itwillbs.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.domain.inventory.IncomingDTO;
import com.itwillbs.domain.inventory.IncomingInsertDTO;
import com.itwillbs.entity.Incoming;

import jakarta.transaction.Transactional;

@Repository
public interface IncomingRepository extends JpaRepository<Incoming, String> {

	/**
	 * 입고 페이지 진입할 때 입고테이블 조회(페이징 처리)
	 */
	@Query("SELECT DISTINCT new com.itwillbs.domain.inventory.IncomingDTO(ic.incomingId, ic.incomingStartDate, ic.incomingEndDate, ic.managerId, m.name, ic.status, ic.productionId, ic.qualityOrderId) "
			+ "FROM Incoming ic LEFT JOIN fetch Manager m ON ic.managerId = m.managerId "
			+ "ORDER BY ic.incomingId DESC")
	Page<IncomingDTO> getIncomingLists(Pageable pageable);

	/**
	 * 입고 검색 조회
	 */
	@Query("SELECT DISTINCT new com.itwillbs.domain.inventory.IncomingDTO(ic.incomingId, ic.incomingStartDate, ic.incomingEndDate, ic.managerId, m.name, ic.status, ic.productionId, ic.qualityOrderId) "
			+ "FROM Incoming ic " + "LEFT JOIN ic.manager m " + "LEFT JOIN ic.incomingItems ii "
			+ "LEFT JOIN ii.item i " + "WHERE " + "(:reasonOfIncoming = '' OR "
			+ " (:reasonOfIncoming = '생산완료' AND ic.productionId IS NOT NULL) OR "
			+ " (:reasonOfIncoming = '검품완료' AND ic.qualityOrderId IS NOT NULL)) "
			+ "AND (:incomingStartDate_start IS NULL OR ic.incomingStartDate >= :incomingStartDate_start) "
			+ "AND (:incomingStartDate_end IS NULL OR ic.incomingStartDate <= :incomingStartDate_end) "
			+ "AND (:incomingId = '' OR ic.incomingId LIKE CONCAT('%', :incomingId, '%')) "
			+ "AND (:prodOrQualId = '' OR ic.productionId LIKE CONCAT('%', :prodOrQualId, '%') OR ic.qualityOrderId LIKE CONCAT('%', :prodOrQualId, '%')) "
			+ "AND (:status = '' OR ic.status = :status) "
			+ "AND (:managerCodeOrName = '' OR m.managerId LIKE CONCAT('%', :managerCodeOrName, '%') OR m.name LIKE CONCAT('%', :managerCodeOrName, '%')) "
			+ "AND (:itemCodeOrName = '' OR i.itemCode LIKE CONCAT('%', :itemCodeOrName, '%') OR i.itemName LIKE CONCAT('%', :itemCodeOrName, '%')) "
			+ "ORDER BY ic.incomingId DESC")
	Page<IncomingDTO> findIncomingLists(@Param("reasonOfIncoming") String reasonOfIncoming,
			@Param("incomingStartDate_start") Timestamp incomingStartDate_start,
			@Param("incomingStartDate_end") Timestamp incomingStartDate_end, @Param("incomingId") String incomingId,
			@Param("prodOrQualId") String prodOrQualId, @Param("status") String status,
			@Param("managerCodeOrName") String managerCodeOrName, @Param("itemCodeOrName") String itemCodeOrName,
			Pageable pageable);

	/**
	 * 입고 상세 정보 모달에서 입고 완료로 업데이트
	 */
	@Transactional
	@Modifying
	@Query("UPDATE Incoming ic SET ic.status = '입고 완료', ic.incomingEndDate = :incomingEndDate WHERE ic.incomingId = :incomingId AND ic.status = '입고 진행중'")
	int updateIncomingStatus(@Param("incomingId") String incomingId, @Param("incomingEndDate") Timestamp incomingEndDate);
	
	
	/**
	 * 생산 완료되었지만 입고등록되지 않은 데이터 조회
	 */
	@Query("SELECT new com.itwillbs.domain.inventory.IncomingInsertDTO(mfo.orderId, mfo.orderState, mfo.orderDate, mfo.orderItem, mfo.orderAmount) " +
			"FROM MFOrder mfo LEFT JOIN Incoming i ON mfo.orderId = i.productionId " +
			"WHERE mfo.orderState = '작업 완료' " +
			"AND i.productionId IS NULL")
	List<IncomingInsertDTO> findAllEndOfProduction();
	
	
	/**
	 * 입하검품 완료되었지만 입고등록되지 않은 데이터 조회
	 */
	@Query("SELECT new com.itwillbs.domain.inventory.IncomingInsertDTO(qs.quality_sale_id, qs.status, qs.order_date) " +
			"FROM QualitySale qs LEFT JOIN Incoming i ON qs.quality_sale_id = i.qualityOrderId " +
			"WHERE qs.status = '검품완료' " +
			"AND i.qualityOrderId IS NULL")
	List<IncomingInsertDTO> findAllEndOfQuality();
	
	
	
	
	
	
}
