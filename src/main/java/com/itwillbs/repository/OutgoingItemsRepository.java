package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.inventory.OutgoingItemsDTO;
import com.itwillbs.entity.OutgoingItems;

@Repository
public interface OutgoingItemsRepository extends JpaRepository<OutgoingItems, String>{
	//하나의 출고코드에 해당되는 품목들 조회
	@Query("SELECT new com.itwillbs.domain.inventory.OutgoingItemsDTO(i.itemCode, i.itemName) " +
			"FROM OutgoingItems ii left join fetch Item i ON ii.itemCode = i.itemCode " +
			"WHERE outgoingId = :outgoingId")
	List<OutgoingItemsDTO> findOutgoingItemsListById(@Param("outgoingId") String outgoingId);
	
	//출고 상세 조회
	@Query("SELECT new com.itwillbs.domain.inventory.OutgoingItemsDTO(i.itemCode, i.itemName, i.itemType, ii.quantity) " +
			"FROM OutgoingItems ii left join fetch Item i ON ii.itemCode = i.itemCode " +
			"WHERE outgoingId = :outgoingId")
	List<OutgoingItemsDTO> findByOutgoingItems(@Param("outgoingId") String outgoingId);

	
	//출고 검품완료된 해당 검품코드의 품목들 조회
	// 지금 quality_sale_items entity가 없어서 구현 불가하므로 임시 주석처리
//	@Query("SELECT new com.itwillbs.domain.inventory.OutgoingItemsDTO(i.itemCode, i.itemName, i.itemType, qsi.quantity) " +
//			"FROM qualitySaleItems qsi left join fetch Item i ON qsi.itemCode = i.itemCode " + 
//			"WHERE qsi.quality_sale_id = :qualitySaleId")
//	List<OutgoingItemsDTO> findQualitySaleItemsById(String qualitySaleId);
}
