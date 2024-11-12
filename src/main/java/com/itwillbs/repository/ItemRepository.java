package com.itwillbs.repository;

import java.util.List;

import com.itwillbs.domain.transaction.TxItemsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.masterdata.ItemDTO;
import com.itwillbs.domain.manufacture.MFRmDTO;
import com.itwillbs.domain.manufacture.MFRmListDTO;
import com.itwillbs.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

	//손강수
	@Query("SELECT MAX(i.itemCode)" + 
			"FROM Item i " +
			"WHERE i.itemCode LIKE CONCAT(:itemType, '%')")
	String findMaxItemCodeByItemType(@Param("itemType") String itemType);

	@Query("SELECT i FROM Item i " +
	       "WHERE (:itemName IS NULL OR i.itemName LIKE %:itemName%) " +
	       "AND (:itemType IS NULL OR i.itemType = :itemType) " +
	       "AND ((:includeUnused = true) OR i.useYN = 'Y')")
	Page<Item> findBySearchConditions(
	    @Param("itemName") String itemName, 
	    @Param("itemType") String itemType,
	    @Param("includeUnused") Boolean includeUnused, 
	    Pageable pageable
	);
	
	@Query("SELECT new com.itwillbs.domain.masterdata.ItemDTO(i.itemCode, i.itemName, i.itemType, i.useYN) " +
		       "FROM Item i " +
		       "WHERE i.itemType = :itemType " +
		       "AND (:itemName IS NULL OR i.itemName LIKE %:itemName%) " +
		       "AND i.useYN = :useYN")
	List<ItemDTO> findItemsForModal(
	    @Param("itemType") String itemType, 
	    @Param("itemName") String itemName, 
	    @Param("useYN") String useYN
	);

	//김성목
	@Query("SELECT i FROM Item i " + 
				"WHERE i.itemType = 'PP' "
				+ "AND (:searchId IS NULL OR i.itemCode LIKE %:searchId%) "
				+ "AND (:searchName IS NULL OR i.itemName LIKE %:searchName%)")
	List<Item> findByItemType(
			@Param("searchId") String searchId,
			@Param("searchName") String searchName);

	@Query("SELECT new com.itwillbs.domain.manufacture.MFRmDTO(i.itemName, b.quantity) "
				+ "FROM BOM b JOIN Item i ON b.rawMaterial.itemCode = i.itemCode "
				+ "WHERE b.processedProduct.itemCode = :itemCode")
	List<MFRmDTO> findRmList(@Param("itemCode") String itemCode);

	@Query("SELECT new com.itwillbs.domain.manufacture.MFRmListDTO(i.itemCode, i.itemName, b.quantity, ii.quantity) "
			+ "FROM Item i JOIN BOM b ON i.itemCode = b.rawMaterial.itemCode "
			+ "JOIN InventoryItem ii ON i.itemCode = ii.itemCode "
			+ "WHERE b.processedProduct.itemCode = (SELECT i2.itemCode FROM Item i2 where i2.itemName = :itemName)")
	List<MFRmListDTO> findRM(@Param("itemName") String itemName);


	// 이은지 작성: 거래 품목 가져오기 (+ 이름 검색 포함)
	@Query("SELECT new com.itwillbs.domain.transaction.TxItemsDTO" +
			"(i.itemCode, i.itemName, i.itemType, COALESCE(ii.quantity, 0), COALESCE(ii.minReqQuantity, 0)) " +
			"FROM Item i LEFT JOIN i.inventoryItem ii WHERE " +
			"(:itemName IS NULL OR i.itemName LIKE :itemName) AND " +
			"i.useYN = 'Y' AND i.itemType IN :itemTypes " +
			"ORDER BY ii.quantity")
	List<TxItemsDTO> findItemsOnTX(@Param("itemName") String itemName,
								   @Param("itemTypes") List<String> itemTypes);


}
