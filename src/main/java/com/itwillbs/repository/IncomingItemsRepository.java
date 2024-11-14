package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.inventory.IncomingItemsDTO;
import com.itwillbs.entity.IncomingItems;

@Repository
public interface IncomingItemsRepository extends JpaRepository<IncomingItems, String> {

	
	

	//하나의 입고코드에 해당되는 품목들 조회
	@Query("SELECT new com.itwillbs.domain.inventory.IncomingItemsDTO(i.itemCode, i.itemName) " +
			"FROM IncomingItems ii left join fetch Item i ON ii.itemCode = i.itemCode " +
			"WHERE incomingId = :incomingId")
	List<IncomingItemsDTO> findIncomingItemsListById(@Param("incomingId") String incomingId);
	
	//입고 상세 조회
	@Query("SELECT new com.itwillbs.domain.inventory.IncomingItemsDTO(i.itemCode, i.itemName, i.itemType, ii.quantity) " +
			"FROM IncomingItems ii left join fetch Item i ON ii.itemCode = i.itemCode " +
			"WHERE incomingId = :incomingId")
	List<IncomingItemsDTO> findByIncomingItems(@Param("incomingId") String incomingId);


	//입하 검품완료된 해당 검품코드의 불량 아닌 통과된 품목들만 조회
	@Query("SELECT new com.itwillbs.domain.inventory.IncomingItemsDTO(i.itemCode, i.itemName, i.itemType, qoi.quantity) " +
			"FROM QualityOrderItems qoi " +
			"LEFT JOIN qoi.item i " +
			"LEFT JOIN qoi.quality_order qo " +
			"WHERE qo.quality_order_id = :qualityOrderId " +
			"AND qoi.status = '통과'")
	List<IncomingItemsDTO> findQualityOrderItemsById(@Param("qualityOrderId") String qualityOrderId);
	
	
	//입고 등록페이지에서 선택한 생산완료입고대상자의 데이터 조회
	@Query("SELECT new com.itwillbs.domain.inventory.IncomingItemsDTO(i.itemCode, i.itemName, i.itemType, mfo.orderAmount) " +
			"FROM MFOrder mfo " +
			"LEFT JOIN mfo.item i " +
			"WHERE mfo.orderId = :prodOrQualId")
	List<IncomingItemsDTO> findIncomingInsertProdItemsById(@Param("prodOrQualId") String prodOrQualId);
	
}
