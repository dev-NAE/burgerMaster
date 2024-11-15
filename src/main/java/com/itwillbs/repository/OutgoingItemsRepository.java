package com.itwillbs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.inventory.OutgoingItemsDTO;

import com.itwillbs.entity.OutgoingItems;

@Repository
public interface OutgoingItemsRepository extends JpaRepository<OutgoingItems, String>{
	
	
	
	@Query("SELECT ogi FROM OutgoingItems ogi WHERE ogi.outgoing.outgoingId = :outgoingId")
	List<OutgoingItems> findOutgoingItemsListById(@Param("outgoingId") String outgoingId);

	//출고 상세 조회
	@Query("SELECT ogi FROM OutgoingItems ogi WHERE ogi.outgoing.outgoingId = :outgoingId")
	List<OutgoingItems> findByOutgoingItems(@Param("outgoingId") String outgoingId);


	
	//수주완료된 해당 수주코드의 품목들 조회
	@Query("SELECT new com.itwillbs.domain.inventory.OutgoingItemsDTO(i.itemCode, i.itemName, i.itemType, si.quantity) " +
			"FROM SaleItems si " +
			"LEFT JOIN si.item i " +
			"LEFT JOIN si.sale s " +
			"WHERE s.saleId = :saleId")
	List<OutgoingItemsDTO> findSaleItemsById(@Param("saleId") String saleId);
	
	
	
	
	//출고 등록페이지에서 선택한 생산요청출고대상자의 데이터 조회
	@Query("SELECT new com.itwillbs.domain.inventory.OutgoingItemsDTO(i.itemCode, i.itemName, i.itemType, mfo.orderAmount) " +
			"FROM MFOrder mfo " +
			"LEFT JOIN mfo.item i " +
			"WHERE mfo.orderId = :prodOrSaleId")
	List<OutgoingItemsDTO> findOutgoingInsertProdItemsById(@Param("prodOrSaleId") String prodOrSaleId);


}
