package com.itwillbs.domain.inventory;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IncomingDTO {
	
	private String incomingId;
	private Timestamp incomingStartDate;
	private Timestamp incomingEndDate;
	private String managerId; //담당자 코드
	private String name; //담당자 이름
	private String status;
	private String order_id;
	private String qualityOrderId;
	
	private String incomingItemName;
	
	

	
}
