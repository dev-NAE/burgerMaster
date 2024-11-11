package com.itwillbs.domain.dashboard;


import lombok.Data;

@Data
public class IncomingItemDTO {
	private int quantity;
    private String itemName;
    private String itemCode;
    private String incomingItemId;

    public IncomingItemDTO(int quantity, String itemName,String itemCode,String incomingItemId) {
        this.quantity = quantity;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.incomingItemId = incomingItemId;
    }
}
