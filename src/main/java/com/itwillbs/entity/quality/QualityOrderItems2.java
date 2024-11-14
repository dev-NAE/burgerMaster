package com.itwillbs.entity.quality;

import com.itwillbs.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Table(name = "quality_order_items")
@Getter
@Setter
@ToString
public class QualityOrderItems2 {

    @Id
    @Column(name = "quality_order_item_id" , length = 20 , nullable = false)
    private String qualityOrderItemsId;

    @Column(name = "quantity" ,nullable = false)
    private int quantity;

    @Column(name = "status" , length = 20 ,nullable = false)
    private String status;

    @Column(name = "note" , length = 100)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quality_order_id")
    private QualityOrder2 qualityOrder2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    private Item item;


}

