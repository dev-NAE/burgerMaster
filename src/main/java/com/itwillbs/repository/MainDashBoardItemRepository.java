package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.test.ITEM;


@Repository
public interface MainDashBoardItemRepository extends JpaRepository<ITEM, String> {

	
	 // item_type이 특정 값과 일치하는 항목 조회
	List<ITEM> findByItemType (String itemType);
	
	

}//
