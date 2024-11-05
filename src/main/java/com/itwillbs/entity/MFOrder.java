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
	private String mOrderId;
	
	@Column(name = "order_amount", nullable = false)
	private int mOrderAmount;
	
	@Column(name = "order_deadline", nullable = false)
	private Timestamp mOrderDeadline;
	
	@Column(name = "order_date", nullable = false)
	private Timestamp mOrderDate;
	
	@Column(name = "order_state", length = 20, nullable = false)
	private String mOrderState;
	
	@Column(name = "order_item", length = 20, nullable = false)
	private String mOrderItem;
}
