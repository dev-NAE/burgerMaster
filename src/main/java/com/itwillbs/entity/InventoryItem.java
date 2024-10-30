package com.itwillbs.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "InventoryItem")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class InventoryItem {

	
	@Column(name = "item_code", length = 20)
	private String itemCode;
	
	@Column(name = "quantity", columnDefinition = "INT DEFAULT 0")
	private int quantity;
	
	@Column(name = "min_req_quantity", columnDefinition = "INT DEFAULT -1")
	private int minReqQuantity;
	
}
