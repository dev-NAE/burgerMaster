package com.itwillbs.entity;

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

/**
 * 출고 품목 테이블
 */
@Entity
@Table(name = "outgoing_items")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OutgoingItems {

	
	@Id
	@Column(name = "outgoing_item_id", length = 20)
	private String outgoingItemId;
	
	@Column(name = "outgoing_id", length = 20)
	private String outgoingId;
	
	@Column(name = "item_code", length = 20)
	private String itemCode;
	
	@Column(name = "quantity")
	private int quantity;
	
    // Many-to-One 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outgoing_id", insertable = false, updatable = false)
    private Outgoing outgoing;
    
    // Many-to-One 관계 설정 (Item 엔티티와 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code", insertable = false, updatable = false)
    private Item item;
}
