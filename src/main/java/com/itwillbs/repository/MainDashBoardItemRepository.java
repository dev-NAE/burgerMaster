package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.test.ItemDashDTO;
import com.itwillbs.entity.Item;


@Repository
public interface MainDashBoardItemRepository extends JpaRepository<Item, String> {

	
	 // item_type이 특정 값과 일치하는 항목 조회
	
//	 @Query("SELECT b.quantity, i.itemName " +
//	           "FROM Item i JOIN Bom b ON i.itemCode = b.rmCode.itemCode " +
//	           "WHERE i.itemType = :itemType")
//	List<Object[]> findByItemType (@Param("itemType") String itemType);
	
	
	 @Query("SELECT new com.itwillbs.domain.test.ItemDashDTO(b.quantity, i.itemName) " +
		       "FROM Item i JOIN Bom b ON i.itemCode = b.rmCode.itemCode " +  // 여기서 b.rmCode는 Item 객체
		       "WHERE i.itemType = :itemType")
	    List<ItemDashDTO> findByItemType(@Param("itemType") String itemType);
}//
