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
@ToString(exclude = "outgoing") // 'outgoing' 필드를 toString에서 제외
@NoArgsConstructor
public class OutgoingItems {

	
    @Id
    @Column(name = "outgoing_item_id", length = 20)
    private String outgoingItemId;

    @Column(name = "quantity")
    private int quantity;


    // Many-to-One 관계 설정: OutgoingItems과 Outgoing
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outgoing_id")
    private Outgoing outgoing;

    // Many-to-One 관계 설정: OutgoingItems과 Item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    private Item item;
}
