package com.itwillbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "OrderItems")
@Getter
@Setter
@ToString
public class OrderItems {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "orderitem_id")
    private String orderItemId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "subtotal_price")
    private int subtotalPrice;

    @Column(name = "item_code")
    private String itemCode;



}
