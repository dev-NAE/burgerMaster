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
	private Integer quantity; //품목의 수량
	
	private String itemName; //품목 이름 한개만
	private Integer otherCount; //품목 외 갯수 ex)패티 외 2건
	
	private String itemType; //품목 유형
	
	// 입고 조회에서 품목이름과 갯수 확인할 떄 쓰는 생성자
	public IncomingItemsDTO(String itemCode, String itemName) {
//		super();
		this.itemCode = itemCode;
		this.itemName = itemName;
	}
	
	// 입고 상세 조회에서 테이블 출력할 때 쓰는 생성자
	public IncomingItemsDTO(String itemCode, String itemName, String itemType, Integer quantity) {
		super();
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemType = itemType;
		this.quantity = quantity;
	}



	
	
	
}
