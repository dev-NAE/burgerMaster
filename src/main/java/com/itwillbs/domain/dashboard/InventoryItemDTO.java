package com.itwillbs.domain.dashboard;


import lombok.Data;

@Data
public class InventoryItemDTO {
	private int quantity;
    private String itemName;
    private int minReqQuantity;

    public InventoryItemDTO(int quantity, String itemName,int minReqQuantity) {
        this.quantity = quantity;
        this.itemName = itemName;
        this.minReqQuantity = minReqQuantity;
    }
}
