package com.itwillbs.domain.manufacture;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MFOrderDTO {
	
	private String orderId;
	private String orderItem;
	private int orderAmount;
	private Timestamp orderDeadline;
	private Timestamp orderDate;
	private String orderState;
	
}
