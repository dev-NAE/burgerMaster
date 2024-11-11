package com.itwillbs.domain.masterdata;

import com.itwillbs.entity.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDTO {
    private String itemCode;
    private String itemName;
    private String itemType;
    private String useYN;

    // JPQL new 연산자용 생성자 추가
    public ItemDTO(String itemCode, String itemName, String itemType, String useYN) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemType = itemType;
        this.useYN = useYN;
    }

    // Entity -> DTO 변환 생성자
    public ItemDTO(Item item) {
        this.itemCode = item.getItemCode();
        this.itemName = item.getItemName();
        this.itemType = item.getItemType();
        this.useYN = item.getUseYN();
    }
}