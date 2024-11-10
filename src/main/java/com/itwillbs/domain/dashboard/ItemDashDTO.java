package com.itwillbs.domain.dashboard;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemDashDTO {
	private BigDecimal quantity;
    private String itemName;
    private String itemCode;

    public ItemDashDTO(BigDecimal quantity, String itemName,String itemCode) {
        this.quantity = quantity;
        this.itemName = itemName;
        this.itemCode = itemCode;
    }
}
