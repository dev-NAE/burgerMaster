package com.itwillbs.repository.quality;

import com.itwillbs.domain.quality.QualitySaleDTO;
import com.itwillbs.entity.quality.QualitySale2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualitySaleRepository extends JpaRepository<QualitySale2, String> {

    @Query("select new com.itwillbs.domain.quality.QualitySaleDTO(q.qualitySaleId, q.orderDate, q.dueDate, q.status, m.name)" +
            "from QualitySale2 q join Manager m on m.managerId = q.manager.managerId"
    )
    List<QualitySaleDTO> findQualitySaleList();
}
