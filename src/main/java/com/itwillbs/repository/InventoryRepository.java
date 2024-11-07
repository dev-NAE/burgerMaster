package com.itwillbs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * 재고 전체 조회 쿼리 (페이지네이션 지원)
     */
    @Query("SELECT new com.itwillbs.domain.inventory.InventoryItemDTO(i.itemCode, i.itemName, i.itemType, ii.quantity, ii.minReqQuantity) " +
           "FROM Item i LEFT JOIN fetch InventoryItem ii ON i.itemCode = ii.itemCode")
    Page<InventoryItemDTO> getAllInventoryItems(Pageable pageable);

    /**
     * 재고 부족 품목만 조회 쿼리 (페이지네이션 지원)
     */
    @Query("SELECT new com.itwillbs.domain.inventory.InventoryItemDTO(i.itemCode, i.itemName, i.itemType, ii.quantity, ii.minReqQuantity) " +
           "FROM Item i LEFT JOIN fetch InventoryItem ii ON i.itemCode = ii.itemCode " +
           "WHERE ii.quantity < ii.minReqQuantity " +
           "AND (:itemCodeOrName IS NULL OR :itemCodeOrName = '' OR i.itemCode LIKE CONCAT('%', :itemCodeOrName, '%') OR i.itemName LIKE CONCAT('%', :itemCodeOrName, '%')) " +
           "AND (:itemType = '' OR i.itemType = :itemType)")
    Page<InventoryItemDTO> findInventoryItemsByOutOfStock(
            @Param("itemCodeOrName") String itemCodeOrName,
            @Param("itemType") String itemType,
            Pageable pageable);

    /**
     * 재고 검색 쿼리 (페이지네이션 지원)
     */
    @Query("SELECT new com.itwillbs.domain.inventory.InventoryItemDTO(i.itemCode, i.itemName, i.itemType, ii.quantity, ii.minReqQuantity) " +
           "FROM Item i LEFT JOIN fetch InventoryItem ii ON i.itemCode = ii.itemCode " +
           "WHERE (:itemCodeOrName IS NULL OR :itemCodeOrName = '' OR i.itemCode LIKE CONCAT('%', :itemCodeOrName, '%') OR i.itemName LIKE CONCAT('%', :itemCodeOrName, '%')) " +
           "AND (:itemType = '' OR i.itemType = :itemType)")
    Page<InventoryItemDTO> findInventoryItems(
            @Param("itemCodeOrName") String itemCodeOrName,
            @Param("itemType") String itemType,
            Pageable pageable);


}

