package com.itwillbs.repository;



import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.itwillbs.domain.inventory.InventoryItemDTO;
import com.itwillbs.entity.Incoming;


@Repository
public interface IncomingRepository extends JpaRepository<Incoming, String>{

    /**
     * 입고 전체 조회
     */
//    @Query("SELECT new com.itwillbs.domain.inventory.IncomingDTO(ic.incomingId, ic.incomingStartDate, ic.incomingEndDate, m.managerId, m.name, ic.status, ic.qualityOrderId) " +
//    		"FROM Incoming ic LEFT JOIN fetch Manager")
//	Page<InventoryItemDTO> getAllIncomingLists(Pageable pageable);
	
	
}
