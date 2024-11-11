package com.itwillbs.domain.transaction;

// 거래상품 선택을 위한 item, inventory DB 조회정보 저장

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Getter
@Setter
@ToString
public class TxItemsDTO {

    private String itemCode;
    private String itemName;
    private String itemType;

    private Integer quantity;
    private Integer minReqQuantity;

    public TxItemsDTO(String itemCode, String itemName, String itemType, Integer quantity, Integer minReqQuantity) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantity = quantity;
        this.minReqQuantity = minReqQuantity;
    }

}
