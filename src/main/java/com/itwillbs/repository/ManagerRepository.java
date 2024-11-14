package com.itwillbs.repository;

import com.itwillbs.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {
    @Query("SELECT m FROM Manager m " +
            "where m.managerId LIKE concat('%', :search, '%') " +
            "or m.name LIKE concat('%', :search, '%') " +
            "or m.email LIKE concat('%', :search, '%') " +
            "or m.phone LIKE concat('%', :search, '%') " +
            "or m.managerRole LIKE concat('%', :search, '%')")
    Page<Manager> findBySearch(Pageable pageable, @Param("search")String search);

    // 이은지 작성: 거래 담당자 가져오기 (+ 이름 검색 포함)
    @Query("SELECT m FROM Manager m WHERE " +
            "(:managerName IS NULL OR m.name LIKE :managerName) AND " +
            "m.managerRole IN ('ROLE_ADMIN', 'ROLE_TRANSACTION')")
    List<Manager> findManagerOnTX(@Param("managerName") String managerName);

    // 이은지 작성: 검품 담당자 가져오기 (+ 이름 검색 포함)
    @Query("SELECT m FROM Manager m WHERE " +
            "(:managerName IS NULL OR m.name LIKE :managerName) AND " +
            "m.managerRole IN ('ROLE_ADMIN', 'ROLE_QUALITY')")
    List<Manager> findManagerOnQuality(@Param("managerName") String managerName);
}
