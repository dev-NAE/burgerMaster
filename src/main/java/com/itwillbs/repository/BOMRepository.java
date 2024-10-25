package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.BOM;

@Repository
public interface BOMRepository extends JpaRepository<BOM, String> {
	
}
