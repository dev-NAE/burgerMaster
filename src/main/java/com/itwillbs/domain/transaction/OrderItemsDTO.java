package com.itwillbs.domain.transaction;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 거래에 '실제로' 들어가는 상품 정보 저장

@Getter
@Setter
@ToString
public class OrderItemsDTO {
    private String itemCode;
    private int price;
    private int quantity;
    private int subtotalPrice;
}
