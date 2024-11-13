package com.itwillbs.entity.quality;

import com.itwillbs.entity.Franchise;
import com.itwillbs.entity.Manager;
import com.itwillbs.entity.Sale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Entity
@Table(name = "quality_sale")
@Getter
@Setter
@ToString
public class QualitySale2 {

    @Id
    @Column(name = "quality_sale_id" , length = 20 , nullable = false)
    private String qualitySaleId;

    @Column(name = "order_date" , nullable = false)
    private Timestamp orderDate;

    @Column(name = "due_date" , nullable = false)
    private Timestamp dueDate;

    @Column(name = "status" , length = 20 ,nullable = false)
    private String status;

    @Column(name = "note" , length = 100)
    private String note;

    // 수주랑 품질관리 수주는 1:1이라고 생각함
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager")
    private Manager manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_code")
    private Franchise franchise;

}

