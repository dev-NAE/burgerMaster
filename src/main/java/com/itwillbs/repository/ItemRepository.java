package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

	@Query("SELECT MAX(i.itemCode) FROM Item i WHERE i.itemCode LIKE CONCAT(:itemType, '%')")
	String findMaxItemCodeByItemType(@Param("itemType") String itemType);
}
