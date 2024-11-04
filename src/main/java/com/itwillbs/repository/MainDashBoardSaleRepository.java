package com.itwillbs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.dashboard.SaleDash;


@Repository
public interface MainDashBoardSaleRepository extends JpaRepository<SaleDash, String> {

	

	
	    List<SaleDash> findByStatus(@Param("status") String status);
	 
	 
	 
	 
}//
