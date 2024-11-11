package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.domain.manufacture.MFOrderDTO;
import com.itwillbs.entity.MFOrder;

@Repository
public interface MFRepository extends JpaRepository<MFOrder, String>{
	
	@Query("SELECT new com.itwillbs.domain.manufacture.MFOrderDTO(m.orderId, i.itemName, m.orderAmount, m.orderDeadline, m.orderDate, m.orderState) " 
				+ "FROM MFOrder m JOIN Item i ON m.orderItem = i.itemCode")
	List<MFOrderDTO> findOrderList();
	
	@Query("SELECT MAX(m.orderId) FROM MFOrder m")
	String findMaxId();
	
}
