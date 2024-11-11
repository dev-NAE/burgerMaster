package com.itwillbs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "shipment")
@Getter
@Setter
public class Shipment {

    @Id
    @Column(name = "shipment_id")
    private String shipmentId;

    @Column(name = "ship_date")
    private Timestamp shipDate;

    @Column(name = "real_date")
    private Timestamp realDate;

    @Column(name = "status")
    private String status;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager")
    private Manager manager;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;

}