package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsDTO {
    private String itemCode;
    private int quantity;
    private int subtotalPrice;
}
