package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.entity.InventoryItem;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, String> {

	
	
	//재고 전체 조회 쿼리
	//품목코드, 품목명, 품목유형, 재고량, 최소필요재고량
	@Query("SELECT new com.itwillbs.domain.inventory.InventoryItemDTO(i.itemCode, i.itemName, i.itemType, ii.quantity, ii.minReqQuantity) " +
		       "FROM Item i LEFT JOIN InventoryItem ii ON i.itemCode = ii.itemCode")
		List<InventoryItemDTO> getItemInventory();
	
	
	//재고 검색 쿼리 
	//품목코드, 품목이름, 품목유형(원재료, 가공품, 완제품)을 검색하는 기능 구현
	//셋 다 빈 문자열('')인 경우 해당조건을 무시하고 테이블 전체를 출력함
	@Query("SELECT new com.itwillbs.domain.inventory.InventoryItemDTO(i.itemCode, i.itemName, i.itemType, ii.quantity, ii.minReqQuantity) " +
		       "FROM Item i LEFT JOIN InventoryItem ii ON i.itemCode = ii.itemCode " +
		       "WHERE (:itemCode = '' OR i.itemCode LIKE CONCAT('%', :itemCode, '%')) " +
		       "AND (:itemName = '' OR i.itemName LIKE CONCAT('%', :itemName, '%')) " +
		       "AND (:itemType = '' OR i.itemType = :itemType)")
		List<InventoryItemDTO> findItemWithInventory(
		       @Param("itemCode") String itemCode,
		       @Param("itemName") String itemName,
		       @Param("itemType") String itemType);



}
