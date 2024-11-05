package com.itwillbs.domain.dashboard;


import lombok.Data;

@Data
public class IncomingItemDTO {
	private int quantity;
    private String itemName;

    public IncomingItemDTO(int quantity, String itemName) {
        this.quantity = quantity;
        this.itemName = itemName;
    }
}
