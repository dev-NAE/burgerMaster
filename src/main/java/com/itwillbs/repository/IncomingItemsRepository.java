package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.inventory.IncomingItemsDTO;
import com.itwillbs.entity.IncomingItems;

@Repository
public interface IncomingItemsRepository extends JpaRepository<IncomingItems, String> {

	@Query("SELECT new com.itwillbs.domain.inventory.IncomingItemsDTO(i.itemName) " +
			"FROM IncomingItems ii left join fetch Item i ON ii.itemCode = i.itemCode " +
			"WHERE incomingId = :incomingId")
	List<IncomingItemsDTO> findIncomingItemsListById(@Param("incomingId") String incomingId);
	
	
}
