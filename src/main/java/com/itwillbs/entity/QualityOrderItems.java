package com.itwillbs.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "quality_order_items")
@Getter
@Setter
@ToString
public class QualityOrderItems {
	
	@Id
	@Column(name = "quality_order_item_id" , length = 20 , nullable = false)
	private String quality_order_item_id;
	
	@ManyToOne
	@JoinColumn(name = "quality_order_id")
	private QualityOrder quality_order;
	
	@Column(name = "quantity" , length = 20 , nullable = false)
	private int quantity;

	@Column(name = "status" , length = 20 , nullable = false)
	private String status;
	
	@Column(name = "note" , length = 100)
	private String note;

	@ManyToOne
	@JoinColumn(name = "item_code")
	private Item item;
	

}
