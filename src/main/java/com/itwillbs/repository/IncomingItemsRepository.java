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


	//입하 검품완료된 해당 검품코드의 품목들 조회
	// 지금 quality_order_items entity가 없어서 구현 불가하므로 임시 주석처리
//	@Query("SELECT new com.itwillbs.domain.inventory.IncomingItemsDTO(i.itemCode, i.itemName, i.itemType, qoi.quantity) " +
//			"FROM qualityOrderItems qoi left join fetch Item i ON qoi.itemCode = i.itemCode " + 
//			"WHERE qoi.quality_order_id = :qualityOrderId")
//	List<IncomingItemsDTO> findQualityOrderItemsById(String qualityOrderId);
	
}
