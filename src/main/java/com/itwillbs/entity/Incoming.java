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

/**
 * 입고 테이블
 */
@Entity
@Table(name = "incoming")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Incoming {

	@Id
	@Column(name = "incoming_id", length = 20)
	private String incomingId;
	
	@Column(name = "incoming_start_date")
	private Timestamp incomingStartDate;
	
	@Column(name = "incoming_end_date")
	private Timestamp incomingEndDate;
	
	@Column(name = "manager_id")
	private String managerId;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "order_id")
	private String orderId;
	
	@Column(name = "quality_order_id")
	private String qualityOrderId;
	
	
	
	
}
