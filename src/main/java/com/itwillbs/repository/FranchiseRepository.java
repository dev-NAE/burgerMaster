package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.Franchise;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, String> {
	
}
