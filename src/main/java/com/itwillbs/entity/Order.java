package com.itwillbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "Order")
@Getter
@Setter
@ToString
public class Order {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "order_date")
    private Timestamp orderDate;

    @Column(name = "due_date")
    private Timestamp dueDate;

    @Column(name = "status")
    private String status;

    @Column(name = "note")
    private String note;

    @Column(name = "manager")
    private String manager;

    @Column(name = "supplier_code")
    private String supplierCode;

}
