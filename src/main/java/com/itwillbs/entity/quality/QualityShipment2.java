package com.itwillbs.entity.quality;

import com.itwillbs.entity.Manager;
import com.itwillbs.entity.Sale;
import com.itwillbs.entity.Shipment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Table(name = "quality_shipment")
@Getter
@Setter
@ToString
public class QualityShipment2 {

    @Id
    @Column(name = "quality_shipment_id" , length = 20 , nullable = false)
    private String qualityShipmentId;

    @Column(name = "ship_date" , nullable = false)
    private Timestamp shipDate;


    @Column(name = "status" , length = 20 ,nullable = false)
    private String status;

    @Column(name = "note" , length = 100)
    private String note;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager")
    private Manager manager;

    // 수주랑 품질관리 수주는 1:1이라고 생각함
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;
}

