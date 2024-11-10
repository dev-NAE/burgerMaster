package com.itwillbs.domain.inventory;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IncomingItemsDTO {
	

	private String incomingItemId;
	private String incomingId = "";
	private String itemCode = "";
	private Integer quantity;
	
	private String itemName; //품목 이름 한개만
	private Integer otherCount; //품목 외 갯수 ex)패티 외 2건
	
	public IncomingItemsDTO(String itemCode, String itemName) {
//		super();
		this.itemCode = itemCode;
		this.itemName = itemName;
	}


	
	
	
}
