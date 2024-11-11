package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
public class ShipmentDTO {

    private String shipmentId;
    private Timestamp shipDate;
    private Timestamp realDate;
    private String status;
    private String note;
    private String manager;
    private String saleId;

    // 출하검품 완료상태 : 리스트, 상세화면에서 사용
    private String qsStatus;

    // 출하 시 연관 수주정보 출력용
    private String franchiseName;
    private String itemName;
    private int itemCount;
    private int totalPrice;
    private Timestamp dueDate;

}
