package com.itwillbs.repository;

import org.springframework.stereotype.Repository;


import java.sql.Timestamp;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.domain.inventory.IncomingDTO;
import com.itwillbs.domain.inventory.IncomingInsertDTO;
import com.itwillbs.domain.inventory.IncomingItemsDTO;
import com.itwillbs.entity.Incoming;
import com.itwillbs.entity.IncomingItems;

import jakarta.transaction.Transactional;

@Repository
public interface IncomingRepository extends JpaRepository<Incoming, String> {

	/**
	 * 입고 페이지 진입할 때 입고테이블 조회(페이징 처리)
	 */
	@Query("SELECT DISTINCT new com.itwillbs.domain.inventory.IncomingDTO(ic.incomingId, ic.incomingStartDate, ic.incomingEndDate, m.managerId, m.name, ic.status, ic.productionId, ic.qualityOrderId) "
			+ "FROM Incoming ic LEFT JOIN ic.manager m "
			+ "ORDER BY ic.incomingId DESC")
	Page<IncomingDTO> getIncomingLists(Pageable pageable);

    // Fetch Join을 사용하여 한 번의 쿼리로 필요한 연관 데이터들을 로딩
	@Query("SELECT DISTINCT ic FROM Incoming ic " +
	        "LEFT JOIN FETCH ic.manager m " +
	        "LEFT JOIN FETCH ic.incomingItems ii " +
	        "LEFT JOIN FETCH ii.item i " +
	        "WHERE (:reasonOfIncoming = '' OR " +
	        "       (:reasonOfIncoming = '생산 완료' AND ic.productionId IS NOT NULL) OR " +
	        "       (:reasonOfIncoming = '발주 완료' AND ic.qualityOrderId IS NOT NULL)) " +
	        "AND (:incomingStartDate_start IS NULL OR ic.incomingStartDate >= :incomingStartDate_start) " +
	        "AND (:incomingStartDate_end IS NULL OR ic.incomingStartDate <= :incomingStartDate_end) " +
	        "AND (:incomingId = '' OR ic.incomingId LIKE CONCAT('%', :incomingId, '%')) " +
	        "AND (:prodOrQualId = '' OR ic.productionId LIKE CONCAT('%', :prodOrQualId, '%') OR ic.qualityOrderId LIKE CONCAT('%', :prodOrQualId, '%')) " +
	        "AND (:status = '' OR ic.status = :status) " +
	        "AND (:managerCodeOrName = '' OR m.managerId LIKE CONCAT('%', :managerCodeOrName, '%') OR m.name LIKE CONCAT('%', :managerCodeOrName, '%')) " +
	        "AND (:itemCodeOrName = '' OR i.itemCode LIKE CONCAT('%', :itemCodeOrName, '%') OR i.itemName LIKE CONCAT('%', :itemCodeOrName, '%')) " +
	        "ORDER BY ic.incomingId DESC")
	Page<Incoming> findIncomingEntities(
	        @Param("reasonOfIncoming") String reasonOfIncoming,
	        @Param("incomingStartDate_start") Timestamp incomingStartDate_start,
	        @Param("incomingStartDate_end") Timestamp incomingStartDate_end,
	        @Param("incomingId") String incomingId,
	        @Param("prodOrQualId") String prodOrQualId,
	        @Param("status") String status,
	        @Param("managerCodeOrName") String managerCodeOrName,
	        @Param("itemCodeOrName") String itemCodeOrName,
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
	@Query("SELECT new com.itwillbs.domain.inventory.IncomingInsertDTO(mfo.orderId, mfo.orderState, mfo.orderDate, mfo.item.itemName, mfo.orderAmount) " +
			"FROM MFOrder mfo LEFT JOIN Incoming i ON mfo.orderId = i.productionId " +
			"WHERE mfo.orderState = '작업 완료' " +
			"AND i.productionId IS NULL")
	List<IncomingInsertDTO> findAllEndOfProduction();
	
	
//	/**
//	 * 검품 완료되었지만 입고등록되지 않은 데이터 조회
//	 */
//	@Query("SELECT new com.itwillbs.domain.inventory.IncomingInsertDTO(qo.quality_order_id, qo.status, qo.order_date) " +
//		       "FROM QualityOrder qo " +
//		       "LEFT JOIN qo.quality_order_items qoi " +
//		       "LEFT JOIN Incoming i ON qo.quality_order_id = i.qualityOrderId " +
//		       "WHERE qo.status = '검품완료' AND qoi.status = '통과' AND i.qualityOrderId IS NULL"
//		       )
//	List<IncomingInsertDTO> findAllEndOfQaul();

	

	/**
	 * 발주 완료되었지만 입고등록되지 않은 데이터 조회
	 */
	@Query("SELECT new com.itwillbs.domain.inventory.IncomingInsertDTO(o.orderId, o.status, o.orderDate) " +
		       "FROM Order o " +
		       "WHERE o.status = '발주완료' " +
		       "AND o.orderId NOT IN (SELECT inc.order.orderId FROM Incoming inc WHERE inc.order IS NOT NULL)")
		List<IncomingInsertDTO> findAllEndOfOrder();
	

	//입고등록번호 제일 높은 숫자 조회
	Optional<Incoming> findTopByOrderByIncomingIdDesc();


	
	
	
	
	
	
}
