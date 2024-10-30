package com.itwillbs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Item {

    @Id
    @Column(name = "item_code", length = 20)
    private String itemCode;

    @Column(name = "item_name", length = 100, nullable = false)
    private String itemName;

    @Column(name = "item_type", length = 10, nullable = false)
    private String itemType;

    @Column(name = "use_yn", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private char useYN = 'Y';

}
