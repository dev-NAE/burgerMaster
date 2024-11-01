package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.entity.InventoryItem;

import java.util.List;

/**
 *  전체 조회 쿼리명: get...() <br>
 *  검색 조회 쿼리명: find...() <br>
 */
@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, String> {

	
	
	
	/**
	 * 재고 전체 조회 쿼리<br>
	 * 품목코드, 품목명, 품목유형, 재고량, 최소필요재고량<br>
	 */
	@Query("SELECT new com.itwillbs.domain.inventory.InventoryItemDTO(i.item_code, i.item_name, i.item_type, ii.quantity, ii.min_req_quantity) " +
		       "FROM Item i LEFT JOIN inventory_items ii ON i.itemCode = ii.itemCode")
		List<InventoryItemDTO> getAllInventoryItems();
	
	/**
	 * 재고 부족 품목만 조회 쿼리<br>
	 */
	@Query("SELECT new com.itwillbs.domain.inventory.InventoryItemDTO(i.itemCode, i.itemName, i.itemType, ii.quantity, ii.minReqQuantity) " +
		       "FROM Item i LEFT JOIN InventoryItem ii ON i.itemCode = ii.itemCode " +
		       "WHERE ii.quantity < ii.minReqQuantity")
		List<InventoryItemDTO> findInventoryItemsByOutOfStock();
	

	
	/**
	 * 재고 검색 쿼리<br>
	 * 품목코드or품목이름, 품목유형(원재료, 가공품, 완제품)을 검색하는 기능 구현 <br> 
	 * 검색어가 없을경우(빈 문자열('')인 경우) 해당조건을 무시하고 테이블 전체를 출력함 <br>
	 */
	@Query("SELECT new com.itwillbs.domain.inventory.InventoryItemDTO(i.itemCode, i.itemName, i.itemType, ii.quantity, ii.minReqQuantity) " +
		       "FROM Item i LEFT JOIN InventoryItem ii ON i.itemCode = ii.itemCode " +
		       "WHERE (:itemCodeOrName = '' OR i.itemCode LIKE CONCAT('%', :itemCodeOrName, '%') OR i.itemName LIKE CONCAT('%', :itemCodeOrName, '%')) " +
		       "AND (:itemType = '' OR i.itemType = :itemType)")
		List<InventoryItemDTO> findInventoryItems(
		       @Param("itemCodeOrName") String itemCodeOrName,
		       @Param("itemType") String itemType);

}
