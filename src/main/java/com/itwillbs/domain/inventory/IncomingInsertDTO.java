package com.itwillbs.domain.inventory;

import java.sql.Timestamp;

import lombok.Data;


//입고등록
@Data
public class IncomingInsertDTO {
	
    private String IncomingInsertCode;
    private String reasonOfIncoming; //생산 완료, 검품완료
    private Timestamp prodOrQualDate;
    private String itemName;
    private Integer quantity;
    private Integer totalAmount; //총 수량
    
	private String incomingItemDisplay; //보여줄 품목 1개
	private Integer otherCount; //품목 외 갯수 ex)패티 외 2건
    
    private String managerId;
    private String managerName;
    
	public IncomingInsertDTO(String incomingInsertCode, String reasonOfIncoming, Timestamp prodOrQualDate, String itemName,
			Integer quantity) {
//		super();
		IncomingInsertCode = incomingInsertCode;
		this.reasonOfIncoming = reasonOfIncoming;
		this.prodOrQualDate = prodOrQualDate;
		this.itemName = itemName;
		this.quantity = quantity;
	}

	public IncomingInsertDTO(String incomingInsertCode, String reasonOfIncoming, Timestamp prodOrQualDate) {
//		super();
		IncomingInsertCode = incomingInsertCode;
		this.reasonOfIncoming = reasonOfIncoming;
		this.prodOrQualDate = prodOrQualDate;
	}
    
    
    
    
    
}
