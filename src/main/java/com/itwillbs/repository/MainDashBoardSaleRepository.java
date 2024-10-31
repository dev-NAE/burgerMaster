package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.dashboard.Sale;


@Repository
public interface MainDashBoardSaleRepository extends JpaRepository<Sale, String> {

	

	
	    List<Sale> findByStatus(@Param("status") String status);
	 
	 
	 
	 
}//
