package com.itwillbs.domain.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventoryItemDTO {//item테이블과 inventoryItem테이블을 join하기 위한 DTO

	
    private String itemCode;
    private String itemName;
    private String itemType;
    private Integer quantity;
    private Integer minReqQuantity;
	
    public InventoryItemDTO(String itemCode) {
        this.itemCode = itemCode;
    }
    
    public InventoryItemDTO(String itemCode, String itemName, String itemType, Integer quantity, Integer minReqQuantity) {
		super();
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemType = itemType;
		this.quantity = quantity;
		this.minReqQuantity = minReqQuantity;
	}

	//재고 검색
    private String itemCodeOrName;
    
}
