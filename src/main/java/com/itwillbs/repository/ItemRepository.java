package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

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

	@Query("SELECT i FROM Item i " + 
				"WHERE i.itemType = 'PP'")
	List<Item> findByItemType();
}
