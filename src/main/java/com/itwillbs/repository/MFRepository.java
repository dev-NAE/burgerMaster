package com.itwillbs.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.domain.manufacture.MFOrderDTO;
import com.itwillbs.entity.MFOrder;

@Transactional
@Repository
public interface MFRepository extends JpaRepository<MFOrder, String>{
	
	@Query("SELECT new com.itwillbs.domain.manufacture.MFOrderDTO(m.orderId, i.itemName, m.orderAmount, m.orderDeadline, m.orderDate, m.orderState) " 
				+ "FROM MFOrder m JOIN Item i ON m.item.itemCode = i.itemCode "
				+ "WHERE (:searchDeadline IS NULL OR m.orderDeadline = :searchDeadline) "
				+ "AND (:searchState = '' OR m.orderState = :searchState) "
				+ "AND (:searchId = '' OR m.orderId LIKE %:searchId%) "
				+ "AND (:searchName = '' OR i.itemName LIKE %:searchName%) "
				+ "ORDER BY CASE m.orderState "
				+ "WHEN '작업 전달 전' THEN 1 "
				+ "WHEN '작업 대기' THEN 2 "
				+ "WHEN '작업 중' THEN 3 "
				+ "WHEN '작업 완료' THEN 4 "
				+ "WHEN '작업 종료' THEN 5 "
				+ "END ASC, "
				+ "m.orderDeadline ASC")
	List<MFOrderDTO> findOrderList(
			@Param("searchDeadline") LocalDate searchDeadline,
			@Param("searchState") String searchState,
			@Param("searchId") String searchId,
			@Param("searchName") String searchName);
	
	@Query("SELECT MAX(m.orderId) FROM MFOrder m")
	String findMaxId();
	
	@Modifying
	@Query("UPDATE MFOrder m SET m.orderState = '작업 대기' "
			+ "WHERE m.orderId = :key")
	void transmitOrder(@Param("key") String key);
	
	@Modifying
	@Query("UPDATE MFOrder m SET m.orderState = '작업 중' "
			+ "WHERE m.orderId = :key")
	void startOrder(@Param("key") String key);

	@Modifying
	@Query("UPDATE MFOrder m SET m.orderState = '작업 종료' " 
			+ "WHERE m.orderId = :key")
	void completeOrder(@Param("key") String key);
}
