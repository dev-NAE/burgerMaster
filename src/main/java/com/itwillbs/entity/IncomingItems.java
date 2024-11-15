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
 * 입고 품목 테이블
 */
@Entity
@Table(name = "incoming_items")
@Getter
@Setter
@ToString(exclude = "incoming") // 'incoming' 필드를 toString에서 제외
@NoArgsConstructor
public class IncomingItems {

    @Id
    @Column(name = "incoming_item_id", length = 20)
    private String incomingItemId;

    @Column(name = "quantity")
    private int quantity;

//    @Column(name = "subtotal_price")
//    private int subtotalPrice;

    // Many-to-One 관계 설정: IncomingItems과 Incoming
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incoming_id")
    private Incoming incoming;

    // Many-to-One 관계 설정: IncomingItems과 Item
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    private Item item;
}
