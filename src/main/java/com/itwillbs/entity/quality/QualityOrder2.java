package com.itwillbs.entity.quality;

import com.itwillbs.entity.Manager;
import com.itwillbs.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Table(name = "quality_order")
@Getter
@Setter
@ToString
public class QualityOrder2 {

    @Id
    @Column(name = "quality_order_id" , length = 20 , nullable = false)
    private String quality_order_id;

    @Column(name = "order_date" , nullable = false)
    private Timestamp order_date;

    @Column(name = "due_date" , nullable = false)
    private Timestamp due_date;

    @Column(name = "stock_date" , nullable = false)
    private Timestamp stock_date;

    @Column(name = "status" , length = 20 ,nullable = false)
    private String status;

    @Column(name = "note" , length = 100)
    private String note;

    // 발주랑 품질관리 발주는 1:1이라고 생각함
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager")
    private Manager manager;


}

