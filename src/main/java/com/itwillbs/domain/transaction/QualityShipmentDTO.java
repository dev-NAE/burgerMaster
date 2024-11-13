package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Setter
@Getter
@ToString
public class QualityShipmentDTO {


    private String qualityShipmentId;

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
    private String franchiseCode;
    private String franchiseName;
    private String itemName;
    private int itemCount;
    private int totalPrice;
    private Timestamp orderDate;
    private Timestamp dueDate;
    private String managerId;
    private String managerName;

    public QualityShipmentDTO() {}

    public QualityShipmentDTO(String status, String qsStatus) {
        this.status = status;
        this.qsStatus = qsStatus;
    }

    public QualityShipmentDTO(String shipmentId, Timestamp shipDate, String status, String saleId, String franchiseCode,
                       String franchiseName, String managerId, String managerName, Timestamp orderDate, Timestamp dueDate, String note,
                       int totalPrice, String qsStatus) {
        this.shipmentId = shipmentId;
        this.shipDate = shipDate;
        this.status = status;
        this.saleId = saleId;
        this.franchiseCode = franchiseCode;
        this.franchiseName = franchiseName;
        this.managerId = managerId;
        this.managerName = managerName;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
        this.note = note;
        this.totalPrice = totalPrice;
        this.qsStatus = qsStatus;
    }




}
