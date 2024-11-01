package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItemsDTO {
    private String itemCode;
    private int price;
    private int quantity;
    private int subtotalPrice;
}
