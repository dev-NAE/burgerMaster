package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.QualityOrder;

@Repository
public interface QualityOrderRepository extends JpaRepository<QualityOrder, String>{

	
	
}
