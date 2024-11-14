package com.itwillbs.domain.inventory;

import java.sql.Timestamp;

import lombok.Data;

//출고등록
@Data
public class OutgoingInsertDTO {
	
    private String OutgoingInsertCode;
    private String reasonOfOutgoing; //생산 완료, 검품요청
    private Timestamp prodOrQualDate;
    private String itemName;
    private Integer quantity;
    private Integer totalAmount; //총 수량
    
	private String outgoingItemDisplay; //보여줄 품목 1개
	private Integer otherCount; //품목 외 갯수 ex)패티 외 2건
    
    private String managerId;
    private String managerName;
    
	public OutgoingInsertDTO(String outgoingInsertCode, String reasonOfOutgoing, Timestamp prodOrQualDate, String itemName,
			Integer quantity) {
//		super();
		OutgoingInsertCode = outgoingInsertCode;
		this.reasonOfOutgoing = reasonOfOutgoing;
		this.prodOrQualDate = prodOrQualDate;
		this.itemName = itemName;
		this.quantity = quantity;
	}

	public OutgoingInsertDTO(String outgoingInsertCode, String reasonOfOutgoing, Timestamp prodOrQualDate) {
//		super();
		OutgoingInsertCode = outgoingInsertCode;
		this.reasonOfOutgoing = reasonOfOutgoing;
		this.prodOrQualDate = prodOrQualDate;
	}
    
}
