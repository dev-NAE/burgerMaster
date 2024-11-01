package com.itwillbs.entity.dashboard;



import com.itwillbs.entity.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class InventoryItem {
	
	@Id
    @Column(name = "item_code", length = 20)
    private String itemCode;

    @Column(name = "quantity",nullable = false)
    private int quantity;


    @Column(name = "min_req_quantity")
    private int minReqQuantity;

    // Item과 일대일 관계 설정
    @OneToOne
    @JoinColumn(name = "item_code")  // Item의 item_code를 참조하는 외래 키 설정
    private Item item;
	
}
