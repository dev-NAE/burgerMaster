package com.itwillbs.entity.quality;

import com.itwillbs.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Table(name = "quality_sale_items")
@Getter
@Setter
@ToString
public class QualitySaleItems2 {

    @Id
    @Column(name = "quality_sale_item_id" , length = 20 , nullable = false)
    private String qualitySaleItemId;

    @Column(name = "quantity" ,nullable = false)
    private int quantity;

    @Column(name = "status" , length = 20 ,nullable = false)
    private String status;

    @Column(name = "note" , length = 100)
    private String note;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quality_sale_id")
    private QualitySale2 qualitySale2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    private Item item;

}

