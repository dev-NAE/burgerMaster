package com.itwillbs.domain.inventory;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itwillbs.entity.SaleItems;

import lombok.Data;

//출고등록
@Data
public class OutgoingInsertDTO {
	
    private String prodOrSaleId;
    private String reasonOfOutgoing; //생산 요청, 수주 완료

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp prodOrSaleDate;

    
    private String itemName;
    private Integer quantity;
    private Integer totalAmount; //총 수량
    
	private String outgoingItemDisplay; //보여줄 품목 1개
	private Integer otherCount = 0; //품목 외 갯수 ex)패티 외 2건
    
    private String managerId;
    private String managerName;
    
    private List<SaleItems> saleItems;
    
    
	public OutgoingInsertDTO(String prodOrSaleId, String reasonOfOutgoing, Timestamp prodOrSaleDate, String outgoingItemDisplay,
			Integer totalAmount) {
//		super();
		this.prodOrSaleId = prodOrSaleId;
		this.reasonOfOutgoing = reasonOfOutgoing;
		this.prodOrSaleDate = prodOrSaleDate;
		this.outgoingItemDisplay = outgoingItemDisplay;
		this.totalAmount = totalAmount;
	}

	public OutgoingInsertDTO(String prodOrSaleId, String reasonOfOutgoing, Timestamp prodOrSaleDate) {
//		super();
		this.prodOrSaleId = prodOrSaleId;
		this.reasonOfOutgoing = reasonOfOutgoing;
		this.prodOrSaleDate = prodOrSaleDate;
	}
    
    
    
}
