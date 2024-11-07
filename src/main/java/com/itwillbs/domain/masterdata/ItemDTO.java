package com.itwillbs.domain.masterdata;

import com.itwillbs.entity.Item;

import lombok.Data;

@Data
public class ItemDTO {
    private String itemCode;
    private String itemName;
    private String itemType;
    private String useYN;

    // entity -> DTO
    public ItemDTO(Item item) {
        this.itemCode = item.getItemCode();
        this.itemName = item.getItemName();
        this.itemType = item.getItemType();
        this.useYN = item.getUseYN();
    }

    // JPQL new 연산자용
    public ItemDTO(String itemCode, String itemName, String itemType, String useYN) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemType = itemType;
        this.useYN = useYN;
    }
}
