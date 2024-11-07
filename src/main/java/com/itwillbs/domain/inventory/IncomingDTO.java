package com.itwillbs.domain.inventory;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 입고 조회
 */
@Data
@NoArgsConstructor
public class IncomingDTO {
	
	private String incomingId;
	private Timestamp incomingStartDate;
	private Timestamp incomingEndDate;
	private String managerId = ""; //담당자 코드
	private String managerName = ""; //담당자 이름
	private String status;
	private String productionId;
	private String qualityOrderId;
	
	//입고 조회하는 생성자

	public IncomingDTO(String incomingId, Timestamp incomingStartDate, Timestamp incomingEndDate, String managerId,
			String managerName, String status, String productionId, String qualityOrderId) {
		super();
		this.incomingId = incomingId;
		this.incomingStartDate = incomingStartDate;
		this.incomingEndDate = incomingEndDate;
		this.managerId = managerId;
		this.managerName = managerName;
		this.status = status;
		this.productionId = productionId;
		this.qualityOrderId = qualityOrderId;
	}




	private String incomingItemName;


	
	

	
}
