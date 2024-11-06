package com.itwillbs.domain.inventory;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomingDTO {
	
	private String incomingId;
	private Timestamp incomingStartDate;
	private Timestamp incomingEndDate;
	private String managerId;
	private String status;
	private String qualityOrderId;
	
	
	private String incomingItem;
}
