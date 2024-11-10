package com.itwillbs.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.Franchise;

import java.util.List;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, String> {
    
	// 손강수
    @Query("SELECT MAX(f.franchiseCode) " +
    		"FROM Franchise f")
    String findMaxFranchiseCode();
    
    @Query("SELECT f FROM Franchise f " +
	       "WHERE (:franchiseName IS NULL OR f.franchiseName LIKE %:franchiseName%) " +
	       "AND (:ownerName IS NULL OR f.ownerName LIKE %:ownerName%) " +
	       "AND (:businessNumber IS NULL OR f.businessNumber LIKE %:businessNumber%) " +
	       "AND (:contractDateFrom IS NULL OR f.contractStartDate >= :contractDateFrom) " +
	       "AND (:contractDateTo IS NULL OR f.contractStartDate <= :contractDateTo) " +
	       "AND ((:includeUnused = true) OR f.useYN = 'Y')")
    	Page<Franchise> findBySearchConditions(
    	    @Param("franchiseName") String franchiseName,
    	    @Param("ownerName") String ownerName,
    	    @Param("businessNumber") String businessNumber,
    	    @Param("contractDateFrom") LocalDate contractDateFrom,
    	    @Param("contractDateTo") LocalDate contractDateTo,
    	    @Param("includeUnused") Boolean includeUnused,
    	    Pageable pageable
    	);

	boolean existsByBusinessNumber(String businessNumber);

    // 이은지 작성: 가맹점 가져오기 (+ 이름 검색 포함)
    @Query("SELECT f FROM Franchise f WHERE " +
            "(:franchiseName IS NULL OR f.franchiseName LIKE :franchiseName) AND " +
            "f.useYN = 'Y'")
    List<Franchise> findFranchiseOnTX(@Param("franchiseName") String franchiseName);

}
