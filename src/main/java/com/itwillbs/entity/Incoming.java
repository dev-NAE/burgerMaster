package com.itwillbs.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 입고 테이블
 */
@Entity
@Table(name = "incoming")
@Getter
@Setter
@ToString(exclude = {"manager", "incomingItems"}) // 'manager'와 'incomingItems' 필드를 toString에서 제외
@NoArgsConstructor
@AllArgsConstructor
public class Incoming {

    @Id
    @Column(name = "incoming_id", length = 20)
    private String incomingId;

    @Column(name = "incoming_start_date")
    private Timestamp incomingStartDate;

    @Column(name = "incoming_end_date")
    private Timestamp incomingEndDate;

    @Column(name = "status")
    private String status;

    //생산번호 : 
    @Column(name = "production_id")
    private String productionId;

    //입고검품번호
    @Column(name = "quality_order_id")
    private String qualityOrderId;

    //발주번호 : orders테이블의 order_id
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "order_id")
    private Order order;
    
    // Many-to-One 관계 설정: Incoming과 Manager
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    // One-to-Many 관계 설정: Incoming과 IncomingItems
    @OneToMany(mappedBy = "incoming", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncomingItems> incomingItems = new ArrayList<>();
}