package com.itwillbs.domain.transaction;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.itwillbs.entity.Franchise;
import com.itwillbs.entity.SaleItems;
import com.itwillbs.repository.SaleRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

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

    // 출하처리를 위한 검품 및 출고정보
    private String qualityStatus;
    private String outGoingId;
    private String outGoingStatus;
    private List<SaleItemsDTO> saleItems;

    public SaleDTO() {}

    public SaleDTO(String saleId, int totalPrice, Timestamp orderDate,
                   Timestamp dueDate, Franchise franchise, String outGoingId, String outGoingStatus) {
        this.saleId = saleId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
        this.franchiseCode = franchise.getFranchiseCode();
        this.franchiseName = franchise.getFranchiseName();
        this.outGoingId = outGoingId;
        this.outGoingStatus = outGoingStatus;
    }


}
