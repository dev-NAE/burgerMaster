package com.itwillbs.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "order_deadline", nullable = false)
	private LocalDate orderDeadline;
	
	@Column(name = "order_date", nullable = false)
	private Timestamp orderDate;
	
	@Column(name = "order_state", length = 20, nullable = false)
	private String orderState;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_item")
	private Item item;
	
	public MFOrder(String orderId, int orderAmount, LocalDate orderDeadline, Timestamp orderDate, String orderState, Item item) {
		this.orderId = orderId;
		this.orderAmount = orderAmount;
		this.orderDeadline = orderDeadline;
		this.orderDate = orderDate;
		this.orderState = orderState;
		this.item = item;
	}
}
