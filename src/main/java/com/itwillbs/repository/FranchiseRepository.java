package com.itwillbs.repository;

import com.itwillbs.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.entity.Franchise;

import java.util.List;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, String> {

    // 이은지 작성: 가맹점 가져오기 (+ 이름 검색 포함)
    @Query("SELECT f FROM Franchise f WHERE " +
            "(:franchiseName IS NULL OR f.franchiseName LIKE :franchiseName) AND " +
            "f.useYN = 'Y'")
    List<Franchise> findFranchiseOnTX(@Param("franchiseName") String franchiseName);

}
