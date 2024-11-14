package com.itwillbs.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

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
@ToString(exclude = "item")
@NoArgsConstructor
public class InventoryItem {

	@Id
	@Column(name = "item_code", length = 20)
	private String itemCode;
	
	@Column(name = "quantity", columnDefinition = "INT DEFAULT 0")
	private Integer quantity;
	
	@Column(name = "min_req_quantity", columnDefinition = "INT DEFAULT -1")
	private Integer minReqQuantity;
	
	
	
    // Item과 일대일 관계 설정
	//삭제 x 겹치면 삭제o 소연
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "item_code")  // Item의 item_code를 참조하는 외래 키 설정
    private Item item;
//	
	
}
