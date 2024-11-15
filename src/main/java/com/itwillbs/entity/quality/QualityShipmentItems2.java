package com.itwillbs.entity.quality;

import com.itwillbs.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Table(name = "quality_shipment_items")
@Getter
@Setter
@ToString
public class QualityShipmentItems2 {

    @Id
    @Column(name = "quality_shipment_item_id" , length = 20 , nullable = false)
    private String qualityShipmentItemId;

    @Column(name = "quantity" ,nullable = false)
    private int quantity;

    @Column(name = "status" , length = 20 ,nullable = false)
    private String status;

    @Column(name = "note" , length = 100)
    private String note;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quality_shipment_id")
    private QualityShipment2 qualityShipment2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_code")
    private Item item;

}

