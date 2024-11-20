package com.itwillbs.domain.inventory;

import java.sql.Timestamp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwillbs.entity.OrderItems;

import lombok.Data;


//입고등록
@Data
public class IncomingInsertDTO {
	
    private String prodOrOrderId;
    private String reasonOfIncoming; //생산 완료, 발주 완료

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp prodOrOrderDate;

    
    private String itemName;
    private Integer quantity;
    private Integer totalAmount; //총 수량
    
	private String incomingItemDisplay; //보여줄 품목 1개
	private Integer otherCount = 0; //품목 외 갯수 ex)패티 외 2건
    
    private String managerId;
    private String managerName;
    
    private List<OrderItems> orderItems;
    
    
	public IncomingInsertDTO(String prodOrOrderId, String reasonOfIncoming, Timestamp prodOrOrderDate, String incomingItemDisplay,
			Integer totalAmount) {
//		super();
		this.prodOrOrderId = prodOrOrderId;
		this.reasonOfIncoming = reasonOfIncoming;
		this.prodOrOrderDate = prodOrOrderDate;
		this.incomingItemDisplay = incomingItemDisplay;
		this.totalAmount = totalAmount;
	}

	public IncomingInsertDTO(String prodOrOrderId, String reasonOfIncoming, Timestamp prodOrOrderDate) {
//		super();
		this.prodOrOrderId = prodOrOrderId;
		this.reasonOfIncoming = reasonOfIncoming;
		this.prodOrOrderDate = prodOrOrderDate;
	}
    
    

    
    
}
