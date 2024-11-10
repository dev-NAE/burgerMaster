package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class SaleDTO {

    private String saleId;
    private int totalPrice;
    private Timestamp orderDate;
    private Timestamp dueDate;
    private Timestamp realDate;
    private String status;
    private String note;
    private String manager;
    private String franchiseCode;

    // 리스트 출력용 데이터
    private String franchiseName;
    private String itemName;
    private int itemCount;

}
