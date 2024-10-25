package com.itwillbs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {
	
}
