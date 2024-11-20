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
	 * 출고 조회 페이지 진입할 때 출고테이블 조회(페이징 처리)
	 */
	@Query("SELECT DISTINCT new com.itwillbs.domain.inventory.OutgoingDTO(og.outgoingId, og.outgoingStartDate, og.outgoingEndDate, m.managerId, m.name, og.status, mfo.orderId, s.saleId) "
			+ "FROM Outgoing og LEFT JOIN og.manager m LEFT JOIN og.mfOrder mfo LEFT JOIN og.sale s "
			+ "ORDER BY og.outgoingId DESC")
	Page<OutgoingDTO> getOutgoingLists(Pageable pageable);

    // Fetch Join을 사용하여 한 번의 쿼리로 필요한 연관 데이터들을 로딩
	@Query("SELECT DISTINCT og FROM Outgoing og " +
	        "LEFT JOIN FETCH og.manager m " +
	        "LEFT JOIN FETCH og.outgoingItems ii " +
	        "LEFT JOIN FETCH ii.item i " +
	        "LEFT JOIN FETCH og.sale s " +
	        "LEFT JOIN FETCH og.mfOrder mfo " +
	        "WHERE (:reasonOfOutgoing = '' OR " +
	        "       (:reasonOfOutgoing = '생산 요청' AND mfo.orderId IS NOT NULL) OR " +
	        "       (:reasonOfOutgoing = '수주 완료' AND s.saleId IS NOT NULL)) " +
	        "AND (:outgoingStartDate_start IS NULL OR og.outgoingStartDate >= :outgoingStartDate_start) " +
	        "AND (:outgoingStartDate_end IS NULL OR og.outgoingStartDate <= :outgoingStartDate_end) " +
	        "AND (:outgoingId = '' OR og.outgoingId LIKE CONCAT('%', :outgoingId, '%')) " +
	        "AND (:prodOrSaleId = '' OR mfo.orderId LIKE CONCAT('%', :prodOrSaleId, '%') OR s.saleId LIKE CONCAT('%', :prodOrSaleId, '%')) " +
	        "AND (:status = '' OR og.status = :status) " +
	        "AND (:managerCodeOrName = '' OR m.managerId LIKE CONCAT('%', :managerCodeOrName, '%') OR m.name LIKE CONCAT('%', :managerCodeOrName, '%')) " +
	        "AND (:itemCodeOrName = '' OR i.itemCode LIKE CONCAT('%', :itemCodeOrName, '%') OR i.itemName LIKE CONCAT('%', :itemCodeOrName, '%')) " +
	        "ORDER BY og.outgoingId DESC")
	Page<Outgoing> findOutgoingEntities(
	        @Param("reasonOfOutgoing") String reasonOfOutgoing,
	        @Param("outgoingStartDate_start") Timestamp outgoingStartDate_start,
	        @Param("outgoingStartDate_end") Timestamp outgoingStartDate_end,
	        @Param("outgoingId") String outgoingId,
	        @Param("prodOrSaleId") String prodOrSaleId,
	        @Param("status") String status,
	        @Param("managerCodeOrName") String managerCodeOrName,
	        @Param("itemCodeOrName") String itemCodeOrName,
	        Pageable pageable);

	/**
	 * 출고 상세 정보 모달에서 출고 완료로 업데이트
	 */
	@Transactional
	@Modifying
	@Query("UPDATE Outgoing og SET og.status = '출고 완료', og.outgoingEndDate = :outgoingEndDate WHERE og.outgoingId = :outgoingId AND og.status = '출고 진행중'")
	int updateOutgoingStatus(@Param("outgoingId") String outgoingId, @Param("outgoingEndDate") Timestamp outgoingEndDate);
	
	
	/**
	 * 생산 요청되었지만 출고등록되지 않은 데이터 조회
	 */
	@Query("SELECT new com.itwillbs.domain.inventory.OutgoingInsertDTO(mfo.orderId, mfo.orderState, mfo.orderDate, mfo.item.itemName, mfo.orderAmount) " +
			"FROM MFOrder mfo " +
			"WHERE mfo.orderState = '작업 대기' " +
			"AND mfo.orderId NOT IN (SELECT inc.mfOrder.orderId FROM Outgoing inc WHERE inc.mfOrder IS NOT NULL)")
	List<OutgoingInsertDTO> findAllEndOfProduction();

	

	/**
	 * 발주 완료되었지만 출고등록되지 않은 데이터 조회
	 */
	@Query("SELECT new com.itwillbs.domain.inventory.OutgoingInsertDTO(s.saleId, s.status, s.orderDate) " +
		       "FROM Sale s " +
		       "WHERE s.status = '수주완료' " +
		       "AND s.saleId NOT IN (SELECT inc.sale.saleId FROM Outgoing inc WHERE inc.sale IS NOT NULL)")
		List<OutgoingInsertDTO> findAllEndOfSale();
	

	//출고등록번호 제일 높은 숫자 조회
	@Query("SELECT o FROM Outgoing o ORDER BY CAST(SUBSTRING(o.outgoingId, 4) AS int) DESC")
	List<Outgoing> findAllOrderByNumericOutgoingIdDesc();

	
}
