package com.itwillbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.inventory.IncomingItemsDTO;
import com.itwillbs.entity.Incoming;
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

	
	//입하 검품완료된 검품코드중 입고 등록이 되지 않은걸 조건으로 해당 검품코드의 품목들 조회
	// 지금 quality_sale_items entity가 없어서 구현 불가하므로 임시 주석처리
//	@Query("SELECT new com.itwillbs.domain.inventory.IncomingItemsDTO(i.itemCode, i.itemName, i.itemType, qsi.quantity) " +
//			"FROM qualitySaleItems qsi left join fetch Item i ON qsi.itemCode = i.itemCode " + 
//			"WHERE qsi.quality_sale_id = :qualitySaleId")
//	List<IncomingItemsDTO> findQualitySaleItemsById(String qualitySaleId);
	
}
