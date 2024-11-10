package com.itwillbs.entity;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 입고 품목 테이블
 */
@Entity
@Table(name = "incoming_items")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class IncomingItems {

	@Id
	@Column(name = "incoming_item_id", length = 20)
	private String incomingItemId;
	
	@Column(name = "incoming_id", length = 20)
	private String incomingId;
	
	@Column(name = "item_code", length = 20)
	private String itemCode;
	
	@Column(name = "quantity")
	private int quantity;
	
}
