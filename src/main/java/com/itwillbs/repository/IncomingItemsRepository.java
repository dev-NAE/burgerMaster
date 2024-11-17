package com.itwillbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.inventory.IncomingItemsDTO;
import com.itwillbs.entity.IncomingItems;

@Repository
public interface IncomingItemsRepository extends JpaRepository<IncomingItems, String> {

	
	

	@Query("SELECT ii FROM IncomingItems ii WHERE ii.incoming.incomingId = :incomingId")
	List<IncomingItems> findIncomingItemsListById(@Param("incomingId") String incomingId);

	//입고 상세 조회
	@Query("SELECT ii FROM IncomingItems ii WHERE ii.incoming.incomingId = :incomingId")
	List<IncomingItems> findByIncomingItems(@Param("incomingId") String incomingId);


	
	//발주완료된 해당 발주코드의 품목들 조회
	@Query("SELECT new com.itwillbs.domain.inventory.IncomingItemsDTO(i.itemCode, i.itemName, i.itemType, oi.quantity) " +
			"FROM OrderItems oi " +
			"LEFT JOIN oi.item i " +
			"LEFT JOIN oi.order o " +
			"WHERE o.orderId = :orderId")
	List<IncomingItemsDTO> findOrderItemsById(@Param("orderId") String orderId);
	
	
	
	
	//입고 등록페이지에서 선택한 생산완료입고대상자의 데이터 조회
	@Query("SELECT new com.itwillbs.domain.inventory.IncomingItemsDTO(i.itemCode, i.itemName, i.itemType, mfo.orderAmount) " +
			"FROM MFOrder mfo " +
			"LEFT JOIN mfo.item i " +
			"WHERE mfo.orderId = :prodOrOrderId")
	List<IncomingItemsDTO> findIncomingInsertProdItemsById(@Param("prodOrOrderId") String prodOrOrderId);


	
}
