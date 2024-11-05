package com.itwillbs.entity.dashboard;


//test용
//엔터티 만들어지면 삭제 

import com.itwillbs.entity.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "incoming_items")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class IncommingItemDash {
	
	
	@Id
    @Column(name = "incoming_item_id", length = 20)
    private String incomingItemId;

    @Column(name = "quantity",nullable = false)
    private int quantity;


    @ManyToOne
    @JoinColumn(name = "item_code", referencedColumnName = "item_code")
    private Item itemCode;

}
