package com.itwillbs.repository;

import com.itwillbs.entity.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {
    @Query("SELECT m FROM Manager m where m.managerId = :search or m.email= :search or m.phone= :search or m.managerRole=:search")
    Page<Manager> findBySearch(Pageable pageable, @Param("search")String search);
}
