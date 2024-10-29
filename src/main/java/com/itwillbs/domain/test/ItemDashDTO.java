package com.itwillbs.domain.test;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ItemDashDTO {
	private BigDecimal quantity;
    private String itemName;

    public ItemDashDTO(BigDecimal quantity, String itemName) {
        this.quantity = quantity;
        this.itemName = itemName;
    }
}
