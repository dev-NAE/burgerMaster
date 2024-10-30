package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsDTO {
    private String item_code;
    private int quantity;
    private int subtotal_price;
}
