package com.itwillbs.repository;

import com.itwillbs.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.Supplier;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {

	@Query("SELECT MAX(s.supplierCode)" + 
			"FROM Supplier s ")
	String findMaxSupplierCode();
	
	@Query("select s "
			+ "from Supplier s "
			+ "where (:supplierName is null or s.supplierName like %:supplierName%)"
			+ "and (:businessNumber is null or s.businessNumber like %:businessNumber%)"
			+ "and (:contactPerson is null or s.contactPerson like %:contactPerson%)"
			+ "and ((:includeUnused = true) or s.useYN = 'Y')")
	Page<Supplier> findBySearchConditions(
			@Param("supplierName") String supplierName,
			@Param("businessNumber") String businessNumber, 
			@Param("contactPerson") String contactPerson,
			@Param("includeUnused") Boolean includeUnused, 
			Pageable pageable
			);
	
	boolean existsByBusinessNumber(String businessNumber);

	// 이은지 작성: 거래처 가져오기 (+ 이름 검색 포함)
	@Query("SELECT s FROM Supplier s WHERE " +
			"(:supplierName IS NULL OR s.supplierName LIKE :supplierName) AND " +
			"s.useYN = 'Y'")
	List<Supplier> findSupplierOnTX(@Param("supplierName") String supplierName);

}
