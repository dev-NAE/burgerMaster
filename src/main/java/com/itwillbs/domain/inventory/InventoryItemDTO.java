package com.itwillbs.domain.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor 
public class InventoryItemDTO {//item테이블과 inventoryItem테이블을 join하기 위한 DTO

	
    private String itemCode;
    private String itemName;
    private String itemType;
    private int quantity;
    private int minReqQuantity;
	
    
}
