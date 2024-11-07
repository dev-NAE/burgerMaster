package com.itwillbs.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "manufacture_order")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MFOrder {
	
	@Id
	@Column(name = "order_id", length = 20, nullable = false)
	private String orderId;
	
	@Column(name = "order_amount", nullable = false)
	private int orderAmount;
	
	@Column(name = "order_deadline", nullable = false)
	private Timestamp orderDeadline;
	
	@Column(name = "order_date", nullable = false)
	private Timestamp orderDate;
	
	@Column(name = "order_state", length = 20, nullable = false)
	private String orderState;
	
	@Column(name = "order_item", length = 20, nullable = false)
	private String orderItem;
	
	public MFOrder(String orderId, int orderAmount, Timestamp orderDeadline, Timestamp orderDate, String orderState, String orderItem) {
		this.orderId = orderId;
		this.orderAmount = orderAmount;
		this.orderDeadline = orderDeadline;
		this.orderDate = orderDate;
		this.orderState = orderState;
		this.orderItem = orderItem;
	}
	
//	public static MFOrder createOrder(int orderAmount, Timestamp orderDeadline, String orderItem) {
//		
//		MFOrder mfOrder = MFOrder(???);
//		
//		return mfOrder;
//	}
}
