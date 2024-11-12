package com.itwillbs.domain.inventory;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OutgoingDTO {
	private String outgoingId;
	private Timestamp outgoingStartDate;
	private Timestamp outgoingEndDate;
	private String managerId = ""; //담당자 코드
	private String managerName = ""; //담당자 이름
	private String status;
	private String productionId;
	private String qualityOrderId;
	
	private String outgoingItemDisplay; //보여줄 품목 1개
	private Integer otherCount; //품목 외 갯수 ex)패티 외 2건
	
	
	
	//입고 조회하는 생성자
	public OutgoingDTO(String outgoingId, Timestamp outgoingStartDate, Timestamp outgoingEndDate, String managerId,
			String managerName, String status, String productionId, String qualityOrderId) {
//		super();
		this.outgoingId = outgoingId;
		this.outgoingStartDate = outgoingStartDate;
		this.outgoingEndDate = outgoingEndDate;
		this.managerId = managerId;
		this.managerName = managerName;
		this.status = status;
		this.productionId = productionId;
		this.qualityOrderId = qualityOrderId;
	}

}
