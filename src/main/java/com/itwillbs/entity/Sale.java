package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "sale")
@Getter
@Setter
public class Sale {

    @Id
    @Column(name = "sale_id")
    private String saleId;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "order_date")
    private Timestamp orderDate;

    @Column(name = "due_date")
    private Timestamp dueDate;

    @Column(name = "real_date")
    private Timestamp realDate;

    @Column(name = "status")
    private String status;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager")
    private Manager manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_code")
    private Franchise franchise;

    // SaleItems와의 관계 설정
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItems> saleItems = new ArrayList<>();

}
