package com.itwillbs.repository;

import com.itwillbs.domain.masterdata.BOMListDTO;
import com.itwillbs.entity.BOM;
import com.itwillbs.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BOMRepository extends JpaRepository<BOM, Long> {
    
	//손강수
	@Query("SELECT new com.itwillbs.domain.masterdata.BOMListDTO(" +
           "b.bomId, " +
           "p.itemCode, p.itemName, " +
           "r.itemCode, r.itemName, " +
           "b.quantity, b.useYN) " +
           "FROM BOM b " +
           "JOIN b.processedProduct p " +
           "JOIN b.rawMaterial r " +
           "WHERE (:ppName IS NULL OR p.itemName LIKE %:ppName%) " +
           "AND (:rmName IS NULL OR r.itemName LIKE %:rmName%) " +
           "AND ((:includeUnused = true) OR b.useYN = 'Y')")
    Page<BOMListDTO> findBySearchConditions(
        @Param("ppName") String ppName,
        @Param("rmName") String rmName,
        @Param("includeUnused") Boolean includeUnused,
        Pageable pageable
    );

    @Query("SELECT b FROM BOM b " +
           "JOIN FETCH b.processedProduct " +
           "JOIN FETCH b.rawMaterial " +
           "WHERE b.bomId = :bomId")
    Optional<BOM> findWithItemsById(@Param("bomId") Long bomId);
    
    boolean existsByProcessedProductAndRawMaterial(Item processedProduct, Item rawMaterial);
}