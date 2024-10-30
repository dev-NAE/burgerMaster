package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class OrderDTO {

    private String orderId;
    private int totalPrice;
    private Timestamp orderDate;
    private Timestamp dueDate;
    private Timestamp realDate;
    private String status;
    private String note;
    private String manager;
    private String supplierCode;

}
