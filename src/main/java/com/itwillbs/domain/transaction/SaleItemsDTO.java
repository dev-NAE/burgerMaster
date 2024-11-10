package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 거래에 '실제로' 들어가는 상품 정보 저장

@Getter
@Setter
@ToString
public class SaleItemsDTO {
    private String itemCode;
    private int price;
    private int quantity;
    private int subtotalPrice;

    private String itemName;

    public SaleItemsDTO(String itemCode, String itemName, int price, int quantity, int subtotalPrice) {
        this.itemCode = itemCode;
        this.price = price;
        this.quantity = quantity;
        this.subtotalPrice = subtotalPrice;
        this.itemName = itemName;
    }

    public SaleItemsDTO() {}

}
